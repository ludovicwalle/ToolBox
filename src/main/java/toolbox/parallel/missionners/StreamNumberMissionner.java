package toolbox.parallel.missionners;

import java.io.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link StreamNumberMissionner} impl�mente un g�n�rateur qui retourne les num�ros lus depuis un flux.
 * @author Ludovic WALLE
 */
public class StreamNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * @param reader Flux d'o� proviennent les num�ros.
	 */
	public StreamNumberMissionner(Reader reader) {
		this.reader = new BufferedReader(reader);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected IntegerMission delegateGetNext() throws Exception {
		String number;

		for (;;) {
			if ((reader == null) || ((number = reader.readLine()) == null)) {
				return null;
			} else {
				return new IntegerMission(Integer.parseInt(number));
			}
		}
	}



	/**
	 * Flux d'o� proviennent les num�ros.
	 */
	private final BufferedReader reader;



}
