package se.miun.student.dt042g;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * BoardLabel, används för att måla tre lager av bitmap-bilder, bakgrund,
 * innehåll och förgrund bilderna skalas om till BoardLabel-objektets
 * dimensioner
 * 
 * Grafisk text via setText(String) kommer att skrivas över av bilder som
 * används
 * 
 */
public class BoardLabel extends JLabel {

	private ImageIcon bg; // background, ie water
	private ImageIcon ship; // ship
	private ImageIcon shot; // hit or miss
	private boolean shipIsSet = false;
	private static final long serialVersionUID = 2819107404126557124L;

	public BoardLabel() {
		super();

	}

	public void setBgImage(ImageIcon background) {
		bg = background;
	}

	public void setShipImage(ImageIcon ship) {
		this.ship = ship;
		shipIsSet = true;
	}

	public void setShotImage(ImageIcon shot) {
		this.shot = shot;
	}

	public boolean isShipSet() {
		return shipIsSet;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w, h;

		w = getBounds().width;
		h = getBounds().height;

		if (bg != null) {
			g.drawImage(bg.getImage(), 0, 0, w, h, null);
		}
		if (ship != null) {
			g.drawImage(ship.getImage(), 0, 0, w, h, null);
		}
		if (shot != null) {
			g.drawImage(shot.getImage(), 0, 0, w, h, null);
		}
	}
}