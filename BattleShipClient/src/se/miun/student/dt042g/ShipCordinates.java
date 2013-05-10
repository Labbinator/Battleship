package se.miun.student.dt042g;

public class ShipCordinates {
	private int x;
	private int y;
	private boolean xAlign;
	
	public ShipCordinates(int x, int y, boolean xAlign) {
		this.x = x;
		this.y = y;
		this.xAlign = xAlign;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getXAlign() {
		return xAlign;
	}

}
