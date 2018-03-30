package toolbox.parallel.missionners;

import java.io.*;

import toolbox.*;



/**
 * La classe {@link FileDelimitedStringMissionner} impl�mente un g�n�rateur qui retourne des chaines d�limit�es lues depuis un flux.
 * @author Ludovic WALLE
 */
public class FileDelimitedStringMissionner extends StreamDelimitedStringMissionner {



	/**
	 * Extrait de ce flux une chaine d�limit�e par la premi�re occurrence de la chaine de d�but indiqu�e et la premi�re occurrence de la chaine de fin indiqu�e.<br>
	 * Si les chaines de d�but et de fin ne sont pas trouv�es, la m�thode retourne <code>null</code> et le flux est toujours ouvert mais compl�tement consomm�.<br>
	 * Si la chaine de d�but est vide, la chaine extraite commencera � la position courante dans le flux.<br>
	 * Si la chaine de fin est vide, la chaine extraite s'arr�tera � la fin du flux.<br>
	 * Donc si les chaines de d�but et de fin sont toutes deux vides, la chaine retourn�e sera tout le flux � partir de la position courante. Et si de plus la position courante est le d�but du flux,
	 * la chaine retourn�e sera la totalit� du flux.
	 * @param fileName Nom du fichier d'o� proviennent les chaines d�limit�es.
	 * @param from Chaine de d�but (ne doit pas �tre <code>null</code>).<br>
	 * @param fromIncluded Indicateur de chaine de d�but inclue dans la chaine extraite.
	 * @param upto Chaine de fin (ne doit pas �tre <code>null</code>).<br>
	 *            Si la chaine est vide, la chaine extraite ira jusqu'� la fin du fichier.
	 * @param uptoIncluded Indicateur de chaine de fin inclue dans la chaine extraite.
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource") public FileDelimitedStringMissionner(String fileName, String from, boolean fromIncluded, String upto, boolean uptoIncluded) throws FileNotFoundException {
		super(new BufferedReader(new FileReader(fileName)), from, fromIncluded, upto, uptoIncluded);
		this.fileName = fileName;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected int delegateComputeExpectedCount() throws Throwable {
		int count = 0;

		try (DelimitedReader reader = new DelimitedReader(new BufferedReader(new FileReader(fileName)))) {
			while (reader.read(getFrom(), isFromIncluded(), getUpto(), isUptoIncluded()) != null) {
				count++;
			}
			return count;
		}
	}



	/**
	 * Nom du fichier.
	 */
	private final String fileName;



}
