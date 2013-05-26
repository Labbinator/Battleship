package se.miun.student.dt042g;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//import javax.swing.JOptionPane;

public class MessageHandlerGUI {

	IBattleShipUI battleShipGUI;// = new BattleShipGUI();
	BaseBoard[] boards;

	private int xMove = -1;
	private int yMove = -1;

	private EnumMoveResult lastMoveResult = EnumMoveResult.MISS;

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String hostname;
	
	public MessageHandlerGUI(String hostname, boolean useConsole) {
		this.hostname = hostname;
		
		if (useConsole) {
			battleShipGUI = new BattleShipUI();
		} else {
			battleShipGUI = new BattleShipGUI();
		}
	}

	//Användaren har angett ip-adress
	public MessageHandlerGUI(String hostname) {
		this.hostname = hostname;
		battleShipGUI = new BattleShipUI();
	}
	
	//Om ingen ip-adress anges sätts den till localhost
	public MessageHandlerGUI(){
		hostname = "127.0.0.1";
		battleShipGUI = new BattleShipGUI();
	}

	public void run() {

		boards = new BaseBoard[2];
		boards[0] = new GameBoard(); // MyBoard
		boards[1] = new BlindBoard(); // OpponentsBoard

		// String hostname = "127.0.0.1"; // För att testa i början (localhost)
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

				if (sendMess == null
						&& ((recievedMess.getHeader() == EnumHeader.MOVERESPONSE && ((MessageMoveResponse) recievedMess)
								.getResponse() == EnumMoveResult.WIN) || (recievedMess
								.getHeader() == EnumHeader.SERVERREQUEST && ((MessageServerRequest) recievedMess)
								.getRequest() == EnumRequestType.LOSE))) {
					battleShipGUI.Message("Avslutas på rätt sätt");
					keepPlaying = false;
				}

			} catch (EOFException e) {
				battleShipGUI
						.Message("Fanns inget att läsa från servern. Avbryter");
				break;
			} catch (IOException e) {
				battleShipGUI
						.Message("Kunde int skriva eller ta emot data från servern. Avbryter");
				break;
			} catch (ClassNotFoundException e) {

				break;
			}
		}

		battleShipGUI
				.Message("\n\nUppkopplingen kommer att stängas ner.\nOm du vill spela igen får du starta om klienten.");
		closeEveryThing();
	}

	private void closeEveryThing() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			battleShipGUI.Message("Avslutades visst inte på rätt sätt.");
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
			battleShipGUI.showDialog("Skeppet är sänkt");
			//JOptionPane.showMessageDialog(null, "Skeppet är sänkt!");
			break;
		case WIN:
			((BlindBoard) boards[1]).setShot(xMove, yMove, EnumCellStatus.HIT);
			battleShipGUI.showDialog("Du har vunnit! Starta om klienten för att spela igen.");
			//JOptionPane.showMessageDialog(null, "Du har vunnit! Starta om klienten för att spela igen.");
			break;

		default:
			break;
		}
		battleShipGUI.updateGameBoard(boards);

		if (moveResult == EnumMoveResult.WIN) {
			battleShipGUI.Message("Grattis, du har vunnit!");
		}
	}

	private Message serverRequest(EnumRequestType requestType, String message) {
		switch (requestType) {
		case PLACEMENT:
			return getPlacement();
		case MOVE:
			battleShipGUI.Message("Bomba!");
			MessageMove tmpMess = battleShipGUI.getMove(lastMoveResult);
			xMove = tmpMess.getX();
			yMove = tmpMess.getY();
			battleShipGUI.Message("Vänta på motståndaren.");
			return tmpMess;
		case LOBBYRESPONSE:
			break;
		case ABORTGAME:
			break;
		case LOSE:
			battleShipGUI.showDialog("Du har förlorat! Starta om klienten för att spela igen.");
			//JOptionPane.showMessageDialog(null, "Du har förlorat! Starta om klienten för att spela igen.");
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

		battleShipGUI.Message("Placera dina ubåtar");
		placementSubs(placeBuilder);

		ShipPlacement placement = placeBuilder.getShipPlacement();
		((GameBoard) boards[0]).setupPlacement(placement);
		return new MessagePlacement(placement);
	}

	private void placementSubs(ShipPlacementBuilder placeBuilder) {
		for (int i = 0; i < 5; i++) {
			String message = "Vart vill du placera ubåt " + (i + 1) + "?";
			Ship ship = battleShipGUI.placeShip(1, boards[0], message, false);

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
			String message = "Vart vill du placera jagare " + (i + 1) + "?";
			Ship ship = battleShipGUI.placeShip(3, boards[0], message, true);

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
			String message = "Vart vill du placera ditt hangarfartyg?";
			Ship ship = battleShipGUI.placeShip(5, boards[0], message, true);

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
