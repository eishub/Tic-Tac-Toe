package tictactoe.sound;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import tictactoe.sound.ecouteurs.EcouteurSon;
import tictactoe.sound.exceptions.SonErreurDiverse;
import tictactoe.sound.exceptions.SonErreurLecture;
import tictactoe.sound.exceptions.SonException;
import tictactoe.sound.exceptions.SonIntrouvableException;
import tictactoe.sound.exceptions.SonTypeException;

/**
 * Reprsente un son. <br>
 * Si le son ne sera plus utilis, il faut le dtruire par la mthode fermer, <br>
 * afin de librer la mmoire et le flux o se trouve le son. <br>
 * Attention, une fois dtruit, le son n'est plus utilisable. <br>
 */
public class Sound implements Runnable {
	// Dure du son
	private Duree duree;
	// Flux de kecture audio
	private AudioInputStream lecteurAudio;
	// Format du son
	private AudioFormat format;
	// Clip jouant le son
	private Clip clip;
	// Thread permettant de jouer le son en tche de fond
	private Thread thread;
	// Nombre de boucle restante effectu
	private int tour;
	// pause : inqique si le son est en pause ou non
	// fermerALaFin : indique si le son doit tre dtruit une fois la dernire
	// boucle de son excute
	private boolean pause, fermerALaFin;
	// Ecouteurs des vnement sons
	private Vector<EcouteurSon> ecouteurs = new Vector<>();

	/**
	 * Construit un son situ une URL prcise
	 *
	 * @param url URL du son
	 * @throws SonException Si il y a un problme de construction du son
	 */
	public Sound(final URL url) throws SonException {
		this.initialise(url);
	}

	/**
	 * Construit un son partir d'un fichier
	 *
	 * @param fichier Fichier contenant le son
	 * @throws SonException Si il y a un problme de construction du son
	 */
	public Sound(final File fichier) throws SonException {
		if (!fichier.exists()) {
			throw new SonIntrouvableException(fichier);
		}
		this.initialise(fichier);
	}

	// Initialise le son
	private void initialise(final File fichier) throws SonException {
		try {
			// Cre le flux
			this.lecteurAudio = AudioSystem.getAudioInputStream(fichier);
			// Rcupre le format de codage du son
			this.format = this.lecteurAudio.getFormat();

			// On ne peut pas ouvrir directement des format ALAW/ULA, il faut
			// les convertir en PCM
			if ((this.format.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (this.format.getEncoding() == AudioFormat.Encoding.ALAW)) {
				// convertion du format
				final AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(),
						this.format.getSampleSizeInBits() * 2, this.format.getChannels(),
						this.format.getFrameSize() * 2, this.format.getFrameRate(), true);
				// convertion du flux
				this.lecteurAudio = AudioSystem.getAudioInputStream(tmp, this.lecteurAudio);
				// On a convertit le format, si bien qu'il change
				this.format = tmp;
			}
			// On cre une information avec le format du flux et en caculant la
			// logueneur totale du son
			final DataLine.Info info = new DataLine.Info(Clip.class, this.lecteurAudio.getFormat(),
					((int) this.lecteurAudio.getFrameLength() * this.format.getFrameSize()));
			// Grac cette information, on peut creer un clip
			this.clip = (Clip) AudioSystem.getLine(info);
			// On ouvre le son
			reouvrir();
		} catch (final UnsupportedAudioFileException uafe) {
			throw new SonTypeException();
		} catch (final IOException ioe) {
			throw new SonErreurLecture();
		} catch (final Exception e) {
			throw new SonErreurDiverse(e);
		}

		// On calcul la dure du son en microseconde
		this.duree = new Duree(longueurSonMicroseconde());
	}

