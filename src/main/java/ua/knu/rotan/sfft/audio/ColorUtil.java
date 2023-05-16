package ua.knu.rotan.sfft.audio;

import java.awt.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorUtil {
  public static Color linearColorInterpolation(Color color1, Color color2, double position) {
    int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * position);
    int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * position);
    int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * position);

    return new Color(red, green, blue);
  }
}
