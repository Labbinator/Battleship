package se.miun.student.dt042g;

public interface PlayerInterface {
	
	public Message sendMessage() throws Exception;
	
	public void getMessage(Message mess) throws Exception;
	
	public void close();
	
	public void setPlacement(GameBoard p);
	
	public GameBoard getPlacement();
}
