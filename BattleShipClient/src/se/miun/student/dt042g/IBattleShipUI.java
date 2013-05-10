package se.miun.student.dt042g;

public interface IBattleShipUI {
	
	public void updateGameBoard(BaseBoard[] boards);
	
	public MessageMove getMove(EnumMoveResult lastMoveResult);
	
	public ShipPlacement getPlacement();

	public Message getLobbyChoice();

	public int getMoveX();

	public int getMoveY();	
	
	public void Message(String message);
}
