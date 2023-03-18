package ua.knu.rotan.sfft.config;

import javax.sound.sampled.AudioFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
