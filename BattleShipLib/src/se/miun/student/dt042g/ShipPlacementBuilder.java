package se.miun.student.dt042g;

public class ShipPlacementBuilder {
	private Ship[] subs;
	private Ship[] destroyers;
	private Ship[] carriers;

	private int addedSubs;
	private int addedDestroyers;
	private int addedCarriers;

	private final int NO_SUBS = 5;
	private final int NO_DESTROYERS = 3;
	private final int NO_CARRIERS = 1;

	private final int SUB_LENGTH = 1;
	private final int DESTROYER_LENGTH = 3;
	private final int CARRIER_LENGTH = 5;

	private boolean[][] grid; // Används enbart för att kontrollera giltig
								// placering
	private static final int gridW = 10;
	private static final int gridH = 10;

	public ShipPlacementBuilder() {
		subs = new Ship[NO_SUBS];
		destroyers = new Ship[NO_DESTROYERS];
		carriers = new Ship[NO_CARRIERS];

		grid = new boolean[gridW][gridH];
		resetGrid(grid);
		addedSubs = addedCarriers = addedDestroyers = 0;
	}

	public boolean addSub(int x, int y, boolean xAligned) {
		if (getAddedSubs() < NO_SUBS) {
			Ship ship = new Ship(x, y, SUB_LENGTH, xAligned);
			if (addShip(ship)) {
				subs[getAddedSubs()] = ship;
				addedSubs++;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean addDestroyer(int x, int y, boolean xAligned) {
		if (getAddedDestroyers() < NO_DESTROYERS) {
			Ship ship = new Ship(x, y, DESTROYER_LENGTH, xAligned);
			if (addShip(ship)) {
				destroyers[getAddedDestroyers()] = ship;
				addedDestroyers++;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean addCarrier(int x, int y, boolean xAligned) {
		if (getAddedCarriers() < NO_CARRIERS) {
			Ship ship = new Ship(x, y, CARRIER_LENGTH, xAligned);
			if (addShip(ship)) {
				carriers[getAddedCarriers()] = ship;
				addedCarriers++;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean addShip(Ship ship) {

		try {
			for (int j = 0; j < ship.getLength(); j++) {
				if (ship.getXAligned()) {
					if (grid[ship.getStartX() + j][ship.getStartY()]) {

						return false;
					}
				} else { // getXAligned = false
					if (grid[ship.getStartX()][ship.getStartY() + j]) {
						/*
						 * DEBUG printGrid(grid);
						 * System.out.println("Illegal: index: " + i +
						 * ", start x: " + ships[i].getStartX() + ", start y: "
						 * + ships[i].getStartY() + ", length: " +
						 * ships[i].getLength());
						 */
						return false;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

		addToGrid(ship, this.grid);
		return true;
	}

	public ShipPlacement getShipPlacement() {
		ShipPlacement shipPlacement = new ShipPlacement();

		for (int i = 0; i < addedSubs; i++) {
			shipPlacement.addShip(subs[i], EnumCellStatus.SUBMARINE);
		}

		for (int i = 0; i < addedDestroyers; i++) {
			shipPlacement.addShip(destroyers[i], EnumCellStatus.DESTROYER);
		}

		for (int i = 0; i < addedCarriers; i++) {
			shipPlacement.addShip(carriers[i], EnumCellStatus.CARRIER);
		}

		return shipPlacement;
	}

	public void printGrid(boolean[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (grid[j][i]) {
					System.out.print(" O ");
				} else {
					System.out.print(" X ");
				}
			}
			System.out.println("");
		}
	}

	public static boolean isGood(ShipPlacement sp) {
		boolean[][] grid = new boolean[gridW][gridH];
		resetGrid(grid);

		for (int i = 0; i < sp.getNoShips(); i++) {
			/*
			 * Check if ship[i] all positions are available, ie FALSE
			 */
			for (int j = 0; j < sp.getShip(i).getLength(); j++) {
				if (sp.getShip(i).getXAligned()) {
					if (grid[sp.getShip(i).getStartX() + j][sp.getShip(i)
							.getStartY()]) {

						return false;
					}
				} else if (grid[sp.getShip(i).getStartX()][sp.getShip(i)
						.getStartY() + j]) {
					/*
					 * DEBUG printGrid(grid);
					 * System.out.println("Illegal: index: " + i + ", start x: "
					 * + ships[i].getStartX() + ", start y: " +
					 * ships[i].getStartY() + ", length: " +
					 * ships[i].getLength());
					 */
					return false;
				}

			}
			/*
			 * Paint ships and positions around to true in grid
			 */
			addToGrid(sp.getShip(i), grid);

			return true;
		}
		/*
		 * DEBUG System.out.println("Placement is good"); printGrid(grid);
		 */
		return true;
	}

	private static void addToGrid(Ship s, boolean[][] grid) {

		int x_;
		int y_;
		if (s.getXAligned()) {
			x_ = s.getLength() + 1;
			y_ = 2;
		} else {
			x_ = 2;
			y_ = s.getLength() + 1;
		}

		for (int j = -1; j < x_; j++) {
			for (int k = -1; k < y_; k++) {
				paintBool(s.getStartX() + j, s.getStartY() + k, grid);
			}
		}
	}

	private static void paintBool(int x, int y, boolean[][] grid) {
		if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
			grid[x][y] = true;
		}
	}

	private static void resetGrid(boolean[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = false;
			}
		}
	}

	public int getAddedCarriers() {
		return addedCarriers;
	}

	public int getAddedDestroyers() {
		return addedDestroyers;
	}

	public int getAddedSubs() {
		return addedSubs;
	}
}