package tictactoe.game;

import tictactoe.gui.Gui;
import tictactoe.gui.Settings;
import tictactoe.player.Player;
import tictactoe.player.PlayerAI;
import tictactoe.player.PlayerHuman;
import tictactoe.player.PlayerKind;
import tictactoe.player.PlayerType;

/**
 * Creates unique instance (singleton) of Tic-Tac-Toe game.
 *
 * Allows to obtain the current instance statically.
 *
 * @author Yohann CIURLIK
 * @author K.Hindriks
 * @author W.Pasman 5jul12 added {@link #closeTicTacToe()}.
 */
public class TicTacToe {
	/**
	 * Static game instance
	 */
	private static TicTacToe ticTacToe = null;
	/**
	 * Game configuration.
	 */
	private static Configuration configuration;
	/**
	 * Game interface.
	 */
	public static boolean showGui = true;
	private static Gui gameInterface = null;
	/**
	 * The X and O player variables keep track of the type of player.
	 */
	private Player xplayer, oplayer;
	/**
	 * Statistics.
	 */
	private int gamesWonByXPlayer = 0, gamesWonByOPlayer = 0, draws = 0;

	/**
	 * Launches the game.
	 *
	 * @param argv Arguments are ignored.
	 */
	public static void main(final String argv[]) {
		// Set default initial players: X player is a human, O player is AI.
		TicTacToe.getTicTacToe().setXPlayer(new PlayerHuman(PlayerType.XPLAYER));
		TicTacToe.getTicTacToe().setOPlayer(new PlayerAI(PlayerType.OPLAYER));
		// Launch GUI.
		TicTacToe.getGui();
		// Start new game.
		TicTacToe.getTicTacToe().newGame();
	}

	/**
	 * Game constructor. Use getTicTacToe() to get the game instance.
	 */
	private TicTacToe() {
	}

	/**
	 * Returns game instance.
	 *
	 * @return The game instance.
	 */
	public static TicTacToe getTicTacToe() {
		if (ticTacToe == null) {
			ticTacToe = new TicTacToe();
		}
		return ticTacToe;
	}

	/**
	 * Closes the Tic-Tac-Toe game board GUI.
	 */
	public static void closeTicTacToe() {
		if (TicTacToe.showGui) {
			// Store settings.
			Settings.storeSettings(getGui().getX(), getGui().getY());
			// Close GUI.
			getGui().dispose();
		}
		ticTacToe = null;
		gameInterface = null;
	}

	/**
	 * Sets the type of the X player.
	 *
	 * @param player The player type (an instance of {@link Player}).
	 */
	public void setXPlayer(final Player player) {
		this.xplayer = player;
	}

	/**
	 * Sets the type of the O player.
	 *
	 * @param player The player type (an instance of {@link Player}).
	 */
	public void setOPlayer(final Player player) {
		this.oplayer = player;
	}

	/**
	 * Returns game interface.
	 *
	 * @since 1.1
	 * @return game interface.
	 */
	public static Gui getGui() {
		if (showGui && gameInterface == null) {
			gameInterface = new Gui();
		}
		return gameInterface;
	}

	/**
	 * Returns game configuration.
	 *
	 * @since 1.1
	 * @return game configuration.
	 * @see Configuration
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Set game configuration.
	 *
	 * @since 1.2alpha
	 * @param c new configuration
	 */
	public void setConfiguration(final Configuration c) {
		TicTacToe.configuration = c;
	}

	/**
	 * Generates random square position.
	 *
	 * @return int square position (range 0-8)
	 */
	public int getRandomPosition() {
		int i;
		do {
			i = (int) (Math.random() * 9);
		} while ((i < 0) || (i > 8));
		return i;
	}

