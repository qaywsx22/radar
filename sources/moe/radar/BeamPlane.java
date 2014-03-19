package moe.radar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;

public class BeamPlane extends JPanel {
	static final int FADE_COUNT = 4;
	static final int MAX_STEPS = 135;
	static double coeff = Math.PI / 180.0d;

	private Color currentFadeColor;
	private int angleStep;
	private int currentAngle;
	private Display display;

	BeamPlane(Display display) {
		super();
		this.display = display;
		setOpaque(false);
		init();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		Color oldColor = g.getColor();
		int radius = display.getRadius();
		currentFadeColor = Display.BEAM_COLOR;
		g.setColor(currentFadeColor);
		int startOffsetX = 0;
		int startOffsetY = 0;
		Insets ins = getInsets();
		if (ins != null) {
			width -= ins.left + ins.right;
			height -= ins.top + ins.bottom;
			startOffsetX = ins.left;
			startOffsetY = ins.top;
		}
		int w = 2 * radius;
	
		int centerX = width / 2;
		int centerY = height; 
		int x = (int)Math.round((double)w * Math.cos(coeff * currentAngle));
		int y = (int)Math.round((double)w * Math.sin(coeff * currentAngle));
		g.drawLine(centerX, centerY - 1, centerX + x, centerY - y - 1);
		currentFadeColor = currentFadeColor.darker(); 
		int fadeAngle = currentAngle; 
		w = 2 * Math.max(width, height);
		startOffsetX += (width - w) / 2;
		startOffsetY += height - w / 2;
		for (int i = 0; i< FADE_COUNT; i++) {
			fadeAngle -= angleStep; 
			if (fadeAngle < 0) {
				break;
			}
			g.setColor(currentFadeColor);
			g.fillArc(startOffsetX, startOffsetY, w, w, fadeAngle, angleStep);
			currentFadeColor = currentFadeColor.darker(); 
		}
		g.setColor(oldColor);
	}
	
	void init() {
		angleStep = 180 / MAX_STEPS;
		currentAngle = 0;
	}
	
	void refresh() {
		currentAngle += angleStep;
		if (Math.abs(currentAngle) >= 180) {
			currentAngle = 0;
		}
	}
}
