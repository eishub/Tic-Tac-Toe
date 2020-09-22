package tictactoe.sound.exceptions;

/**
 * Exception leve si le type de son n'est pas reconnu <br>
 */
public class SonTypeException extends SonException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception
	 */
	public SonTypeException() {
		super("Le type du son n'est pas reconnu");
	}

}