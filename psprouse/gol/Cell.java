/*
 * Name: Parker Sprouse
 * Date: 12/5/2013
 * Assignment: Conway's Game of Life
 */

package psprouse.gol;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Cell extends JPanel {

	public static final Color OFF = Color.LIGHT_GRAY;
	public static final Color ON = Color.BLACK;
	
	private int x, y;
	
	public Cell(int y, int x) {
		this.setBackground(OFF);
		this.x = x;
		this.y = y;
		this.setBorder(new LineBorder(Color.GRAY, 1));
	}
	
	// Duplicate method so references aren't kept between the old grid and the new grid
	// Returns a new cell with the same attributes as the old cell
	public Cell duplicate() {
		Cell newCell = new Cell(this.x, this.y);
		if (isOn())
			newCell.turnOn();
		else
			newCell.turnOff();
		return newCell;
	}
	
	public void turnOn() {
		this.setBackground(ON);
	}
	
	public void turnOff() {
		this.setBackground(OFF);
	}
	
	public boolean isOn() {
		return this.getBackground().equals(ON);
	}
	
	public int getXpos() {
		return this.x;
	}
	
	public int getYpos() {
		return this.y;
	}
	
}
