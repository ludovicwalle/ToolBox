package toolbox;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.zip.*;



/**
 * La classe {@link HttpTools} .
 * @author Ludovic WALLE
 */
public class HttpTools {



	/**
	 * Retourne le flux d'entrée de la connection indiquée, au travers d'un décompresseur si nécessaire.
	 * @param connection Connection contenant le flux d'entrée.
	 * @return Le flux d'entrée.
	 * @throws IOException
	 */
	public static InputStream getInputStream(HttpURLConnection connection) throws IOException {
		String contentEncoding;
		ZipInputStream zipInputStream;

		contentEncoding = connection.getContentEncoding();
		if ("compress".equals(contentEncoding)) {
			zipInputStream = new ZipInputStream(connection.getInputStream());
			zipInputStream.getNextEntry();
			return zipInputStream;
		} else if ("gzip".equals(contentEncoding)) {
			return new GZIPInputStream(connection.getInputStream());
		} else if ("deflate".equals(contentEncoding)) {
			return new InflaterInputStream(connection.getInputStream());
		} else {
			return connection.getInputStream();
		}
	}



	/**
	 * Retourne les octets lus depuis le flux d'entrée de la connexion indiquée.
	 * @param connection Connection.
	 * @return Les octets lus depuis le flux d'entrée de la connexion indiquée.
	 * @throws IOException
	 */
	public static byte[] readBytes(HttpURLConnection connection) throws IOException {
		byte[] allBytes = new byte[0];
		byte[] readBytes = new byte[1024 * 1024];
		byte[] bytes;
		int readCount;

		try (InputStream in = getInputStream(connection)) {
			while ((readCount = in.read(readBytes)) > 0) {
				bytes = allBytes;
				allBytes = new byte[bytes.length + readCount];
				System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
				System.arraycopy(readBytes, 0, allBytes, bytes.length, readCount);
			}
		}
		return allBytes;
	}



	/**
	 * Retourne une chaine contenant les données textuelles lues depuis le flux d'entrée de la connexion indiquée, ou <code>null</code> si le flux n'est pas textuel.
	 * @param connection Connection.
	 * @return Une chaine contenant les données textuelles lues depuis le flux d'entrée de la connexion indiquée, ou <code>null</code> si le flux n'est pas textuel.
	 * @throws IOException
	 */
	public static String readString(HttpURLConnection connection) throws IOException {
		byte[] allBytes = new byte[0];
		byte[] readBytes = new byte[1024 * 1024];
		byte[] bytes;
		int readCount;
		String encoding = "UTF-8";
		Matcher matcher;

		if ((matcher = TEXTUAL_CONTENT_TYPE_PATTERN.matcher(connection.getContentType())).matches()) {
			if (matcher.group(1) != null) {
				encoding = matcher.group(1);
			}
			try (InputStream in = getInputStream(connection)) {
				while ((readCount = in.read(readBytes)) > 0) {
					bytes = allBytes;
					allBytes = new byte[bytes.length + readCount];
					System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
					System.arraycopy(readBytes, 0, allBytes, bytes.length, readCount);
				}
			}
			return new String(allBytes, encoding);
		} else {
			return null;
		}
	}



	/**
	 * Modèle pour les types de contenus textuels reconnus.
	 */
	private static final Pattern TEXTUAL_CONTENT_TYPE_PATTERN = Pattern.compile("(?:text/xml|application/xml|text/html)(?: *; *charset=(utf-8|UTF-8|ISO-8859-1|ISO-8859-1))?");



}
