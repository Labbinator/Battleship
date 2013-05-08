package se.miun.student.dt042g;

public class MessageMove extends Message {
	
	private final int x;
	private final int y;
	
	public MessageMove(int x, int y){
		super(EnumHeader.MOVE);
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
