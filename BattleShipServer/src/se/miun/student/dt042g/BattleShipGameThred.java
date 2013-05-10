package se.miun.student.dt042g;

import java.net.Socket;

public class BattleShipGameThred extends Thread {

	PlayerInterface playerOne;
	PlayerInterface playerTwo;
	Message mess;

	public BattleShipGameThred(PlayerInterface playerOne, PlayerInterface playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}

	public void run() {
		// Först vill vi ha uppställningen sen kommer spelarna alternera om att
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
				EnumRequestType.ABORTGAME, "Dra dit pepparn växer!!!"));
		playerOne.close();
		playerTwo.getMessage(new MessageServerRequest(
				EnumRequestType.ABORTGAME, "Dra dit pepparn växer!!!"));
		playerTwo.close();
	}

	private void placementRequest(PlayerInterface player) {

		mess = player.sendMessage();
		checkMessage(mess, EnumHeader.PLACEMENT);
		// Lägg till kontroll av uppställning här...

		ShipPlacement tmpPlacement = ((MessagePlacement) mess).getShipPlacement();
		
		if (!ShipPlacementBuilder.isGood(tmpPlacement)) {
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
		// Vid tid, fixa random för att välja spelare
		boolean playGame = true;
		while (playGame) {
			playGame = playerMove(playerOne, playerTwo);
			playGame = playerMove(playerTwo, playerOne);
		}
		playerOne.close();
		playerTwo.close();
	}

	private boolean playerMove(PlayerInterface player, PlayerInterface opponent) {
		while (true) {
			player.getMessage(new MessageServerRequest(EnumRequestType.MOVE, ""));
			mess = player.sendMessage();
			checkMessage(mess, EnumHeader.MOVE);
			// om move träff så continue

			MessageMove move = (MessageMove) mess;

			EnumMoveResult result = player.getPlacement().checkShot(
					move.getX(), move.getY());

			switch (result) {
			case HIT:
				
			case SINK:
				player.getMessage(new MessageMoveResponse(result));
				opponent.getMessage(move);
				break;
			case FAIL:
				player.getMessage(new MessageMoveResponse(result));
				break;
			case MISS:
				player.getMessage(new MessageMoveResponse(result));
				opponent.getMessage(move);
				return true;
			case WIN:
				player.getMessage(new MessageMoveResponse(result));
				opponent.getMessage(move);
				opponent.getMessage(new MessageServerRequest(EnumRequestType.LOSE, "You lose!!"));
				return false;
			default:
				break;
			}
		}
	}
}
