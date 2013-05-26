package se.miun.student.dt042g;

import java.io.IOException;
import java.net.ServerSocket;

public class BattleShipLobby {

	private PlayerInterface playerOne;
	private PlayerInterface playerTwo;
	private ServerSocket ss;
	private Message inputData;
	private EnumLobbyChoice choice;
	private EnumLobbyState state;

	/**
	 * Konstruktor f�r klassen
	 * 
	 * Tar tar in en port som den ska lyssna p�
	 * 
	 * @param port
	 *            int
	 */
	public BattleShipLobby(int port) {

		state = EnumLobbyState.EMPTY;

		try {
			ss = new ServerSocket(port); // Skapar en serversocket
			System.out.println("BattlShip Server lyssnar p� port: " + port);
		} catch (IOException e) {
			System.out
					.println("BattlShip Server failed to create ServerSocket on: "
							+ port);
			return;
		}
	}

	public void run() {
		while (true) {
			playerOne = getPlayer();
			state = EnumLobbyState.PLAYERWAITING;
			playerTwo = getPlayer();
			state = EnumLobbyState.EMPTY;

			new BattleShipGameThred(playerOne, playerTwo).start();
		}
	}

	private PlayerInterface getPlayer() {

		while (true) { // Evig loop, tar emot nya spelare bryter och returnerar
						// n�r en spelar valt att spela mot annan spelare
			try {
				PlayerInterface player = new Player(ss.accept());

				// System.out.println("Anslutning etablerad fr�n: " +
				// player.getInetAddress().getHostAddress());

				// Skicka lobbyinfo till spelare
				player.getMessage(new MessageLobbyStatus(state));

				inputData = player.sendMessage();
				if (inputData.getHeader() != EnumHeader.LOBBYCHOICE) {
					//Om spelaren svarar n�got annat �n det han �r ombedd att svara s� sparkas han ut.
					player.close();
					continue;
				} else {
					choice = ((MessageLobbyChoice) inputData).getChoice();
				}

				// om spelare vill spela mot ai s� skapas spel och continue k�rs
				// f�r att fors�tta lyssna efter spelare
				if (choice == EnumLobbyChoice.WAIT_FOR_PLAYER) {
					System.out
							.println("Spelare har valt att spela mot en annan spelare.");
					return player; // F�r att g� ur while loopen och ta emot
									// n�sta spelare.
				} else {
					System.out.println("Spelare har valt att spela mot AI.");
					//new BattleShipGameThred(player, new PlayerAI()).start();
					new BattleShipGameThred(player, new PlayerAISmart()).start();
					continue; // G�r till b�rjan, av while loopen f�r att h�mta
								// en ny spelare
				}
			} catch (IOException e) {
				System.out.println("IOException i BattleShipLobby.java::getplayer");
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("NullPointerException i BattleShipLobby.java::getplayer");
				System.out.println("Om detta meddelande loopar beror det p� att servern inte kan �ppna socket f�r port 5511");
				e.printStackTrace();
			}
		}
	}
}