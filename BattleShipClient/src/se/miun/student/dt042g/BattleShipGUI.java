package se.miun.student.dt042g;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class BattleShipGUI extends JFrame implements IBattleShipUI,
		MouseListener, MouseMotionListener {

	private JPanel opponentPanel, playerPanel;
	private Vector<BoardLabel> opponentVector, playerVector;
	private int shipSize = 0;
	private boolean horizontal = true, placeShipMode = false, bombMode = false;
	private Vector<Integer> availableLabels;
	// Index på placerat skepp och bomb
	private int placedShip, bombed;
	private ShipPlacementBuilder shipPlacement;
	private Ship ship;
	private IconHolder ih;
	private Vector<Integer> placedShipsVector;

	// Konstruktor
	public BattleShipGUI() {
		initiateInstanceVariables();
		makeFrame();
	}
	
	private void initiateInstanceVariables() {
		ih = IconHolder.getInstance();
		shipPlacement = new ShipPlacementBuilder();
		availableLabels = new Vector<Integer>();
		opponentVector = new Vector<BoardLabel>();
		playerVector = new Vector<BoardLabel>();
		placedShipsVector = new Vector<Integer>();
		opponentPanel = new JPanel(new BorderLayout());
		opponentPanel
				.setBorder(BorderFactory.createTitledBorder("Motståndare"));
		opponentPanel.setLayout(new GridLayout(10, 10));
		playerPanel = new JPanel(new BorderLayout());
		playerPanel.setBorder(BorderFactory.createTitledBorder("Din plan"));
		playerPanel.setLayout(new GridLayout(10, 10));
		initiateBoard(playerPanel, true, playerVector);
		initiateBoard(opponentPanel, false, opponentVector);
		playMusic();
	}
	
	public void playMusic(){
	    try{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("audio/BattleShipMarch.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception ex){
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}

	private void initiateBoard(JPanel panel, boolean addMousL,
			Vector<BoardLabel> vector) {
		BoardLabel label;
		EtchedBorder e = new EtchedBorder();
		for (int i = 0; i < 100; i++) {
			label = new BoardLabel();
			Dimension d = new Dimension(40, 40);
			label.setPreferredSize(d);
			label.setBorder(e);
			label.setForeground(Color.BLACK);
			label.setBgImage(ih.water);
			label.setOpaque(true);
			if (addMousL) {
				label.addMouseListener(this);
				label.addMouseMotionListener(this);
			}
			panel.add(label);
			vector.add(label);
		}
	}

	private void makeFrame() {
		setTitle("BattleShip");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		add(opponentPanel);
		add(playerPanel);
		pack();
		setVisible(true);
	}

	// Vector som håller alla index för labels
	private void initiateFreeVector() {
		// Radera vectorn om den innehöll något tidigare
		availableLabels.clear();
		for (int i = 0; i < 100; i++) {
			availableLabels.add(i);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		BoardLabel label = (BoardLabel) e.getSource();
		JPanel parent = (JPanel) label.getParent();

		// Kollar vilken panel som är aktiv och tar ut vart berörd label finns
		// int index = -1;
		// if (parent.equals(playerPanel)) {
		int index = playerVector.indexOf(label);
		// Måla grått där musen inte är
		for (int i = 0; i < 100; i++) {
			label = playerVector.elementAt(i);
			label.setBackground(null);
			// Om vi ska placera ut skepp
			if (placeShipMode) {
				// Rita annan färg där det går att placera skeppet
				paintPossibleShip(index, label);
			}
		}
		// }
	}

	// Måla ut möjlig skeppsplacering
	private void paintPossibleShip(int index, BoardLabel label) {
		int x = transformFromInt(index)[0];
		int y = transformFromInt(index)[1];
		ship.setStartX(x);
		ship.setStartY(y);
		if (shipPlacement.testShip(ship)) {
			for (int i = 0; i < shipSize; i++) {
				// Det kan inte vara något annat bräde än spelarens eget då man
				// inte får sätta ut skepp i det andra
				if (horizontal) {
					label = playerVector.elementAt(index);
					index++;
					label.setBackground(Color.RED);
				} else {
					label = playerVector.elementAt(index);
					index += 10;
					label.setBackground(Color.RED);
				}
			}
		}
	}

	// Placera ut skeppet på spelarens plan
	private void placeShip(int index, BoardLabel label) {
		int x = transformFromInt(index)[0];
		int y = transformFromInt(index)[1];
		ship.setStartX(x);
		ship.setStartY(y);
		if (shipPlacement.testShip(ship)) {
			// Om det är en ubåt som ska placeras
			if (shipSize == 1) {
				label.setShipImage(ih.sub);
				placedShipsVector.add(index);
			} else {
				for (int i = 0; i < shipSize; i++) {
					if (horizontal) {
						label = playerVector.elementAt(index);
						if (shipSize == 3) {
							label.setShipImage(ih.destroyer_h[i]);
							placedShipsVector.add(index);
						} else {
							label.setShipImage(ih.carrier_h[i]);
							placedShipsVector.add(index);
						}
						index += 1;
					} else {
						label = playerVector.elementAt(index);
						if (shipSize == 3) {
							label.setShipImage(ih.destroyer[i]);
							placedShipsVector.add(index);
						} else {
							label.setShipImage(ih.carrier[i]);
							placedShipsVector.add(index);
						}
						index += 10;
					}
				}
			}
			if (removePlaceAble(x, y)) {
				placedShip = index;
			}
		}
	}

	// Lägger till skeppet som är placerat och tar bort alla rutor som inte är
	// placerbara längre
	private boolean removePlaceAble(int x, int y) {
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
	public void mouseReleased(MouseEvent e) {
		// Högerklick
		if (e.isMetaDown()) {
			if (horizontal) {
				horizontal = false;
			} else {
				horizontal = true;
			}
			ship.setXAligned(horizontal);
			// Vanligt klick
		} else {
			BoardLabel label = (BoardLabel) e.getSource();
			// Kollar vilken panel som är aktiv och tar ut vart berörd label
			// finns
			JPanel parent = (JPanel) label.getParent();
			// Tar ut rätt index för markerad label
			int index = -1;
			if (parent.equals(playerPanel)) {
				index = playerVector.indexOf(label);
			}
			if (parent.equals(opponentPanel)) {
				index = opponentVector.indexOf(label);
			}
			if (placeShipMode) {
				placeShip(index, label);
			} else if (bombMode) {
				if (bombIsPlaceable(index)) {
					bombed = index;
					availableLabels.remove(availableLabels.indexOf(index));
				}
			}
		}
	}

	// Kontrollerar om man kan bomba på vald plats
	private boolean bombIsPlaceable(int index) {
		if (availableLabels.contains(index)) {
			return true;
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
				setCellStatus(playerValue, transformToInt(x, y1), playerVector);

			}
			for (int y2 = 0; y2 < 10; y2++) {
				EnumCellStatus opponentValue = opponentBoard.getPositionValue(
						x, y2);
				setCellStatus(opponentValue, transformToInt(x, y2),
						opponentVector);
			}
		}

	}

	// Ritar ut cellstatus på de olika spelbrädena
	private void setCellStatus(EnumCellStatus tmpValue, int index,
			Vector<BoardLabel> vector) {
		BoardLabel label = null;
		switch (tmpValue) {
		case EMPTY:
		case SUBMARINE:
		case DESTROYER:
		case CARRIER:
			break;
		case MISS:
			label = vector.elementAt(index);
			label.setShotImage(ih.miss);
			label.setText(" ");
			break;
		case SUBMARINE_HIT:
		case DESTROYER_HIT:
		case CARRIER_HIT:
		case HIT:
			label = vector.elementAt(index);
			if (vector.equals(playerVector)) {
				if (placedShipsVector.contains(index)) {
					label.setShotImage(ih.hit);
				} else {
					label.setShotImage(ih.miss);
				}
			} else {
				label.setShotImage(ih.hit);
			}
			label.setText(" ");
			break;
		}
	}

	@Override
	public synchronized MessageMove getMove(EnumMoveResult lastMoveResult) {
		if (!bombMode) {
			placeShipMode = false;
			initiateFreeVector();
			bombMode = true;
			shipSize = 0;
			inititateMouseListenerOnOpponent();
			JOptionPane.showMessageDialog(this, "Vart vill du bomba?");
		}
		bombed = -1;
		int x = -1, y = -1;
		while (true) {
			try {
				this.wait(10);
			} catch (InterruptedException e) {
			}
			if (bombed != -1) {
				if (lastMoveResult == EnumMoveResult.FAIL) {
					Message("Ditt föregående skott var inte giltigt.");
				} else {
					x = transformFromInt(bombed)[0];
					y = transformFromInt(bombed)[1];
					return new MessageMove(x, y);
				}
			}
		}
	}

	// Tar bort muslyssning på spelarens bräde och aktiverar muslyssning på
	// opponentens bräde
	private void inititateMouseListenerOnOpponent() {
		BoardLabel opponentLabel;
		BoardLabel playerLabel;
		for (int i = 0; i < 100; i++) {
			opponentLabel = opponentVector.elementAt(i);
			playerLabel = playerVector.elementAt(i);
			opponentLabel.addMouseListener(this);
			playerLabel.setBackground(Color.LIGHT_GRAY);
			playerLabel.removeMouseListener(this);
			playerLabel.removeMouseMotionListener(this);
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
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Returnerar det placerade skeppet
	 */
	@Override
	public synchronized Ship placeShip(int size) {
		if (!placeShipMode) {
			placeShipMode = true;
			// initiateFreeVector();
		}
		placedShip = -1;
		shipSize = size;
		ship = new Ship(-1, -1, shipSize, horizontal);
		// Loopar så länge ett skepp inte har blivit placerat
		while (true) {
			try {
				this.wait(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (placedShip != -1) {
				shipSize = 0;
				return ship;
			}
		}
	}

	// Gör om en int till två kordinater
	private int[] transformFromInt(int i) {
		int x = i % 10;
		int y = (i - x) / 10;
		int[] xANDy = { x, y };
		return xANDy;

	}

	// Gör om två kordinater till en int
	private int transformToInt(int x, int y) {
		return y * 10 + x;
	}

	// -----------------------------------------------------------
	// Dessa används inte här

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public ShipCordinates getShipPlacement(BaseBoard board, String message,
			boolean xAlign) {
		return null;
	}

}
