package ua.knu.rotan.sfft.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SlidingImagePanel extends VerticalPanelNavigator {
  private final BufferedImage image;
  private int shift;

  public SlidingImagePanel(int maxWidth, int maxHeight) {
    super();
    this.shift = 0;
    setPreferredSize(new Dimension(maxWidth, maxHeight));
    setVisible(true);
    image = new BufferedImage(maxWidth * 2, maxHeight, BufferedImage.TYPE_INT_RGB);
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

  public synchronized void update(Color[] raw) {
    Color[] resizedRow = boxScaling1D(raw, image.getHeight());
    for (int y = 0; y < resizedRow.length; y++) {
      image.setRGB(shift, y, resizedRow[y].getRGB());
      image.setRGB(shift + image.getWidth() / 2, y, resizedRow[y].getRGB());
    }
    shift = (shift + 1) % (image.getWidth() / 2);
  }

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
