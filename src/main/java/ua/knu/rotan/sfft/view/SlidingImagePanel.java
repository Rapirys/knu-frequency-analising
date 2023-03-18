package ua.knu.rotan.sfft.view;

import static ua.knu.rotan.sfft.view.ColorMapper.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SlidingImagePanel extends JPanel {
  private final BufferedImage image;
  private final int width;
  private final int height;
  private int shift;

  public SlidingImagePanel(int width, int height) {
    this.width = width;
    this.height = height;
    this.shift = 0;
    setSize(width, height);
    setVisible(true);
    image = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_RGB);
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, width, height, shift, 0, shift + width, height, this);
  }

  public void update(double[] raw) {
    for (int y = 0; y < height; y++) {
      Color color = getBlackBodyGradient(raw[y]);
      image.setRGB(shift, y, color.getRGB());
      image.setRGB(shift + width, y, color.getRGB());
    }
    shift = (shift + 1) % width;
  }
}