	/**
	 * Start new game.
	 *
	 * @since 1.1
	 */
	public void newGame() {
		configuration = new Configuration();
		if ((this.xplayer.getKindofPlayer() == PlayerKind.AI) && (this.oplayer.getKindofPlayer() != PlayerKind.AI)) {
			configuration.setPlayerAtSquare(getRandomPosition(), PlayerType.XPLAYER);
			if (showGui && gameInterface.getGameBoard().isSoundEnabled()) {
				gameInterface.getGameBoard().playSound();
			}
		} else {
			if ((this.xplayer.getKindofPlayer() == PlayerKind.AI)
					&& (this.oplayer.getKindofPlayer() == PlayerKind.AI)) {
				gameAIvsAI();
			}
		}
		if (showGui) {
			gameInterface.repaint();
		}
	}

	/**
	 * Plays AI versus AI games.
	 *
	 * @since 1.2
	 */
	public void gameAIvsAI() {
		/*
		 * Cre une nouvelle configuration avec un coup alatoire de l'AI numero1
		 */
		configuration = new Configuration(configuration, getRandomPosition(), PlayerType.XPLAYER);
		if (gameInterface.getGameBoard().isSoundEnabled()) {
			gameInterface.getGameBoard().playSound();
		}
		gameInterface.repaint();
		PlayerType currentPlayer = this.oplayer.getType();
		int p;
		while (!configuration.isEndOfGame()) {
			switch (currentPlayer) {
			case OPLAYER:
				p = ((PlayerAI) getPlayer2()).coupOrdi();
				configuration.setPlayerAtSquare(p, getPlayer2().getType());
				if (gameInterface.getGameBoard().isSoundEnabled()) {
					gameInterface.getGameBoard().playSound();
				}
				gameInterface.getGameBoard().getRootPane().repaint();
				currentPlayer = this.xplayer.getType();
				break;
			case XPLAYER:
				p = ((PlayerAI) getPlayer1()).coupOrdi();
				configuration.setPlayerAtSquare(p, getPlayer1().getType());
				if (gameInterface.getGameBoard().isSoundEnabled()) {
					gameInterface.getGameBoard().playSound();
				}
				gameInterface.getGameBoard().getRootPane().repaint();
				currentPlayer = this.oplayer.getType();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Returns player 1.
	 *
	 * @return player 1
	 */
	public Player getPlayer1() {
		return this.xplayer;
	}

	/**
	 * Set player 1.
	 *
	 * @param j Player
	 */
	public void setPlayer1(final Player j) {
		this.xplayer = j;
	}

	/**
	 * Returns player 2
	 *
	 * @return player 2
	 */
	public Player getPlayer2() {
		return this.oplayer;
	}

	/**
	 * Set player 2
	 *
	 * @param j Player
	 */
	public void setPlayer2(final Player j) {
		this.oplayer = j;
	}

	/**
	 * Returns the number of games won by X player.
	 *
	 * @return The games won by X player so far.
	 */
	public int getGamesWonByXPlayer() {
		return this.gamesWonByXPlayer;
	}

	/**
	 * Increases games won by X player by 1.
	 */
	public void incrementGamesWonByXPlayer() {
		this.gamesWonByXPlayer++;
	}

	/**
	 * Returns the number of games won by O player.
	 *
	 * @return The games won by O player so far.
	 */
	public int getGamesWonByOplayer() {
		return this.gamesWonByOPlayer;
	}

	/**
	 * Increases games won by O player by 1.
	 */
	public void incrementGamesWonByOPlayer() {
		this.gamesWonByOPlayer++;
	}

	/**
	 * Returns the number of draws.
	 *
	 * @return The number of draws so far.
	 */
	public int getDraws() {
		return this.draws;
	}

	/**
	 * Increases number of draws by 1.
	 */
	public void incrementDraws() {
		this.draws++;
	}

	/**
	 * Resets counters for games won, draws
	 */
	public void resetGameStatistics() {
		this.gamesWonByXPlayer = 0;
		this.gamesWonByOPlayer = 0;
		this.draws = 0;
	}
}
