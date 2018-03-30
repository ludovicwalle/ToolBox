package toolbox.parallel.missionners;

import java.io.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link StreamNumberMissionner} implémente un générateur qui retourne les numéros lus depuis un flux.
 * @author Ludovic WALLE
 */
public class StreamNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * @param reader Flux d'où proviennent les numéros.
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
	 * Flux d'où proviennent les numéros.
	 */
	private final BufferedReader reader;



}
