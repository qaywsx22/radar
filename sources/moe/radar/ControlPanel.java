package moe.radar;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class ControlPanel extends JPanel  implements SerialPortEventListener {
	static final boolean debug = false;
    // move controls
	private JButton stepLeft;
	private JButton stepRight;
	private JButton cycleLeft;
	private JButton cycleRight;
	private JButton sequence;
	private JButton measure;
	private JButton stop;
	private JPanel moveBox;

	// parameter controls
	private JSpinner stepValue;
	private JSpinner sequenceLength;
	private JButton reset;
	private JLabel labSV;
	private JLabel labSL;
	private JPanel parameterBox;

	// connection controls
	private JComboBox comport;
	private JButton connect;
	private JButton disconnect;
	private JLabel labCP;
	private JPanel connectionBox;

	// log controls;
	private JButton clearLog;
	private JTextArea log;
	private JPanel logBox;
	private JScrollPane logsp;

	private ActionListener al;
	
	// communication
	private HashSet<CommPortIdentifier> comportSet;
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;	
    private boolean connected = false;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    // implementation stuff
//    private int currentPosition = 0;

    public ControlPanel(ActionListener l) {
		this.al = l;
		BoxLayout bl = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(bl);
		createControls();
		createLayout();
		fillPortComboBox();
		init();
	}

	private void init() {
		initControls();
		comport.setEnabled(true);
		connect.setEnabled(true);
		clearLog.setEnabled(true);
		log.setEnabled(true);
	}

	private void initControls() {
		stepLeft.setEnabled(connected || debug);
		stepRight.setEnabled(connected || debug);
		cycleLeft.setEnabled(connected || debug);
		cycleRight.setEnabled(connected || debug);
		sequence.setEnabled(connected || debug);
		measure.setEnabled(connected || debug);
		stop.setEnabled(connected || debug);
		stepValue.setEnabled(connected || debug);
		sequenceLength.setEnabled(connected || debug);
		reset.setEnabled(connected || debug);
		disconnect.setEnabled(connected || debug);
	}

	public void reset() {
		init();
	}
	
	private void createLayout() {
		createMoveBoxLayout();
		createParameterBoxLayout();
		createConnectionBoxLayout();
		createLogBoxLayout();
	}

	private void createControls() {
		createMoveControls();
		createParameterControls();
		createConnectionControls();
		createLogControls();
	}

	private void createMoveBoxLayout() {
		add(moveBox);
		GroupLayout layout = new GroupLayout(moveBox);
		moveBox.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		Component glue = Box.createGlue(); 
		
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
	        .addGroup(layout.createSequentialGroup()
	        	.addComponent(stepLeft, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		        .addComponent(stepRight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		        )
	        .addGroup(layout.createSequentialGroup()
		        .addComponent(cycleLeft, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		        .addComponent(cycleRight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		        )
		    .addComponent(sequence, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	        .addGroup(layout.createSequentialGroup()
	        	.addComponent(measure, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			    .addComponent(stop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			)
		    .addComponent(glue)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
				.addComponent(stepLeft)
				.addComponent(stepRight))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
				.addComponent(cycleLeft)
				.addComponent(cycleRight))
			.addComponent(sequence)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
				.addComponent(measure)
				.addComponent(stop))
		    .addComponent(glue)
		);
//		Border b = BorderFactory.createLoweredBevelBorder();
//		moveBox.setBorder(b);

		int maxHeight = stepLeft.getPreferredSize().height;
		Dimension max = new Dimension(Short.MAX_VALUE, maxHeight);
		stepLeft.setMaximumSize(max);
		stepRight.setMaximumSize(max);
		cycleLeft.setMaximumSize(max);
		cycleRight.setMaximumSize(max);
		sequence.setMaximumSize(max);
		measure.setMaximumSize(max);
		stop.setMaximumSize(max);

		max = new Dimension(moveBox.getPreferredSize().width, Short.MAX_VALUE);
		moveBox.setMaximumSize(max);
		moveBox.setAlignmentY(TOP_ALIGNMENT);
	}
	
	private void createParameterBoxLayout() {
		add(parameterBox);
		GroupLayout layout = new GroupLayout(parameterBox);
		parameterBox.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		Component strut = Box.createVerticalStrut(reset.getPreferredSize().height); 

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
			.addGroup(layout.createSequentialGroup()
					.addComponent(labSV, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(stepValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			.addGroup(layout.createSequentialGroup()
					.addComponent(labSL, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(sequenceLength, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			.addComponent(reset, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(strut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//			.addComponent(glue)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(labSV, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(stepValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(labSL)
					.addComponent(sequenceLength))
				.addComponent(reset)
				.addComponent(strut)
//				.addComponent(glue)
		);
		layout.linkSize(SwingConstants.HORIZONTAL, labSV, labSL);
		layout.linkSize(SwingConstants.HORIZONTAL, stepValue, sequenceLength);
//		Border b = BorderFactory.createLoweredBevelBorder();
//		parameterBox.setBorder(b);

		int maxHeight = reset.getPreferredSize().height;
		Dimension max = new Dimension(Short.MAX_VALUE, maxHeight);
		reset.setMaximumSize(max);
		labSV.setMaximumSize(max);
		stepValue.setMaximumSize(max);
		labSL.setMaximumSize(max);
		sequenceLength.setMaximumSize(max);

		max = new Dimension(parameterBox.getPreferredSize().width, Short.MAX_VALUE);
		parameterBox.setMaximumSize(max);
		parameterBox.setAlignmentY(TOP_ALIGNMENT);
	}

	private void createConnectionBoxLayout() {
		add(connectionBox);
		GroupLayout layout = new GroupLayout(connectionBox);
		connectionBox.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		Component strut1 = Box.createVerticalStrut(reset.getPreferredSize().height); 
//		Component strut2 = Box.createVerticalStrut(reset.getPreferredSize().height); 

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labCP, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(comport, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//			.addComponent(strut2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addGroup(layout.createSequentialGroup()
				.addComponent(connect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(disconnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			.addComponent(strut1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
				.addComponent(labCP)
				.addComponent(comport))
//			.addComponent(strut2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
				.addComponent(connect)
				.addComponent(disconnect))
			.addComponent(strut1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.linkSize(SwingConstants.HORIZONTAL, labCP, connect);
		layout.linkSize(SwingConstants.HORIZONTAL, comport, disconnect);
//		Border b = BorderFactory.createLoweredBevelBorder();
//		connectionBox.setBorder(b);

		int maxHeight = connect.getPreferredSize().height;
		Dimension max = new Dimension(Short.MAX_VALUE, maxHeight);
		connect.setMaximumSize(max);
		comport.setMaximumSize(max);
		disconnect.setMaximumSize(max);

		max = new Dimension(connectionBox.getPreferredSize().width, Short.MAX_VALUE);
		connectionBox.setMaximumSize(max);
		connectionBox.setAlignmentY(TOP_ALIGNMENT);
	}
	
	private void createLogBoxLayout() {
		add(logBox);

		GroupLayout layout = new GroupLayout(logBox);
		logBox.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
			.addComponent(logsp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(clearLog, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(logsp)
			.addComponent(clearLog)
		);

//		Border b = BorderFactory.createLoweredBevelBorder();
//		logBox.setBorder(b);

		logsp.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

		logBox.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		logBox.setAlignmentY(TOP_ALIGNMENT);
	}
	
	private void createMoveControls() {
		stepLeft = new JButton("Step <");
		stepLeft.setActionCommand("stepLeft");
		stepLeft.addActionListener(al);
		
		stepRight = new JButton("Step >");
		stepRight.setActionCommand("stepRight");
		stepRight.addActionListener(al);
		stepRight.setAlignmentX(1.0f);

		cycleLeft = new JButton("Cycle <");
		cycleLeft.setActionCommand("cycleLeft");
		cycleLeft.addActionListener(al);

		cycleRight = new JButton("Cycle >");
		cycleRight.setActionCommand("cycleRight");
		cycleRight.addActionListener(al);
		cycleRight.setAlignmentX(1.0f);

		sequence = new JButton("<-->");
		sequence.setActionCommand("sequence");
		sequence.addActionListener(al);

		measure = new JButton("Measure");
		measure.setActionCommand("measure");
		measure.addActionListener(al);

		stop = new JButton("Stop");
		stop.setActionCommand("stop");
		stop.addActionListener(al);
		
		moveBox = new JPanel();
	}
	
	private void createParameterControls() {
		reset = new JButton("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(al);

		labSV = new JLabel("Step value");

		stepValue = new JSpinner(new SpinnerNumberModel(Settings.INIT_STEP_VALUE, 1, 180, 1));

		labSL = new JLabel("Sequence length");

		// value 0 means sequence runs continuous
		sequenceLength = new JSpinner(new SpinnerNumberModel(Settings.INIT_SEQUENCE_LENGTH, 0, Short.MAX_VALUE, 1));
		
		parameterBox = new JPanel();
	}
	
	private void createConnectionControls() {
		connect = new JButton("Connect");
		connect.setActionCommand("connect");
		connect.addActionListener(al);

		labCP = new JLabel("COM Port");

		comport = new JComboBox();

		disconnect = new JButton("Disconnect");
		disconnect.setActionCommand("disconnect");
		disconnect.addActionListener(al);
		
		connectionBox = new JPanel();
	}

	private void createLogControls() {
		log = new JTextArea();
		log.setEditable(false);
		log.setRows(7);
		DefaultCaret caret = (DefaultCaret)log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		clearLog = new JButton("Clear log");
		clearLog.setActionCommand("clearLog");
		clearLog.addActionListener(al);

		logsp = new JScrollPane(log);
		Border b = BorderFactory.createLoweredBevelBorder();
		logsp.setBorder(b);

		logBox = new JPanel();
	}
	
	private void fillPortComboBox() {
		comportSet = ControlPanel.getAvailableSerialPorts();
		Iterator<CommPortIdentifier> it = comportSet.iterator();
		while (it.hasNext()) {
			CommPortIdentifier com = it.next();
			comport.addItem(com.getName());
		}
	}

	public void clearLog() {
		log.setText("");
    }

	/**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration<CommPortIdentifier> thePorts = (Enumeration<CommPortIdentifier>)CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() + ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }

    //connect to the selected port in the combo box
    public void connect() {
    	if (connected) {
    		JOptionPane.showMessageDialog(this, "Sonar is connected. To reconnect you should disconnect first");
    		return;
    	}
        String selectedPort = (String)comport.getSelectedItem();
        selectedPortIdentifier = null;
		Iterator<CommPortIdentifier> it = comportSet.iterator();
		while (it.hasNext()) {
			CommPortIdentifier com = it.next();
			if (selectedPort.equals(com.getName())) {
		        selectedPortIdentifier = com;
		        break;
			}
		}

        CommPort commPort = null;

        try {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("Radar", Settings.TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
            connected = true;
			if (initIOStream()) {
		        try {
		            serialPort.addEventListener(this);
		            serialPort.notifyOnDataAvailable(true);
		        }
		        catch (TooManyListenersException e) {
		        	System.out.println("Too many listeners. (" + e.toString() + ")");
		        }
			}
			
            //CODE ON SETTING BAUD RATE ETC OMITTED
            //ASSUMED TO HAVE SAME SETTINGS ALREADY
        }
        catch (PortInUseException e) {
            System.out.println(selectedPort + " is in use. (" + e.toString() + ")");
            connected = false;
        }
        catch (Exception e) {
        	System.out.println("Failed to open " + selectedPort + "(" + e.toString() + ")");
            connected = false;
        }
        finally {
        	initControls();
        }
    }

    //disconnect the serial port
    public void disconnect() {
    	if (!connected) {
    		return;
    	}
        //close the serial port
        try {
//            writeData(0, 0);
            serialPort.removeEventListener();
            serialPort.close();
            if (input != null) {
                input.close();
            }
            if (output != null) {
            	output.close();
            }
            connected = false;
        }
        catch (Exception e) {
        	System.out.println("Failed to close " + serialPort.getName()
                              + "(" + e.toString() + ")");
        }
        finally {
        	initControls();
        }
    }

    //open the input and output streams
    private boolean initIOStream() {
        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
//            writeData(0, 0);
            return true;
        }
        catch (IOException e) {
        	System.out.println("I/O Streams failed to open. (" + e.toString() + ")");
            return false;
        }
    }

//    public void setCurrentPosition(int position) {
//    	currentPosition = position;
//    }

    public void doStep(boolean rights) {
        try {
          output. write(Settings.S_ASCII);
          if (rights) {
              output. write(Settings.B_ASCII);
          }
          else {
              output.write(Settings.F_ASCII);
          }
          output. write(Settings.HASH_ASCII);
          String step = ((Integer)stepValue.getValue()).toString();
          for (int i=0; i < step.length(); i++) {
        	  byte digit = (byte)step.charAt(i);
              output. write(digit);
          }
//          output. write(step);
          output. write(Settings.END_MARK);
          output.flush();
      }
      catch (Exception e) {
      	System.out.println("doStep: Failed to write data. (" + e.toString() + ")");
      }
    }

    public void doCycle(boolean rights) {
        try {
          output. write(Settings.C_ASCII);
          if (rights) {
              output. write(Settings.B_ASCII);
          }
          else {
              output.write(Settings.F_ASCII);
          }
          output. write(Settings.HASH_ASCII);
          String step = ((Integer)stepValue.getValue()).toString();
          for (int i=0; i < step.length(); i++) {
        	  byte digit = (byte)step.charAt(i);
              output. write(digit);
          }
//          output. write(step);
          output. write(Settings.END_MARK);
          output.flush();
      }
      catch (Exception e) {
      	System.out.println("doCycle: Failed to write data. (" + e.toString() + ")");
      }
    }

    public void doMeasure() {
        try {
            output. write(Settings.M_ASCII);
            output. write(Settings.S_ASCII);
            output. write(Settings.HASH_ASCII);
            output. write(Settings.END_MARK);
            output.flush();
        }
        catch (Exception e) {
        	System.out.println("doMeasure: Failed to write data. (" + e.toString() + ")");
        }
    }

    //method that can be called to send data
    public void writeData(int leftThrottle, int rightThrottle) {
        try {
//            output.write(leftThrottle);
//            output.flush();
//            //this is a delimiter for the data
//            output.write(DASH_ASCII);
//            output.flush();
//
//            output.write(rightThrottle);
//            output.flush();
//            //will be read as a byte so it is a space key
//            output.write(SPACE_ASCII);
//            output.flush();
        }
        catch (Exception e) {
        	System.out.println("Failed to write data. (" + e.toString() + ")");
        }
    }    

    //what happens when data is received
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                byte singleData = (byte)input.read();

                if (singleData != Settings.NEW_LINE_ASCII) {
                    String logText = new String(new byte[] {singleData});
                    log.append(logText);
                }
                else {
                    log.append("\n");
                }
            }
            catch (Exception e) {
            }
        }
    }
}
