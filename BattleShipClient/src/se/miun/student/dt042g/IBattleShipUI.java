package se.miun.student.dt042g;

public interface IBattleShipUI {

	public void updateGameBoard(BaseBoard[] boards);

	public MessageMove getMove(EnumMoveResult lastMoveResult);

	public Message getLobbyChoice();

	public void Message(String message);

	public ShipCordinates getShipPlacement(BaseBoard board, String message,
			boolean xAlign);

	//public ShipCordinates placeShip(int ship);
	
	public Ship placeShip(int ship);

	//public ShipCordinates getShipPlacement(int i);

}
