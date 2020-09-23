package tictactoe.eis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eis.EIDefaultImpl;
import eis.PerceptUpdate;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.QueryException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import tictactoe.game.Configuration;
import tictactoe.game.TicTacToe;
import tictactoe.player.PlayerAI;
import tictactoe.player.PlayerAgent;
import tictactoe.player.PlayerHuman;
import tictactoe.player.PlayerKind;
import tictactoe.player.PlayerType;

/**
 * Environment interface for connecting GOAL agents to Tic Tac Toe game.
 *
 * @author K.Hindriks
 */
public class TicTacToeEnvironmentInterface extends EIDefaultImpl {
	private static final long serialVersionUID = 1L;

	/**
	 * ENUM names to refer to the initialization parameters that can be set.
	 */
	private enum InitKey {
		XPLAYER, OPLAYER, GUI;

		/**
		 * Returns the ENUM constant that corresponds with the key.
		 *
		 * @param key A string indicating a parameter that can be set.
		 * @return The ENUM constant corresponding to the provided key.
		 * @throws ManagementException In case key is not recognized as a valid player.
		 */
		static InitKey toKey(final String key) throws ManagementException {
			try {
				return valueOf(key.toUpperCase());
			} catch (final IllegalArgumentException e) {
				throw new ManagementException(key + "  is not a valid key for the Tic-Tac-Toe environment", e);
			}
		}
	}

	/**
	 * Types of players.
	 *
	 * @author W.Pasman 5jul12
	 */
	private enum PlayerReasoner {
		/**
		 * Human plays. GUI required.
		 */
		HUMAN,
		/**
		 * GOAL agent plays. Environment required.
		 */
		AGENT,
		/**
		 * Built-in AI plays.
		 */
		AI;

		/**
		 * accepts the values as string and returns the proper enum.
		 *
		 * @param engine is string, case insensitive.
		 * @return PlayerEngine type.
		 * @throws ManagementException In case engine was not recognized as a valid
		 *                             {@link PlayerReasoner}.
		 */
		public static PlayerReasoner fromString(final String engine) throws ManagementException {
			try {
				return PlayerReasoner.valueOf(engine.toUpperCase());
			} catch (final IllegalArgumentException e) {
				throw new ManagementException("Did not recognize " + engine + " as a valid player.", e);
			}
		}
	}

	/**
	 * Only after informing both players about the end of game state the state of
	 * the environment can be transitioned to {link eis.EnvironmentState.PAUSED}
	 * TODO Link not working.
	 */
	private boolean informedXPlayerOfEndOfGame = false;
	private boolean informedOPlayerOfEndOfGame = false;

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
	 * Launches the Tic-Tac-Toe game upon loading the EIS interface. Just a stub to
	 * get started. We need initialization information before we can start setting
	 * up things. E.g., entities can be made available only when we know what type
	 * of entities we should be creating.
	 */
	public TicTacToeEnvironmentInterface() {
	}

