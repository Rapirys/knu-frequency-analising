package ua.knu.rotan.sfft.config;

import static ua.knu.rotan.sfft.alg.WindowFunction.BLACKMAN;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ua.knu.rotan.sfft.alg.ValueRange;
import ua.knu.rotan.sfft.alg.WindowFunction;

@Data
@Component
@ConfigurationProperties(prefix = "audio.representation.spectrogram")
public class FrequencyRepresentationProperties {
  private ValueRange log2FFTLength;
  private ValueRange overlappingCoefficient;
  private ValueRange lowPassFilter;
  private ValueRange windowFunction =
      new ValueRange(0, WindowFunction.values().length - 1, BLACKMAN.ordinal());
}
