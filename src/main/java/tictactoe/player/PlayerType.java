package tictactoe.player;

/**
 * Enum class for representing:<BR>
 * (i) the side of a player,<BR>
 * (ii) which player occupies a square of the game board (where NONE means the
 * square is empty), and<BR>
 * (iii) for keeping track of the current player of the Tic-Tac-Toe game:
 * x-player (cross), o-player (noughts), or none.
 * 
 * @author koen
 * 
 */
public enum PlayerType {
	XPLAYER {
		public String toString() {
			return "x";
		}
	},
	OPLAYER {
		public String toString() {
			return "o";
		}
	},
	/**
	 * The NONE player is used for representing that currently no player can
	 * make a move.
	 */
	NONE {
		public String toString() {
			return "empty";
		}
	};

	/**
	 * accepts the values as string and returns the proper enum.
	 * 
	 * @param engine
	 *            is string, case insensitive.
	 * @return PlayerType.
	 */
	public static PlayerType fromString(String engine) {
		try {
			return PlayerType.valueOf(engine.toUpperCase());
		} catch (IllegalArgumentException e) {
			return PlayerType.NONE;
		}
	}

}