	/**************************************************************/
	/******************** Support functions ***********************/
	/**************************************************************/
	/**
	 *
	 * @param position position
	 * @param player   player
	 */
	public void actionoccupy(final int position, final String player) {
		final PlayerType playerType = PlayerType.fromString(player);
		final Configuration currentConfiguration = TicTacToe.getConfiguration();

		if (currentConfiguration.getCurrentPlayerType() == playerType) {
			// also checks whether game has not yet ended
			if (TicTacToe.getConfiguration().getPlayerAtSquare(position - 1) == PlayerType.NONE) {
				TicTacToe.getConfiguration().setPlayerAtSquare(position - 1, playerType);
				if (TicTacToe.showGui) {
					TicTacToe.getGui().getRootPane().repaint();
				}

				final Percept percept = new Percept("pos", new Numeral(position),
						new Identifier(playerType.toString()));
				try {
					notifyAgents(percept);
				} catch (final EnvironmentInterfaceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // a move can only be made if the square is free; otherwise,
				// simply ignore the attempt to make a move

			// Check if computer (ai) player needs to move
			if (!currentConfiguration.isEndOfGame()) {
				if (currentConfiguration.getCurrentPlayer().getKindofPlayer() == PlayerKind.AI) {
					final int p = ((PlayerAI) currentConfiguration.getCurrentPlayer()).coupOrdi();
					currentConfiguration.setPlayerAtSquare(p, currentConfiguration.getCurrentPlayerType());
					if (TicTacToe.getGui().getGameBoard().isSoundEnabled()) {
						TicTacToe.getGui().getGameBoard().getGameSound().play();
					}
					TicTacToe.getGui().getRootPane().repaint();
				}
				currentConfiguration.isEndOfGame();
			}
		} // we simply ignore actions that are requested when it's not the
			// player's turn
	}

	/**************************************************************/
	/********** Implements EnvironmentInterface *******************/
	/**************************************************************/

	private final Map<String, List<Percept>> previousPercepts = new ConcurrentHashMap<>();

	/**
	 * Returns current board configuration as list of percepts and the player whose
	 * turn it is.
	 */
	@Override
	protected PerceptUpdate getPerceptsForEntity(final String entity) throws PerceiveException, NoEnvironmentException {
		final List<Percept> percepts = new LinkedList<>();

		final PlayerType currentPlayer = TicTacToe.getConfiguration().getCurrentPlayerType();
		if (currentPlayer != PlayerType.NONE) {
			percepts.add(new Percept("turn", new Identifier(currentPlayer.toString())));
		}

		final PlayerType[][] gameConfiguration = TicTacToe.getConfiguration().getGameBoardConfiguration();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				percepts.add(new Percept("pos", new Numeral(i * 3 + j + 1),
						new Identifier(gameConfiguration[i][j].toString())));
			}
		}

		List<Percept> previous = this.previousPercepts.get(entity);
		if (previous == null) {
			previous = new ArrayList<>(0);
		}
		final List<Percept> addList = new ArrayList<>(percepts);
		addList.removeAll(previous);
		final List<Percept> delList = new ArrayList<>(previous);
		delList.removeAll(percepts);
		this.previousPercepts.put(entity, percepts);

		final PlayerType winner = TicTacToe.getConfiguration().getWinner();
		if (winner != null) {
			if (winner != PlayerType.NONE) {
				addList.add(new Percept("winner", new Identifier(winner.toString())));
			} else {
				addList.add(new Percept("draw"));
			}
			try {
				this.informedXPlayerOfEndOfGame |= (InitKey.toKey(entity) == InitKey.XPLAYER);
				this.informedOPlayerOfEndOfGame |= (InitKey.toKey(entity) == InitKey.OPLAYER);
				// Game has ended. If both players have been informed, set
				// environment state to PAUSED.
				if (this.informedXPlayerOfEndOfGame && this.informedOPlayerOfEndOfGame) {
					setState(EnvironmentState.PAUSED);
				}
			} catch (final ManagementException e) {
				// TODO: for now the best we can do??
				System.out.println("End of Tic-Tac-Toe game but could not change EIS to PAUSED state.");
			}
		}

		return new PerceptUpdate(addList, delList);
	}

	/**
	 * All entities can perform the action 'occupy' and only that action.
	 *
	 * @return true if action name is 'occupy'; false otherwise.
	 */
	@Override
	protected boolean isSupportedByEntity(final Action action, final String entity) {
		return action.getName().equals("occupy");
	}

	@Override
	protected boolean isSupportedByEnvironment(final Action arg0) {
		return true;
	}

	@Override
	protected boolean isSupportedByType(final Action arg0, final String arg1) {
		return true;
	}

	@Override
	protected void performEntityAction(final Action action, final String entity) throws ActException {
		if (!isSupportedByEntity(action, entity)) {
			throw new ActException(ActException.FAILURE, "action " + action
					+ "is not supported; only 'occupy' action with single integer argument is supported.");
		}

		/* first check parameter */
		final List<Parameter> params = action.getParameters();
		if (params.size() != 1) {
			throw new ActException(ActException.FAILURE,
					"action " + action + "has single argument, but found argument(s) " + params);
		}
		if (!(params.get(0) instanceof Numeral)) {
			throw new ActException(ActException.FAILURE,
					"action " + action + "should have integer argument, but found argument " + params);
		}
		final int position = ((Numeral) params.get(0)).getValue().intValue();
		if (position < 1 || position > 9) {
			throw new ActException(ActException.FAILURE,
					"action " + action + "should have integer argument between 1 and 9, but found " + params);
		}

		actionoccupy(position, entity);
	}

	@Override
	public void init(final Map<String, Parameter> parameters) throws ManagementException {
		setState(EnvironmentState.INITIALIZING);
		TicTacToe.getTicTacToe();
		processParameters(parameters);
		TicTacToe.getGui();
		TicTacToe.getTicTacToe().newGame();
		setState(EnvironmentState.PAUSED);

		try { // now make entities available via eis, if not already so
			if (!getEntities().contains("xplayer")) {
				this.addEntity("xplayer");
			}
			if (!getEntities().contains("oplayer")) {
				this.addEntity("oplayer");
			}
		} catch (final EntityException e) {
			throw new ManagementException("failed to announce entities", e);
		}

		setState(EnvironmentState.RUNNING);
	}

