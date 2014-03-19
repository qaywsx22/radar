package moe.radar;

import java.awt.Color;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Display extends JPanel {
	static final Color BACKGROUND = Color.BLACK;
	static final Color LINE_COLOR = new Color(44, 169, 5);
	static final Color BEAM_COLOR = new Color(76, 229, 27);
	private static final Border MARGIN = new EmptyBorder(2, 2, 2, 2);
	static final int CIRCLE_COUNT = 5;
	static final int SECTOR_COUNT = 4;
	static final int TARGET_POINT_DIAMETER = 10;
	private static final int DISPLAY_BUFFER_LENGTH = 16;
	static final boolean debug = true;
	
	private Pulse[] displayBuffer = new Pulse[DISPLAY_BUFFER_LENGTH];

	private BackgroundPlane backgroundPlane;
	private BeamPlane beamPlane;
	private ScalePlane scalePlane;
	private TargetPlane targetPlane;
	
	public Display() {
		setBorder(MARGIN);
		setLayout(new OverlayLayout(this));
		backgroundPlane = new BackgroundPlane();
		targetPlane = new TargetPlane(this);
		beamPlane = new BeamPlane(this);
		scalePlane = new ScalePlane(this);
		add(scalePlane);
		add(targetPlane);
		add(beamPlane);
		add(backgroundPlane);
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
		int step = height / CIRCLE_COUNT;
		int radius = CIRCLE_COUNT * step;
		int w = 2 * radius;
		if (w > width) {
			step = width / (2 * CIRCLE_COUNT);
			radius = CIRCLE_COUNT * step;
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
		for (int i=0; i<DISPLAY_BUFFER_LENGTH; i++) {
			if (!clear) {
				p = new Pulse(rand.nextInt(181), rand.nextInt(radius));
			}
			displayBuffer[i] = p;
		}
	}
}
