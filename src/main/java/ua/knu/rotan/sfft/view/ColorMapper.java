package ua.knu.rotan.sfft.view;

import java.awt.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ColorMapper {
  public static Color getPurpleYellowGradient(double value) {
    // Define the purple and yellow colors
    Color purple = new Color(128, 0, 128); // dark purple
    Color yellow = new Color(255, 255, 0); // yellow

    // Define the gamma correction factor
    double gamma = 2.0;

    // Calculate the color based on the value
    int red =
        (int)
            (Math.pow(1 - value, gamma) * purple.getRed()
                + Math.pow(value, gamma) * yellow.getRed());
    int green =
        (int)
            (Math.pow(1 - value, gamma) * purple.getGreen()
                + Math.pow(value, gamma) * yellow.getGreen());
    int blue =
        (int)
            (Math.pow(1 - value, gamma) * purple.getBlue()
                + Math.pow(value, gamma) * yellow.getBlue());

    // Create and return the color
    return new Color(red, green, blue);
  }

  //    public static Color getBlackBodyGradient(double temperature) {
  //        // Define the color temperature range
  //        double[] colorTemperature = { 1000, 4000, 7000 };
  //
  //        // Calculate the color based on the temperature
  //        if (temperature <= colorTemperature[0]) {
  //            return Color.RED;
  //        } else if (temperature <= colorTemperature[1]) {
  //            double t = (temperature - colorTemperature[0]) / (colorTemperature[1] -
  // colorTemperature[0]);
  //            return interpolateColor(Color.RED, Color.WHITE, t);
  //        } else if (temperature <= colorTemperature[2]) {
  //            double t = (temperature - colorTemperature[1]) / (colorTemperature[2] -
  // colorTemperature[1]);
  //            return interpolateColor(Color.WHITE, Color.BLUE, t);
  //        } else {
  //            return Color.BLUE;
  //        }
  //    }
  //
  //    public static Color interpolateColor(Color c1, Color c2, double t) {
  //        // Calculate the color based on the interpolation factor
  //        int r = (int) (c1.getRed() * (1 - t) + c2.getRed() * t);
  //        int g = (int) (c1.getGreen() * (1 - t) + c2.getGreen() * t);
  //        int b = (int) (c1.getBlue() * (1 - t) + c2.getBlue() * t);
  //
  //        // Create and return the color
  //        return new Color(r, g, b);
  //    }

  public static Color getBlackBodyGradient(double temperature) {
    // Define the constants used in the calculation
    double c1 = 3.74183e-16; // W m^2
    double c2 = 1.4388e-2; // m K

    // Calculate the color based on the temperature
    double lambda = 780e-9; // Red
    double maxLambda = 380e-9; // Violet
    double intensity = 0;

    for (double l = lambda; l >= maxLambda; l -= 1e-9) {
      double power = c1 / Math.pow(l, 5) * (1 / (Math.exp(c2 / (l * temperature)) - 1));
      intensity += power * 1e-9;
    }

    // Normalize the intensity to the range [0, 1]
    intensity /= 2.9e14;

    // Calculate the color based on the normalized intensity
    int r = (int) (255 * Math.pow(intensity, 0.8));
    int g = (int) (255 * Math.pow(intensity, 0.6));
    int b = (int) (255 * Math.pow(intensity, 0.4));

    // Create and return the color
    return new Color(r, g, b);
  }
}
