package ua.knu.rotan.sfft.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SlidingImagePanel extends VerticalPanelNavigator {
  private final BufferedImage image;
  private int shift;

  public SlidingImagePanel(int maxWidth, int maxHeight) {
    super();
    this.shift = 0;
    setPreferredSize(new Dimension(maxWidth, maxHeight));
    setVisible(true);
    image = new BufferedImage(maxWidth * 2, maxHeight, BufferedImage.TYPE_INT_RGB);
    image.setAccelerationPriority(1);
  }

  @Override
  public synchronized void paint(Graphics g) {
    g.drawImage(
        image,
        0,
        0,
        getWidth(),
        getHeight(),
        shift,
        (int) (image.getHeight() * getMinY()),
        shift + image.getWidth() / 2,
        (int) (image.getHeight() * getMaxY()),
        this);
  }

  public synchronized void update(Color[][] raw) {
    if (raw.length > image.getWidth() / 2)
      raw = Arrays.copyOfRange(raw, raw.length - image.getWidth() / 2, raw.length);
    if (raw.length > image.getWidth() / 2 - shift) {
      int splitIndex =
          image.getWidth() / 2
              - shift; // Math.max(0, Math.min(image.getWidth() / 2 - shift, raw.length));
      Color[][] firstPart = Arrays.copyOfRange(raw, 0, splitIndex);
      Color[][] secondPart = Arrays.copyOfRange(raw, splitIndex, raw.length);
      updateImage(firstPart);
      updateImage(secondPart);
    } else updateImage(raw);
  }

  private synchronized void updateImage(Color[][] raw) {
    if (raw.length == 0) return;
    BufferedImage newImage = generateImage(raw);

    Graphics g = image.createGraphics();
    g.drawImage(
        newImage,
        shift,
        0,
        shift + raw.length,
        image.getHeight(),
        0,
        0,
        newImage.getWidth(),
        newImage.getHeight(),
        this);
    g.drawImage(
        newImage,
        shift + image.getWidth() / 2,
        0,
        shift + image.getWidth() / 2 + raw.length,
        image.getHeight(),
        0,
        0,
        newImage.getWidth(),
        newImage.getHeight(),
        this);
    shift = (shift + raw.length) % (image.getWidth() / 2);

    g.dispose();
  }

  private BufferedImage generateImage(Color[][] raw) {
    int width = raw.length;
    int height = raw[0].length;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, raw[x][y].getRGB());
      }
    }

    return image;
  }

  @Deprecated
  public synchronized void update(Color[] raw) {
    Color[] resizedRow = boxScaling1D(raw, image.getHeight());
    for (int y = 0; y < resizedRow.length; y++) {
      image.setRGB(shift, y, resizedRow[y].getRGB());
      image.setRGB(shift + image.getWidth() / 2, y, resizedRow[y].getRGB());
    }
    shift = (shift + 1) % (image.getWidth() / 2);
  }

  @Deprecated
  public static Color[] boxScaling1D(Color[] input, int outputSize) {
    Color[] output = new Color[outputSize];

    // Determine the size of each interval in the input and output arrays
    double inputIntervalSize = (double) input.length / outputSize;

    // Loop through each element in the output array
    for (int i = 0; i < outputSize; i++) {
      // Determine the start and end indices of the input interval that overlaps with the current
      // output interval
      int startInputIndex = (int) Math.floor(i * inputIntervalSize);
      int endInputIndex = (int) Math.ceil((i + 1) * inputIntervalSize);

      // Calculate the average color of the input interval
      int sumRed = 0, sumGreen = 0, sumBlue = 0;
      int count = 0;
      for (int j = startInputIndex; j < endInputIndex; j++) {
        sumRed += input[j].getRed();
        sumGreen += input[j].getGreen();
        sumBlue += input[j].getBlue();
        count++;
      }
      int averageRed = sumRed / count;
      int averageGreen = sumGreen / count;
      int averageBlue = (sumBlue / count);

      // Set the current element in the output array to the average color of the input interval
      output[i] = new Color(averageRed, averageGreen, averageBlue);
    }

    return output;
  }

  public synchronized Dimension getResolution() {
    return new Dimension(image.getWidth() / 2, image.getHeight());
  }
}
