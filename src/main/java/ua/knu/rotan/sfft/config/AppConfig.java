package ua.knu.rotan.sfft.config;

import java.awt.*;
import javax.sound.sampled.AudioFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.knu.rotan.sfft.view.ColorMapper;

@Configuration
public class AppConfig {
  @Bean
  public AudioFormat getAudioFormat(
      @Value("${audio.format.sampleRate:48000}") float sampleRate,
      @Value("${audio.format.sampleSizeInBits:16}") int sampleSizeInBits,
      @Value("${audio.format.channels:2}") int channels) {

    if (sampleSizeInBits != 16)
      throw new IllegalArgumentException("Only supported sampleSizeInBits is 16");
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
  }

  @Bean
  public ColorMapper getColorMapper() {
    double min = -70, max = -20;
    return ColorMapper.of(new Color(0, 0, 0), min + (max - min) * 0 / 8)
        .add(new Color(0, 0, 79), min + (max - min) * 1 / 8)
        .add(new Color(79, 0, 123), min + (max - min) * 2 / 8)
        .add(new Color(152, 0, 119), min + (max - min) * 3 / 8)
        .add(new Color(211, 0, 63), min + (max - min) * 4 / 8)
        .add(new Color(245, 32, 0), min + (max - min) * 5 / 8)
        .add(new Color(255, 174, 0), min + (max - min) * 6 / 8)
        .add(new Color(255, 250, 106), min + (max - min) * 7 / 8)
        .add(new Color(255, 255, 255), min + (max - min) * 8 / 8);
  }
}
