package toolbox;

import java.io.*;



/**
 * La classe {@link FileTools} contient des m�thodes statiques d'usage g�n�ral relatives aux fichiers.
 * @author Ludovic WALLE
 */
public abstract class FileTools {



	/**
	 * Verrouille le fichier indiqu� en mode non bloquant. G�n�re une exception si le fichier est d�j� verrouill�.
	 * @param file Fichier.
	 * @param fileName Nom du fichier.
	 * @throws IOException
	 */
	public static void lock(final RandomAccessFile file, final String fileName) throws IOException {
		if (file.getChannel().tryLock() == null) {
			throw new IOException("Le fichier \"" + fileName + "\" est d�ja verrouill�.");
		}
	}



	/**
	 * Lit le contenu du fichier dont le nom est indiqu�.
	 * @param fileName Nom du fichier.
	 * @return Les octets du fichier.
	 * @throws IOException
	 */
	public static byte[] readFully(final String fileName) throws IOException {
		byte[] bytes;

		try (RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
			bytes = new byte[(int) file.length()];
			file.readFully(bytes);
			file.close();
			return bytes;
		}
	}



}
