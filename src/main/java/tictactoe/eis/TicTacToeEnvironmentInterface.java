package tictactoe.eis;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import tictactoe.game.Configuration;
import tictactoe.game.TicTacToe;
import tictactoe.player.PlayerAI;
import tictactoe.player.PlayerAgent;
import tictactoe.player.PlayerHuman;
import tictactoe.player.PlayerKind;
import tictactoe.player.PlayerType;
import eis.EIDefaultImpl;
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

/**
 * Environment interface for connecting GOAL agents to Tic Tac Toe game.
 * 
 * @author K.Hindriks
 */
public class TicTacToeEnvironmentInterface extends EIDefaultImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ENUM names to refer to the initialization parameters that can be set.
	 */
	private enum InitKey {
		XPLAYER, OPLAYER, GUI;

		/**
		 * Returns the ENUM constant that corresponds with the key.
		 * 
		 * @param key
		 *            A string indicating a parameter that can be set.
		 * @return The ENUM constant corresponding to the provided key.
		 * @throws ManagementException
		 *             In case key is not recognized as a valid player.
		 */
		static InitKey toKey(String key) throws ManagementException {
			try {
				return valueOf(key.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ManagementException(
						key
								+ "  is not a valid key for the Tic-Tac-Toe environment",
						e);
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
		 * @param engine
		 *            is string, case insensitive.
		 * @return PlayerEngine type.
		 * @throws ManagementException
		 *             In case engine was not recognized as a valid
		 *             {@link PlayerReasoner}.
		 */
		public static PlayerReasoner fromString(String engine)
				throws ManagementException {
			try {
				return PlayerReasoner.valueOf(engine.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ManagementException("Did not recognize " + engine
						+ " as a valid player.", e);
			}
		}
	};

	/**
	 * Only after informing both players about the end of game state the state
	 * of the environment can be transitioned to {@link EnvironmentState.PAUSED}
	 * .
	 */
	private boolean informedXPlayerOfEndOfGame = false;
	private boolean informedOPlayerOfEndOfGame = false;

	/**
	 * Launches the game.
	 * 
	 * @param argv
	 *            Arguments are ignored.
	 */
	public static void main(String argv[]) {
		// Set default initial players: X player is a human, O player is AI.
		TicTacToe.getTicTacToe()
				.setXPlayer(new PlayerHuman(PlayerType.XPLAYER));
		TicTacToe.getTicTacToe().setOPlayer(new PlayerAI(PlayerType.OPLAYER));
		// Launch GUI.
		TicTacToe.getGui();
		// Start new game.
		TicTacToe.getTicTacToe().newGame();
	}

	/**
	 * Launches the Tic-Tac-Toe game upon loading the EIS interface. Just a stub
	 * to get started. We need initialization information before we can start
	 * setting up things. E.g., entities can be made available only when we know
	 * what type of entities we should be creating.
	 */
	public TicTacToeEnvironmentInterface() {
	}

	/**************************************************************/
	/******************** Support functions ***********************/
	/**************************************************************/

	public void actionoccupy(int position, String player) {

		PlayerType playerType = PlayerType.fromString(player);
		Configuration currentConfiguration = TicTacToe.getConfiguration();

		if (currentConfiguration.getCurrentPlayerType() == playerType) { // also
																			// checks
																			// whether
																			// game
																			// has
																			// not
																			// yet
																			// ended
			if (TicTacToe.getConfiguration().getPlayerAtSquare(position - 1) == PlayerType.NONE) {
				TicTacToe.getConfiguration().setPlayerAtSquare(position - 1,
						playerType);
				if (TicTacToe.showGui) {
					TicTacToe.getGui().getRootPane().repaint();
				}

				Percept percept = new Percept("pos", new Numeral(position),
						new Identifier(playerType.toString()));
				try {
					notifyAgents(percept);
				} catch (EnvironmentInterfaceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // a move can only be made if the square is free; otherwise,
				// simply ignore the attempt to make a move

			// Check if computer (ai) player needs to move
			if (!currentConfiguration.isEndOfGame()) {
				if (currentConfiguration.getCurrentPlayer().getKindofPlayer() == PlayerKind.AI) {
					int p = ((PlayerAI) currentConfiguration.getCurrentPlayer())
							.coupOrdi();
					currentConfiguration.setPlayerAtSquare(p,
							currentConfiguration.getCurrentPlayerType());
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

	/**
	 * Supports EIS v0.3.
	 * 
	 * @see eis.EnvironmentInterfaceStandard#requiredVersion()
	 */
	@Override
	public String requiredVersion() {
		return "0.3";
	}

	/**
	 * Returns current board configuration as list of percepts and the player
	 * whose turn it is.
	 */
	@Override
	protected LinkedList<Percept> getAllPerceptsFromEntity(String entity)
			throws PerceiveException, NoEnvironmentException {

		LinkedList<Percept> percepts = new LinkedList<Percept>();

		PlayerType currentPlayer = TicTacToe.getConfiguration()
				.getCurrentPlayerType();
		if (currentPlayer != PlayerType.NONE) {
			percepts.add(new Percept("turn", new Identifier(currentPlayer
					.toString())));
		}

		PlayerType[][] gameConfiguration = TicTacToe.getConfiguration()
				.getGameBoardConfiguration();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				percepts.add(new Percept("pos", new Numeral(i * 3 + j + 1),
						new Identifier(gameConfiguration[i][j].toString())));
			}
		}

		PlayerType winner = TicTacToe.getConfiguration().getWinner();
		if (winner != null) {
			if (winner != PlayerType.NONE) {
				percepts.add(new Percept("winner", new Identifier(winner
						.toString())));
			} else {
				percepts.add(new Percept("draw"));
			}
			try {
				this.informedXPlayerOfEndOfGame |= (InitKey.toKey(entity) == InitKey.XPLAYER);
				this.informedOPlayerOfEndOfGame |= (InitKey.toKey(entity) == InitKey.OPLAYER);
				// Game has ended. If both players have been informed, set
				// environment state to PAUSED.
				if (this.informedXPlayerOfEndOfGame
						&& this.informedOPlayerOfEndOfGame) {
					setState(EnvironmentState.PAUSED);
				}
			} catch (ManagementException e) {
				// TODO: for now the best we can do??
				System.out
						.print("End of Tic-Tac-Toe game but could not change EIS to PAUSED state.");
			}
		}

		return percepts;
	}

	/**
	 * All entities can perform the action 'occupy' and only that action.
	 * 
	 * @return true if action name is 'occupy'; false otherwise.
	 */
	@Override
	protected boolean isSupportedByEntity(Action action, String entity) {
		// TODO Auto-generated method stub
		return action.getName().equals("occupy");
	}

	@Override
	protected boolean isSupportedByEnvironment(Action arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean isSupportedByType(Action arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected Percept performEntityAction(String entity, Action action)
			throws ActException {

		int position;

		if (!isSupportedByEntity(action, entity)) {
			throw new ActException(
					ActException.FAILURE,
					"action "
							+ action
							+ "is not supported; only 'occupy' action with single integer argument is supported.");
		}

		/* first check parameter */
		LinkedList<Parameter> params = action.getParameters();
		if (params.size() != 1) {
			throw new ActException(ActException.FAILURE, "action " + action
					+ "has single argument, but found argument(s) " + params);
		}
		if (!(params.getFirst() instanceof Numeral)) {
			throw new ActException(ActException.FAILURE, "action " + action
					+ "should have integer argument, but found argument "
					+ params);
		}
		position = ((Numeral) params.getFirst()).getValue().intValue();
		if (position < 1 || position > 9) {
			throw new ActException(
					ActException.FAILURE,
					"action "
							+ action
							+ "should have integer argument between 1 and 9, but found "
							+ params);
		}

		actionoccupy(position, entity);

		return null; // TODO: new Percept("pos", new Numeral(position), new
						// Identifier());
	}

	@Override
	public void init(Map<String, Parameter> parameters)
			throws ManagementException {
		setState(EnvironmentState.INITIALIZING);
		TicTacToe.getTicTacToe();
		processParameters(parameters);
		TicTacToe.getGui();
		TicTacToe.getTicTacToe().newGame();
		setState(EnvironmentState.PAUSED);
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
	public void reset(Map<String, Parameter> parameters)
			throws ManagementException {
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
	 * @return "1" if the current player has won the game; "-1" if other player
	 *         has won the game; "0" otherwise.
	 */
	@Override
	public String queryProperty(String property) throws QueryException {
		if (property.startsWith("REWARD ")) {
			HashSet<String> entities;
			String agentName = property.substring(REWARD.length());
			try {
				entities = getAssociatedEntities(agentName);
			} catch (AgentException e) {
				throw new QueryException("can't get entities associated with "
						+ agentName, e);
			}
			if (entities.size() != 1) {
				throw new QueryException("agent " + agentName
						+ " has not exactly 1 associated entity:" + entities);
			}
			String entity = entities.iterator().next();
			PlayerType playerType = PlayerType.fromString(entity);
			PlayerType winner = TicTacToe.getConfiguration().getWinner();
			if (winner == null || winner == playerType.NONE) {
				return "0"; // game still running or ended in draw
			}
			if (winner == playerType) {
				return "1";
			} else {
				return "-1";
			}
		}
		return null;
	}

	/**
	 * DOC
	 * 
	 * @param parameters
	 * @throws ManagementException
	 */
	private void processParameters(Map<String, Parameter> parameters)
			throws ManagementException {

		// GUI is enabled by default
		// boolean guimode = true;

		// Class InitKeys lists all valid keys.
		for (String key : parameters.keySet()) {

			Parameter value = parameters.get(key);
			// handle initialization
			switch (InitKey.toKey(key)) {
			case XPLAYER: // that is player 1
				if (value instanceof Identifier) {
					switch (PlayerReasoner.fromString(((Identifier) value)
							.getValue())) {
					case HUMAN:
						TicTacToe.getTicTacToe().setPlayer1(
								new PlayerHuman(PlayerType.XPLAYER));
						break;
					case AGENT:
						TicTacToe.getTicTacToe().setPlayer1(
								new PlayerAgent(PlayerType.XPLAYER));
						// now make entity available via eis, if not already so
						if (!getEntities().contains("xplayer")) {
							try {
								this.addEntity("xplayer");
							} catch (EntityException e) {
								e.printStackTrace();
							}
						}
						break;
					case AI:
						TicTacToe.getTicTacToe().setPlayer1(
								new PlayerAI(PlayerType.XPLAYER));
						break;
					default:
						throw new ManagementException(
								"Player type 'human', 'agent', or 'ai' expected as value for key"
										+ "'xplayer' but got " + value);
					}
				} else {
					throw new ManagementException(
							"Player type 'human', 'agent', or 'ai' expected as value for key"
									+ "'xplayer' but got " + value);
				}

				// make entity available
				break;
			case OPLAYER: // that is player 2
				if (value instanceof Identifier) {
					switch (PlayerReasoner.fromString(((Identifier) value)
							.getValue())) {
					case HUMAN:
						TicTacToe.getTicTacToe().setPlayer2(
								new PlayerHuman(PlayerType.OPLAYER));
						break;
					case AGENT:
						TicTacToe.getTicTacToe().setPlayer2(
								new PlayerAgent(PlayerType.OPLAYER));
						// now make entity available via eis, if not already so
						if (!getEntities().contains("oplayer")) {
							try {
								this.addEntity("oplayer");
							} catch (EntityException e) {
								e.printStackTrace();
							}
						}
						break;
					case AI:
						TicTacToe.getTicTacToe().setPlayer2(
								new PlayerAI(PlayerType.OPLAYER));
						break;
					default:
						throw new ManagementException(
								"Player type 'human', 'agent', or 'ai' expected as value for key"
										+ "'xplayer' but got " + value);
					}
				} else {
					throw new ManagementException(
							"Player type 'human', 'agent', or 'ai' expected as value for key"
									+ "'xplayer' but got " + value);
				}
				break;
			case GUI:
				if (value instanceof Identifier) {
					TicTacToe.showGui = Boolean
							.parseBoolean(((Identifier) value).getValue());
				} else {
					throw new ManagementException(
							"\"true\" or \"false\" expected as value for key "
									+ "\"gui\" but got " + value);
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
