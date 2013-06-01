package se.miun.student.dt042g;

import java.io.IOException;
import java.util.Scanner;

public class BattleShipUI implements IBattleShipUI {

	@Override
	public void updateGameBoard(BaseBoard[] boards) {

		clearScreen();

		for (int i = 0; i < 10; i++) {
			System.out.print("  " + i);
		}

		System.out.print("    ");

		for (int i = 0; i < 10; i++) {
			System.out.print("  " + i);
		}

		System.out.println();

		for (int outer = 0; outer < 10; outer++) {

			System.out.print(outer + " ");

			for (int inner1 = 0; inner1 < 10; inner1++) {
				EnumCellStatus tmpValue = boards[0].getPositionValue(inner1,
						outer);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}

			System.out.print(" " + outer + "  ");

			for (int inner1 = 0; inner1 < 10; inner1++) {
				EnumCellStatus tmpValue = boards[1].getPositionValue(inner1,
						outer);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}
			System.out.print("\n");
		}
	}

	private char getCellStatusChar(EnumCellStatus tmpValue) {

		switch (tmpValue) {
		case EMPTY:
			return '.';

		case MISS:
			return 'X';
		case SUBMARINE:
		case DESTROYER:
		case CARRIER:
			return 'O';

		case SUBMARINE_HIT:
		case DESTROYER_HIT:
		case CARRIER_HIT:
		case HIT:
			return '*';
		}
		return '.';
	}

	@Override
	public MessageMove getMove(EnumMoveResult lastMoveResult) {

		if (lastMoveResult == EnumMoveResult.FAIL) {
			System.out.println("Ditt föregående skott var inte giltigt.");
		}

		return getCheckMoveInput();
	}

	private MessageMove getCheckMoveInput() {
		boolean inputOK = false;
		Scanner input = new Scanner(System.in);
		int xMove = -1;
		int yMove = -1;

		while (!inputOK) {
			System.out.print("Vart vill du bomba X,Y? ");
			String inputString = new String(input.nextLine());
			String[] movePointsinput = inputString.split(",");
			try {
				xMove = Integer.parseInt(movePointsinput[0]);
				yMove = Integer.parseInt(movePointsinput[1]);
				inputOK = true;
			} catch (Exception e) {
				inputOK = false;
				System.out.println("Ogiltigt drag\nFörsök igen\n");
			}
		}

		return new MessageMove(xMove, yMove);
	}

	private void writeBoard(BaseBoard board) {
		for (int i = 0; i < 10; i++) {
			System.out.print("  " + i);
		}

		System.out.println();

		for (int outer = 0; outer < 10; outer++) {

			System.out.print(outer + " ");

			for (int inner1 = 0; inner1 < 10; inner1++) {
				EnumCellStatus tmpValue = board.getPositionValue(inner1, outer);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}

			System.out.print("\n");
		}
	}

	@Override
	public Message getLobbyChoice() {
		boolean inputOK = false;
		Scanner input = new Scanner(System.in);
		MessageLobbyChoice messLobby = null;

		while (!inputOK) {
			inputOK = false;
			System.out
					.print("Vill du spela mot en AI eller mot en klient (A/K)?");
			String inputString = new String(input.nextLine());

			if (inputString.toLowerCase().equals("a")) {
				messLobby = new MessageLobbyChoice(EnumLobbyChoice.PLAY_VS_AI);
				inputOK = true;
			} else if (inputString.toLowerCase().equals("k")) {
				messLobby = new MessageLobbyChoice(
						EnumLobbyChoice.WAIT_FOR_PLAYER);
				inputOK = true;
			} else {
				System.out.println("Felaktig inmatning\nFörsök igen.");
			}
		}

		return messLobby;
	}

	private void clearScreen() {
		try {
			if (System.getProperty("os.name").toLowerCase().contains("window")) {
				Runtime.getRuntime().exec("cmd /c cls");

			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	@Override
	public void Message(String message) {
		System.out.println(message);
	}

	@Override
	public ShipCordinates getShipPlacement(BaseBoard board, String message,
			boolean askXAlign) {

		writeBoard(board);

		ShipCordinates shipCord = getCheckPlacement(message);

		if (askXAlign) {
			shipCord.setXAlign(getCheckOrientation());
		}

		return shipCord;
	}

	private boolean getCheckOrientation() {
		Scanner input = new Scanner(System.in);
		boolean inputOK = false;
		boolean returnValue = false;

		while (!inputOK) {
			System.out
					.print("Ska den ligga horisontelt eller vertikalt (h/v)?");
			String inputString = new String(input.nextLine());

			if (inputString.toLowerCase().equals("h")
					|| inputString.toLowerCase().equals("v")) {
				inputOK = true;
				if (inputString.toLowerCase().equals("h")) {
					returnValue = true;
				} else {
					returnValue = false;
				}
			} else {
				System.out.println("Ogiltig orientering\nFörsök igen\n");
				inputOK = false;
			}
		}
		return returnValue;
	}

	private ShipCordinates getCheckPlacement(String message) {
		boolean inputOK = false;
		int x = -1;
		int y = -1;

		while (!inputOK) {
			Scanner input = new Scanner(System.in);
			System.out.print(message);
			String inputString = new String(input.nextLine());
			String[] movePointsinput = inputString.split(",");
			try {
				x = Integer.parseInt(movePointsinput[0]);
				y = Integer.parseInt(movePointsinput[1]);
				inputOK = true;
			} catch (Exception e) {
				inputOK = false;
				System.out.println("Ogiltig utplacering\nFörsök igen\n");
			}
		}

		return new ShipCordinates(x, y);
	}

	@Override
	public Ship placeShip(int ship) {
		return null;
	}

	@Override
	public void showDialog(String message) {
		System.out.println(message);
		
	}

	@Override
	public Ship placeShip(int size, BaseBoard board, String message,
			boolean askXAlign) {
		
		ShipCordinates shipCord = getShipPlacement(board, message, askXAlign);		

		return new Ship(shipCord.getX(), shipCord.getY(), size, shipCord.getXAlign());
	}
}
