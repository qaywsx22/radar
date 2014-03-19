package moe.radar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;

public class BeamPlane extends JPanel {
	private int angleStep;
	private int currentAngle;
	private Display display;

	BeamPlane(Display display) {
		super();
		this.display = display;
		setOpaque(true);
		setBackground(Settings.BACKGROUND);
		init();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		Color oldColor = g.getColor();
		int radius = display.getRadius();
		g.setColor(Settings.BEAM_COLOR);
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
		int x = (int)Math.round((double)w * Math.cos(Settings.COEFF * currentAngle));
		int y = (int)Math.round((double)w * Math.sin(Settings.COEFF * currentAngle));
		g.drawLine(centerX, centerY - 1, centerX + x, centerY - y - 1);
		int fadeAngle = currentAngle; 
		w = 2 * Math.max(width, height);
		startOffsetX += (width - w) / 2;
		startOffsetY += height - w / 2;
		for (int i = 0; i< Settings.FADE_COUNT; i++) {
			fadeAngle -= angleStep; 
			if (fadeAngle < 0) {
				break;
			}
			g.setColor(Settings.FADE_COLORS[i]);
			g.fillArc(startOffsetX, startOffsetY, w, w, fadeAngle, angleStep);
		}
		g.setColor(oldColor);
	}
	
	void init() {
		angleStep = Settings.MAX_ANGLE / Settings.MAX_STEPS;
		currentAngle = 0;
	}
	
	void refresh() {
		currentAngle += angleStep;
		if (Math.abs(currentAngle) >= Settings.MAX_ANGLE) {
			currentAngle = 0;
		}
	}
	
	int getAngleStep() {
		return angleStep;
	}
	
	int getCurrentAngle() {
		return currentAngle;
	}
}
