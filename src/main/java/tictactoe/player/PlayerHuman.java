package tictactoe.player;

/**
 * Human player.
 *
 * @author Yohann CIURLIK
 * @author Koen
 * @since 1.2
 */
public class PlayerHuman extends Player {
	/**
	 * Create new human player of certain type (x or o player, or none).
	 *
	 * @param type Player type
	 */
	public PlayerHuman(final PlayerType type) {
		super(type);
	}

	/**
	 * Returns kind of player (Human)
	 *
	 * @return PlayerKind Human
	 * @since 1.2a
	 */
	@Override
	public PlayerKind getKindofPlayer() {
		return PlayerKind.HUMAN;
	}
}
