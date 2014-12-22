/*
 * Name: Parker Sprouse
 * Date: 12/5/2013
 * Assignment: Conway's Game of Life
 */

package psprouse.gol;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class Grid extends JPanel implements MouseListener, MouseMotionListener {

	private Cell[][] grid;
	private int sizeX, sizeY;
	private boolean isCellOn;
	private Timer timer;
	private TimerTask timerTask;

	public Grid(int size) {
		this.sizeX = this.sizeY = size;
		createGrid();
	}

	/*
	 * I know setting the X size to the Y parameter
	 *    and vice versa is weird, but there is a reason.
	 * When creating a multi-dimensional array in Java,
	 *    the first dimension in the array references the "vertical" component (Y)
	 *    and the second dimension references the "horizonal" (X)
	 * We normally think in terms of X (horizontal) being first, and Y
	 *    (vertical) being second, but we have to adjust for Java.
	 * So every time I reference "x" in the following code, it's actually
	 *    referencing the first (vertical) component 
	 *    and "y" is referencing the second (horizontal) component.
	 */
	public Grid(int sizeX, int sizeY) {
		this.sizeX = sizeY;
		this.sizeY = sizeX;
		createGrid();
	}

	/*
	 * Run at creation of the Grid object.
	 * Creates an array using the instance variables,
	 * sets it layout to a GridLayout,
	 * and then adds a new cell to each point in the grid
	 */
	public void createGrid() {
		this.grid = new Cell[this.sizeX][this.sizeY];
		this.setLayout(new GridLayout(this.sizeX, this.sizeY, 0, 0));
		this.setBackground(Color.GRAY);
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				Cell c = new Cell(x, y);
				this.grid[x][y] = c;
				this.add(c);
			}
		}
	}

	/*
	 * Go through the grid and turn each cell off
	 */
	public void resetCells() {
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				this.grid[x][y].turnOff();
			}
		}
	}

	/*
	 * Start the timer, which runs every 750 milliseconds, 3/4 of a second
	 * At each loop, it runs the runGrid() method
	 */
	public void startTimer() {
		this.timer = new Timer();
		this.timerTask = new TimerTask() {
			public void run() {
				runGrid();
			}
		};
		this.timer.scheduleAtFixedRate(this.timerTask, 0, 750);
	}
	
	/*
	 * Stop the timer from running
	 * by cancelling its tasks
	 */
	public void stopTimer() {
		this.timer.cancel();
		this.timerTask.cancel();
	}
	
	/*
	 * Run the runGrid() method one time
	 * with no delay
	 */
	public void step() {
		this.timer = new Timer();
		this.timerTask = new TimerTask() {
			public void run() {
				runGrid();
			}
		};
		this.timer.schedule(this.timerTask, 0);
	}
	
	/*
	 * Creates a temporary grid and then creates a duplicate cell
	 *    point by point from the main grid into the temporary grid.
	 * Then checks each cell in the temporary grid and determines whether
	 *    it should be alive or dead.
	 * Once the whole grid has been checked and updated, we set the
	 *    temporary grid as the main grid to show the changes.
	 */
	private void runGrid() {
		Cell[][] newCells = new Cell[this.sizeX][this.sizeY];

		// Copies the grid into a temporary grid
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				newCells[x][y] = this.grid[x][y].duplicate();
			}
		}

		// Checks each cell in the temporary grid
		// and determines their life state
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				int neighbors = numNeighbors(x, y);
				Cell curCell = newCells[x][y];
				
				if(neighbors == 3 && !curCell.isOn()) 
					curCell.turnOn();
				else if(neighbors < 2 && curCell.isOn())
					curCell.turnOff();	
				else if(neighbors > 3 && curCell.isOn())
					curCell.turnOff();
			}
		}
		updateGrid(newCells);
	}

	/*
	 * Replace the old grid with the new grid
	 *    using the temporary grid from runGrid()
	 */
	private void updateGrid(Cell[][] newGrid) {
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				if(newGrid[x][y].isOn())
					this.grid[x][y].turnOn();
				else
					this.grid[x][y].turnOff();
			}
		}
	}

	/*
	 * This one is a bit of a doozy.
	 * First we have to check for the entire grid except for the borders.
	 * Then the next 4 checks check each border specifically.
	 * Along with checking each border, we check the opposite border
	 *    at the time same to simulate a "wrap around" effect
	 *    
	 * The parameters are the x and y points that represent the location of a cell.
	 * Within each check, we take those points and check the 8 locations around it.
	 * Each check that passes adds one to the counter to tell us it is an "on"
	 *    neighbor.
	 * Then we return the total number of neighbors that are "on."
	 */
	public int numNeighbors(int x, int y) {
		int numOfNeighbors = 0;
		if (x > 0 && y > 0 && x < this.sizeX-1 && y < this.sizeY-1) {
			if (this.grid[x-1][y].isOn()) // Top
				numOfNeighbors++;
			if (this.grid[x][y+1].isOn()) // Right
				numOfNeighbors++;
			if (this.grid[x+1][y].isOn()) // Bottom
				numOfNeighbors++;
			if (this.grid[x][y-1].isOn()) // Left
				numOfNeighbors++;
			if (this.grid[x-1][y-1].isOn()) // Top Left
				numOfNeighbors++;
			if (this.grid[x-1][y+1].isOn()) // Top Right
				numOfNeighbors++;
			if (this.grid[x+1][y-1].isOn()) // Bottom Left
				numOfNeighbors++;
			if (this.grid[x+1][y+1].isOn()) // Bottom Right
				numOfNeighbors++;
		}
		
		if (x == 0 && (y != 0 && y != this.sizeY-1)) {
			if (this.grid[this.sizeX-1][y].isOn())
				numOfNeighbors++;
			if (this.grid[this.sizeX-1][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[this.sizeX-1][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y+1].isOn())
				numOfNeighbors++;
		}
		
		if (y == 0 && (x != 0 && x != this.sizeX-1)) {
			if (this.grid[x][this.sizeY-1].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][this.sizeY-1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][this.sizeY-1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y+1].isOn())
				numOfNeighbors++;
		}
		
		if (x == this.sizeX-1 && (y != 0 && y != this.sizeY-1)) {
			if (this.grid[0][y].isOn())
				numOfNeighbors++;
			if (this.grid[0][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[0][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x][y+1].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y+1].isOn())
				numOfNeighbors++;
		}
		
		if (y == this.sizeY-1 && (x != 0 && x != this.sizeX-1)) {
			if (this.grid[x][0].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][0].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][0].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y].isOn())
				numOfNeighbors++;
			if (this.grid[x][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x-1][y-1].isOn())
				numOfNeighbors++;
			if (this.grid[x+1][y-1].isOn())
				numOfNeighbors++;
		}
			
		return numOfNeighbors;
	}

	public void mouseClicked(MouseEvent e) {
		Rectangle currentCell;
		Point mousePos = new Point(e.getX(), e.getY());
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				Cell thisCell = this.grid[x][y];
				currentCell = new Rectangle(thisCell.getX(), 
						thisCell.getY(), 
						thisCell.getWidth(), 
						thisCell.getHeight());
				if (currentCell.contains(mousePos))
					if (thisCell.isOn())
						thisCell.turnOff();
					else
						thisCell.turnOn();
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		Rectangle currentCell;
		Point mousePos = new Point(e.getX(), e.getY());
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				Cell thisCell = this.grid[x][y];
				currentCell = new Rectangle(thisCell.getX(), 
						thisCell.getY(), 
						thisCell.getWidth(), 
						thisCell.getHeight());
				if (currentCell.contains(mousePos) && thisCell.isOn())
					this.isCellOn = true;
				else if (currentCell.contains(mousePos) && !thisCell.isOn())
					this.isCellOn = false;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		Rectangle currentCell;
		Point mousePos = new Point(e.getX(), e.getY());
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				Cell thisCell = this.grid[x][y];
				currentCell = new Rectangle(thisCell.getX(), 
						thisCell.getY(), 
						thisCell.getWidth(), 
						thisCell.getHeight());
				if (currentCell.contains(mousePos)) {
					if (this.isCellOn)
						thisCell.turnOff();
					else
						thisCell.turnOn();
				}
			}
		}
	}

	// Unused but required implementation methods
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}
	// End unused required methods

}
