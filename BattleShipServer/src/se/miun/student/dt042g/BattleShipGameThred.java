package se.miun.student.dt042g;

public class BattleShipGameThred extends Thread {

	PlayerInterface playerOne;
	PlayerInterface playerTwo;
	Message mess;

	public BattleShipGameThred(PlayerInterface playerOne,
			PlayerInterface playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}

	public void run() {
		// Först vill vi ha uppställningen sen kommer spelarna alternera om att
		// skjuta
		try{
			playerOne.getMessage(new MessageServerRequest(
					EnumRequestType.PLACEMENT, ""));
			playerTwo.getMessage(new MessageServerRequest(
					EnumRequestType.PLACEMENT, ""));
			placementRequest(playerOne);
			//Om spelare två svarar med sin uppställning innan spelare 1 så ligger denna kvar och väntar på att bli inläst.
			placementRequest(playerTwo); 
	
			gameLoop();
		}catch (Exception e) {
			abortGame();
			return;
		}
	}// Slutet av run, här dör tråden själv

	private void abortGame(){
		try {
			playerOne.getMessage(new MessageServerRequest(
					EnumRequestType.ABORTGAME,
					"Någon spelar har skickat felaktig data, spelet avslutas!"));
			playerOne.close();
		} catch (Exception e){
			//Spelare ett är redan borta, varför bry sig.
		}
		
		try {
			playerTwo.getMessage(new MessageServerRequest(
					EnumRequestType.ABORTGAME,
					"Någon spelar har skickat felaktig data, spelet avslutas!"));
			playerTwo.close();
		} catch (Exception e) {
			//Spelare två är redan borta, varför bry sig.
		}		
	}

	private void placementRequest(PlayerInterface player) throws Exception {
		//try{
			mess = player.sendMessage();
			checkMessage(mess, EnumHeader.PLACEMENT);
	
			ShipPlacement tmpPlacement = ((MessagePlacement) mess)
					.getShipPlacement();
	
			//Kontrollerar uppställningen
			if (!ShipPlacementBuilder.isGood(tmpPlacement)) {
				abortGame();
			}
			GameBoard board = new GameBoard();
			board.setupPlacement(((MessagePlacement) mess).getShipPlacement());
			player.setPlacement(board);
		//}catch (Exception e) {
			
		//}
	}

	private void checkMessage(Message mess, EnumHeader header) throws Exception {
		if (mess.getHeader() != header) {
			abortGame();
		}
	}

	private void gameLoop() throws Exception {
		if ((Math.random() * 10) > 5) {
			// byta plats på spelarna randomiserat låter slumpad spelare starta
			PlayerInterface playerTmp = playerTwo;
			playerTwo = playerOne;
			playerTwo = playerTmp;
		}
		boolean playGame = true;
		while (playGame) {
			playGame = playerMove(playerOne, playerTwo);
			if (playGame) {
				// Lade till så att player två enbart får spela om spelet inte
				// är slut.
				playGame = playerMove(playerTwo, playerOne);
			}
		}
		playerOne.close();
		playerTwo.close();
	}

	private boolean playerMove(PlayerInterface player, PlayerInterface opponent) throws Exception {
		while (true) {
			player.getMessage(new MessageServerRequest(EnumRequestType.MOVE, ""));
			mess = player.sendMessage();
			checkMessage(mess, EnumHeader.MOVE);

			MessageMove move = (MessageMove) mess;

			EnumMoveResult result = opponent.getPlacement().checkShot(
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
				opponent.getMessage(new MessageServerRequest(
						EnumRequestType.LOSE, "Du har förlorat!!"));
				return false;
			default:
				break;
			}
		}
	}
}
