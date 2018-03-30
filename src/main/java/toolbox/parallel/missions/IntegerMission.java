package toolbox.parallel.missions;

import toolbox.parallel.*;

/**
 * La classe {@link IntegerMission} impl�mente une mission d�finie par un num�ro.
 * @author Ludovic WALLE
 */
public class IntegerMission implements Mission {



	/**
	 * @param number Num�ro.
	 */
	public IntegerMission(int number) {
		this.number = number;
	}



	/**
	 * @param string Num�ro sous forme de chaine.
	 */
	public IntegerMission(String string) {
		this.number = Integer.parseInt(string);
	}



	/**
	 * @param mission Mission d�finie par une chaine.
	 */
	public IntegerMission(StringMission mission) {
		this.number = Integer.parseInt(mission.getString());
	}



	/**
	 * Retourne le num�ro.
	 * @return Le num�ro.
	 */
	public int getNumber() {
		return number;
	}



	/**
	 * Num�ro.
	 */
	private final int number;



}
