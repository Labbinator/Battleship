package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MessageHandler {

	IBattleShipUI battleShipUI = new BattleShipUI();
	GameBoard[] boards;
	//GameBoard opponentsBoard;
	
	private int xMove = -1;
	private int yMove = -1;
	
	
	public void run() {
		boards = new GameBoard[2];
		boards[0] = new GameBoard(); //MyBoard		
		boards[1] = new GameBoard(); //OpponentsBoard

		String hostname = "127.0.0.1"; // För att testa i början (localhost)
		int port = 5511;

		Message recievedMess = null;
		Message sendMess = null;
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
			try {
				recievedMess = (Message) in.readObject();
				sendMess = handleMessage(recievedMess);
				//battleShipUI.UpdateGameBoard(GetGameBoards());
				//mess = new MessageServerRequest(EnumRequestType.ABORTGAME,
				//		"Testar allt detta :p");
				
				if (sendMess != null) {												
					out.writeObject(sendMess); // Skickar object till server.
					out.flush();
				}
				//mess = (Message) in.readObject();
			
				
				/*
				recievedMess = new MessageLobbyStatus(EnumLobbyState.PLAYERWAITING);
				handleMessage(recievedMess);
				
				recievedMess = new MessageServerRequest(EnumRequestType.PLACEMENT, "");
				handleMessage(recievedMess);
				
				recievedMess = new MessageServerRequest(EnumRequestType.MOVE, "");
				handleMessage(recievedMess);
				*/
				
				
				
				//mess = new MessageServerRequest(EnumRequestType.ABORTGAME, "");
				//handleMessage(mess);				
				
				//mess = new MessageServerRequest(EnumRequestType.LOBBYRESPONSE, "");
				//handleMessage(mess);
				/*
				recievedMess = new MessageMoveResponse(EnumMoveResult.HIT);
				handleMessage(recievedMess);
				*/
			//	break;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Kunde skriva eller ta emot data från servern. Avbryter");
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		//case PLACEMENT:
			//placement();
			//break;
		case MOVE:
			move(mess);
			return null;
		case MOVERESPONSE:
			moveResponse(((MessageMoveResponse)mess).getResponse());
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
			ShipPlacement placement = battleShipUI.getPlacement();
			boards[0].setupPlacement(placement);
			return new MessagePlacement(placement);
		case MOVE:
			//int xMove, yMove;
			boolean tryAgain = false;
			if (xMove != -1)
			{
				tryAgain = true;
			}
			
			battleShipUI.getMove(tryAgain);
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

	private void moveResponse(EnumMoveResult moveResult) {
		
		System.out.print(moveResult.toString() + " X värde: " + xMove + " Y värde: " + yMove);
		
		//Ska in i motståndarens board.
		
		switch (moveResult) {
		case HIT:
			boards[1].setShot(xMove, yMove, EnumCellStatus.HIT);
			
			break;
		case MISS:
			boards[1].setShot(xMove, yMove, EnumCellStatus.MISS);
			break;
		case SINK:
			boards[1].setShot(xMove, yMove, EnumCellStatus.HIT);
			break;
		case WIN:
			boards[1].setShot(xMove, yMove, EnumCellStatus.HIT);
			break;

		default:
			break;
		}
		
		xMove = -1;
		yMove = -1;
		
	}

	private void move(Message mess) {
		int x = ((MessageMove)mess).getX();
		int y = ((MessageMove)mess).getY();
		
		//Ska in när jag fått kims kod.
		//boards[0].checkShot(x, y);
		
		//battleShipUI.updateGameBoard(boards);
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
