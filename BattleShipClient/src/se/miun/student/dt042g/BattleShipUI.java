package se.miun.student.dt042g;

public class BattleShipUI implements IBattleShipUI {

	@Override
	public void UpdateGameBoard(GameBoard[] gameBoard) {

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
				EnumCellStatus tmpValue = gameBoard[0].getPositionValue(outer,
						inner1);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}
				
			System.out.print(" " + outer + "  ");
			
			for (int inner1 = 0; inner1 < 10; inner1++) {
				EnumCellStatus tmpValue = gameBoard[0].getPositionValue(outer,
						inner1);
				System.out.print(getCellStatusChar(tmpValue));
				System.out.print("  ");
			}
			System.out.print("\n");
		}

		System.out.println("Skriv uttrycket du vill få uträknat.");
		System.out.print("Input: ");
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
			return '*';
		}
		return '.';
	}

	@Override
	public MessageMove GetMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
