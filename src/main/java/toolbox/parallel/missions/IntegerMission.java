package toolbox.parallel.missions;

import toolbox.parallel.*;

/**
 * La classe {@link IntegerMission} implémente une mission définie par un numéro.
 * @author Ludovic WALLE
 */
public class IntegerMission implements Mission {



	/**
	 * @param number Numéro.
	 */
	public IntegerMission(int number) {
		this.number = number;
	}



	/**
	 * @param string Numéro sous forme de chaine.
	 */
	public IntegerMission(String string) {
		this.number = Integer.parseInt(string);
	}



	/**
	 * @param mission Mission définie par une chaine.
	 */
	public IntegerMission(StringMission mission) {
		this.number = Integer.parseInt(mission.getString());
	}



	/**
	 * Retourne le numéro.
	 * @return Le numéro.
	 */
	public int getNumber() {
		return number;
	}



	/**
	 * Numéro.
	 */
	private final int number;



}
