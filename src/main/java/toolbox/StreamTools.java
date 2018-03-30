package toolbox;

import java.io.*;
import java.util.*;



/**
 * La classe {@link StreamTools} contient des méthodes statiques d'usage général relatives aux flux.
 * @author Ludovic WALLE
 */
public class StreamTools {



	/**
	 * Lit le contenu du flux indiqué.<br>
	 * Le flux n'est pas fermé à la fin.
	 * @param stream Flux d'entrée.
	 * @return Les octets du flux.
	 * @throws IOException
	 */
	public static byte[] readFully(final InputStream stream) throws IOException {
		byte[] bytes = new byte[0x10000];
		byte[] allBytes = NO_BYTE;
		int byteCount = 0;
		int read;

		while ((read = stream.read(bytes, byteCount, bytes.length - byteCount)) != -1) {
			byteCount += read;
			if (byteCount == bytes.length) {
				allBytes = ArraysTools.concat(allBytes, bytes);
				byteCount = 0;
			}
		}
		if (byteCount > 0) {
			if (byteCount < bytes.length) {
				bytes = Arrays.copyOf(bytes, byteCount);
			}
			if (allBytes == NO_BYTE) {
				allBytes = bytes;
			} else {
				allBytes = ArraysTools.concat(allBytes, bytes);
			}
		}
		return allBytes;
	}



	/**
	 * Tableau vide d'octets.
	 */
	private static final byte[] NO_BYTE = new byte[0];



}
