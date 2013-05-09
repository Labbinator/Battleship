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
		
		//Fixa in ship placement
		
		
		
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

		if (!((MessagePlacement)mess).getShipPlacement().isGood()) {
			abortGame();
		}
		GameBoard board = new GameBoard();
		board.setupPlacement(((MessagePlacement)mess).getShipPlacement());
		player.setPlacement(board);
	}

	private void checkMessage(Message mess, EnumHeader header) {
		if (mess.getHeader() != header) {
			abortGame();
		}
	}

	private void gameLoop() {
		// Vid tid, fixa random f�r att v�lja spelare
		while(true){
			while (true) {
				playerOne.getMessage(new MessageServerRequest(EnumRequestType.MOVE,
						""));
				mess = playerOne.sendMessage();
				checkMessage(mess, EnumHeader.MOVE);
				//om move tr�ff s� continue
				
				MessageMove move = (MessageMove)mess;
				
				EnumMoveResult result = playerOne.getPlacement()).checkShot(move.getX(),move.getY());
				
				switch (result) {
				case HIT:
				case FAIL:
				case SINK:
					break;
				case MISS:				
					break;
				case WIN:
					break;
				default:
					break;
				}
				
				
				break;
				//om move miss break;
			}
			while(true){
				
			}
		}
	}

	private void dafuq() {

		/*
		 * while(true){//Evil loop, gjord f�r testsyfte. Tar // emot data och
		 * printar lite info. Message mess; try { mess =
		 * (Message)in.readObject();
		 * 
		 * EnumHeader en = mess.getHeader();
		 * 
		 * switch (en) { case LOBBYSTATUS: mess = (MessageLobbyStatus)mess;
		 * break; case PLACEMENT:
		 * 
		 * break; case MOVE: break; case MOVERESPONSE:
		 * 
		 * break; case SERVERREQUEST: MessageServerRequest servermess =
		 * (MessageServerRequest)mess;
		 * System.out.println(servermess.getRequest() + " " +
		 * servermess.getMessage());
		 * 
		 * break; default: break; }
		 * 
		 * out.writeObject(mess); } catch (ClassNotFoundException | IOException
		 * e) { break; } try{ //Efter while loopen st�nger sockets och //
		 * str�mmar in.close(); out.close(); //sock.close();
		 * System.out.println("Tr�d:" + this.getId() + " fr�nkopplad"); }
		 * catch(IOException e){ System.out.println("Tr�d:" + this.getId() +
		 * " kunde inte st�nga str�mmar och/eller socket." ); }
		 */
	}
}
