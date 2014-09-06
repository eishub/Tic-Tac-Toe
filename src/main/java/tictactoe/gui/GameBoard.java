package tictactoe.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import tictactoe.game.Configuration;
import tictactoe.game.TicTacToe;
import tictactoe.player.PlayerAI;
import tictactoe.player.PlayerKind;
import tictactoe.player.PlayerType;
import tictactoe.sound.Sound;

/**
 * Tic-Tac-Toe game board.<br>
 * Handles events by user.
 * 
 * @since 1.0a
 * @modified koen
 */
public class GameBoard extends JPanel {

	private static final long serialVersionUID = 1L;

	/* Cross and nought symbols */
	private Image cross = new ImageIcon(
			GameBoard.class.getResource("images/cross.png")).getImage();
	private Image nought = new ImageIcon(
			GameBoard.class.getResource("images/nought.png")).getImage();

	/* Mouse coordinates */
	private int mouseX, mouseY;
	/* Game sounds */
	private Sound gameSound, winningSound;
	/* Taille d'une image */
	private int tailleImage;
	/* Coordinates and size of graphical game board */
	private int morpX, morpY, morpSizeX, morpSizeY;
	/* Boolean for controlling sound */
	/* Sound is enabled by default since menu is default enabled. */
	private boolean soundEnabled = true;

