package toolbox;

import java.io.*;



/**
 * La classe {@link DelimitedReader}
 * @author Ludovic WALLE
 */
public class DelimitedReader extends Reader {



	/**
	 * @param reader Lecteur (ne doit pas �tre <code>null</code>).
	 */
	public DelimitedReader(Reader reader) {
		if (reader == null) {
			throw new NullPointerException();
		}
		this.reader = reader;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void close() throws IOException {
		reader.close();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void mark(int readAheadLimit) throws IOException {
		reader.mark(readAheadLimit);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int read(char[] chars, int offset, int charCount) throws IOException {
		return reader.read(chars, offset, charCount);
	}



	/**
	 * Extrait de ce flux une chaine d�limit�e par la premi�re occurrence de la chaine de d�but indiqu�e et la premi�re occurrence de la chaine de fin indiqu�e.<br>
	 * Si les chaines de d�but et de fin ne sont pas trouv�es, la m�thode retourne <code>null</code> et le flux est toujours ouvert mais compl�tement consomm�.<br>
	 * Si la chaine de d�but est vide, la chaine extraite commencera � la position courante dans le flux.<br>
	 * Si la chaine de fin est vide, la chaine extraite s'arr�tera � la fin du flux.<br>
	 * Donc si les chaines de d�but et de fin sont toutes deux vides, la chaine retourn�e sera tout le flux � partir de la position courante. Et si de plus la position courante est le d�but du flux,
	 * la chaine retourn�e sera la totalit� du flux.
	 * @param from Chaine de d�but (ne doit pas �tre <code>null</code>).<br>
	 * @param fromIncluded Indicateur de chaine de d�but inclue dans la chaine extraite.
	 * @param upto Chaine de fin (ne doit pas �tre <code>null</code>).<br>
	 *            Si la chaine est vide, la chaine extraite ira jusqu'� la fin du flux.
	 * @param uptoIncluded Indicateur de chaine de fin inclue dans la chaine extraite.
	 * @return La chaine extraite si elle a �t� trouv�e, <code>null</code> sinon.
	 * @throws IOException
	 */
	public synchronized String read(String from, boolean fromIncluded, String upto, boolean uptoIncluded) throws IOException {
		char[] fromChars = from.toCharArray();
		char[] uptoChars = upto.toCharArray();
		StringBuilder fragment = new StringBuilder();
		CharCircularStorage lastChars;
		char[] chars;
		int read = 0;

		if (fromChars.length > 0) {
			lastChars = new CharCircularStorage(fromChars.length);
			while (!lastChars.equals(fromChars) && ((read = reader.read()) != -1)) {
				lastChars.add((char) read);
			}
			if ((read != -1) && (fromIncluded)) {
				fragment.append(from);
			}
		}
		if (read != -1) {
			if (uptoChars.length == 0) {
				chars = new char[1024 * 1024];
				while ((read = reader.read(chars, 0, chars.length)) != -1) {
					fragment.append(chars, 0, read);
				}
				return fragment.toString();
			} else {
				lastChars = new CharCircularStorage(uptoChars.length);
				while (!lastChars.equals(uptoChars) && ((read = reader.read()) != -1)) {
					fragment.append((char) read);
					lastChars.add((char) read);
				}
				if (read != -1) {
					if (!uptoIncluded) {
						fragment.setLength(fragment.length() - upto.length());
					}
					return fragment.toString();
				}
			}
		}
		return null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void reset() throws IOException {
		reader.reset();
	}



	/**
	 * Lecteur de donn�es sur lequel s'appuie ce lecteur.
	 */
	private final Reader reader;



}