	private void initialise(final URL url) throws SonException {
		try {
			// Cre le flux
			this.lecteurAudio = AudioSystem.getAudioInputStream(url);
			// Rcupre le format de codage du son
			this.format = this.lecteurAudio.getFormat();

			// On ne peut pas ouvrir directement des format ALAW/ULA, il faut
			// les convertir en PCM
			if ((this.format.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (this.format.getEncoding() == AudioFormat.Encoding.ALAW)) {
				// convertion du format
				final AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(),
						this.format.getSampleSizeInBits() * 2, this.format.getChannels(),
						this.format.getFrameSize() * 2, this.format.getFrameRate(), true);
				// convertion du flux
				this.lecteurAudio = AudioSystem.getAudioInputStream(tmp, this.lecteurAudio);
				// On a convertit le format, si bien qu'il change
				this.format = tmp;
			}
			// On cre une information avec le format du flux et en caculant la
			// logueneur totale du son
			final DataLine.Info info = new DataLine.Info(Clip.class, this.lecteurAudio.getFormat(),
					((int) this.lecteurAudio.getFrameLength() * this.format.getFrameSize()));
			// Grac cette information, on peut creer un clip
			this.clip = (Clip) AudioSystem.getLine(info);
			// On ouvre le son
			reouvrir();
		} catch (final UnsupportedAudioFileException uafe) {
			throw new SonTypeException();
		} catch (final IOException ioe) {
			throw new SonErreurLecture();
		} catch (final Exception e) {
			throw new SonErreurDiverse(e);
		}

		// On calcul la dure du son en microseconde
		this.duree = new Duree(longueurSonMicroseconde());
	}

	/**
	 * Joue le son une fois
	 */
	public void play() {
		// Si le son n'est pas initialiser, on l'initialise
		if (this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
		}
		// On va le jouer une fois
		this.tour = 1;
	}

	/**
	 * Joue le son plusieurs fois
	 *
	 * @param nbFois Nombre de fois que le son est jou
	 */
	public void boucle(final int nbFois) {
		// Si le son n'est pas initialiser, on l'initialise
		if (this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
		}
		// On va le jouer nbFois fois
		this.tour = nbFois;
	}

	/**
	 * Joue le son un tn trs grand nombre de fois
	 */
	public void boucle() {
		this.boucle(Integer.MAX_VALUE);
	}

	/**
	 * Action du son, ne jamais appel cette mthode directement, elle est public pour
	 * respecter l'implmentation de Runnable
	 */
	@Override
	public void run() {
		// Tant que le son est vivant
		while (this.thread != null) {
			// Pause de 0.123 seconde
			try {
				Thread.sleep(123);
			} catch (final Exception e) {
			}
			// Si on doit jouer le son au moins une fois
			if (this.tour > 0) {
				// On lance le son
				this.clip.start();
				// pause de 0.099 seconde (le son est jouer pendant ce temps)
				try {
					Thread.sleep(99);
				} catch (final Exception e) {
				}
				// Tant que le son n'est pas terminer ou que l'on soit en pause
				// et est vivant
				while ((this.clip.isActive() || this.pause) && (this.thread != null)) {
					// Si on est pas en pause, on avance sur le son
					if (!this.pause) {
						avancer();
					}
					// Pause de 0.099 seconde
					try {
						Thread.sleep(99);
					} catch (final Exception e) {
						break;
					}
				}
				// Arrte le son
				this.clip.stop();
				// On se place au dbut du son
				placeMicroseconde(0);
				// On un tour de moins jouer
				this.tour--;
				if (this.tour < 1) {
					// Si on a fini de jouer, on tremine
					terminer();
					// Si on doit fermer la fin, on ferme dfinitivement le
					// son
					if (this.fermerALaFin) {
						fermer();
					}
				}
			}
		}
	}

	// Permet de rouvrir le son, ou de l'ouvrir
	private void reouvrir() throws Exception {
		this.clip.open(this.lecteurAudio);
	}

	/**
	 * Met le son en pause
	 */
	public void pause() {
		// Si on est pas dj en pause, on se met en pause
		if (!this.pause) {
			this.clip.stop();
			this.pause = true;
		}
	}

	/**
	 * Reprend le son ou il tait rendu (enlve la pause)
	 */
	public void reprise() {
		// Si on est en pause, on enlve la pause
		if (this.pause) {
			this.pause = false;
			this.clip.start();
		}
	}

	/**
	 * Arrte de jouer le son et retour du son au dbut
	 */
	public void stop() {
		this.clip.stop();
		placeMicroseconde(0);
		this.pause = false;
		this.tour = 0;
		this.thread = null;
	}

