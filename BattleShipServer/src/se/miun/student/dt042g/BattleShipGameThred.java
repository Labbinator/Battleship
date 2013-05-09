package se.miun.student.dt042g;

import java.net.Socket;

public class BattleShipGameThred extends Thread {

	PlayerInterface playerOne;
	PlayerInterface playerTwo;
	Message mess;

	public BattleShipGameThred(Socket playerOne, Socket playerTwo) {
		this.playerOne = new Player(playerOne);

		if (playerTwo == null) {
			this.playerTwo = new PlayerAI();
		} else {
			this.playerTwo = new Player(playerTwo);
		}
	}

	public void run() {
		// F�rst vill vi ha uppst�llningen sen kommer spelarna alternera om att
		// skjuta

		playerOne.getMessage(new MessageServerRequest(
				EnumRequestType.PLACEMENT, ""));
		playerTwo.getMessage(new MessageServerRequest(
				EnumRequestType.PLACEMENT, ""));
		placementRequest(playerOne);
		placementRequest(playerTwo);

		// Fixa in ship placement

		gameLoop();

	}

	private void abortGame() {
		playerOne.getMessage(new MessageServerRequest(
				EnumRequestType.ABORTGAME, "Dra dit pepparn v�xer!!!"));
		playerOne.close();
		playerTwo.getMessage(new MessageServerRequest(
				EnumRequestType.ABORTGAME, "Dra dit pepparn v�xer!!!"));
		playerTwo.close();
	}

	private void placementRequest(PlayerInterface player) {

		mess = player.sendMessage();
		checkMessage(mess, EnumHeader.PLACEMENT);
		// L�gg till kontroll av uppst�llning h�r...

		if (!((MessagePlacement) mess).getShipPlacement().isGood()) {
			abortGame();
		}
		GameBoard board = new GameBoard();
		board.setupPlacement(((MessagePlacement) mess).getShipPlacement());
		player.setPlacement(board);
	}

	private void checkMessage(Message mess, EnumHeader header) {
		if (mess.getHeader() != header) {
			abortGame();
		}
	}

	private void gameLoop() {
		// Vid tid, fixa random f�r att v�lja spelare
		boolean playGame = true;
		while (playGame) {
			playGame = playerMove(playerOne, playerTwo);
			playGame = playerMove(playerTwo, playerOne);
		}
	}

	private boolean playerMove(PlayerInterface player, PlayerInterface opponent) {
		while (true) {
			player.getMessage(new MessageServerRequest(EnumRequestType.MOVE, ""));
			mess = player.sendMessage();
			checkMessage(mess, EnumHeader.MOVE);
			// om move tr�ff s� continue

			MessageMove move = (MessageMove) mess;

			EnumMoveResult result = player.getPlacement().checkShot(
					move.getX(), move.getY());

			switch (result) {
			case HIT:
			case FAIL:
			case SINK:
				player.getMessage(new MessageMoveResponse(result));
				opponent.getMessage(move);
				break;
			case MISS:
				return true;
			case WIN:
				return false;
			default:
				break;
			}
		}
	}
}
