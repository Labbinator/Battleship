package se.miun.student.dt042g;

public interface IBattleShipUI {
	
	public void updateGameBoard(GameBoard[] gameBoard);
	
	public MessageMove getMove(boolean tryAgain);
	
	public ShipPlacement getPlacement();

	public Message getLobbyChoice();

	public int getMoveX();

	public int getMoveY();	
}
