package tictactoe.game;

import tictactoe.player.Player;
import tictactoe.player.PlayerType;

/**
 * Represents the current game configuration and keeps track of the contents of
 * each of the nine squares. A square can either be empty, occupied by the cross
 * (x) player or by the noughts (o) player. The configuration also keeps track
 * of the player whose turn it is.
 *
 * @author Yohann CIURLIK
 * @since 1.1
 * @author koen
 */
public class Configuration {
	/* keeps track of current player */
	private PlayerType currentPlayer;
	private final PlayerType[][] gameBoardConfiguration;
	private boolean endOfGame;
	private PlayerType winner = null;

	/**
	 * Creates the 'empty' configuration with all empty squares.
	 */
	public Configuration() {
		this.currentPlayer = PlayerType.XPLAYER;
		this.endOfGame = false;
		this.gameBoardConfiguration = new PlayerType[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.gameBoardConfiguration[i][j] = PlayerType.NONE;
			}
		}
	}

	/**
	 * Creates a configuration with position p occupied by player
	 *
	 * @since 1.1
	 * @param previous Configuration previous configuration.
	 * @param p        Square position.
	 * @param player   Player who occupies position.
	 */
	public Configuration(final Configuration previous, final int p, final PlayerType player) {
		/* On recopie la conf prcdente et on ajoute la position p */
		/* On recopie la conf prcdente */
		final PlayerType[][] tabP = previous.getGameBoardConfiguration();
		this.gameBoardConfiguration = new PlayerType[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.gameBoardConfiguration[i][j] = tabP[i][j];
			}
		}
		this.gameBoardConfiguration[p / 3][p % 3] = player;
		swap(player);
	}

	/**
	 * Creates a copy of a configuration used to evaluate a board configuration. No
	 * current player is set in this configuration.
	 *
	 * @param gameBoard game board configuration.
	 * @since 1.2
	 */
	public Configuration(final PlayerType gameBoard[][]) {
		this.gameBoardConfiguration = new PlayerType[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.gameBoardConfiguration[i][j] = gameBoard[i][j];
			}
		}
	}

	/**
	 * Evaluates the game board configuration.
	 *
	 * @since 1.2
	 * @return int evaluation 1, 0, -1.
	 */
	public int evaluation() {
		final int eval = evaluationInterne();
		if (eval > 0) {
			return 1;
		} else {
			if (eval < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Returns the current configuration of the game board.
	 *
	 * @since 1.1
	 * @return int[][] game board configuration (table of 3x3=9 squares).
	 */
	public PlayerType[][] getGameBoardConfiguration() {
		return this.gameBoardConfiguration;
	}

	/**
	 * Returns whether square position is free.
	 *
	 * @param position position
	 * @return true if position is free; false otherwise.
	 */
	public boolean isFree(final int position) {
		return (this.gameBoardConfiguration[position / 3][position % 3] == PlayerType.NONE);
	}

	/**
	 * In case game has ended, prints winner and asks whether user wants to play
	 * another game.
	 *
	 * @return boolean true if game has ended; false otherwise.
	 */
	public boolean isEndOfGame() {
		// if we already know game has ended, return immediately.
		if (this.endOfGame) {
			return this.endOfGame;
		}

		final PlayerType winner = TicTacToe.getConfiguration().determineWinner();
		this.endOfGame = (winner != PlayerType.NONE);

		// Check for draws; it's a draw if all positions have been occupied
		// and there is no winner.
		if (!this.endOfGame) {
			this.endOfGame = true;
			final PlayerType[][] tabCase = TicTacToe.getConfiguration().getGameBoardConfiguration();

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (tabCase[i][j] == PlayerType.NONE) {
						this.endOfGame = false;
					}
				}
			}
		}

		if (!this.endOfGame) {
			return false;
		}

		if (TicTacToe.showGui && TicTacToe.getGui().getGameBoard().isSoundEnabled()) {
			TicTacToe.getGui().getGameBoard().getWinningGameSound();
		}
		this.winner = winner;
		switch (winner) {
		case NONE:
			TicTacToe.getTicTacToe().incrementDraws();
			break;
		case XPLAYER:
			TicTacToe.getTicTacToe().incrementGamesWonByXPlayer();
			break;
		case OPLAYER:
			TicTacToe.getTicTacToe().incrementGamesWonByOPlayer();
			break;
		}

		/**
		 * Reset panel
		 */
		if (TicTacToe.showGui) {
			TicTacToe.getGui().getOptionPanel().refreshStatCounters();
		}

		/**
		 * Reset player; end of game, no one can make a move anymore
		 */
		this.currentPlayer = PlayerType.NONE;
		return true;
	}

	/**
	 * Returns the player whose turn it is.
	 *
	 * @since 1.1
	 * @return the current player
	 */
	public PlayerType getCurrentPlayerType() {
		return this.currentPlayer;
	}

	/**
	 * Sets the player whose turn it is. When game has started, players should take
	 * turns starting with the x player.
	 *
	 * @param player player whose turn it is.
	 * @since 1.1
	 */
	public void setCurrentPlayerType(final PlayerType player) {
		this.currentPlayer = player;
	}

	/**
	 * Swaps current player to other (x or o) type.
	 *
	 * @param player PlayerType current player.
	 */
	public void swap(final PlayerType player) {
		if (player == PlayerType.XPLAYER) {
			this.currentPlayer = PlayerType.OPLAYER;
		} else {
			this.currentPlayer = PlayerType.XPLAYER;
		}
	}

	public Player getCurrentPlayer() {
		switch (this.currentPlayer) {
		case XPLAYER:
			return TicTacToe.getTicTacToe().getPlayer1();
		case OPLAYER:
			return TicTacToe.getTicTacToe().getPlayer2();
		default:
			return null;
		}
	}

	/**
	 * Returns winner of game, if any and game has finished.
	 *
	 * @return PlayerType XPLAYER if x player has won; OPLAYER if o player has won;
	 *         NONE if game ended in draw; null if game is still ongoing.
	 */
	public PlayerType getWinner() {
		return this.winner;
	}

	/**
	 * Return who occupies a square, if any.
	 *
	 * @param num number of square.
	 * @return PlayerType either XPLAYER, OPLAYER, or NONE.
	 */
	public PlayerType getPlayerAtSquare(final int num) {
		return this.gameBoardConfiguration[num / 3][num % 3];
	}

	/**
	 * Occupy square numbered num by player.
	 *
	 * @since 1.2
	 * @param num    number of square.
	 * @param player player who occupies the square.
	 */
	public void setPlayerAtSquare(final int num, final PlayerType player) {
		this.gameBoardConfiguration[num / 3][num % 3] = player;
		swap(player);
	}

	/**
	 * Evaluation interne d'une conf.
	 *
	 * @return int Evaluation interne
	 * @since 1.1
	 */
	private int evaluationInterne() {
		int evalMax = 0, evalMin = 0;
		int evalTemp = 0;
		final PlayerType[][] tabP = getGameBoardConfiguration();

		/* Parcours en ligne et colonne */
		for (int parcours = 0; parcours < 2; parcours++) {
			for (int i = 0; i < 3; i++) {
				if (parcours == 0) {
					evalTemp = parcoursLigne(tabP, i);
				} else {
					evalTemp = parcoursColonne(tabP, i);
				}
				/* La ligne est vierge */
				if (evalTemp == 0) {
					evalMax += 1;
					evalMin += 1;
				} else {

					/* La ligne est favorable pour MAX */
					if ((evalTemp % 100) == 0) {
						evalMax += 1;
					} else {

						/* La ligne est favorable pour MIN */
						if ((evalTemp % 3) == 0) {
							evalMin += 1;
						}
					}
				}
			}
		}

		/* Parcours en diagonales */
		for (int parcours = 0; parcours < 2; parcours++) {
			if (parcours == 0) {
				evalTemp = parcoursDiagonaleMontante(tabP);
			} else {
				evalTemp = parcoursDiagonaleDescendante(tabP);
			}
			/* La ligne est vierge */
			if (evalTemp == 0) {
				evalMax += 1;
				evalMin += 1;
			} else {

				/* La ligne est favorable pour MAX */
				if ((evalTemp % 100) == 0) {
					evalMax += 1;
				} else {

					/* La ligne est favorable pour MIN */
					if ((evalTemp % 3) == 0) {
						evalMin += 1;
					}
				}
			}
		}
		return (evalMax - evalMin);
	}

	/**
	 * Effectue un parcours en ligne via des modulo
	 *
	 * @param tab Tableau de positions
	 * @param x   Numero de la ligne
	 * @return int Nombre (modulo) interne.
	 * @since 1.1
	 */
	private int parcoursLigne(final PlayerType[][] tab, final int x) {
		int retour = 0;
		for (int j = 0; j < 3; j++) {
			switch (tab[x][j]) {
			case XPLAYER:
				retour += 100;
				break;
			case OPLAYER:
				retour += 3;
				break;
			default:
				retour += 0;
				break;
			}
		}
		return retour;
	}

	/**
	 * Effectue un parcours en colonne via des modulo
	 *
	 * @param tab Tableau de positions
	 * @param y   Numero de la colonne
	 * @return int Nombre (modulo) interne.
	 * @since 1.1
	 */
	private int parcoursColonne(final PlayerType[][] tab, final int y) {
		int retour = 0;
		for (int i = 0; i < 3; i++) {
			switch (tab[i][y]) {
			case XPLAYER:
				retour += 100;
				break;
			case OPLAYER:
				retour += 3;
				break;
			default:
				retour += 0;
				break;
			}
		}
		return retour;
	}

	/**
	 * Effectue un parcours en diagonale descendante.
	 *
	 * @param tab Tableau de position
	 * @return int Nombre (modulo) interne.
	 * @since 1.1
	 */
	private int parcoursDiagonaleDescendante(final PlayerType[][] tab) {
		int retour = 0;
		for (int i = 0; i < 3; i++) {
			switch (tab[i][i]) {
			case XPLAYER:
				retour += 100;
				break;
			case OPLAYER:
				retour += 3;
				break;
			default:
				retour += 0;
				break;
			}
		}
		return retour;
	}

	/**
	 * Effectue un parcours en diagonale montante.
	 *
	 * @param tab Tableau de position
	 * @return int Nombre (modulo) interne.
	 * @since 1.1
	 */
	private int parcoursDiagonaleMontante(final PlayerType[][] tab) {
		int retour = 0;
		for (int i = 2; i >= 0; i--) {
			switch (tab[i][Math.abs(i - 2)]) {
			case XPLAYER:
				retour += 100;
				break;
			case OPLAYER:
				retour += 3;
				break;
			default:
				retour += 0;
				break;
			}
		}
		return retour;
	}

	/**
	 * Returns player whose turn it is.
	 *
	 * @see PlayerType
	 * @return enum either XPLAYER or OPLAYER
	 * @since 1.1
	 */
	public PlayerType determineWinner() {
		final int pDD = parcoursDiagonaleDescendante(this.gameBoardConfiguration);
		final int pDM = parcoursDiagonaleMontante(this.gameBoardConfiguration);

		int pC, pL;

		if (pDD == 9 || pDM == 9) {
			return PlayerType.OPLAYER;
		}
		if (pDD == 300 || pDM == 300) {
			return PlayerType.XPLAYER;
		}
		for (int i = 0; i < 3; i++) {
			pC = parcoursColonne(this.gameBoardConfiguration, i);
			pL = parcoursLigne(this.gameBoardConfiguration, i);
			if (pC == 9 || pL == 9) {
				return PlayerType.OPLAYER;
			}
			if (pC == 300 || pL == 300) {
				return PlayerType.XPLAYER;
			}
		}
		return PlayerType.NONE;
	}
}
