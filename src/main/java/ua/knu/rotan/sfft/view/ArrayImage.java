package ua.knu.rotan.sfft.view;

import static ua.knu.rotan.sfft.view.ColorMapper.getPurpleYellowGradient;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import ua.knu.rotan.sfft.alg.CycleBuffer;

public class ArrayImage extends JFrame {
  private final CycleBuffer<double[]> array;
  private final int width;
  private final int height;

  private final BufferedImage image;

  public ArrayImage(int width, int height) {
    this(new double[width][height]);
  }

  public ArrayImage(double[][] array) {
    this.array = new CycleBuffer<>(array);
    this.width = array.length;
    this.height = array[0].length;
    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  public void update(double[] column) {
    array.addInEnd(column);
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        double value = array.get(col)[row];
        g.setColor(getPurpleYellowGradient(value));
        g.fillRect(col, row, 1, 1);
      }
    }
  }
}
