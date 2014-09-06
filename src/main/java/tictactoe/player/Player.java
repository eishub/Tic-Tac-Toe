package tictactoe.player;

/**
 * Player class.
 * 
 * @author Yohann CIURLIK
 * @modified K.Hindriks
 */
public abstract class Player {

	/**
	 * Player type (either x or o player, or none)
	 */
	protected PlayerType type;

	/**
	 * Creates a new player of a particular type.
	 * 
	 * @param type
	 *            Player type
	 */
	public Player(PlayerType type) {
		this.type = type;
	}

	/**
	 * Returns player type.
	 * 
	 * @return PlayerType either x or o player, or none.
	 */
	public PlayerType getType() {
		return type;
	}

	/**
	 * Returns info on kind of player: human, AI, or agent.
	 * 
	 * @return PlayerKind Kind of player, either human, AI or agent.
	 */
	public abstract PlayerKind getKindofPlayer();

}
