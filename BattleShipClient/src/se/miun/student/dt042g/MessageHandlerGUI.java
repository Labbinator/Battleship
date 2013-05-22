package se.miun.student.dt042g;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandlerGUI {

	IBattleShipUI battleShipGUI = new BattleShipGUI();
	BaseBoard[] boards;

	private int xMove = -1;
	private int yMove = -1;

	private EnumMoveResult lastMoveResult = EnumMoveResult.MISS;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	public void run() {

		boards = new BaseBoard[2];
		boards[0] = new GameBoard(); // MyBoard
		boards[1] = new BlindBoard(); // OpponentsBoard

		String hostname = "127.0.0.1"; // F�r att testa i b�rjan (localhost)
		int port = 5511;
		boolean keepPlaying = true;

		Message recievedMess = null;
		Message sendMess = null;
		Socket s = null;

		try { // �ppnar sockets och str�mmar
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

				if (sendMess == null
						&& ((recievedMess.getHeader() == EnumHeader.MOVERESPONSE && ((MessageMoveResponse) recievedMess)
								.getResponse() == EnumMoveResult.WIN) || (recievedMess
								.getHeader() == EnumHeader.SERVERREQUEST && ((MessageServerRequest) recievedMess)
								.getRequest() == EnumRequestType.LOSE))) {
					battleShipGUI.Message("Avslutas p� r�tt s�tt");
					keepPlaying = false;
				}

			} catch (EOFException e) {
				battleShipGUI
						.Message("Fanns inget att l�sa fr�n servern. Avbryter");
				break;
			} catch (IOException e) {
				battleShipGUI
						.Message("Kunde skriva eller ta emot data fr�n servern. Avbryter");
				break;
			} catch (ClassNotFoundException e) {

				break;
			}
		}

		battleShipGUI
				.Message("\n\nUppkopplingen kommer att st�ngas ner.\nOm du vill spela igen f�r du starta om klienten.");
		closeEveryThing();
	}

	private void closeEveryThing() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			battleShipGUI.Message("Avslutades visst inte p� r�tt s�tt.");
		}
	}

	private Message handleMessage(Message mess) {

		switch (mess.getHeader()) {
		case LOBBYSTATUS:
			return battleShipGUI.getLobbyChoice();
		case MOVE:
			move(mess);
			return null;
		case MOVERESPONSE:
			moveResponse(((MessageMoveResponse) mess).getResponse());
			break;
		case SERVERREQUEST:
			MessageServerRequest tmpMess = (MessageServerRequest) mess;
			return serverRequest(tmpMess.getRequest(), tmpMess.getMessage());
		}
		return null;
	}

	private void move(Message mess) {
		int x = ((MessageMove) mess).getX();
		int y = ((MessageMove) mess).getY();

		((GameBoard) boards[0]).checkShot(x, y);
		battleShipGUI.updateGameBoard(boards);
	}

	// ---------------------------------------------------------------------------------

	private void moveResponse(EnumMoveResult moveResult) {

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
		battleShipGUI.updateGameBoard(boards);

		if (moveResult == EnumMoveResult.WIN) {
			battleShipGUI.Message("Grattis, du har vunnit!");
		}
	}

	// ---------------------------------------------------------------------------------

	private Message serverRequest(EnumRequestType requestType, String message) {
		switch (requestType) {
		case PLACEMENT:
			return getPlacement();
		case MOVE:
			/*try {
				this.wait(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("H�r????");
				e.printStackTrace();
			}*/
			MessageMove tmpMess = battleShipGUI.getMove(lastMoveResult);
			xMove = tmpMess.getX();
			yMove = tmpMess.getY();
			return tmpMess;
		case LOBBYRESPONSE:
			break;
		case ABORTGAME:
			break;
		case LOSE:
			battleShipGUI.Message(message);
			break;
		default:
			break;
		}
		return null;
	}

	private Message getPlacement() {
		ShipPlacementBuilder placeBuilder = new ShipPlacementBuilder();

		battleShipGUI.Message("Placera ditt hangarfartyg");
		placementCarrier(placeBuilder);

		battleShipGUI.Message("Placera dina jagare");
		placementDestroyer(placeBuilder);

		battleShipGUI.Message("Placera dina ub�tar");
		placementSubs(placeBuilder);

		ShipPlacement placement = placeBuilder.getShipPlacement();
		((GameBoard) boards[0]).setupPlacement(placement);
		return new MessagePlacement(placement);
	}

	private void placementSubs(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 5; i++) {
			Ship ship = battleShipGUI.placeShip(1);

			if (!placeBuilder.addSub(ship.getStartX(), ship.getStartY(),
					ship.getXAligned())) {
				battleShipGUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard) boards[0]).setupPlacement(placeBuilder
						.getShipPlacement());
			}
		}
	}

	private void placementDestroyer(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 3; i++) {
			Ship ship = battleShipGUI.placeShip(3);

			if (!placeBuilder.addDestroyer(ship.getStartX(), ship.getStartY(),
					ship.getXAligned())) {
				battleShipGUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard) boards[0]).setupPlacement(placeBuilder
						.getShipPlacement());
			}
		}
	}

	private void placementCarrier(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 1; i++) {
			Ship ship = battleShipGUI.placeShip(5);

			if (!placeBuilder.addCarrier(ship.getStartX(), ship.getStartY(),
					ship.getXAligned())) {
				battleShipGUI.Message("Felutplacering");
				i--;
			} else {
				((GameBoard) boards[0]).setupPlacement(placeBuilder
						.getShipPlacement());
			}
		}
	}

}