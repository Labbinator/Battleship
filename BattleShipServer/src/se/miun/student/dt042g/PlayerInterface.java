package se.miun.student.dt042g;

public interface PlayerInterface {
	
	public Message sendMessage();
	
	public void getMessage(Message mess);
	
	public void close();
	
	public void setPlacement(GameBoard p);
	
	public GameBoard getPlacement();
}
