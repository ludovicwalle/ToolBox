package toolbox.parallel.missions;

import toolbox.parallel.*;

/**
 * La classe {@link StringMission} implémente une mission définie par une chaine.
 * @author Ludovic WALLE
 */
public class StringMission implements Mission {



	/**
	 * @param string Chaine.
	 */
	public StringMission(String string) {
		this.string = string;
	}



	/**
	 * Retourne la chaine.
	 * @return La chaine.
	 */
	public String getString() {
		return string;
	}



	/**
	 * Chaine.
	 */
	private final String string;



}
