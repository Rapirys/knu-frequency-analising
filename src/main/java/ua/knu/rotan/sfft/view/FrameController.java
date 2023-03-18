package ua.knu.rotan.sfft.view;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Thread.sleep;

import jakarta.annotation.PostConstruct;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.knu.rotan.sfft.alg.Complex;
import ua.knu.rotan.sfft.alg.FFT;
import ua.knu.rotan.sfft.audio.AudioSupplier;

@Component
public class FrameController implements ApplicationRunner {
  @Autowired private AudioFormat audioFormat;
  @Autowired private AudioSupplier audioSupplier;

  @Value("${view.spectrogram.width:1024}")
  private int SPECTROGRAM_WIDTH;

  @Value("${view.spectrogram.high:256}")
  private int SPECTROGRAM_HIGH;

  private final List<SlidingImagePanel> spectrograms = new ArrayList<>();
  private final JFrame frame = new JFrame("Short-time FFT");
  private final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);

  @PostConstruct
  public void initUI() {
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    frame.setBackground(Color.BLACK);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setPreferredSize(
        (new Dimension(SPECTROGRAM_WIDTH, SPECTROGRAM_HIGH * audioFormat.getChannels())));

    for (int i = 0; i < audioFormat.getChannels(); i++) {
      SlidingImagePanel spectrogram = new SlidingImagePanel(SPECTROGRAM_WIDTH, SPECTROGRAM_HIGH);
      spectrograms.add(spectrogram);
      centerPanel.add(spectrogram);
    }
    frame.add(centerPanel, BorderLayout.CENTER);

    slider.setMajorTickSpacing(1);
    slider.setPaintTicks(true);
    frame.add(slider, BorderLayout.SOUTH);

    //    slider.setVisible(!slider.isVisible());
    //    frame.setUndecorated(true);

    frame.pack();
    frame.setVisible(true);
  }

  @Override
  @SneakyThrows
  public void run(ApplicationArguments args) {
    while (true) {
      Instant start = Instant.now();
      List<short[]> channelsData = audioSupplier.getAudioStream(512);
      for (int i = 0; i < channelsData.size(); i++) {
        spectrograms
            .get(i)
            .update(
                Arrays.stream(FFT.fft(channelsData.get(i)))
                    .mapToDouble(Complex::magnitude)
                    .map(x -> log(x + 1) * pow(10, 8))
                    .limit(256)
                    .toArray());
        spectrograms.get(i).repaint();
      }
//      sleep(6);
      System.out.println(Duration.between(start, Instant.now()).toMillis());
    }
  }
}
