
package moe.radar;

import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class Display extends JPanel {
  static final boolean debug = true;
  
  private Pulse[] displayBuffer = new Pulse[Settings.DISPLAY_BUFFER_LENGTH];

  private BeamPlane beamPlane;
  private ScalePlane scalePlane;
  private TargetPlane targetPlane;
  
  public Display() {
    setBorder(Settings.MARGIN);
    setLayout(new OverlayLayout(this));
    targetPlane = new TargetPlane(this);
    beamPlane = new BeamPlane(this);
    scalePlane = new ScalePlane(this);
    add(scalePlane);
    add(targetPlane);
    add(beamPlane);
  }

  Pulse[] getDisplayBuffer() {
    return displayBuffer;
  }

  void init(boolean clear) {
    initDisplayBuffer(clear);
    beamPlane.init();
  }

  void refresh() {
    repaint();
    beamPlane.refresh();
  }

  int getRadius() {
    int width = getWidth();
    int height = getHeight();
    Insets ins = getInsets();
    if (ins != null) {
      width -= ins.left + ins.right;
      height -= ins.top + ins.bottom;
    }
    int step = height / Settings.CIRCLE_COUNT;
    int radius = Settings.CIRCLE_COUNT * step;
    int w = 2 * radius;
    if (w > width) {
      step = width / (2 * Settings.CIRCLE_COUNT);
      radius = Settings.CIRCLE_COUNT * step;
    }
    return radius;
  }

  void initDisplayBuffer(boolean clear) {
    Pulse p = null;
    Random rand = null;
    int radius = 0;
    if (!clear) {
      rand = new Random();
      radius = getRadius();
    }
    for (int i=0; i<Settings.DISPLAY_BUFFER_LENGTH; i++) {
      if (!clear) {
        p = new Pulse(rand.nextInt(181), rand.nextInt(radius));
      }
      displayBuffer[i] = p;
    }
  }
}
