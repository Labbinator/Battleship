package se.miun.student.dt042g;

public interface IBattleShipUI {

	public void updateGameBoard(BaseBoard[] boards);

	public MessageMove getMove(EnumMoveResult lastMoveResult);

	public Message getLobbyChoice();

	public void Message(String message);

	public ShipCordinates getShipPlacement(BaseBoard board, String message,
			boolean xAlign);

	public Ship placeShip(int ship);

}
