/*
 * Name: Parker Sprouse
 * Date: 12/5/2013
 * Assignment: Conway's Game of Life
 */

package psprouse.gol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GameOfLifeMain extends JFrame implements MouseListener, MouseMotionListener {

	private Grid grid;
	private JPanel buttonBack;
	private JButton start, reset, step;

	// Constants for labels for buttons 
	public static String START = "Start";
	public static String PAUSE = "Pause";
	public static String RESET = "Reset";
	public static String STEP = "Step";

	public GameOfLifeMain() {

		/*
		 * Configuring the main JFrame
		 */
		setTitle("The Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 700));
		setResizable(false);
		setLayout(new BorderLayout());

		/*
		 * Initialization of instance variables
		 * The size of the grid can be changed by
		 * changing the parameter to new Grid().
		 * I know the assignment was 75x75, but it just looks
		 * weird to me, and making it 75x75 is literally
		 * as easy as changing the parameter to 75
		 */
		this.grid = new Grid(50);
		this.buttonBack = new JPanel();
		this.start = new JButton(START);
		this.reset = new JButton(RESET);
		this.step = new JButton(STEP);

		/*
		 * Setting up the JPanel that the
		 * control buttons will be on
		 */
		this.buttonBack.add(this.step);
		this.buttonBack.add(this.start);
		this.buttonBack.add(this.reset);
		this.buttonBack.setBackground(Color.GRAY);

		/*
		 * Configure the buttons
		 * setBackground() changes the unclicked background color
		 * setFocusPainted(false) removes the border on click
		 * Both of these are simply aesthetic
		 */
		this.start.setBackground(Color.WHITE);
		this.start.setFocusPainted(false);
		this.reset.setBackground(Color.WHITE);
		this.reset.setFocusPainted(false);
		this.step.setBackground(Color.WHITE);
		this.step.setFocusPainted(false);

		/*
		 * Add ActionListeners to the buttons
		 * to allow them to act as, you know, buttons
		 */
		this.start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GameOfLifeMain.this.start.getText().equals(START)) {
					GameOfLifeMain.this.start.setText(PAUSE);
					GameOfLifeMain.this.grid.startTimer();
				}
				else if (GameOfLifeMain.this.start.getText().equals(PAUSE)) {
					GameOfLifeMain.this.start.setText(START);
					GameOfLifeMain.this.grid.stopTimer();
				}
			}
		});

		this.reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameOfLifeMain.this.grid.resetCells();
				GameOfLifeMain.this.grid.stopTimer();
				if (GameOfLifeMain.this.start.getText().equals(PAUSE))
					GameOfLifeMain.this.start.setText(START);
			}
		});

		this.step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GameOfLifeMain.this.start.getText().equals(START))
					GameOfLifeMain.this.grid.step();
			}
		});

		/* 
		 * Add this JFrame as a MouseListener
		 * and MouseMotionListener to the grid
		 */
		this.grid.addMouseListener(this);
		this.grid.addMouseMotionListener(this);

		/*
		 *  Add the grid and the buttons to the main frame
		 */
		getContentPane().add(this.grid, BorderLayout.CENTER);
		getContentPane().add(this.buttonBack, BorderLayout.SOUTH);

		pack();
	}


	/*
	 * MouseListeners whose events
	 * are being passed to the underlying component(s)
	 * for them to control
	 */
	public void mouseClicked(MouseEvent e) {
		this.grid.mouseClicked(e);
	}
	public void mouseEntered(MouseEvent e) {
		this.grid.mouseEntered(e);
	}
	public void mouseExited(MouseEvent e) {
		this.grid.mouseExited(e);
	}
	public void mousePressed(MouseEvent e) {
		this.grid.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e) {
		this.grid.mouseReleased(e);
	}
	public void mouseDragged(MouseEvent e) {
		this.grid.mouseDragged(e);
	}
	public void mouseMoved(MouseEvent e) {
		this.grid.mouseMoved(e);
	}
	/*
	 * End passing actions to underlying components
	 */

	public static void main(String[] args) {

		/*
		 * There is a bug in Java 7 related to layouts.
		 * This is a workaround for the bug.
		 * See: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7075600
		 */
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

		// Allows me to change the background color of my buttons when pressed
		UIManager.put("Button.select", Color.LIGHT_GRAY);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameOfLifeMain m = new GameOfLifeMain();
				m.setVisible(true);
			}
		});
	}
}
