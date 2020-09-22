package tictactoe.player;

/**
 * Agent player.
 *
 * @author koen
 */
public class PlayerAgent extends Player {
	/**
	 * Creates new agent player of certain type (x or o player, or none).
	 *
	 * @param type Player type
	 */
	public PlayerAgent(final PlayerType type) {
		super(type);
	}

	/**
	 * Returns kind of player (Agent)
	 *
	 * @return PlayerKind agent
	 * @since 1.2a
	 */
	@Override
	public PlayerKind getKindofPlayer() {
		return PlayerKind.AGENT;
	}
}
