package ua.knu.rotan.sfft.view;

import jakarta.annotation.PostConstruct;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.knu.rotan.sfft.config.UIProperties;

@Component
public class MainWindow extends JFrame {
  @Autowired private UIProperties uiConfig;
  private final List<SlidingImagePanel> spectrograms = new ArrayList<>();
  private final JPanel centerPanel = new JPanel();
  private final List<SliderPanel> sliders = new ArrayList<>();
  private final JPanel controlPanel = new JPanel();

  @PostConstruct
  private void initUI() {
    setTitle("My Frame");
    setExtendedState(Frame.MAXIMIZED_BOTH);
    setBackground(Color.BLACK);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setFocusTraversalKeysEnabled(false);

    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    add(centerPanel, BorderLayout.CENTER);

    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    add(controlPanel, BorderLayout.EAST);
    addKayPressedAction(KeyEvent.VK_TAB, () -> controlPanel.setVisible(!controlPanel.isVisible()));

    setPreferredSize(new Dimension(uiConfig.width, uiConfig.height));
    pack();
  }

  public void addSpectrogram() {
    int maxBufferImageWidth = uiConfig.renderWidth;
    int maxHeight = uiConfig.renderHeight;

    SlidingImagePanel spectrogram = new SlidingImagePanel(maxBufferImageWidth, maxHeight);
    centerPanel.add(spectrogram);
    spectrograms.add(spectrogram);
  }

  public void addSlider(SliderPanel slider) {
    controlPanel.removeAll();
    sliders.add(slider);

    JPanel innerPanel = new JPanel();
    controlPanel.add(innerPanel, BorderLayout.WEST);
    innerPanel.setLayout(new GridLayout(2, 0));
    for (int i = 0; i < sliders.size() - sliders.size() % 2; i++) innerPanel.add(sliders.get(i));
    if (sliders.size() % 2 != 0)
      controlPanel.add(sliders.get(sliders.size() - 1), BorderLayout.EAST);
  }

  public SlidingImagePanel getSpectrogram(int i) {
    return spectrograms.get(i);
  }

  public void addKayPressedAction(int kayKode, Runnable action) {
    Action wrappedAction =
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            action.run();
          }
        };

    String actionID = String.format("kay code : '%s' pressed", kayKode);
    getRootPane()
        .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(kayKode, 0), actionID);
    getRootPane().getActionMap().put(actionID, wrappedAction);
  }

  public void addSpectrogram(int channels) {
    for (int i = 0; i < channels; i++) addSpectrogram();
  }
}
