package ua.knu.rotan.sfft.view;

import static ua.knu.rotan.sfft.alg.audio.ColorUtil.linearColorInterpolation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColorMapper {
  private final List<ColorNumberPair> colorNumberPairs = new ArrayList<>();

  private ColorMapper(Color color, double number) {
    colorNumberPairs.add(new ColorNumberPair(color, number));
  }

  public static ColorMapper of(Color color, double number) {
    return new ColorMapper(color, number);
  }

  public ColorMapper add(Color color, double number) {
    colorNumberPairs.add(new ColorNumberPair(color, number));
    colorNumberPairs.sort(Comparator.comparingDouble(ColorNumberPair::number));
    return this;
  }

  public Color mapNumberToColor(double number) {
    int index = 0;
    int size = colorNumberPairs.size();

    // Find the index of the first color-number pair whose number is greater than the given number
    while (index < size && colorNumberPairs.get(index).number() <= number) {
      index++;
    }

    // If we have only one point to create gradient or
    // the given number is greater than or equal to the number of the first color-number pair,
    // return its color
    if (colorNumberPairs.size() == 1 || index == 0) return colorNumberPairs.get(0).color;
    // If the given number is greater than or equal to the number of the last color-number pair,
    // return its color
    if (index == size) {
      return colorNumberPairs.get(size - 1).color();
    }

    // Calculate the position of the given number within the range of the color-number pair
    double prevNumber = colorNumberPairs.get(index - 1).number();
    double nextNumber = colorNumberPairs.get(index).number();
    double position = (number - prevNumber) / (nextNumber - prevNumber);

    // Interpolate between the colors of the two neighboring color-number pairs based on the
    // position
    Color prevColor = colorNumberPairs.get(index - 1).color();
    Color nextColor = colorNumberPairs.get(index).color();

    return linearColorInterpolation(prevColor, nextColor, position);
  }

  private record ColorNumberPair(Color color, double number) {}
}
