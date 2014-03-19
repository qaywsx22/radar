package moe.radar;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class TargetPlane extends JPanel {

	private Display display;

	TargetPlane(Display display) {
		super();
		this.display = display;
		setOpaque(false);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		Color oldColor = g.getColor();
		g.setColor(Settings.BEAM_COLOR);
		int centerX = width / 2;
		int centerY = height; 
		int radius = display.getRadius();
		double sectorAngle = Math.PI / (2.0d *(double)Settings.SECTOR_COUNT);
		Pulse[] displayBuffer = display.getDisplayBuffer();
		for (int i=0; i<displayBuffer.length; i++) {
			Pulse p = displayBuffer[i];
			if (p != null) {
				if (p.distance > radius) {
					continue;
				}
				sectorAngle = Settings.COEFF * (double)p.angle;
				int x = (int)Math.round((double)p.distance * Math.cos(sectorAngle));
				int y = (int)Math.round((double)p.distance * Math.sin(sectorAngle));
				g.fillOval(x + centerX, centerY - y, Settings.TARGET_POINT_DIAMETER, Settings.TARGET_POINT_DIAMETER);
			}
		}
		g.setColor(oldColor);
	}

}
