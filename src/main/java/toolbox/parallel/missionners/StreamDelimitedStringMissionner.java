package toolbox.parallel.missionners;

import java.io.*;

import toolbox.*;
import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link StreamDelimitedStringMissionner} implémente un générateur qui retourne des chaines délimitées lues depuis un flux.
 * @author Ludovic WALLE
 */
public class StreamDelimitedStringMissionner extends Missionner<StringMission> {



	/**
	 * Extrait de ce flux une chaine délimitée par la première occurrence de la chaine de début indiquée et la première occurrence de la chaine de fin indiquée.<br>
	 * Si les chaines de début et de fin ne sont pas trouvées, la méthode retourne <code>null</code> et le flux est toujours ouvert mais complètement consommé.<br>
	 * Si la chaine de début est vide, la chaine extraite commencera à la position courante dans le flux.<br>
	 * Si la chaine de fin est vide, la chaine extraite s'arrêtera à la fin du flux.<br>
	 * Donc si les chaines de début et de fin sont toutes deux vides, la chaine retournée sera tout le flux à partir de la position courante. Et si de plus la position courante est le début du flux,
	 * la chaine retournée sera la totalité du flux.
	 * @param reader Flux d'où proviennent les chaines délimitées.
	 * @param from Chaine de début (ne doit pas être <code>null</code>).<br>
	 * @param fromIncluded Indicateur de chaine de début inclue dans la chaine extraite.
	 * @param upto Chaine de fin (ne doit pas être <code>null</code>).<br>
	 *            Si la chaine est vide, la chaine extraite ira jusqu'à la fin du flux.
	 * @param uptoIncluded Indicateur de chaine de fin inclue dans la chaine extraite.
	 */
	public StreamDelimitedStringMissionner(Reader reader, String from, boolean fromIncluded, String upto, boolean uptoIncluded) {
		if ((from == null) || (upto == null)) {
			throw new NullPointerException();
		}
		this.reader = new DelimitedReader(reader);
		this.from = from;
		this.fromIncluded = fromIncluded;
		this.upto = upto;
		this.uptoIncluded = uptoIncluded;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected StringMission delegateGetNext() throws Exception {
		String record;

		for (;;) {
			if ((reader == null) || ((record = reader.read(from, fromIncluded, upto, uptoIncluded)) == null)) {
				return null;
			} else {
				return new StringMission(record);
			}
		}
	}



	/**
	 * Retourne la chaine de début.
	 * @return La chaine de début.
	 */
	public String getFrom() {
		return from;
	}



	/**
	 * Retourne la chaine de fin.
	 * @return La chaine de fin.
	 */
	public String getUpto() {
		return upto;
	}



	/**
	 * Retourne l'indicateur de chaine de début inclue dans la chaine extraite.
	 * @return L'indicateur de chaine de début inclue dans la chaine extraite.
	 */
	public boolean isFromIncluded() {
		return fromIncluded;
	}



	/**
	 * Retourne l'indicateur de chaine de fin inclue dans la chaine extraite.
	 * @return L'indicateur de chaine de fin inclue dans la chaine extraite.
	 */
	public boolean isUptoIncluded() {
		return uptoIncluded;
	}



	/**
	 * Chaine de début (ne doit pas être <code>null</code>).
	 */
	private final String from;



	/**
	 * Indicateur de chaine de début inclue dans la chaine extraite.
	 */
	private final boolean fromIncluded;



	/**
	 * Flux d'où proviennent les chaines délimitées.
	 */
	private final DelimitedReader reader;



	/**
	 * Chaine de début (ne doit pas être <code>null</code>).<br>
	 * Si la chaine est vide, la chaine extraite ira jusqu'à la fin du flux.
	 */
	private final String upto;



	/**
	 * Indicateur de chaine de fin inclue dans la chaine extraite.
	 */
	private final boolean uptoIncluded;



}
