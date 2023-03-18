package ua.knu.rotan.sfft.alg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WindowFunctions {
  public static double[] hannWindow(int length) {
    double[] window = new double[length];
    for (int i = 0; i < length; i++) {
      window[i] = 0.5 * (1 - Math.cos(2 * Math.PI * i / (length - 1)));
    }
    return window;
  }

  public static double[] identity(int length) {
    double[] window = new double[length];
    for (int i = 0; i < length; i++) {
      window[i] = 1;
    }
    return window;
  }
}
