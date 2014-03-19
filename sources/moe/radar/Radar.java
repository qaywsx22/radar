package moe.radar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class Radar  implements ActionListener {
	static final boolean debug = true;
	
	private Display display;
	private ControlPanel controlPanel;
	private SweepUnit sweepUnit;
	
	public Radar() {
		display = new Display();
		sweepUnit = new SweepUnit(display, this);
		controlPanel = new ControlPanel(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Radar r = new Radar();
		Display d = r.getDisplay();
		MyFrame sonar = new MyFrame(r); 
		JToolBar tb = new JToolBar();
		JButton exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(sonar);
		tb.add(exit);
		sonar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sonar.setSize(Settings.INIT_FRAME_SIZE);
		sonar.getContentPane().setLayout(new BorderLayout());
		sonar.getContentPane().add(d, BorderLayout.CENTER);
		sonar.getContentPane().add(tb, BorderLayout.NORTH);
		ControlPanel cp = r.getControlPanel();
		sonar.getContentPane().add(cp, BorderLayout.SOUTH);
		sonar.setTitle("Sonar");
		sonar.setVisible(true);
	}

	public SweepUnit getSweepUnit() {
		return sweepUnit;
	}

	public Display getDisplay() {
		return display;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public void actionPerformed(ActionEvent e) {
    	String command = e.getActionCommand();
//    	System.out.println(command + " received");
    	if ("refreshDisplay".equals(command)) {
//    		display.repaint();
    		display.refresh();
    	}
    	else if ("measure".equals(command)) {
    		controlPanel.doMeasure();
    	}
    	else if ("stop".equals(command)) {
//    		display.stop();
    		sweepUnit.stop();
    	}
    	else if ("reset".equals(command)) {
//    		display.reset();
    		sweepUnit.reset();
    		controlPanel.reset();
//    		display.start();
    		sweepUnit.start();
    	}
    	else if ("connect".equals(command)) {
    		controlPanel.connect();
    	}
    	else if ("disconnect".equals(command)) {
    		controlPanel.disconnect();
    	}
    	else if ("clearLog".equals(command)) {
    		controlPanel.clearLog();
    	}
    	else if ("stepLeft".equals(command)) {
    		controlPanel.doStep(false);
    	}
    	else if ("stepRight".equals(command)) {
    		controlPanel.doStep(true);
    	}
    	else if ("cycleLeft".equals(command)) {
    		controlPanel.doCycle(false);
    	}
    	else if ("cycleRight".equals(command)) {
    		controlPanel.doCycle(true);
    	}
    }

}

class MyFrame extends JFrame implements ActionListener {
	private Radar radar;
	MyFrame(Radar radar) {
		this.radar = radar;
	}
	public void actionPerformed(ActionEvent e) {
		if ("exit".equals(e.getActionCommand())) {
			if (radar != null) {
//				radar.getDisplay().reset();
				radar.getSweepUnit().reset();
				radar.getControlPanel().disconnect();
			}
			MyFrame.this.setVisible(false);
			MyFrame.this.dispose();
		}
	}
}
