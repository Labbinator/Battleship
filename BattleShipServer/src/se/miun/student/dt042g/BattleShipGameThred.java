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
		// F�rst vill vi ha uppst�llningen sen kommer spelarna alternera om att
		// skjuta
		try{
			playerOne.getMessage(new MessageServerRequest(
					EnumRequestType.PLACEMENT, ""));
			playerTwo.getMessage(new MessageServerRequest(
					EnumRequestType.PLACEMENT, ""));
			placementRequest(playerOne);
			//Om spelare tv� svarar med sin uppst�llning innan spelare 1 s� ligger denna kvar och v�ntar p� att bli inl�st.
			placementRequest(playerTwo); 
	
			gameLoop();
		}catch (Exception e) {
			abortGame();
			return;
		}
	}// Slutet av run, h�r d�r tr�den sj�lv

	private void abortGame(){
		try {
			playerOne.getMessage(new MessageServerRequest(
					EnumRequestType.ABORTGAME,
					"N�gon spelar har skickat felaktig data, spelet avslutas!"));
			playerOne.close();
		} catch (Exception e){
			//Spelare ett �r redan borta, varf�r bry sig.
		}
		
		try {
			playerTwo.getMessage(new MessageServerRequest(
					EnumRequestType.ABORTGAME,
					"N�gon spelar har skickat felaktig data, spelet avslutas!"));
			playerTwo.close();
		} catch (Exception e) {
			//Spelare tv� �r redan borta, varf�r bry sig.
		}		
	}

	private void placementRequest(PlayerInterface player) throws Exception {
		//try{
			mess = player.sendMessage();
			checkMessage(mess, EnumHeader.PLACEMENT);
	
			ShipPlacement tmpPlacement = ((MessagePlacement) mess)
					.getShipPlacement();
	
			//Kontrollerar uppst�llningen
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
			// byta plats p� spelarna randomiserat l�ter slumpad spelare starta
			PlayerInterface playerTmp = playerTwo;
			playerTwo = playerOne;
			playerTwo = playerTmp;
		}
		boolean playGame = true;
		while (playGame) {
			playGame = playerMove(playerOne, playerTwo);
			if (playGame) {
				// Lade till s� att player tv� enbart f�r spela om spelet inte
				// �r slut.
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
						EnumRequestType.LOSE, "Du har f�rlorat!!"));
				return false;
			default:
				break;
			}
		}
	}
}
