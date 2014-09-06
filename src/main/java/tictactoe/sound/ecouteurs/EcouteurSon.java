package tictactoe.sound.ecouteurs;

import tictactoe.sound.Sound;

/**
 * Indique que l'objet coute les vnements sons <br>
 */

public interface EcouteurSon {
	/**
	 * Indique qu'un son est termin
	 * 
	 * @param son
	 *            Son qui a finit de jou
	 */
	public void sonTermine(Sound son);

	/**
	 * Indique qu'un son vient d'avancer sur sa lecture
	 * 
	 * @param son
	 *            Son qui est entrain d'avancer
	 */
	public void sonChangePosition(Sound son);
}
