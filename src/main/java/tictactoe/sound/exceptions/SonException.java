package tictactoe.sound.exceptions;

/**
 * Exception generale <br>
 */
public class SonException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception
	 *
	 * @param message Message de l'exception
	 */
	public SonException(final String message) {
		super(message);
	}
}