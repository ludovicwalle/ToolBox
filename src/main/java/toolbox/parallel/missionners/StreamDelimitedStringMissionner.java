package toolbox.parallel.missionners;

import java.io.*;

import toolbox.*;
import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link StreamDelimitedStringMissionner} impl�mente un g�n�rateur qui retourne des chaines d�limit�es lues depuis un flux.
 * @author Ludovic WALLE
 */
public class StreamDelimitedStringMissionner extends Missionner<StringMission> {



	/**
	 * Extrait de ce flux une chaine d�limit�e par la premi�re occurrence de la chaine de d�but indiqu�e et la premi�re occurrence de la chaine de fin indiqu�e.<br>
	 * Si les chaines de d�but et de fin ne sont pas trouv�es, la m�thode retourne <code>null</code> et le flux est toujours ouvert mais compl�tement consomm�.<br>
	 * Si la chaine de d�but est vide, la chaine extraite commencera � la position courante dans le flux.<br>
	 * Si la chaine de fin est vide, la chaine extraite s'arr�tera � la fin du flux.<br>
	 * Donc si les chaines de d�but et de fin sont toutes deux vides, la chaine retourn�e sera tout le flux � partir de la position courante. Et si de plus la position courante est le d�but du flux,
	 * la chaine retourn�e sera la totalit� du flux.
	 * @param reader Flux d'o� proviennent les chaines d�limit�es.
	 * @param from Chaine de d�but (ne doit pas �tre <code>null</code>).<br>
	 * @param fromIncluded Indicateur de chaine de d�but inclue dans la chaine extraite.
	 * @param upto Chaine de fin (ne doit pas �tre <code>null</code>).<br>
	 *            Si la chaine est vide, la chaine extraite ira jusqu'� la fin du flux.
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
	 * Retourne la chaine de d�but.
	 * @return La chaine de d�but.
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
	 * Retourne l'indicateur de chaine de d�but inclue dans la chaine extraite.
	 * @return L'indicateur de chaine de d�but inclue dans la chaine extraite.
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
	 * Chaine de d�but (ne doit pas �tre <code>null</code>).
	 */
	private final String from;



	/**
	 * Indicateur de chaine de d�but inclue dans la chaine extraite.
	 */
	private final boolean fromIncluded;



	/**
	 * Flux d'o� proviennent les chaines d�limit�es.
	 */
	private final DelimitedReader reader;



	/**
	 * Chaine de d�but (ne doit pas �tre <code>null</code>).<br>
	 * Si la chaine est vide, la chaine extraite ira jusqu'� la fin du flux.
	 */
	private final String upto;



	/**
	 * Indicateur de chaine de fin inclue dans la chaine extraite.
	 */
	private final boolean uptoIncluded;



}
