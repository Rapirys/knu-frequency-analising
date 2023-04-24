package ua.knu.rotan.sfft.view;

import static java.lang.Math.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.*;
import lombok.Getter;

public class VerticalPanelNavigator extends JPanel {
  private static final double ZOOM_INTENSITY = 0.05;
  private static final double MAX_ZOOM = 50;

  @Getter private double minY = 0, maxY = 1;
  private double pressPosition = 0;

  protected VerticalPanelNavigator() {
    addMouseWheelListener(this::zoom);

    addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            synchronized (this) {
              pressPosition = (double) e.getY() / getHeight();
            }
          }
        });
    addMouseMotionListener(
        new MouseAdapter() {
          public void mouseDragged(MouseEvent e) {
            synchronized (this) {
              double currentPressPosition = (double) e.getY() / getHeight();
              double dy = pressPosition - currentPressPosition;
              translate((dy) * (maxY - minY));
              pressPosition = (double) e.getY() / getHeight();
            }
          }
        });
  }

  private synchronized void translate(double dy) {
    maxY = maxY + dy;
    minY = minY + dy;

    returnInRangeByShift();
  }

  private synchronized void returnInRangeByShift() {
    double interval = maxY - minY;
    maxY = min(maxY, 1);
    minY = max(maxY - interval, 0);
    minY = max(minY, 0);
    maxY = min(minY + interval, 1);
  }

  private void zoom(MouseWheelEvent e) {
    final double wheelChange = e.getWheelRotation() < 0 ? -1 : 1;
    double zoom = Math.exp(wheelChange * ZOOM_INTENSITY);
    if (zoom * (maxY - minY) < 1 / MAX_ZOOM) return;

    final double mouseY = (double) e.getY() / getHeight() * (maxY - minY) + minY;
    minY = mouseY * (1 - zoom) + minY * zoom;
    maxY = mouseY * (1 - zoom) + maxY * zoom;

    returnInRangeByShift();
  }
}
