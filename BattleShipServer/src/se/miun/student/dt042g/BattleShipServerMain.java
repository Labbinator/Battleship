package se.miun.student.dt042g;

public class BattleShipServerMain {

	public static final int PORT = 5511;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BattleShipLobby lobby = new BattleShipLobby(PORT);
		lobby.run();
	}
}
