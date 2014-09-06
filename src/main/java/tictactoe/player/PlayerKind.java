package tictactoe.player;

/** 
 * Enum for representing the kinds of players. Three kinds of players are distinguished:
 * i) human players
 * ii) Computer (AI) players
 * iii) external (software) agents
 * 
 * @author koen
 *
 */
public enum PlayerKind {
	/**
	 * Human player (can be selected via interface)
	 */
	HUMAN {
		public String toString() {
			return "Human";
		}
	},
	/**
	 * AI computer players provided with this package.
	 */
	AI {
		public String toString() {
			return "Computer";
		}
	},
	/**
	 * Agents are connected to game through EIS.
	 */
	AGENT {
		public String toString() {
			return "Agent";
		}
	}
}
