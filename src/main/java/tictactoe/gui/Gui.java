package tictactoe.gui;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import tictactoe.game.TicTacToe;

/**
 * Shows game board and options.
 * 
 * @author K.Hindriks
 */
public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	/* Main menu */
	private JMenu mainMenu;
	/* Menu bar */
	private JMenuBar menuBar;
	/* Button to toggle sound */
	private JCheckBoxMenuItem soundCheckBox;
	/* New game and quit menu items */
	private JMenuItem newGame, quit;
	/* Game board panel */
	private GameBoard gameBoardPanel;
	/* Option panel */
	private OptionPanel optionPanel;

	/**
	 * Creates new game board GUI.
	 */
	public Gui() {
		setTitle("Tic Tac Toe");

		setLocation(Settings.getXCoordinate(), Settings.getYCoordinate());
		setSize(600, 310);

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new GridLayout(1, 2));
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		try {
			setIconImage(new ImageIcon(
					Gui.class.getResource("images/Morpion.png")).getImage());
		} catch (NullPointerException e) {
		}

		menuBar = new JMenuBar();

		mainMenu = new JMenu("Game Menu");

		newGame = new JMenuItem("New");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TicTacToe.getTicTacToe().newGame();
				repaint();
			}
		});

		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TicTacToe.closeTicTacToe();

			}
		});

		soundCheckBox = new JCheckBoxMenuItem("Son", true);
		soundCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameBoardPanel.setSound(soundCheckBox.getState());
			}
		});

		mainMenu.add(newGame);
		mainMenu.add(soundCheckBox);
		/**
		 * Disabled W.Pasman #2185. Use {TicTacToeEnvironment#kill()}.
		 */
		// mainMenu.add(quit);

		menuBar.add(mainMenu);

		optionPanel = new OptionPanel();
		gameBoardPanel = new GameBoard();

		getContentPane().add(gameBoardPanel);
		getContentPane().add(optionPanel);
		setJMenuBar(menuBar);

		setVisible(true);
		toFront();
		addWindowListeners();
	}

	private void addWindowListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				Settings.storeSettings(getX(), getY());
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Settings.storeSettings(getX(), getY());
			}
		});
	}

	/**
	 * Returns panel with options
	 * 
	 * @since 1.1
	 * @return PanelOption panneau d'options
	 */
	public OptionPanel getOptionPanel() {
		return optionPanel;
	}

	/**
	 * Retourne le panel du morpion
	 * 
	 * @since 1.1
	 * @return PanelMorpion panneau du morpion
	 */
	public GameBoard getGameBoard() {
		return gameBoardPanel;
	}

}
