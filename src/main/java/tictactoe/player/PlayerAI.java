package tictactoe.player;
import tictactoe.game.Configuration;
import tictactoe.game.TicTacToe;

/**
 * AI Player.
 * 
 * @author Yohann CIURLIK
 * @author Koen
 * @since 1.2
 */
public class PlayerAI extends Player {

	/* Skill level of AI player */
	int skillLevel;
	/* TODO Nombre de coups calculs par l'AI */
	int nbCoups;

	/**
	 * Creates AI player and sets type (x or o player, or none).
	 * 
	 * @param type
	 *            Player type
	 */
	public PlayerAI(PlayerType type) {
		super(type);
		skillLevel = 8;
	}

	/**
	 * TODO Calcul le meilleur coup pour l'ordi
	 * 
	 * @return int Meilleur coup de l'IA
	 * @since 1.2
	 */
	public int coupOrdi() {
		PlayerType[][] position = new PlayerType[3][3];
		int coupsuiv = 0;
		/* On remet a 0 le nbre de cp calculs */
		nbCoups = 0;
		int temp;
		int max;

		/* Adapte l'algorithme  un type de joueur MIN ou MAX */
		if (getType() == PlayerType.OPLAYER) {
			max = 1;
		} else {
			max = -1;
		}

		int p = skillLevel;

		/* Pour tous les coups possibles */
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				/* Recopie la position */
				for (int a = 0; a < 3; a++)
					for (int b = 0; b < 3; b++)
						position[a][b] = TicTacToe.getConfiguration()
								.getGameBoardConfiguration()[a][b];

				/* Test pour savoir si un coup a dj t jou dans cette case */
				if (position[i][j] == PlayerType.NONE) {
					if ((position[2][2] == PlayerType.XPLAYER)
							&& (position[1][2] == PlayerType.XPLAYER)
							&& (position[0][2] == PlayerType.NONE))
						return 2;
					if (getType() == PlayerType.OPLAYER) {
						position[i][j] = PlayerType.OPLAYER;
						temp = miniMax(position, PlayerType.XPLAYER, -1, p);
						if (temp <= max) {
							max = temp;
							coupsuiv = i * 3 + j;
						}
					} else {
						position[i][j] = PlayerType.XPLAYER;
						temp = miniMax(position, PlayerType.OPLAYER, 1, p);
						if (temp >= max) {
							max = temp;
							coupsuiv = i * 3 + j;
						}
					}
				}
			}
		}
		TicTacToe.getTicTacToe();
		TicTacToe.getGui().getOptionPanel().setNbCoups(nbCoups, getType());
		return coupsuiv;
	}

	public int miniMax(PlayerType[][] pos, PlayerType joueur, int max, int p) {
		PlayerType[][] position = new PlayerType[3][3];
		int temp;
		
		nbCoups++; /* Incrementation du nbre de cps calculs */
		if ((p <= 0) || terminalConfiguration(pos)) {
			if (skillLevel < 6) {
				return new Configuration(pos).evaluation();
			}
			return eval(pos);
		} else {
			/* Pour tous les coups restants */
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					/* Recopie la position */
					for (int a = 0; a < 3; a++)
						for (int b = 0; b < 3; b++)
							position[a][b] = pos[a][b];

					/*
					 * Test por savoir si un coup a dj t jou dans cette
					 * case
					 */
					if (position[i][j] == PlayerType.NONE) {
						if (joueur == PlayerType.OPLAYER) {
							position[i][j] = PlayerType.OPLAYER;
							temp = miniMax(position, PlayerType.XPLAYER, -1,
									p - 1);
							if (temp <= max)
								max = temp;
						} else {
							position[i][j] = PlayerType.XPLAYER;
							temp = miniMax(position, PlayerType.OPLAYER, 1,
									p - 1);
							if (temp >= max)
								max = temp;
						}
					}
				}
			}
			return max;
		}
	}

	/**
	 * Checks whether row is occupied by a player.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return PlayerType Player that occupies row.
	 * @since 1.2a
	 */
	public PlayerType row(PlayerType[][] pos) {
		for (int i = 0; i < 3; i++) {
			if ((pos[0][i] == pos[1][i]) && (pos[1][i] == pos[2][i]))
				return pos[0][i];
		}
		return PlayerType.NONE;
	}

	/**
	 * Checks whether column is occupied by a player.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return PlayerType Player that occupies column.
	 * @since 1.2a
	 */
	public PlayerType column(PlayerType[][] pos) {
		for (int i = 0; i < 3; i++) {
			if ((pos[i][0] == pos[i][1]) && (pos[i][1] == pos[i][2]))
				return pos[i][0];
		}
		return PlayerType.NONE;
	}

	/**
	 * Checks whether a diagonal has been occupied by a player.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return PlayerType Player that occupies diagonal
	 * @since 1.2a
	 */
	public PlayerType diagonal(PlayerType[][] pos) {
		if ((pos[0][0] == pos[1][1]) && (pos[1][1] == pos[2][2]))
			return pos[0][0];
		else if ((pos[2][0] == pos[1][1]) && (pos[1][1] == pos[0][2]))
			return pos[2][0];
		else
			return PlayerType.NONE;
	}

	/**
	 * Checks whether a player has won.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return int Player that has won.
	 * @since 1.2a
	 */
	public int eval(PlayerType[][] pos) {
		if ((row(pos) == PlayerType.XPLAYER)
				|| (column(pos) == PlayerType.XPLAYER)
				|| (diagonal(pos) == PlayerType.XPLAYER))
			return 1;
		else if ((row(pos) == PlayerType.OPLAYER)
				|| (column(pos) == PlayerType.OPLAYER)
				|| (diagonal(pos) == PlayerType.OPLAYER))
			return -1;
		else
			return 0;
	}

	/**
	 * Checks whether all squares have been occupied.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return boolean true if all squares have been occupied; false otherwise.
	 * @since 1.2a
	 */
	public boolean allSquaresOccupied(PlayerType[][] pos) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (pos[i][j] == PlayerType.NONE)
					return false;
		return true;
	}

	/**
	 * Checks whether board configuration is terminal one.
	 * 
	 * @param pos
	 *            Game board configuration
	 * @return boolean true if a player has won or all squares are occupied; false otherwise.
	 * @since 1.2a
	 */
	public boolean terminalConfiguration(PlayerType[][] pos) {
		if ((eval(pos) != 0) || allSquaresOccupied(pos)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns what kind of player this is.
	 * 
	 * @return PlayerKind AI
	 * @since 1.2a
	 */
	public PlayerKind getKindofPlayer() {
		return PlayerKind.AI;
	}

	/**
	 * Returns skill level.
	 * 
	 * @return int skill level
	 * @since 1.1
	 */
	public int getSkillLevel() {
		return skillLevel;
	}

	/**
	 * Sets skill level of AI player.
	 * 
	 * @param level
	 *            Skill level of player.
	 * @since 1.1
	 */
	public void setSkillLevel(int level) {
		skillLevel = level;
	}
}