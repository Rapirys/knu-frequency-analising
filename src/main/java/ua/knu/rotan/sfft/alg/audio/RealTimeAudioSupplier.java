package ua.knu.rotan.sfft.alg.audio;

import static java.lang.Math.min;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.knu.rotan.sfft.alg.CycleBuffer;
// import ua.knu.rotan.sfft.view.MainFrame;

@Service
public class RealTimeAudioSupplier implements AudioSupplier {
  @Autowired private AudioFormat audioFormat;

  @Value("${audio.buffer.sizeInSeconds:16}")
  int bufferSizeInSeconds;

  private TargetDataLine line;
  private volatile Thread writingThread;
  private CycleBuffer[] channels;

  @PostConstruct
  @SneakyThrows
  public void init() {
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    line = (TargetDataLine) AudioSystem.getLine(info);
    line.open(audioFormat);

    channels = new CycleBuffer[audioFormat.getChannels()];
    int bufferSize =
        (int) audioFormat.getFrameRate() * audioFormat.getFrameSize() * bufferSizeInSeconds;
    for (int i = 0; i < audioFormat.getChannels(); i++) channels[i] = new CycleBuffer(bufferSize);
  }

  @PreDestroy
  public void destroy() {
    line.close();
  }

  @Override
  public int[] getAudioChunk(int offset, int numberOfSamples, int channel) {
    return channels[channel].get(offset, numberOfSamples);
  }

  @Override
  public int getPointerPosition(int channel) {
    return channels[channel].totalElementsAdded();
  }

  @Override
  public synchronized void start() {
    line.start();
    if (writingThread != null) return;

    writingThread =
        new Thread(
            () -> {
              while (writingThread != null) {
                readDataFromLineInBuffer();
              }
            });

    writingThread.start();
  }

  @Override
  public synchronized void stop() {
    writingThread = null;
    line.stop();
  }

  @Override
  public synchronized void switchRunningState() {
    if (writingThread == null) start();
    else stop();
  }

  private void readDataFromLineInBuffer() {
    byte[] data = new byte[512];
    int numBytesRead = line.read(data, 0, min(data.length, line.available()));

    int[][] channelsData = AudioUtil.parseAudioData(data, numBytesRead, audioFormat);
    for (int c = 0; c < audioFormat.getChannels(); c++)
      channels[c].addAllInEnd(channelsData[c], channelsData[c].length);
  }
}
