package toolbox;

import java.io.*;
import java.util.zip.*;



/**
 * La classe {@link #ZipTools} contient des méthodes statiques d'usage général relatives à la compression / décompression.
 * @author walle
 */
public abstract class ZipTools {



	/**
	 * Décompresse une chaine.<br>
	 * La séquence d'octets compressée est considérée comme étant une chaine encodée en utilisant l'encodage UTF-8.
	 * @param zipFragments Tableaux d'octets contenant des fragments de la chaine compressée (les fragments <code>null</code> sont ignorés).
	 * @return La chaine décompressée.
	 */
	public static final String unzip(byte[]... zipFragments) {
		Inflater inflater = new Inflater();
		byte[] bytes = new byte[0];
		byte[] bytesHolder;
		byte[] pieceBytes = new byte[0x8000];
		int pieceLength;

		try {
			for (byte[] zipFragment : zipFragments) {
				if ((zipFragment != null) && (zipFragment.length > 0)) {
					inflater.setInput(zipFragment);
					while ((pieceLength = inflater.inflate(pieceBytes)) > 0) {
						bytesHolder = bytes;
						bytes = new byte[bytesHolder.length + pieceLength];
						System.arraycopy(bytesHolder, 0, bytes, 0, bytesHolder.length);
						System.arraycopy(pieceBytes, 0, bytes, bytesHolder.length, pieceLength);
					}
				}
			}
			if (!inflater.finished()) {
				throw new RuntimeException("Unexpected end of data.");
			}
			inflater.end();
			return new String(bytes, ZIP_CHARSET);
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException(exception);
		} catch (DataFormatException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Compresse une chaine.<br>
	 * La chaine est d'abord encodée en une séquence d'octets en utilisant l'encodage UTF-8.
	 * @param stringFragments Fragments de la chaine à compresser (les fragments <code>null</code> sont ignorés).
	 * @return Un tableau d'octets contenant la chaine compressée.
	 */
	public static final byte[] zip(String... stringFragments) {
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, false);
		byte[] zipped = new byte[0];
		byte[] zippedHolder;
		byte[] pieceBytes = new byte[0x8000];
		int pieceLength;

		try {
			for (String stringFragment : stringFragments) {
				if ((stringFragment != null) && !stringFragment.isEmpty()) {
					deflater.setInput(stringFragment.getBytes(ZIP_CHARSET));
					while ((pieceLength = deflater.deflate(pieceBytes)) > 0) {
						zippedHolder = zipped;
						zipped = new byte[zippedHolder.length + pieceLength];
						System.arraycopy(zippedHolder, 0, zipped, 0, zippedHolder.length);
						System.arraycopy(pieceBytes, 0, zipped, zippedHolder.length, pieceLength);
					}
				}
			}
			deflater.finish();
			pieceLength = deflater.deflate(pieceBytes);
			zippedHolder = zipped;
			zipped = new byte[zippedHolder.length + pieceLength];
			System.arraycopy(zippedHolder, 0, zipped, 0, zippedHolder.length);
			System.arraycopy(pieceBytes, 0, zipped, zippedHolder.length, pieceLength);
			deflater.end();
			return zipped;
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Encodage utilisé pour transformer les chaines en séquences d'octets et réciproquement dans les méthodes de compression.
	 */
	private static final String ZIP_CHARSET = "UTF-8";



}