	/**
	 * Creates new game board.
	 * 
	 * @since 1.0a
	 */
	public GameBoard() {
		super(true);
		setSize(240, 240);

		/* Create sounds associated with events */
		// Disabled sound, it only is throwing.
		// try {
		// ClassLoader loader = ClassLoader.getSystemClassLoader();
		// setGameSound(new Sound(
		// loader.getResource("tictactoe/sounds/poutch.wav")));
		// winningSound = new Sound(
		// loader.getResource("tictactoe/sounds/gagne.wav"));
		// new Sound(loader.getResource("tictactoe/sounds/perd.wav"));
		// } catch (SonException e) {
		// e.printStackTrace();
		// }

		/* TODO placement en x et y du tictactoe */
		morpX = 10;
		morpY = 10;
		/* Height and width of game board */
		morpSizeX = 240;
		morpSizeY = 240;
		/* TODO hauteur et largeur d'une image */
		tailleImage = (int) (morpSizeX / 3) - 1;

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Configuration currentConfiguration = TicTacToe
						.getConfiguration();

				if (!currentConfiguration.isEndOfGame()) {
					int p = getPositionByCoordinates(mouseX, mouseY);
					if ((p >= 0) && currentConfiguration.isFree(p)) {
						switch (currentConfiguration.getCurrentPlayer()
								.getKindofPlayer()) {
						case HUMAN:
							currentConfiguration
									.setPlayerAtSquare(p, currentConfiguration
											.getCurrentPlayerType());
							if (soundEnabled) {
								getGameSound().play();
							}
							getRootPane().repaint();
							if (!currentConfiguration.isEndOfGame()) {
								// setPlayerAtSquare updates current player
								if (currentConfiguration.getCurrentPlayer()
										.getKindofPlayer() == PlayerKind.AI) {
									p = ((PlayerAI) currentConfiguration
											.getCurrentPlayer()).coupOrdi();
									currentConfiguration.setPlayerAtSquare(p,
											currentConfiguration
													.getCurrentPlayerType());
									if (soundEnabled) {
										getGameSound().play();
									}
									getRootPane().repaint();
								}
							}
							currentConfiguration.isEndOfGame();
							break;
						}
					}
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX = (int) e.getX();
				mouseY = (int) e.getY();
			}
		});
	}

	/**
	 * Paints game board.
	 * 
	 * @param g
	 *            Graphical panel with game board.
	 * @since 1.0a
	 */
	public void paint(Graphics g) {
		PlayerType p;
		Graphics g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		/* Création du fond blanc */
		g2d.fill3DRect(morpX, morpY, morpSizeX, morpSizeY, true);
		g2d.setColor(Color.BLACK);
		/* Creation du cadre noir */
		g2d.drawRect(morpX, morpY, morpSizeX, morpSizeY);
		/* Les deux barres noires verticales */
		g2d.drawLine((int) ((morpSizeX * 1) / 3) + morpX, morpY,
				(int) ((morpSizeX * 1) / 3) + morpX, morpSizeY + morpY);
		g2d.drawLine((int) ((morpSizeX * 2) / 3) + morpX, morpY,
				(int) ((morpSizeX * 2) / 3) + morpX, morpSizeY + morpY);
		/* Les deux barres noires horizontales */
		g2d.drawLine(morpX, (int) ((morpSizeY * 1) / 3) + morpY, morpSizeX
				+ morpX, (int) ((morpSizeY * 1) / 3) + morpY);
		g2d.drawLine(morpX, (int) ((morpSizeY * 2) / 3) + morpY, morpSizeX
				+ morpX, (int) ((morpSizeY * 2) / 3) + morpY);
		TicTacToe.getTicTacToe();
		/* Dessin des "morpions" ou il faut */
		PlayerType[][] tabCase = TicTacToe.getConfiguration()
				.getGameBoardConfiguration();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				p = tabCase[i][j];
				switch (p) {
				case NONE: /* No image */
					break;
				case OPLAYER:
					g2d.drawImage(nought, getXforPosition(3 * i + j),
							getYforPosition(3 * i + j), tailleImage,
							tailleImage, this);
					break;
				case XPLAYER:
					g2d.drawImage(cross, getXforPosition(3 * i + j),
							getYforPosition(3 * i + j), tailleImage,
							tailleImage, this);
					break;
				}
			}
		}
	}

	/**
	 * Determine x coordinate for a square position.
	 * 
	 * @param p
	 *            Number of square position
	 * @return int x coordinate
	 * @since 1.0a
	 */
	private int getXforPosition(int p) {
		int y = 0;
		switch (p + 1) {
		case 1:
			y = morpY + 1;
			break;
		case 2:
			y = morpY + (int) ((morpSizeY * 1) / 3) + 1;
			break;
		case 3:
			y = morpY + (int) ((morpSizeY * 2) / 3) + 1;
			break;
		case 4:
			y = morpY + 1;
			break;
		case 5:
			y = morpY + (int) ((morpSizeY * 1) / 3) + 1;
			break;
		case 6:
			y = morpY + (int) ((morpSizeY * 2) / 3) + 1;
			break;
		case 7:
			y = morpY + 1;
			break;
		case 8:
			y = morpY + (int) ((morpSizeY * 1) / 3) + 1;
			break;
		case 9:
			y = morpY + (int) ((morpSizeY * 2) / 3) + 1;
			break;
		}
		return y;
	}

	/**
	 * Determine y coordinate for position p.
	 * 
	 * @param p
	 *            Number of square position
	 * @return int y coordinate
	 * @since 1.0a
	 */
	private int getYforPosition(int p) {
		int x = 0;
		switch (p + 1) {
		case 1:
			x = morpX + 1;
			break;
		case 2:
			x = morpX + 1;
			break;
		case 3:
			x = morpX + 1;
			break;
		case 4:
			x = morpX + (int) ((morpSizeX * 1) / 3) + 1;
			break;
		case 5:
			x = morpX + (int) ((morpSizeX * 1) / 3) + 1;
			break;
		case 6:
			x = morpX + (int) ((morpSizeX * 1) / 3) + 1;
			break;
		case 7:
			x = morpX + (int) ((morpSizeX * 2) / 3) + 1;
			break;
		case 8:
			x = morpX + (int) ((morpSizeX * 2) / 3) + 1;
			break;
		case 9:
			x = morpX + (int) ((morpSizeX * 2) / 3) + 1;
			break;
		}
		return x;
	}

	/**
	 * Determines number of square position by means of coordinates.
	 * 
	 * @param x
	 *            x-coordinate of position
	 * @param y
	 *            y-coordinate of position
	 * @return int position
	 * @since 1.0a
	 */
	private int getPositionByCoordinates(int x, int y) {
		int caseX, caseY;
		caseY = (int) ((x - morpX) / (tailleImage + 1));
		caseX = (int) ((y - morpY) / (tailleImage + 1));
		if ((caseX >= 0) && (caseX < 3) && (caseY >= 0) && (caseY < 3)) {
			return 3 * caseX + caseY;
		} else {
			return -1;
		}
	}

	// SOUND

	/**
	 * Returns whether sound is enabled.
	 * 
	 * @return true if sound is enabled; false otherwise.
	 */
	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	/**
	 * Set sound on/off
	 * 
	 * @param b
	 *            Boolean indicating whether sound should be on/off
	 * @since 1.1a
	 */
	public void setSound(boolean b) {
		soundEnabled = b;
	}

	/**
	 * Plays sound TODO permettant de savoir qu'un joueur à jouer
	 * 
	 * @since 1.1a
	 */
	public void playSound() {
		getGameSound().play();
	}

	public Sound getGameSound() {
		return gameSound;
	}

	public void setGameSound(Sound gameSound) {
		this.gameSound = gameSound;
	}

	public Sound getWinningGameSound() {
		return winningSound;
	}

}
