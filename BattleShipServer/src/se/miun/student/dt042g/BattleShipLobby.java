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
	 * Konstruktor för klassen
	 * 
	 * Tar tar in en port som den ska lyssna på
	 * 
	 * @param port
	 *            int
	 */
	public BattleShipLobby(int port) {

		state = EnumLobbyState.EMPTY;

		try {
			ss = new ServerSocket(port); // Skapar en serversocket
			System.out.println("BattlShip Server lyssnar på port: " + port);
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
						// när en spelar valt att spela mot annan spelare
			try {
				PlayerInterface player = new Player(ss.accept());

				// System.out.println("Anslutning etablerad från: " +
				// player.getInetAddress().getHostAddress());

				// Skicka lobbyinfo till spelare
				player.getMessage(new MessageLobbyStatus(state));

				inputData = player.sendMessage();
				if (inputData.getHeader() != EnumHeader.LOBBYCHOICE) {
					//Om spelaren svarar något annat än det han är ombedd att svara så sparkas han ut.
					player.close();
					continue;
				} else {
					choice = ((MessageLobbyChoice) inputData).getChoice();
				}

				// om spelare vill spela mot ai så skapas spel och continue körs
				// för att forsätta lyssna efter spelare
				if (choice == EnumLobbyChoice.WAIT_FOR_PLAYER) {
					System.out
							.println("Spelare har valt att spela mot en annan spelare.");
					return player; // För att gå ur while loopen och ta emot
									// nästa spelare.
				} else {
					System.out.println("Spelare har valt att spela mot AI.");
					//new BattleShipGameThred(player, new PlayerAI()).start();
					new BattleShipGameThred(player, new PlayerAISmart()).start();
					continue; // Går till början, av while loopen för att hämta
								// en ny spelare
				}
			} catch (IOException e) {
				System.out.println("IOException i BattleShipLobby.java::getplayer");
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("NullPointerException i BattleShipLobby.java::getplayer");
				System.out.println("Om detta meddelande loopar beror det på att servern inte kan öppna socket för port 5511");
				e.printStackTrace();
			}
		}
	}
}