package moe.radar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;

public class ScalePlane extends JPanel {

	private Display display;
	
	ScalePlane(Display display) {
		super();
		this.display = display;
		setOpaque(false);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		Color oldColor = g.getColor();
		g.setColor(Settings.LINE_COLOR);

		int centerX = width / 2;
		int centerY = height; 
		int startOffsetX = 0;
		int startOffsetY = 0;
		Insets ins = getInsets();
		if (ins != null) {
			width -= ins.left + ins.right;
			height -= ins.top + ins.bottom;
			startOffsetX = ins.left;
			startOffsetY = ins.top;
		}
		int radius = display.getRadius();
		int step = radius / Settings.CIRCLE_COUNT;
		int w = 2 * radius;
	
		startOffsetX += (width - w) / 2;
		startOffsetY += height - radius;
		
		for (int i = 0; i < Settings.CIRCLE_COUNT; i++) {
			g.drawArc(i * step + startOffsetX, i * step + startOffsetY, w - i * 2 * step, 2 * (radius - i * step), 0, 180);
		}

		double sectorAngle = Math.PI / (2.0d *(double)Settings.SECTOR_COUNT);
		for (int i = 0; i<= Settings.SECTOR_COUNT; i++) {
			int x = (int)Math.round((double)radius * Math.cos((double)i * sectorAngle));
			int y = (int)Math.round((double)radius * Math.sin((double)i * sectorAngle));
			g.drawLine(centerX, centerY - 1, centerX + x, centerY - y - 1);
			if (i < Settings.SECTOR_COUNT) {
				g.drawLine(centerX, centerY - 1, centerX - x, centerY - y - 1);
			}
		}
		g.setColor(oldColor);
	}
}
