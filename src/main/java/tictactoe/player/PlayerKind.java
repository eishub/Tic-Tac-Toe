package tictactoe.player;

/**
 * Enum for representing the kinds of players. Three kinds of players are
 * distinguished: i) human players ii) Computer (AI) players iii) external
 * (software) agents
 *
 * @author koen
 */
public enum PlayerKind {
	/**
	 * Human player (can be selected via interface)
	 */
	HUMAN {
		@Override
		public String toString() {
			return "Human";
		}
	},
	/**
	 * AI computer players provided with this package.
	 */
	AI {
		@Override
		public String toString() {
			return "Computer";
		}
	},
	/**
	 * Agents are connected to game through EIS.
	 */
	AGENT {
		@Override
		public String toString() {
			return "Agent";
		}
	}
}
