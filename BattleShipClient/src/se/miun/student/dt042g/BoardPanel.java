package se.miun.student.dt042g;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<BoardLabel> labels = new Vector<BoardLabel>();
	private Vector<Integer> addedBombs = new Vector<Integer>();

	//Konstruktor
	public BoardPanel(String title) {
		setBorder(BorderFactory.createTitledBorder(title));
		setLayout(new GridLayout(10, 10));
	}

	//Lägg till en label till panelen
	public void addLabel(BoardLabel label) {
		labels.add(label);
		this.add(label);
	}

	//Returnera vald label
	public BoardLabel getLabel(int x, int y) {
		int index = transformToInt(x, y);
		return labels.elementAt(index);
	}

	// Lägg till en bomb i vald label
	public void addBomb(int x, int y) {
		int index = transformToInt(x, y);
		addedBombs.add(index);
	}

	// Kontrollerar om vald label innehåller en bomb
	public boolean containsBomb(int x, int y) {
		int i = transformToInt(x, y);
		int index = addedBombs.indexOf(i);
		if (index != -1) {
			return false;
		}
		return true;
	}

	// Gör om två kordinater till en int
	private int transformToInt(int x, int y) {
		return y * 10 + x;
	}

}
