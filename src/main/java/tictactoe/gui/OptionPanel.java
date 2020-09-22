package tictactoe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import tictactoe.game.TicTacToe;
import tictactoe.player.PlayerAI;
import tictactoe.player.PlayerKind;
import tictactoe.player.PlayerType;

/**
 * PanelOption.java
 *
 * Created on 7 January 2006, 21:45
 *
 * Panel with options and information related to game.
 *
 * @author spawnrider
 * @author koen
 */
public class OptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* Labels */
	private final JLabel nbPj1, nbPj2, nbPnul;
	private final JLabel nbPj1x, nbPj2x, nbPnulx;
	private final JLabel nbCoupsJ1, nbCoupsJ2;
	/* Information Panels */
	private final JPanel panelStat;
	private JPanel panelPlayer;
	private final JPanel panelJ1;
	private final JPanel panelJ2;
	private final JPanel panelButton;
	/* new game button */
	private final JButton newGameButton;
	/* Combo boxes for choosing playing strength and type of player */
	private final JComboBox<String> niveauJ1, niveauJ2;
	private final JComboBox<PlayerKind> j1, j2;
	/* Game playing strength options for computer player */
	private final String[] playingStrength = { "2", "3", "4", "5", "6", "7", "8" };

	/**
	 * Creates a panel with options
	 *
	 * @since 1.1
	 */
	public OptionPanel() {
		setLayout(new GridLayout(3, 1));
		setBorder(
				new TitledBorder(new EtchedBorder(), "Statistics", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

		this.nbPj1 = new JLabel("Player 1", SwingConstants.LEFT);
		this.nbPj1.setIcon(new javax.swing.ImageIcon(OptionPanel.class.getResource("images/cross_mini.png")));

		this.nbPj2 = new JLabel("Player 2", SwingConstants.LEFT);
		this.nbPj2.setIcon(new javax.swing.ImageIcon(OptionPanel.class.getResource("images/nought_mini.png")));
		this.nbPnul = new JLabel("Draws", SwingConstants.LEFT);

		this.nbPj1x = new JLabel("0", SwingConstants.CENTER);
		this.nbPj2x = new JLabel("0", SwingConstants.CENTER);
		this.nbPnulx = new JLabel("0", SwingConstants.CENTER);
		this.nbCoupsJ1 = new JLabel("Cp : 0", SwingConstants.CENTER);
		this.nbCoupsJ2 = new JLabel("Cp : 0", SwingConstants.CENTER);

		this.j1 = new JComboBox<>(PlayerKind.values());
		this.j1.setEditable(true);
		this.j1.setSelectedItem(TicTacToe.getTicTacToe().getPlayer1().getKindofPlayer());
		// disabled #3038
		this.j1.setEnabled(false);

		this.j2 = new JComboBox<>(PlayerKind.values());
		this.j2.setEditable(true);
		this.j2.setSelectedItem(TicTacToe.getTicTacToe().getPlayer2().getKindofPlayer());
		// disabled #3038
		this.j2.setEnabled(false);

		this.newGameButton = new JButton("New Game");
		this.newGameButton.addActionListener(e -> {
			TicTacToe.getTicTacToe().newGame();
			TicTacToe.getGui().repaint();
		});

		this.niveauJ1 = new JComboBox<>(this.playingStrength);
		this.niveauJ1.setEnabled(false);
		this.niveauJ1.setSelectedItem("8");
		this.niveauJ1.addActionListener(e -> ((PlayerAI) TicTacToe.getTicTacToe().getPlayer1())
				.setSkillLevel(Integer.parseInt((String) OptionPanel.this.niveauJ1.getSelectedItem())));

		this.niveauJ2 = new JComboBox<>(this.playingStrength);
		this.niveauJ2.setSelectedItem("8");
		this.niveauJ2.addActionListener(e -> ((PlayerAI) TicTacToe.getTicTacToe().getPlayer2())
				.setSkillLevel(Integer.parseInt((String) OptionPanel.this.niveauJ2.getSelectedItem())));

		this.panelPlayer = new JPanel(new BorderLayout());
		this.panelPlayer = new JPanel(new GridLayout(2, 1, 3, 3));
		this.panelStat = new JPanel(new GridLayout(3, 2, 3, 3));
		this.panelStat.setBorder(new LineBorder(Color.GRAY));
		this.panelJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.panelJ1.setBorder(new LineBorder(Color.GRAY));
		this.panelJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.panelJ2.setBorder(new LineBorder(Color.GRAY));

		this.panelStat.add(this.nbPj1);
		this.panelStat.add(this.nbPj1x);
		this.panelStat.add(this.nbPj2);
		this.panelStat.add(this.nbPj2x);
		this.panelStat.add(this.nbPnul);
		this.panelStat.add(this.nbPnulx);

		/* Player 1 options */
		this.panelJ1.add(new JLabel("Player 1:"));
		this.panelJ1.add(this.j1);
		this.panelJ1.add(new JLabel("Strength"));
		this.panelJ1.add(this.niveauJ1);
		this.panelJ1.add(this.nbCoupsJ1);
		/* Player 2 options */
		this.panelJ2.add(new JLabel("Player 2:"));
		this.panelJ2.add(this.j2);
		this.panelJ2.add(new JLabel("Strength"));
		this.panelJ2.add(this.niveauJ2);
		this.panelJ2.add(this.nbCoupsJ2);

		this.panelPlayer.add(this.panelJ1);
		this.panelPlayer.add(this.panelJ2);
		add(this.panelStat, BorderLayout.NORTH);
		add(this.panelPlayer, BorderLayout.CENTER);

		this.panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(this.panelButton, BorderLayout.SOUTH);
	}

	/**
	 * Refresh statistics about the number of games each player won.
	 *
	 * @since 1.2
	 */
	public void refreshStatCounters() {
		final TicTacToe tictactoe = TicTacToe.getTicTacToe();
		this.nbPj1x.setText(tictactoe.getGamesWonByXPlayer() + "");
		this.nbPj2x.setText(tictactoe.getGamesWonByOplayer() + "");
		this.nbPnulx.setText(tictactoe.getDraws() + "");
	}

	/**
	 * Donne le nombre de cp calculs par un joueur AI durant une recherche
	 *
	 * @param nbCoups Nombre de coups calculs par l'IA
	 * @param player  Joueur qui calcul ses nbCoups
	 * @since 1.2
	 */
	public void setNbCoups(final int nbCoups, final PlayerType player) {
		switch (player) {
		case XPLAYER:
			this.nbCoupsJ1.setText("Cp : " + nbCoups);
			break;
		case OPLAYER:
			this.nbCoupsJ2.setText("Cp : " + nbCoups);
			break;
		default:
			break;
		}
	}
}
