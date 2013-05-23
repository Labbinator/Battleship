package se.miun.student.dt042g;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class LabelMouseListener extends MouseAdapter implements MouseListener,
		MouseMotionListener {

	private final InteractionManipulator manipulator;
	private final int tileX, tileY;
	private final BoardPanel panel;

	public LabelMouseListener(InteractionManipulator manipulator, int tileX, int tileY, BoardPanel panel) {
		this.manipulator = manipulator;
		this.tileX = tileX;
		this.tileY = tileY;
		this.panel = panel;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		manipulator.mark(tileX, tileY, panel);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		manipulator.unMark(tileX, tileY, panel);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Högerklick
		if (e.isMetaDown()) {
			manipulator.rotateShip();
			// Vanligt klick
		} else {
			manipulator.clickOnTile(tileX, tileY, panel);
		}
	}

}
