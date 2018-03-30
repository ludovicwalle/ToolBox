package toolbox.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;



/**
 * La classe {@link FilePart} implémente une partie de requète multi parties.
 * @author Ludovic WALLE
 */
public class FilePart implements Part {



	/**
	 * @param item Description de la partie.
	 */
	public FilePart(FileItem item) {
		this.item = item;
		this.name = item.getName();
	}



	/**
	 * @param other Autre {@link FilePart}.
	 * @param name Nom.
	 */
	public FilePart(FilePart other, String name) {
		this.item = other.item;
		this.name = name;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void delete() throws IOException {
		item.delete();
	}



	/**
	 * Retourne les octets du fichier.
	 * @return Les octets du fichier.
	 */
	public byte[] getBytes() {
		byte[] bytes = new byte[(int) item.getSize()];
		int byteCount = 0;
		int byteRead;

		try {
			while ((byteCount < bytes.length) && ((byteRead = item.getInputStream().read(bytes, byteCount, bytes.length - byteCount)) != -1)) {
				byteCount += byteRead;
			}
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		return bytes;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getContentType() {
		return item.getContentType();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getHeader(@SuppressWarnings("hiding") String name) {
		return item.getHeaders().getHeader(name);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Collection<String> getHeaderNames() {
		Collection<String> headers = new HashSet<>();

		for (Iterator<String> iterator = item.getHeaders().getHeaderNames(); iterator.hasNext();) {
			headers.add(iterator.next());
		}
		return headers;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Collection<String> getHeaders(@SuppressWarnings("hiding") String name) {
		Collection<String> headers = new HashSet<>();

		for (Iterator<String> iterator = item.getHeaders().getHeaders(name); iterator.hasNext();) {
			headers.add(iterator.next());
		}
		return headers;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public InputStream getInputStream() throws IOException {
		return item.getInputStream();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getName() {
		return name;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public long getSize() {
		return item.getSize();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getSubmittedFileName() {
		return item.getName();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void write(String fileName) throws IOException {
		try {
			item.write(new File(fileName));
		} catch (Exception exception) {
			throw new IOException(exception);
		}
	}



	/**
	 * Description de la partie.
	 */
	private final FileItem item;



	/**
	 * Nom corrigé;
	 */
	private final String name;


}
