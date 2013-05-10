package se.miun.student.dt042g;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {

	IBattleShipUI battleShipUI = new BattleShipUI();
	BaseBoard[] boards;
	
	private int xMove = -1;
	private int yMove = -1;
	
	private EnumMoveResult lastMoveResult = EnumMoveResult.MISS;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;	
	
	public void run() {
		boards = new BaseBoard[2];
		boards[0] = new GameBoard(); //MyBoard		
		boards[1] = new BlindBoard(); //OpponentsBoard

		String hostname = "127.0.0.1"; // För att testa i början (localhost)
		int port = 5511;
		boolean keepPlaying = true;

		Message recievedMess = null;
		Message sendMess = null;
		Socket s = null;

		try { // Öppnar sockets och strömmar
			s = new Socket(hostname, port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Kunde inte ansluta till server. Avbryter");
			return;
		}

		while (keepPlaying) {
			try {
				recievedMess = (Message) in.readObject();
				sendMess = handleMessage(recievedMess);
				
				if (sendMess != null) {												
					out.writeObject(sendMess); // Skickar object till server.
					out.flush();
				} 
				
				if (sendMess == null &&
					((recievedMess.getHeader() == EnumHeader.MOVERESPONSE 
					&& ((MessageMoveResponse)recievedMess).getResponse() == EnumMoveResult.WIN) 
					|| (recievedMess.getHeader() == EnumHeader.SERVERREQUEST
					&& ((MessageServerRequest)recievedMess).getRequest() == EnumRequestType.LOSE))) {
					battleShipUI.Message("Avslutas på rätt sätt");
					keepPlaying = false;
				}
				
			} catch (EOFException e) {
				battleShipUI.Message("Fanns inget att läsa från servern. Avbryter");
				break;
			} catch (IOException e) {
				battleShipUI.Message("Kunde skriva eller ta emot data från servern. Avbryter");
				break;
			} catch (ClassNotFoundException e) {

				break;
			}
		}
		
		battleShipUI.Message("\n\nUppkopplingen kommer att stängas ner.\nOm du vill spela igen får du starta om klienten.");
		closeEveryThing();
	}
		
	private void closeEveryThing() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			battleShipUI.Message("Avslutades visst inte på rätt sätt.");
		}
	}

	private Message handleMessage(Message mess) {

		switch (mess.getHeader()) {
		case LOBBYSTATUS:
			return battleShipUI.getLobbyChoice();
		case MOVE:
			move(mess);
			return null;
		case MOVERESPONSE:
			moveResponse(((MessageMoveResponse)mess).getResponse());
			break;
		case SERVERREQUEST:
			MessageServerRequest tmpMess = (MessageServerRequest) mess;
			return serverRequest(tmpMess.getRequest(), tmpMess.getMessage());
		}
		return null;
	}

	private Message serverRequest(EnumRequestType requestType, String message) {
		switch (requestType) {
		case PLACEMENT:	
			return getPlacement();
		case MOVE:			
			MessageMove tmpMess = battleShipUI.getMove(lastMoveResult);
			xMove = tmpMess.getX();
			yMove = tmpMess.getY();			
			return tmpMess;			
		case LOBBYRESPONSE:			
			break;
		case ABORTGAME:			
			break;
		case LOSE:
			battleShipUI.Message(message);
			break;
		default:
			break;
		}
		return null;				
	}

	private Message getPlacement() {
		
		ShipPlacementBuilder placeBuilder = new ShipPlacementBuilder();	
		
		battleShipUI.Message("Utplacering av ubåtar.");		
		placementSubs(placeBuilder);		
		
		battleShipUI.Message("Utplacering av jagare.");
		placementDestroyer(placeBuilder);

		battleShipUI.Message("Utplacering av hangarfartyg.");
		placementCarrier(placeBuilder);
		
		ShipPlacement placement = placeBuilder.getShipPlacement();
		((GameBoard) boards[0]).setupPlacement(placement);
		return new MessagePlacement(placement);		
	}

	private void placementCarrier(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 1; i++) {
			String message = "Vart vill du placera ditt hangarfartyg?";
			ShipCordinates shipCord = battleShipUI.getShipPlacement(boards[0], message, true);
			
			if (!placeBuilder.addCarrier(shipCord.getX(), shipCord.getY(), shipCord.getXAlign())) {
				battleShipUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard)boards[0]).setupPlacement(placeBuilder.getShipPlacement());
			}					
		}		
	}

	private void placementDestroyer(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 3; i++) {
			String message = "Vart vill du placera jagare " + (i + 1) + "?";
			ShipCordinates shipCord = battleShipUI.getShipPlacement(boards[0], message, true);
			
			if (!placeBuilder.addDestroyer(shipCord.getX(), shipCord.getY(), shipCord.getXAlign())) {
				battleShipUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard)boards[0]).setupPlacement(placeBuilder.getShipPlacement());
			}						
		}	
	}

	private void placementSubs(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 5; i++) {
			String message = "Vart vill du placera ubåt " + (i + 1) + "?";
			ShipCordinates shipCord = battleShipUI.getShipPlacement(boards[0], message, false);
			
			if (!placeBuilder.addSub(shipCord.getX(), shipCord.getY(), shipCord.getXAlign())) {
				battleShipUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard)boards[0]).setupPlacement(placeBuilder.getShipPlacement());
			}					
		}		
	}

	private void moveResponse(EnumMoveResult moveResult) {
		
		System.out.println(moveResult.toString() + " X värde: " + xMove + " Y värde: " + yMove);
		
		lastMoveResult = moveResult;		
		
		switch (moveResult) {
		case HIT:
			((BlindBoard) boards[1]).setShot(xMove, yMove, EnumCellStatus.HIT);			
			break;
		case MISS:
			((BlindBoard) boards[1]).setShot(xMove, yMove, EnumCellStatus.MISS);
			break;
		case SINK:
			((BlindBoard) boards[1]).setShot(xMove, yMove, EnumCellStatus.HIT);
			break;
		case WIN:
			((BlindBoard) boards[1]).setShot(xMove, yMove, EnumCellStatus.HIT);
			break;

		default:
			break;
		}
		
		battleShipUI.updateGameBoard(boards);
		
		if (moveResult == EnumMoveResult.WIN) {
			battleShipUI.Message("Grattis, du har vunnit!");
		}		
	}

	private void move(Message mess) {
		int x = ((MessageMove)mess).getX();
		int y = ((MessageMove)mess).getY();
		
		((GameBoard) boards[0]).checkShot(x, y);
		
		battleShipUI.updateGameBoard(boards);
	}
}
