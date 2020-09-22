package tictactoe.gui;

import java.awt.Color;
import java.awt.Graphics;
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
 * @author koen
 */
public class GameBoard extends JPanel {
	private static final long serialVersionUID = 1L;

	/* Cross and nought symbols */
	private final Image cross = new ImageIcon(GameBoard.class.getResource("images/cross.png")).getImage();
	private final Image nought = new ImageIcon(GameBoard.class.getResource("images/nought.png")).getImage();

	/* Mouse coordinates */
	private int mouseX, mouseY;
	/* Game sounds */
	private Sound gameSound, winningSound;
	/* Taille d'une image */
	private final int tailleImage;
	/* Coordinates and size of graphical game board */
	private final int morpX, morpY, morpSizeX, morpSizeY;
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

		/* TODO placement en x et y du tictactoe */
		this.morpX = 10;
		this.morpY = 10;
		/* Height and width of game board */
		this.morpSizeX = 240;
		this.morpSizeY = 240;
		/* TODO hauteur et largeur d'une image */
		this.tailleImage = this.morpSizeX / 3 - 1;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				final Configuration currentConfiguration = TicTacToe.getConfiguration();

				if (!currentConfiguration.isEndOfGame()) {
					int p = getPositionByCoordinates(GameBoard.this.mouseX, GameBoard.this.mouseY);
					if ((p >= 0) && currentConfiguration.isFree(p)) {
						switch (currentConfiguration.getCurrentPlayer().getKindofPlayer()) {
						case HUMAN:
							currentConfiguration.setPlayerAtSquare(p, currentConfiguration.getCurrentPlayerType());
							if (GameBoard.this.soundEnabled) {
								getGameSound().play();
							}
							getRootPane().repaint();
							if (!currentConfiguration.isEndOfGame()) {
								// setPlayerAtSquare updates current player
								if (currentConfiguration.getCurrentPlayer().getKindofPlayer() == PlayerKind.AI) {
									p = ((PlayerAI) currentConfiguration.getCurrentPlayer()).coupOrdi();
									currentConfiguration.setPlayerAtSquare(p,
											currentConfiguration.getCurrentPlayerType());
									if (GameBoard.this.soundEnabled) {
										getGameSound().play();
									}
									getRootPane().repaint();
								}
							}
							currentConfiguration.isEndOfGame();
							break;
						default:
							break;
						}
					}
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(final MouseEvent e) {
				GameBoard.this.mouseX = e.getX();
				GameBoard.this.mouseY = e.getY();
			}
		});
	}

	/**
	 * Paints game board.
	 *
	 * @param g Graphical panel with game board.
	 * @since 1.0a
	 */
	@Override
	public void paint(final Graphics g) {
		PlayerType p;
		final Graphics g2d = g;

		g2d.setColor(Color.WHITE);
		/* Cration du fond blanc */
		g2d.fill3DRect(this.morpX, this.morpY, this.morpSizeX, this.morpSizeY, true);
		g2d.setColor(Color.BLACK);
		/* Creation du cadre noir */
		g2d.drawRect(this.morpX, this.morpY, this.morpSizeX, this.morpSizeY);
		/* Les deux barres noires verticales */
		g2d.drawLine((this.morpSizeX * 1) / 3 + this.morpX, this.morpY, (this.morpSizeX * 1) / 3 + this.morpX,
				this.morpSizeY + this.morpY);
		g2d.drawLine((this.morpSizeX * 2) / 3 + this.morpX, this.morpY, (this.morpSizeX * 2) / 3 + this.morpX,
				this.morpSizeY + this.morpY);
		/* Les deux barres noires horizontales */
		g2d.drawLine(this.morpX, (this.morpSizeY * 1) / 3 + this.morpY, this.morpSizeX + this.morpX,
				(this.morpSizeY * 1) / 3 + this.morpY);
		g2d.drawLine(this.morpX, (this.morpSizeY * 2) / 3 + this.morpY, this.morpSizeX + this.morpX,
				(this.morpSizeY * 2) / 3 + this.morpY);
		TicTacToe.getTicTacToe();
		/* Dessin des "morpions" ou il faut */
		final PlayerType[][] tabCase = TicTacToe.getConfiguration().getGameBoardConfiguration();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				p = tabCase[i][j];
				switch (p) {
				case NONE: /* No image */
					break;
				case OPLAYER:
					g2d.drawImage(this.nought, getXforPosition(3 * i + j), getYforPosition(3 * i + j), this.tailleImage,
							this.tailleImage, this);
					break;
				case XPLAYER:
					g2d.drawImage(this.cross, getXforPosition(3 * i + j), getYforPosition(3 * i + j), this.tailleImage,
							this.tailleImage, this);
					break;
				}
			}
		}
	}

	/**
	 * Determine x coordinate for a square position.
	 *
	 * @param p Number of square position
	 * @return int x coordinate
	 * @since 1.0a
	 */
	private int getXforPosition(final int p) {
		int y = 0;
		switch (p + 1) {
		case 1:
			y = this.morpY + 1;
			break;
		case 2:
			y = this.morpY + (this.morpSizeY * 1) / 3 + 1;
			break;
		case 3:
			y = this.morpY + (this.morpSizeY * 2) / 3 + 1;
			break;
		case 4:
			y = this.morpY + 1;
			break;
		case 5:
			y = this.morpY + (this.morpSizeY * 1) / 3 + 1;
			break;
		case 6:
			y = this.morpY + (this.morpSizeY * 2) / 3 + 1;
			break;
		case 7:
			y = this.morpY + 1;
			break;
		case 8:
			y = this.morpY + (this.morpSizeY * 1) / 3 + 1;
			break;
		case 9:
			y = this.morpY + (this.morpSizeY * 2) / 3 + 1;
			break;
		}
		return y;
	}

	/**
	 * Determine y coordinate for position p.
	 *
	 * @param p Number of square position
	 * @return int y coordinate
	 * @since 1.0a
	 */
	private int getYforPosition(final int p) {
		int x = 0;
		switch (p + 1) {
		case 1:
			x = this.morpX + 1;
			break;
		case 2:
			x = this.morpX + 1;
			break;
		case 3:
			x = this.morpX + 1;
			break;
		case 4:
			x = this.morpX + (this.morpSizeX * 1) / 3 + 1;
			break;
		case 5:
			x = this.morpX + (this.morpSizeX * 1) / 3 + 1;
			break;
		case 6:
			x = this.morpX + (this.morpSizeX * 1) / 3 + 1;
			break;
		case 7:
			x = this.morpX + (this.morpSizeX * 2) / 3 + 1;
			break;
		case 8:
			x = this.morpX + (this.morpSizeX * 2) / 3 + 1;
			break;
		case 9:
			x = this.morpX + (this.morpSizeX * 2) / 3 + 1;
			break;
		}
		return x;
	}

	/**
	 * Determines number of square position by means of coordinates.
	 *
	 * @param x x-coordinate of position
	 * @param y y-coordinate of position
	 * @return int position
	 * @since 1.0a
	 */
	private int getPositionByCoordinates(final int x, final int y) {
		int caseX, caseY;
		caseY = (x - this.morpX) / (this.tailleImage + 1);
		caseX = (y - this.morpY) / (this.tailleImage + 1);
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
		return this.soundEnabled;
	}

	/**
	 * Set sound on/off
	 *
	 * @param b Boolean indicating whether sound should be on/off
	 * @since 1.1a
	 */
	public void setSound(final boolean b) {
		this.soundEnabled = b;
	}

	/**
	 * Plays sound TODO permettant de savoir qu'un joueur jouer
	 *
	 * @since 1.1a
	 */
	public void playSound() {
		getGameSound().play();
	}

	public Sound getGameSound() {
		return this.gameSound;
	}

	public void setGameSound(final Sound gameSound) {
		this.gameSound = gameSound;
	}

	public Sound getWinningGameSound() {
		return this.winningSound;
	}
}
