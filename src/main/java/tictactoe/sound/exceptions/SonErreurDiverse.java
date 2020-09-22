package tictactoe.sound.exceptions;

/**
 * Exception leve pour signale un manque de droit, un flux coup en cours de
 * routez, une erreur ... <br>
 */
public class SonErreurDiverse extends SonException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception
	 *
	 * @param e Exception gnre la construction du son
	 */
	public SonErreurDiverse(final Exception e) {
		super("Une erreur s'est produite lors de l'analyse du son : " + e.getMessage());
	}
}