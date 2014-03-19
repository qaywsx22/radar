package moe.radar;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class SweepUnit {
	static final int DISPLAY_REFRESH_INTERVAL = 75;

	private Timer displayRefreshTimer;
	private Display display;
	private ActionListener al;
	
	SweepUnit(Display display, ActionListener l) {
		this.display = display;
		al = l;
	}

	public void start() {
		if (displayRefreshTimer == null) {
			displayRefreshTimer = new Timer(DISPLAY_REFRESH_INTERVAL, al);
			displayRefreshTimer.setActionCommand("refreshDisplay");
		}
		if (displayRefreshTimer.isRunning())  {
			displayRefreshTimer.stop();
		}
		display.init(false);
		displayRefreshTimer.start();
	}

	public void stop() {
		if (displayRefreshTimer == null) {
			return;
		}
		if (displayRefreshTimer.isRunning())  {
			displayRefreshTimer.stop();
		}
	}

	public void reset() {
		stop();
		display.init(true);
		display.repaint();
	}
}
