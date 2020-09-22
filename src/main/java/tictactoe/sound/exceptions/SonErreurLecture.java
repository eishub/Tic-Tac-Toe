package tictactoe.sound.exceptions;

/**
 * Exception leve pour signaler une erreur de lecture du son <br>
 */
public class SonErreurLecture extends SonException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception
	 */
	public SonErreurLecture() {
		super("Erreur lors de la lecture du son");
	}
}