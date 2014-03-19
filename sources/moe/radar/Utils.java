package moe.radar;

import java.awt.Color;

public class Utils {
	static Color[] getFadingColors(Color startColor, int levelCount, float lowBound) {
		Color[] res = new Color[levelCount];
		if (startColor != null) {
			float[] hsb = new float[3];
			Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), hsb);
			float startBrightness = hsb[2];
			float endBrightness = startBrightness * lowBound;
			float step = (startBrightness - endBrightness) / levelCount;
			for (int i = 0; i < levelCount; i++) {
				hsb[2] -= step;
				res[i] = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
			}
		}
		return res;
	}

}
