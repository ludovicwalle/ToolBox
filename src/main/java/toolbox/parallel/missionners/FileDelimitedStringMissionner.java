package toolbox.parallel.missionners;

import java.io.*;

import toolbox.*;



/**
 * La classe {@link FileDelimitedStringMissionner} implémente un générateur qui retourne des chaines délimitées lues depuis un flux.
 * @author Ludovic WALLE
 */
public class FileDelimitedStringMissionner extends StreamDelimitedStringMissionner {



	/**
	 * Extrait de ce flux une chaine délimitée par la première occurrence de la chaine de début indiquée et la première occurrence de la chaine de fin indiquée.<br>
	 * Si les chaines de début et de fin ne sont pas trouvées, la méthode retourne <code>null</code> et le flux est toujours ouvert mais complètement consommé.<br>
	 * Si la chaine de début est vide, la chaine extraite commencera à la position courante dans le flux.<br>
	 * Si la chaine de fin est vide, la chaine extraite s'arrêtera à la fin du flux.<br>
	 * Donc si les chaines de début et de fin sont toutes deux vides, la chaine retournée sera tout le flux à partir de la position courante. Et si de plus la position courante est le début du flux,
	 * la chaine retournée sera la totalité du flux.
	 * @param fileName Nom du fichier d'où proviennent les chaines délimitées.
	 * @param from Chaine de début (ne doit pas être <code>null</code>).<br>
	 * @param fromIncluded Indicateur de chaine de début inclue dans la chaine extraite.
	 * @param upto Chaine de fin (ne doit pas être <code>null</code>).<br>
	 *            Si la chaine est vide, la chaine extraite ira jusqu'à la fin du fichier.
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
