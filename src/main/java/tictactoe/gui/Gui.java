package tictactoe.gui;

import java.awt.Cursor;
import java.awt.GridLayout;
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
	private final JMenu mainMenu;
	/* Menu bar */
	private final JMenuBar menuBar;
	/* Button to toggle sound */
	private final JCheckBoxMenuItem soundCheckBox;
	/* New game and quit menu items */
	private final JMenuItem newGame, quit;
	/* Game board panel */
	private final GameBoard gameBoardPanel;
	/* Option panel */
	private final OptionPanel optionPanel;

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
			setIconImage(new ImageIcon(Gui.class.getResource("images/Morpion.png")).getImage());
		} catch (final NullPointerException e) {
		}

		this.menuBar = new JMenuBar();

		this.mainMenu = new JMenu("Game Menu");

		this.newGame = new JMenuItem("New");
		this.newGame.addActionListener(e -> {
			TicTacToe.getTicTacToe().newGame();
			repaint();
		});

		this.quit = new JMenuItem("Quit");
		this.quit.addActionListener(e -> TicTacToe.closeTicTacToe());

		this.soundCheckBox = new JCheckBoxMenuItem("Son", true);

		this.mainMenu.add(this.newGame);
		this.mainMenu.add(this.soundCheckBox);
		/**
		 * Disabled W.Pasman #2185. Use {TicTacToeEnvironment#kill()}.
		 */
		// mainMenu.add(quit);

		this.menuBar.add(this.mainMenu);

		this.optionPanel = new OptionPanel();
		this.gameBoardPanel = new GameBoard();
		this.soundCheckBox.addActionListener(e -> Gui.this.gameBoardPanel.setSound(Gui.this.soundCheckBox.getState()));

		getContentPane().add(this.gameBoardPanel);
		getContentPane().add(this.optionPanel);
		setJMenuBar(this.menuBar);

		setVisible(true);
		toFront();
		addWindowListeners();
	}

	private void addWindowListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				Settings.storeSettings(getX(), getY());
			}

			@Override
			public void componentResized(final ComponentEvent e) {
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
		return this.optionPanel;
	}

	/**
	 * Retourne le panel du morpion
	 *
	 * @since 1.1
	 * @return PanelMorpion panneau du morpion
	 */
	public GameBoard getGameBoard() {
		return this.gameBoardPanel;
	}
}
