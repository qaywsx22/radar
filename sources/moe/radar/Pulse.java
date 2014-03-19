package moe.radar;

class Pulse {
	public int angle = 0;
	public int distance = Short.MAX_VALUE;
	
	Pulse (int angle, int distance) {
		this.angle = angle;
		this.distance = distance;
	}
}
