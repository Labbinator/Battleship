package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MessageHandler {

	IBattleShipUI battleShipUI = new BattleShipUI();
	
	public void run() {
		

		String hostname = "127.0.0.1"; // För att testa i början (localhost)
		int port = 5511;

		Message mess = null;
		Socket s = null;
		ObjectOutputStream out;
		ObjectInputStream in;

		try { // Öppnar sockets och strömmar
			s = new Socket(hostname, port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Kunde inte ansluta till server. Avbryter");
			return;
		}

		while (true) {
			//try {
				//battleShipUI.UpdateGameBoard(GetGameBoards());
				//mess = new MessageServerRequest(EnumRequestType.ABORTGAME,
				//		"Testar allt detta :p");
				//out.writeObject(mess); // Skickar object till server.
				//out.flush();
				//mess = (Message) in.readObject();

				mess = new MessageLobbyStatus(EnumLobbyState.PLAYERWAITING);
				handleMessage(mess);
				
				mess = new MessageServerRequest(EnumRequestType.PLACEMENT, "");
				handleMessage(mess);
				
				mess = new MessageServerRequest(EnumRequestType.MOVE, "");
				handleMessage(mess);

			//	break;
			//} catch (IOException e) {
			//	e.printStackTrace();
			//	System.out.println("Kunde skriva eller ta emot data från servern. Avbryter");
			//	break;
			//} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
		}
		/*try {// Stänger alla strömmar och sockets
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Kunde inte stänga strömmar/sockets. Detta kan bli ett problem, för servern då.");
		}*/

	}

	private Message handleMessage(Message mess) {

		switch (mess.getHeader()) {

		case LOBBYSTATUS:
			return battleShipUI.getLobbyChoice();
		case PLACEMENT:
			//placement();
			break;
		case MOVE:
			//move();
			break;
		case MOVERESPONSE:
			//moveResponse();
			break;
		case SERVERREQUEST:
			return serverRequest(((MessageServerRequest)mess).getRequest());
		case LOBBYCHOICE:
			//lobbyChoice();
			break;

		}
		return null;

	}

	private void lobbyChoice() {
		// TODO Auto-generated method stub
		
	}

	private Message serverRequest(EnumRequestType requestType) {
		switch (requestType) {
		case PLACEMENT:			
			return new MessagePlacement(battleShipUI.getPlacement());
		case MOVE:
			int xMove, yMove;
			battleShipUI.getMove();
			xMove = battleShipUI.getMoveX();
			yMove = battleShipUI.getMoveY();
			return new MessageMove(xMove, yMove);

		case LOBBYRESPONSE:
			
			break;
		case ABORTGAME:
			
			break;

		default:
			break;
		}
		return null;				
	}

	private void moveResponse() {
		// TODO Auto-generated method stub
		
	}

	private void move() {
		// TODO Auto-generated method stub
		
	}

	private void placement() {
		// TODO Auto-generated method stub
		
	}

	private static GameBoard[] GetGameBoards() {
		GameBoard board1 = new GameBoard();
		GameBoard board2 = new GameBoard();

		board1.setupPlacement(placeShip());

		GameBoard[] returnValue = { board1, board2 };

		return returnValue;
	}

	private static ShipPlacement placeShip() {
		Ship sub1 = new Ship(0, 0, 1, true);
		Ship sub2 = new Ship(3, 3, 1, true);
		Ship sub3 = new Ship(5, 5, 1, false);
		Ship sub4 = new Ship(8, 8, 1, true);
		Ship sub5 = new Ship(3, 5, 1, true);
		Ship destroyer1 = new Ship(0, 9, 3, true);
		Ship destroyer2 = new Ship(9, 0, 3, false);
		Ship destroyer3 = new Ship(5, 0, 3, false);
		Ship carrier = new Ship(0, 7, 5, true);

		return new ShipPlacement(sub1, sub2, sub3, sub4, sub5, destroyer1,
				destroyer2, destroyer3, carrier);

	}

}
