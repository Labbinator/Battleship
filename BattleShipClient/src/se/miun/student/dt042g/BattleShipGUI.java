package se.miun.student.dt042g;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class BattleShipGUI extends JFrame implements IBattleShipUI {
	private static final long serialVersionUID = 1L;
	private JTextPane messageArea;
	private BoardPanel opponentPanel, playerPanel;
	private Vector<BoardLabel> opponentVector, playerVector;
	private int shipSize = 0;
	private boolean horizontal = true, placeShipMode = false, bombMode = false;
	// Indikerar om ett skepp eller en bomb har placerats
	private boolean placedShip, bombed;
	private ShipPlacementBuilder shipPlacement;
	private Ship ship;
	private IconHolder ih;
	private LabelMouseListener mouseListener;
	private int[] bomb = { -1, -1 };

	private boolean yourTurn = false;

	private InteractionManipulator manipulator = new InteractionManipulator() {

		@Override
		public void unMark(int x, int y, BoardPanel panel) {
			if (placeShipMode && panel.equals(playerPanel)) {
				paintShip(x, y, panel, null);
			} else if (bombMode && panel.equals(opponentPanel)) {
				paintBomb(x, y, panel, null);
			}
		}

		@Override
		public void rotateShip() {
			if (horizontal) {
				horizontal = false;
			} else {
				horizontal = true;
			}
			ship.setXAligned(horizontal);
		}

		@Override
		public void mark(int x, int y, BoardPanel panel) {
			if (placeShipMode && panel.equals(playerPanel)) {
				// Måla vart skeppet är placerbart
				paintShip(x, y, panel, Color.RED);
			} else if (bombMode && panel.equals(opponentPanel)) {
				paintBomb(x, y, panel, Color.RED);
			}
		}

		@Override
		public void clickOnTile(int x, int y, BoardPanel panel) {
			if (placeShipMode && panel.equals(playerPanel)) {
				// Ta bort det röda
				paintShip(x, y, panel, null);
				// Placera ut skeppet
				placeShip(x, y, panel);
			} else if (bombMode && panel.equals(opponentPanel)) {
				if (panel.containsBomb(x, y) && yourTurn) {
					// Ta bort det röda
					paintBomb(x, y, panel, null);
					// Bomba på vald plats
					bomb[0] = x;
					bomb[1] = y;
					panel.addBomb(x, y);
					bombed = true;
				}
			}
		}
	};

	// Konstruktor
	public BattleShipGUI() {
		initiateInstanceVariables();
		makeFrame();
	}

	private void initiateInstanceVariables() {
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);
		messageArea = new JTextPane(document);
		messageArea.setBackground(null);
		messageArea.setEditable(false);
		messageArea.setFont(new Font("SansSerif", Font.BOLD, 20));
		ih = IconHolder.getInstance();
		shipPlacement = new ShipPlacementBuilder();
		opponentVector = new Vector<BoardLabel>();
		playerVector = new Vector<BoardLabel>();
		playerPanel = new BoardPanel("Din plan");
		opponentPanel = new BoardPanel("Motståndare");
		initiateBoard(playerPanel, playerVector);
		initiateBoard(opponentPanel, opponentVector);
		playMusic();
	}

	public void playMusic() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File("audio/BattleShipMarch.wav")
							.getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	private void initiateBoard(BoardPanel panel, Vector<BoardLabel> vector) {
		BoardLabel label = null;
		EtchedBorder e = new EtchedBorder();
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				mouseListener = new LabelMouseListener(manipulator, x, y, panel);
				label = new BoardLabel();
				Dimension d = new Dimension(50, 50);
				label.setPreferredSize(d);
				label.setBorder(e);
				label.setOpaque(true);
				label.setBgImage(ih.getWater());
				label.addMouseListener(mouseListener);
				label.addMouseMotionListener(mouseListener);
				panel.addLabel(label);
			}
		}
	}

	private void makeFrame() {
		setTitle("BattleShip");
		setLayout(new BorderLayout());
		JPanel boards = new JPanel(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		boards.add(opponentPanel);
		boards.add(playerPanel);
		add(messageArea, BorderLayout.CENTER);
		add(boards, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	// Ritar ut vart man kan placera ett skepp
	private void paintShip(int x, int y, BoardPanel panel, Color color) {
		ship.setStartX(x);
		ship.setStartY(y);
		BoardLabel label;
		if (shipPlacement.testShip(ship)) {
			for (int i = 0; i < shipSize; i++) {
				label = panel.getLabel(x, y);
				label.setBackground(color);
				if (horizontal) {
					x += 1;
				} else {
					y += 1;
				}
			}
		}
	}

	// Ritar ut vart man kan placera en bomb
	private void paintBomb(int x, int y, BoardPanel panel, Color color) {
		BoardLabel label = panel.getLabel(x, y);
		if (panel.containsBomb(x, y)) {
			label.setBackground(color);
		}
	}

	// Placera ut skeppet på spelarens plan
	private void placeShip(int x, int y, BoardPanel panel) {
		ship.setStartX(x);
		ship.setStartY(y);
		if (shipPlacement.testShip(ship)) {
			BoardLabel label = panel.getLabel(x, y);
			// Om det är en ubåt som ska placeras
			if (shipSize == 1) {
				// Sätt bilden
				label.setShipImage(ih.getSub());
			} else {
				int tmpX = x, tmpY = y;
				for (int i = 0; i < shipSize; i++) {
					label = panel.getLabel(tmpX, tmpY);
					if (horizontal) {
						if (shipSize == 3) {
							label.setShipImage(ih.getDestroyer_h(i));
						} else {
							label.setShipImage(ih.getCarrier_h(i));
						}
						tmpX += 1;
					} else {
						if (shipSize == 3) {
							label.setShipImage(ih.getDestroyer(i));
						} else {
							label.setShipImage(ih.getCarrier(i));
						}
						tmpY += 1;
					}
				}
			}
			if (removePlaceAble(ship)) {
				placedShip = true;
			}
		}
	}

	// Lägger till skeppet som är placerat och tar bort alla rutor som inte är
	// placerbara längre
	private boolean removePlaceAble(Ship ship) {
		int x = ship.getStartX();
		int y = ship.getStartY();
		if (shipSize == 1) {
			return shipPlacement.addSub(x, y, horizontal);
		} else if (shipSize == 3) {
			return shipPlacement.addDestroyer(x, y, horizontal);
		} else if (shipSize == 5) {
			return shipPlacement.addCarrier(x, y, horizontal);
		}
		return false;
	}

	@Override
	public void updateGameBoard(BaseBoard[] boards) {
		BaseBoard playerBoard = boards[0];
		BaseBoard opponentBoard = boards[1];

		for (int x = 0; x < 10; x++) {
			for (int y1 = 0; y1 < 10; y1++) {
				EnumCellStatus playerValue = playerBoard
						.getPositionValue(x, y1);
				setCellStatus(playerValue, x, y1, playerPanel);

			}
			for (int y2 = 0; y2 < 10; y2++) {
				EnumCellStatus opponentValue = opponentBoard.getPositionValue(
						x, y2);
				setCellStatus(opponentValue, x, y2, opponentPanel);
			}
		}
	}

	// Ritar ut cellstatus på de olika spelbrädena
	private void setCellStatus(EnumCellStatus tmpValue, int x, int y,
			BoardPanel panel) {
		BoardLabel label = null;
		switch (tmpValue) {
		case EMPTY:
		case SUBMARINE:
		case DESTROYER:
		case CARRIER:
			break;
		case MISS:
			label = panel.getLabel(x, y);
			label.setShotImage(ih.getMiss());
			label.setText(" ");
			break;
		case SUBMARINE_HIT:
		case DESTROYER_HIT:
		case CARRIER_HIT:
		case HIT:
			label = panel.getLabel(x, y);
			if (panel.equals(playerPanel)) {
				if (label.isShipSet()) {
					label.setShotImage(ih.getHit());
					label.setText(" ");
				} else {
					label.setShotImage(ih.getMiss());
					label.setText(" ");
				}
			} else {
				label.setShotImage(ih.getHit());
				label.setText(" ");
			}
			break;
		}
	}

	@Override
	public synchronized MessageMove getMove(EnumMoveResult lastMoveResult) {
		yourTurn = true;

		if (!bombMode) {
			placeShipMode = false;
			bombMode = true;
			shipSize = 0;
		}
		bombed = false;
		int x = -1, y = -1;
		while (true) {
			try {
				this.wait(10);
			} catch (InterruptedException e) {
			}
			if (bombed) {
				if (lastMoveResult == EnumMoveResult.FAIL) {
					Message("Ditt föregående skott var inte giltigt.");
				} else {
					x = bomb[0];
					y = bomb[1];
					yourTurn = false;
					return new MessageMove(x, y);
				}
			}
		}
	}

	@Override
	public Message getLobbyChoice() {
		boolean inputOK = false;
		MessageLobbyChoice messLobby = null;

		String[] buttons = { "AI", "Klient" };

		while (!inputOK) {
			int returnValue = JOptionPane.showOptionDialog(null,
					"Vad vill du spela mot?", "Välj motspelare",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, buttons, buttons[0]);

			if (returnValue == 0) {
				messLobby = new MessageLobbyChoice(EnumLobbyChoice.PLAY_VS_AI);
				inputOK = true;
			} else if (returnValue == 1) {
				messLobby = new MessageLobbyChoice(
						EnumLobbyChoice.WAIT_FOR_PLAYER);
				inputOK = true;
			} else {
				Message("Felaktig inmatning\nFörsök igen.");
			}
		}
		return messLobby;
	}

	@Override
	public void Message(String message) {
		messageArea.setText(message);
	}

	/**
	 * Returnerar det placerade skeppet
	 */
	@Override
	public synchronized Ship placeShip(int size) {
		if (!placeShipMode) {
			placeShipMode = true;
		}
		placedShip = false;
		shipSize = size;
		ship = new Ship(-1, -1, shipSize, horizontal);
		// Loopar så länge ett skepp inte har blivit placerat
		while (true) {
			try {
				this.wait(10);
			} catch (InterruptedException e) {
			}
			if (placedShip) {
				shipSize = 0;
				return ship;
			}
		}
	}

	@Override
	public ShipCordinates getShipPlacement(BaseBoard board, String message,
			boolean xAlign) {
		return null;
	}

	@Override
	public void showDialog(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	@Override
	public synchronized Ship placeShip(int size, BaseBoard board,
			String message, boolean xAlign) {
		if (!placeShipMode) {
			placeShipMode = true;
		}
		placedShip = false;
		shipSize = size;
		ship = new Ship(-1, -1, shipSize, horizontal);
		// Loopar så länge ett skepp inte har blivit placerat
		while (true) {
			try {
				this.wait(10);
			} catch (InterruptedException e) {
			}
			if (placedShip) {
				shipSize = 0;
				return ship;
			}
		}
	}
}
