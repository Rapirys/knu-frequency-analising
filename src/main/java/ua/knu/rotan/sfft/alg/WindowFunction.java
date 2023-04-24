package ua.knu.rotan.sfft.alg;

import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WindowFunction {
  RECTANGL((i, length) -> 1.0),
  HANN((i, length) -> 0.5 * (1 - Math.cos(2 * Math.PI * i / (length - 1)))),
  HAMMING((i, length) -> 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (length - 1))),
  BLACKMAN(
      (i, length) ->
          0.42
              - 0.5 * Math.cos(2 * Math.PI * i / (length - 1))
              + 0.08 * Math.cos(4 * Math.PI * i / (length - 1))),
  GAUSS(
      (i, length) ->
          Math.exp(-0.5 * Math.pow((i - (length - 1) / 2.0) / (0.25 * (length - 1)), 2)));

  private final BiFunction<Integer, Integer, Double> window;

  public double[] apply(int[] array) {
    double[] windowed_array = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      windowed_array[i] = array[i] * window.apply(i, array.length);
    }
    return windowed_array;
  }

  public double windowSum(int length) {
    double s = 0;
    for (int i = 0; i < length; i++) {
      s += window.apply(i, length);
    }
    return s;
  }
}
