package tictactoe.sound.exceptions;

import java.io.File;
import java.net.URL;

/**
 * Exception leve pour indiquer que le son n'a pas t trouv <br>
 */
public class SonIntrouvableException extends SonException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception pour les fichiers
	 *
	 * @param fichier Fichier non trouve
	 */
	public SonIntrouvableException(final File fichier) {
		super("Le fichier " + fichier.getAbsolutePath() + " est introuvable");
	}

	/**
	 * Construit l'exception pour les URL
	 *
	 * @param url URL non trouve
	 */
	public SonIntrouvableException(final URL url) {
		super("L'URL : " + url.getFile() + " est introuvable");
	}

	/**
	 * Construit l'exception pour les sons de la ressource
	 *
	 * @param nom Nom de la ressource non trouve
	 */
	public SonIntrouvableException(final String nom) {
		super("Le son : " + nom + " est introuvable");
	}
}