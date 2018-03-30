package toolbox.parallel.missionners;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;

/**
 * La classe {@link ArrayStringMissionner} impl�mente un g�n�rateur qui retourne les chaines indiqu�es dans le constructeur.
 * @author Ludovic WALLE
 */
public class ArrayStringMissionner extends Missionner<StringMission> {



	/**
	 * @param strings Chaines (ne doivent pas �tre <code>null</code>).
	 */
	public ArrayStringMissionner(String... strings) {
		for (String string : strings) {
			if (string == null) {
				throw new NullPointerException();
			}
		}
		this.strings = strings.clone();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected StringMission delegateGetNext() throws Exception {
		if (iStrings < strings.length) {
			return new StringMission(strings[iStrings++]);
		} else {
			return null;
		}
	}



	/**
	 * Indice de la prochaine chaine � retourner.
	 */
	private int iStrings = 0;



	/**
	 * Chaines.
	 */
	private final String[] strings;



}
