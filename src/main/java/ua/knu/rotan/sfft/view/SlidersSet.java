package ua.knu.rotan.sfft.view;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.knu.rotan.sfft.alg.WindowFunction;
import ua.knu.rotan.sfft.config.FrequencyRepresentationProperties;

@Component
@Getter
public class SlidersSet {
  @Autowired FrequencyRepresentationProperties frp;

  private final List<SliderPanel> values = new ArrayList<>();
  private SliderPanel NUMBER_OF_SAMPLES_FOR_FFT;
  private SliderPanel OVERLAPPING_COEFFICIENT;
  private SliderPanel WINDOW_FUNCTION;
  private SliderPanel LOW_PASS_FILTER;

  @PostConstruct
  private void init() {
    WINDOW_FUNCTION =
        new SliderPanel(
            "Window function", frp.getWindowFunction(), (x) -> WindowFunction.values()[x].name());
    values.add(WINDOW_FUNCTION);
    NUMBER_OF_SAMPLES_FOR_FFT =
        new SliderPanel(
            "Number of Samples for FFT", frp.getLog2FFTLength(), (x) -> String.valueOf(1 << x));
    values.add(NUMBER_OF_SAMPLES_FOR_FFT);
    OVERLAPPING_COEFFICIENT =
        new SliderPanel(
            "Overlapping Coefficient", frp.getOverlappingCoefficient(), String::valueOf);
    values.add(OVERLAPPING_COEFFICIENT);
    LOW_PASS_FILTER =
        new SliderPanel("Overlapping Coefficient", frp.getLowPassFilter(), String::valueOf);
    values.add(LOW_PASS_FILTER);
  }
}
