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
		Scanner input = new Scanner(System.in);
		
		if (lastMoveResult == EnumMoveResult.FAIL) {
			System.out.println("Ditt föregående skott var inte giltigt.");
		}
		
		System.out.print("Vart vill du bomba X,Y? ");
		String inputString = new String(input.nextLine());
		String[] movePointsinput = inputString.split(",");
		int xMove = Integer.parseInt(movePointsinput[0]);
		int yMove = Integer.parseInt(movePointsinput[1]);
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
				EnumCellStatus tmpValue = board.getPositionValue(inner1,
						outer);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}

			System.out.print("\n");
		}
	}

	@Override
	public Message getLobbyChoice() {
		Scanner input = new Scanner(System.in);	
		System.out.print("Vill du spela mot en AI eller mot en klient (A/K)?");
		String inputString = new String(input.nextLine());	
		
		if (inputString.equals("A")) {
			return new MessageLobbyChoice(EnumLobbyChoice.PLAY_VS_AI);
		}
		else {
			return new MessageLobbyChoice(EnumLobbyChoice.WAIT_FOR_PLAYER);
		}
	}
	
	private void clearScreen() {
		try {
			if (System.getProperty("os.name").toLowerCase().contains("window")) {
				Runtime.getRuntime().exec("cmd /c cls");

			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void Message(String message) {
		System.out.println(message);		
	}

	@Override
	public ShipCordinates getShipPlacement(BaseBoard board, String message, boolean askXAlign) {
		Scanner input = new Scanner(System.in);	
		boolean xAlign = false;
		
		writeBoard(board);
		
		System.out.print(message);					
		String inputString = new String(input.nextLine());
		String[] movePointsinput = inputString.split(",");
		int x = Integer.parseInt(movePointsinput[0]);
		int y = Integer.parseInt(movePointsinput[1]);
		
		if (askXAlign) {
			System.out.print("Ska den ligga horisontelt eller vertikalt (h/v)?");
			inputString = new String(input.nextLine());
			
			if (inputString.equals("h")) {
				xAlign = true;
			}
		}
		
		return new ShipCordinates(x, y, xAlign);
	}	
}
