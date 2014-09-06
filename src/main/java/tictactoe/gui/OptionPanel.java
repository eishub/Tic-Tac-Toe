package tictactoe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JLabel nbPj1, nbPj2, nbPnul;
	private JLabel nbPj1x, nbPj2x, nbPnulx;
	private JLabel nbCoupsJ1, nbCoupsJ2;
	/* Information Panels */
	private JPanel panelStat, panelPlayer, panelJ1, panelJ2, panelButton;
	/* new game button */
	private JButton newGameButton;
	/* Combo boxes for choosing playing strength and type of player */
	private JComboBox niveauJ1, niveauJ2, j1, j2;

	// FIXME use PlayerKind class
	// private static final String HUMAN = "Human Player";
	// private static final String COMPUTER = "Computer";
	// private String[] playerkinds = { HUMAN, COMPUTER };
	/* Game playing strength options for computer player */
	private String[] playingStrength = { "2", "3", "4", "5", "6", "7", "8" };

	/**
	 * Creates a panel with options
	 * 
	 * @since 1.1
	 */
	public OptionPanel() {
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Statistics",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

		nbPj1 = new JLabel("Player 1", SwingConstants.LEFT);
		nbPj1.setIcon(new javax.swing.ImageIcon(OptionPanel.class
				.getResource("images/cross_mini.png")));

		nbPj2 = new JLabel("Player 2", SwingConstants.LEFT);
		nbPj2.setIcon(new javax.swing.ImageIcon(OptionPanel.class
				.getResource("images/nought_mini.png")));
		nbPnul = new JLabel("Draws", SwingConstants.LEFT);

		nbPj1x = new JLabel("0", SwingConstants.CENTER);
		nbPj2x = new JLabel("0", SwingConstants.CENTER);
		nbPnulx = new JLabel("0", SwingConstants.CENTER);
		nbCoupsJ1 = new JLabel("Cp : 0", SwingConstants.CENTER);
		nbCoupsJ2 = new JLabel("Cp : 0", SwingConstants.CENTER);

		j1 = new JComboBox(PlayerKind.values());
		j1.setEditable(true);
		j1.setSelectedItem(TicTacToe.getTicTacToe().getPlayer1()
				.getKindofPlayer());
		// disabled #3038
		j1.setEnabled(false);
		// j1.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// String selectedItem = (String) j1.getSelectedItem();
		// if (selectedItem == HUMAN) {
		// setPlayer(PlayerType.XPLAYER, PlayerKind.HUMAN);
		// } else if (selectedItem == COMPUTER) {
		// setPlayer(PlayerType.XPLAYER, PlayerKind.AI);
		// }
		// }
		// });

		j2 = new JComboBox(PlayerKind.values());
		j2.setEditable(true);
		j2.setSelectedItem(TicTacToe.getTicTacToe().getPlayer2()
				.getKindofPlayer());
		// disabled #3038
		j2.setEnabled(false);
		// j2.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// String selectedItem = (String) j2.getSelectedItem();
		// if (selectedItem == HUMAN) {
		// setPlayer(PlayerType.OPLAYER, PlayerKind.HUMAN);
		// } else if (selectedItem == COMPUTER) {
		// setPlayer(PlayerType.OPLAYER, PlayerKind.AI);
		// }
		// }
		// });

		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TicTacToe.getTicTacToe().newGame();
				TicTacToe.getGui().repaint();
			}
		});

		niveauJ1 = new JComboBox(playingStrength);
		niveauJ1.setEnabled(false);
		niveauJ1.setSelectedItem("8");
		niveauJ1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((PlayerAI) TicTacToe.getTicTacToe().getPlayer1())
						.setSkillLevel(Integer.parseInt((String) niveauJ1
								.getSelectedItem()));
			}
		});

		niveauJ2 = new JComboBox(playingStrength);
		niveauJ2.setSelectedItem("8");
		niveauJ2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((PlayerAI) TicTacToe.getTicTacToe().getPlayer2())
						.setSkillLevel(Integer.parseInt((String) niveauJ2
								.getSelectedItem()));
			}
		});

		panelPlayer = new JPanel(new BorderLayout());
		panelPlayer = new JPanel(new GridLayout(2, 1, 3, 3));
		panelStat = new JPanel(new GridLayout(3, 2, 3, 3));
		panelStat.setBorder(new LineBorder(Color.GRAY));
		panelJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelJ1.setBorder(new LineBorder(Color.GRAY));
		panelJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelJ2.setBorder(new LineBorder(Color.GRAY));

		panelStat.add(nbPj1);
		panelStat.add(nbPj1x);
		panelStat.add(nbPj2);
		panelStat.add(nbPj2x);
		panelStat.add(nbPnul);
		panelStat.add(nbPnulx);

		/* Player 1 options */
		panelJ1.add(new JLabel("Player 1:"));
		panelJ1.add(j1);
		panelJ1.add(new JLabel("Strength"));
		panelJ1.add(niveauJ1);
		panelJ1.add(nbCoupsJ1);
		/* Player 2 options */
		panelJ2.add(new JLabel("Player 2:"));
		panelJ2.add(j2);
		panelJ2.add(new JLabel("Strength"));
		panelJ2.add(niveauJ2);
		panelJ2.add(nbCoupsJ2);

		panelPlayer.add(panelJ1);
		panelPlayer.add(panelJ2);
		add(panelStat, BorderLayout.NORTH);
		add(panelPlayer, BorderLayout.CENTER);

		panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		// disabled #3038
		// panelButton.add(newGameButton);
		/* add(newGameButton,BorderLayout.SOUTH); */
		add(panelButton, BorderLayout.SOUTH);
	}

	/**
	 * Refresh statistics about the number of games each player won.
	 * 
	 * @since 1.2
	 */
	public void refreshStatCounters() {
		TicTacToe tictactoe = TicTacToe.getTicTacToe();
		nbPj1x.setText(tictactoe.getGamesWonByXPlayer() + "");
		nbPj2x.setText(tictactoe.getGamesWonByOplayer() + "");
		nbPnulx.setText(tictactoe.getDraws() + "");
	}

	/**
	 * Set player
	 */
	// public void setPlayer(PlayerType type, PlayerKind kind) {
	// switch (type) {
	// case XPLAYER:
	// switch (kind) {
	// case HUMAN:
	// TicTacToe.getTicTacToe().setPlayer1(
	// new PlayerHuman(PlayerType.XPLAYER));
	// j1.setSelectedItem("Human Player");
	// niveauJ1.setEnabled(false);
	// break;
	// case AI:
	// TicTacToe.getTicTacToe().setPlayer1(
	// new PlayerAI(PlayerType.XPLAYER));
	// j1.setSelectedItem("Computer");
	// niveauJ1.setEnabled(true);
	// break;
	// case AGENT:
	// TicTacToe.getTicTacToe().setPlayer1(
	// new PlayerAgent(PlayerType.XPLAYER));
	// j1.setSelectedItem("Agent");
	// j1.setEnabled(false);
	// niveauJ1.setEnabled(false);
	// break;
	// }
	// break;
	// case OPLAYER:
	// switch (kind) {
	// case HUMAN:
	// TicTacToe.getTicTacToe().setPlayer2(
	// new PlayerHuman(PlayerType.OPLAYER));
	// j2.setSelectedItem("Human Player");
	// niveauJ2.setEnabled(false);
	// break;
	// case AI:
	// TicTacToe.getTicTacToe().setPlayer2(
	// new PlayerAI(PlayerType.OPLAYER));
	// j2.setSelectedItem("Computer");
	// niveauJ2.setEnabled(true);
	// break;
	// case AGENT:
	// TicTacToe.getTicTacToe().setPlayer2(
	// new PlayerAgent(PlayerType.OPLAYER));
	// j2.setSelectedItem("Agent");
	// j2.setEnabled(false);
	// niveauJ2.setEnabled(false);
	// break;
	// }
	// break;
	// }
	// /* Reset counter */
	// TicTacToe.getTicTacToe().resetGameStatistics();
	// refreshStatCounters();
	// /* Start a new game */
	// TicTacToe.getTicTacToe().newGame();
	// TicTacToe.getGui().repaint();
	// }

	/**
	 * Donne le nombre de cp calculs par un joueur AI durant une recherche
	 * 
	 * @param nbCoups
	 *            Nombre de coups calculs par l'IA
	 * @param player
	 *            Joueur qui  calcul ses nbCoups
	 * @since 1.2
	 */
	public void setNbCoups(int nbCoups, PlayerType player) {
		switch (player) {
		case XPLAYER:
			nbCoupsJ1.setText("Cp : " + nbCoups);
			break;
		case OPLAYER:
			nbCoupsJ2.setText("Cp : " + nbCoups);
			break;
		}
	}
}
