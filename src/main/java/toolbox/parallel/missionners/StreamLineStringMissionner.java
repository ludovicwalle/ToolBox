package toolbox.parallel.missionners;

import java.io.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link StreamLineStringMissionner} impl�mente un g�n�rateur qui retourne les lignes lues depuis un flux.
 * @author Ludovic WALLE
 */
public class StreamLineStringMissionner extends Missionner<StringMission> {



	/**
	 * @param reader Flux d'o� proviennent les lignes.
	 */
	public StreamLineStringMissionner(Reader reader) {
		this.reader = new BufferedReader(reader);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected StringMission delegateGetNext() throws Exception {
		String string;

		for (;;) {
			if ((reader == null) || ((string = reader.readLine()) == null)) {
				return null;
			} else {
				return new StringMission(string);
			}
		}
	}



	/**
	 * Flux d'o� proviennent les lignes.
	 */
	private final BufferedReader reader;



}
