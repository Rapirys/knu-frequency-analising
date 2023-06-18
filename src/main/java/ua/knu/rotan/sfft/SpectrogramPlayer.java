package ua.knu.rotan.sfft;

import static java.lang.Math.*;
import static ua.knu.rotan.sfft.audio.AudioUtil.lowPassFilterWithRMS;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.AudioFormat;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.knu.rotan.sfft.alg.Complex;
import ua.knu.rotan.sfft.alg.FFT;
import ua.knu.rotan.sfft.alg.WindowFunction;
import ua.knu.rotan.sfft.audio.AudioSupplier;
import ua.knu.rotan.sfft.config.FrequencyRepresentationProperties;
import ua.knu.rotan.sfft.view.*;

@Component
public class SpectrogramPlayer implements ApplicationRunner {
  @Autowired private FrequencyRepresentationProperties fap;
  @Autowired private AudioFormat audioFormat;
  @Autowired private AudioSupplier audioSupplier;
  @Autowired private MainWindow mainWindow;
  @Autowired private SlidersSet slidersSet;
  @Autowired private ColorMapper colorMapper;

  @Value("${view.fps:60}")
  private int defaultFPS;

  @Override
  @SneakyThrows
  public void run(ApplicationArguments args) {
    mainWindow.addSpectrogram();
    slidersSet.getValues().forEach(mainWindow::addSlider);
    mainWindow.addKayPressedAction(KeyEvent.VK_SPACE, audioSupplier::switchRunningState);
    mainWindow.setVisible(true);

    audioSupplier.start();
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    executor.scheduleAtFixedRate(() -> task(0), 0, (long) 1e9 / defaultFPS, TimeUnit.NANOSECONDS);
  }

  int offset = 0;

  int[][] getAudioData(int numberOfSamples, int channel, int shiftSize) {
    // Number of available bytes can't become smaller, synchronization not needed
    int pointerPosition = audioSupplier.getPointerPosition(channel);
    int chunksN = max(0, (pointerPosition - (offset + numberOfSamples)) / shiftSize);
    int[][] chunks = new int[chunksN][numberOfSamples];
    for (int i = 0; i < chunksN; i++)
      chunks[i] = audioSupplier.getAudioChunk(offset + i * shiftSize, numberOfSamples, channel);
    offset += chunksN * shiftSize;
    return chunks;
  }

  public void task(final int channelIndex) {
    final int numberOfSamples = 1 << fap.getLog2FFTLength().getValue();
    final int shiftSize =
        (int) (numberOfSamples * fap.getOverlappingCoefficient().getValue() / 100.0);
    final WindowFunction window = WindowFunction.values()[fap.getWindowFunction().getValue()];
    final double windowsSum = window.windowSum(numberOfSamples);
    final double cutoffFrequency = fap.getLowPassFilter().getValue();
    final double sampleRate = audioFormat.getSampleRate();
    SlidingImagePanel spectrogram = mainWindow.getSpectrogram(0);

    int[][] audioData = getAudioData(numberOfSamples, channelIndex, shiftSize);
    Color[][] newSpectrogram = new Color[audioData.length][];
    AtomicInteger i = new AtomicInteger();

    Arrays.stream(audioData)
        .parallel()
        .skip(max(0, audioData.length - spectrogram.getResolution().width))
        .map(window::apply)
        .map(x -> lowPassFilterWithRMS(x, sampleRate, cutoffFrequency))
        .map(x -> Arrays.stream(x).mapToObj(Complex::fromReal).toArray(Complex[]::new))
        .map(FFT::fft)
        .map(Arrays::stream)
        .map(
            frequencyDomain ->
                frequencyDomain
                    .limit(numberOfSamples / 2)
                    .mapToDouble(Complex::magnitude)
                    .map(x -> 2 * x / windowsSum)
                    .map(x -> 10 * log10(x / Short.MAX_VALUE)))
        .map(x -> x.mapToObj(colorMapper::mapNumberToColor).toArray(Color[]::new))
        .forEachOrdered(x -> newSpectrogram[i.getAndIncrement()] = x);
    spectrogram.update(newSpectrogram);

    spectrogram.repaint();
  }
}
