package ua.knu.rotan.sfft.view;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Function;
import javax.swing.*;
import ua.knu.rotan.sfft.alg.ValueRange;

public class SliderPanel extends JPanel {
  public static final int MAX_LABELS = 21;
  private final JSlider slider;
  private final JLabel label;
  private final ValueRange range;

  public SliderPanel(String name, ValueRange range, Function<Integer, String> marksLabel) {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.range = range;

    slider = new JSlider(JSlider.VERTICAL, range.getMin(), range.getMax(), range.getValue());
    addLabels(slider, marksLabel);

    slider.addChangeListener((e) -> range.setValue(slider.getValue()));
    slider.setFocusable(false);

    label = new JLabel(name);
    label.setAlignmentX(0.5f);
    label.setUI(new VerticalLabelUI(false));

    add(label);
    add(slider);
  }

  private void addLabels(JSlider slider, Function<Integer, String> marksLabel) {
    int numLabels = Math.min(slider.getMaximum() - slider.getMinimum() + 1, MAX_LABELS);

    slider.setLabelTable(
        createLabelTable(slider.getMinimum(), slider.getMaximum(), numLabels, marksLabel));
    slider.setMajorTickSpacing((slider.getMaximum() - slider.getMinimum()) / (numLabels - 1));
    slider.setMinorTickSpacing((slider.getMaximum() - slider.getMinimum()) / (numLabels - 1) / 2);
    slider.setPaintTicks(true);
    slider.setSnapToTicks(true);
    slider.setPaintLabels(true);
  }

  private static Dictionary<Integer, JLabel> createLabelTable(
      int start, int end, int numLabels, Function<Integer, String> function) {
    Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
    double interval = (double) (end - start) / (numLabels - 1);

    for (int i = 0; i < numLabels; i++) {
      int value = (int) Math.round(start + i * interval);
      labelTable.put(value, new JLabel(function.apply(value)));
    }

    return labelTable;
  }

  public int getValue() {
    return range.getValue();
  }

  public void setValue(int value) {
    range.setValue(value);
  }
}