	@Override
	public void start() throws ManagementException {
		if (TicTacToe.getConfiguration().isEndOfGame()) {
			TicTacToe.getTicTacToe().newGame();
		}

		this.informedXPlayerOfEndOfGame = false;
		this.informedOPlayerOfEndOfGame = false;
		setState(EnvironmentState.RUNNING);
	}

	@Override
	public void pause() throws ManagementException {
		setState(EnvironmentState.PAUSED);
	}

	@Override
	public void reset(final Map<String, Parameter> parameters) throws ManagementException {
		processParameters(parameters);
		this.informedXPlayerOfEndOfGame = false;
		this.informedOPlayerOfEndOfGame = false;
		TicTacToe.getTicTacToe().newGame();

		setState(EnvironmentState.RUNNING);
	}

	@Override
	public void kill() throws ManagementException {
		TicTacToe.closeTicTacToe();
		setState(EnvironmentState.KILLED);
	}

	private static String REWARD = "REWARD ";

	/**
	 * Get current reward.
	 *
	 * @return "1" if the current player has won the game; "-1" if other player has
	 *         won the game; "0" otherwise.
	 */
	@Override
	public String queryProperty(final String property) throws QueryException {
		if (property.startsWith("REWARD ")) {
			Set<String> entities;
			final String agentName = property.substring(REWARD.length());
			try {
				entities = getAssociatedEntities(agentName);
			} catch (final AgentException e) {
				throw new QueryException("can't get entities associated with " + agentName, e);
			}
			if (entities.size() != 1) {
				throw new QueryException("agent " + agentName + " has not exactly 1 associated entity:" + entities);
			}
			final String entity = entities.iterator().next();
			final PlayerType playerType = PlayerType.fromString(entity);
			final PlayerType winner = TicTacToe.getConfiguration().getWinner();
			if (winner == null || winner == PlayerType.NONE) {
				return "0"; // game still running or ended in draw
			} else if (winner == playerType) {
				return "1";
			} else {
				return "-1";
			}
		}
		return null;
	}

	private void processParameters(final Map<String, Parameter> parameters) throws ManagementException {
		// Class InitKeys lists all valid keys.
		for (final String key : parameters.keySet()) {
			final Parameter value = parameters.get(key);
			// handle initialization
			switch (InitKey.toKey(key)) {
			case XPLAYER: // that is player 1
				if (value instanceof Identifier) {
					switch (PlayerReasoner.fromString(((Identifier) value).getValue())) {
					case HUMAN:
						TicTacToe.getTicTacToe().setPlayer1(new PlayerHuman(PlayerType.XPLAYER));
						break;
					case AGENT:
						TicTacToe.getTicTacToe().setPlayer1(new PlayerAgent(PlayerType.XPLAYER));
						break;
					case AI:
						TicTacToe.getTicTacToe().setPlayer1(new PlayerAI(PlayerType.XPLAYER));
						break;
					default:
						throw new ManagementException("Player type 'human', 'agent', or 'ai' expected as value for key"
								+ "'xplayer' but got " + value);
					}
				} else {
					throw new ManagementException("Player type 'human', 'agent', or 'ai' expected as value for key"
							+ "'xplayer' but got " + value);
				}
				break;
			case OPLAYER: // that is player 2
				if (value instanceof Identifier) {
					switch (PlayerReasoner.fromString(((Identifier) value).getValue())) {
					case HUMAN:
						TicTacToe.getTicTacToe().setPlayer2(new PlayerHuman(PlayerType.OPLAYER));
						break;
					case AGENT:
						TicTacToe.getTicTacToe().setPlayer2(new PlayerAgent(PlayerType.OPLAYER));
						break;
					case AI:
						TicTacToe.getTicTacToe().setPlayer2(new PlayerAI(PlayerType.OPLAYER));
						break;
					default:
						throw new ManagementException("Player type 'human', 'agent', or 'ai' expected as value for key"
								+ "'xplayer' but got " + value);
					}
				} else {
					throw new ManagementException("Player type 'human', 'agent', or 'ai' expected as value for key"
							+ "'xplayer' but got " + value);
				}
				break;
			case GUI:
				if (value instanceof Identifier) {
					TicTacToe.showGui = Boolean.parseBoolean(((Identifier) value).getValue());
				} else {
					throw new ManagementException(
							"\"true\" or \"false\" expected as value for key " + "\"gui\" but got " + value);
				}
				break;
			}
		}
	}

	/**
	 * Provides the name of the environment.
	 *
	 * @return The name of the environment.
	 */
	@Override
	public String toString() {
		return "Tic-Tac-Toe";
	}
}
