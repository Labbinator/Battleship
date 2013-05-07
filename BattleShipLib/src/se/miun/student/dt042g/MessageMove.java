package se.miun.student.dt042g;

public class MessageMove extends Message {
	
	public final int x;
	public final int y;
	
	public MessageMove(int x, int y){
		super(EnumHeader.MOVE);
		this.x = x;
		this.y = y;
	}
}
