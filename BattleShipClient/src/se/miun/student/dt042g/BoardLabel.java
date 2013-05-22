package se.miun.student.dt042g;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * BoardLabel, används för att måla tre lager av bitmap-bilder,
 * bakgrund, innehåll och förgrund
 * bilderna skalas om till BoardLabel-objektets dimensioner
 * 
 * Grafisk text via setText(String) kommer att skrivas över av bilder som används
 *
 */
public class BoardLabel extends JLabel {

	private ImageIcon bg; // background, ie water
	private ImageIcon ship; // ship
	private ImageIcon shot; // hit or miss
	private static final long serialVersionUID = 2819107404126557124L;

	public BoardLabel() {
		super();
		
	}

	public void setBgImage(ImageIcon background) {
		bg = background;
	}

	public void setShipImage(ImageIcon ship) {
		this.ship = ship;
	}

	public void setShotImage(ImageIcon shot) {
		this.shot = shot;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int  w, h;
		
		w = getBounds().width ;
		h = getBounds().height ;
		
		if (bg != null) {
			g.drawImage(bg.getImage(), 0, 0, w,
					h, null);
		}
		if (ship != null){
			g.drawImage(ship.getImage(), 0, 0, w,
					h, null);
		}
		if (shot != null) {
			g.drawImage(shot.getImage(), 0, 0, w,
					h, null);
		}
		
		
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.setTitle("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		 * frame.setBackground(Color.white); frame.setOpacity(1.0f);
		 */
		JPanel panel = new JPanel();
		panel.setSize(400, 400);
		panel.setBackground(Color.white);

		Dimension d = new Dimension(50, 50);

		//
		IconHolder ic = IconHolder.getInstance();
		
		BoardLabel bl2 = new BoardLabel();
		bl2.setOpaque(true);
		bl2.setPreferredSize(d);
		
		bl2.setBgImage(ic.water);
		bl2.setShipImage(ic.sub);
		bl2.setShotImage(ic.hit);
		//
		BoardLabel bl = new BoardLabel();
		bl.setOpaque(true);

		// bl.setBackground(Color.BLACK);
		
		bl.setPreferredSize(d);
		bl.setBgImage(ic.water);
		bl.setShotImage(ic.miss);
		//bl.setVisible(true);

		panel.add(bl);
		panel.add(bl2);

		JPanel panel2 = panel;
		// panel2.setBackground(Color.DARK_GRAY);
		frame.add(panel);
		frame.add(panel2);

		frame.setVisible(true);

	}
}