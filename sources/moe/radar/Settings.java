package moe.radar;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Settings {
	static final Color BACKGROUND = Color.BLACK;
	static final Color LINE_COLOR = new Color(44, 169, 5);
	static final Color BEAM_COLOR = new Color(76, 229, 27);
	static final int CIRCLE_COUNT = 5;
	static final int SECTOR_COUNT = 4;
	static final int TARGET_POINT_DIAMETER = 5;
	static final Border MARGIN = new EmptyBorder(2, 2, 2, 2);
	static final int DISPLAY_BUFFER_LENGTH = 16;
	static final Dimension INIT_FRAME_SIZE = new Dimension(980, 720);
	static final int FADE_COUNT = 30;
	static final int MAX_STEPS = 180;
	static final int MAX_ANGLE = 180;
	static final double COEFF = Math.PI / 180.0d;
	static final Color[] FADE_COLORS = Utils.getFadingColors(BEAM_COLOR.darker(), FADE_COUNT, 0.1f);
	static final int DISPLAY_REFRESH_INTERVAL = 50;
	// Control panel settings 
	static final int INIT_STEP_VALUE = 10;
	static final int INIT_SEQUENCE_LENGTH = 2;
	//the timeout value for connecting with the port
	final static int TIMEOUT = 2000;
	//some ascii values for for certain things
	final static int B_ASCII = 66;
	final static int C_ASCII = 67;
	final static int F_ASCII = 70;
	final static int M_ASCII = 77;
	final static int S_ASCII = 83;
//	final static int TAB_ASCII = 9;
	final static int HASH_ASCII = 35;
	final static int END_MARK = 255;
	final static int NEW_LINE_ASCII = 10;
}
