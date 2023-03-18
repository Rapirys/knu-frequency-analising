package ua.knu.rotan.sfft.view;

import java.awt.*;
import javax.swing.*;

public class AppFrame extends JFrame {
  private JPanel panel1, panel2;
  private JSlider slider;

  public AppFrame() {
    // Set up the JFrame
    super("My Frame");
    setExtendedState(JFrame.MAXIMIZED_BOTH); // Set full screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create the two panels
    panel1 = new JPanel();
    panel1.setPreferredSize(new Dimension(1024, 256));
    panel1.setBackground(Color.RED);

    panel2 = new JPanel();
    panel2.setAlignmentY(257);
    panel2.setPreferredSize(new Dimension(1024, 256));
    panel2.setBackground(Color.BLUE);

    // Create the slider
    slider = new JSlider();
    slider.setMinimum(0);
    slider.setMaximum(100);
    slider.setValue(50);

    // Add the components to the JFrame
    add(panel1); // , BorderLayout.CENTER);
    add(panel2); // , BorderLayout.CENTER);
    add(slider, BorderLayout.SOUTH);

    // Display the JFrame
    pack();
    setVisible(true);
  }

  public static void main(String[] args) {
    // Create an instance of the MyFrame class
    new AppFrame();
  }
}