	/**
	 * Dtruit proprement le son
	 */
	public void fermer() {
		stop();
		this.clip.close();
		this.clip = null;
		this.duree = null;
		this.ecouteurs.clear();
		this.ecouteurs = null;
		this.format = null;
	}

	/**
	 * Indique si le son sera dtruit aprs sa dernire fois ou il joue
	 *
	 * @return <b>true</b> si le son est dtruit quand c'est finit
	 */
	public boolean estFermerALaFin() {
		return this.fermerALaFin;
	}

	/**
	 * Change l'tat de fermeture la fin
	 *
	 * @param fermer <b>true</b> pour indiqu que l'on dsire que le son soit dtruit
	 *               aprs la dernire fois qu'il joue
	 */
	public void setFermerALaFin(final boolean fermer) {
		this.fermerALaFin = fermer;
	}

	/**
	 * Longeur du son en microseconde
	 *
	 * @return Longueur du son
	 */
	public long longueurSonMicroseconde() {
		return this.clip.getMicrosecondLength();
	}

	/**
	 * Nombre de microsecondes coules depuis le dbut du son
	 *
	 * @return Dure en microseconde de l'coute
	 */
	public long getRenduMicroseconde() {
		return this.clip.getMicrosecondPosition();
	}

	/**
	 * Dure de l'coute
	 *
	 * @return Dure de l'coute
	 */
	public Duree getRendu() {
		return new Duree(getRenduMicroseconde());
	}

	/**
	 * Place le son cette dure en milliseconde.
	 *
	 * @param microseconde Place laquelle on dsire commenc le son
	 */
	public void placeMicroseconde(final long microseconde) {
		this.clip.setMicrosecondPosition(microseconde);
	}

	/**
	 * Place le son cette dure
	 *
	 * @param duree Place laquelle on dsire commenc le son
	 */
	public void placeDuree(final Duree duree) {
		placeMicroseconde(duree.getMicroseconde());
	}

	/**
	 * Remet le son au dpart
	 */
	public void placeDepart() {
		this.clip.setMicrosecondPosition(0);
	}

	/**
	 * Indique si le son est en pause
	 *
	 * @return <b> true</b> si le son est en pause
	 */
	public boolean estEnPause() {
		return this.pause;
	}

	/**
	 * Indique si le son est entrain d'tre jouer
	 *
	 * @return <b>true</b> si le son est entrain d'tre jou
	 */
	public boolean estEntrainDeJouer() {
		return !this.pause && (this.tour > 0);
	}

	/**
	 * Ajout un couteur d'vnement son
	 *
	 * @param ecouteur Ecouteur ajout
	 */
	public void ajouteEcouteurSon(final EcouteurSon ecouteur) {
		if (ecouteur != null) {
			this.ecouteurs.addElement(ecouteur);
		}
	}

	/**
	 * Retire un couteur d'vnement son
	 *
	 * @param ecouteur Ecouteur retir
	 */
	public void retireEcouteurSon(final EcouteurSon ecouteur) {
		if (ecouteur != null) {
			this.ecouteurs.removeElement(ecouteur);
		}
	}

	// Indique tout les couteurs d'vnements son, que le son est termin
	private void terminer() {
		final Thread t = new Thread() {
			@Override
			public void run() {
				Sound.this.terminer1();
			}
		};
		t.start();
	}

	// Indique tout les couteurs d'vnements son, que le son est termin
	private void terminer1() {
		final int nb = this.ecouteurs.size();
		for (int i = 0; i < nb; i++) {
			final EcouteurSon ecouteur = this.ecouteurs.elementAt(i);
			ecouteur.sonTermine(this);
		}
	}

	// Indique tout les couteurs d'vnements son, que le son a avanc
	private void avancer() {
		final Thread t = new Thread() {
			@Override
			public void run() {
				Sound.this.avancer1();
			}
		};
		t.start();
	}

	// Indique tout les couteurs d'vnements son, que le son a avanc
	private void avancer1() {
		final int nb = this.ecouteurs.size();
		for (int i = 0; i < nb; i++) {
			final EcouteurSon ecouteur = this.ecouteurs.elementAt(i);
			ecouteur.sonChangePosition(this);
		}
	}

	/**
	 * Renvoie la dure du son
	 *
	 * @return La dure du son
	 */
	public Duree getDuree() {
		return this.duree;
	}
}