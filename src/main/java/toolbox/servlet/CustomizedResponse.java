package toolbox.servlet;

import javax.servlet.http.*;

import toolbox.*;



/**
 * La classe {@link CustomizedResponse} étend la classe {@link HttpServletRequest}, ajoutant des fonctionnalités pour définir les caractéristiques du résultat.
 * @author Ludovic WALLE
 */
public class CustomizedResponse extends HttpServletResponseWrapper {



	/**
	 * @param response Données de sortie de la servlet.
	 */
	public CustomizedResponse(HttpServletResponse response) {
		super(response);
	}



	/**
	 * Spécifie le type mime de la réponse.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : null, null);
	}



	/**
	 * Spécifie le type mime de la réponse.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 * @param charset Encodage de la réponse (peut être <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType, Charset charset) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : null, charset);
	}



	/**
	 * Génère la partie d'entête indiquant la forme d'une réponse de type fichier.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 * @param charset Encodage de la réponse (peut être <code>null</code>).
	 * @param contentDisposition Forme de la réponse de type fichier (ne doit pas être <code>null</code>).
	 * @param filename le nom du fichier (peut être <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType, Charset charset, ContentDisposition contentDisposition, String filename) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : "application/octet-stream", charset);
		super.setHeader("Content-Disposition", contentDisposition + ((filename != null) ? "; filename=" + filename : ""));
	}



	/**
	 * Spécifie le type mime de la réponse.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 */
	public void setContentHeaders(String mimeType) {
		setContentHeaders(mimeType, null);
	}



	/**
	 * Spécifie le type mime de la réponse.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 * @param charset Encodage de la réponse (peut être <code>null</code>).
	 */
	public void setContentHeaders(String mimeType, Charset charset) {
		if ((mimeType != null) || (charset != null)) {
			super.setContentType(FormatTools.formatList("", ";", "", (mimeType != null) ? mimeType : null, (charset != null) ? "charset=" + charset : null));
		}
	}



	/**
	 * Génère la partie d'entête indiquant la forme d'une réponse de type fichier.
	 * @param mimeType Type mime de la réponse (peut être <code>null</code>).
	 * @param charset Encodage de la réponse (peut être <code>null</code>).
	 * @param contentDisposition Forme de la réponse de type fichier (ne doit pas être <code>null</code>).
	 * @param filename le nom du fichier (peut être <code>null</code>).
	 */
	public void setContentHeaders(String mimeType, Charset charset, ContentDisposition contentDisposition, String filename) {
		setContentHeaders((mimeType != null) ? mimeType : "application/octet-stream", charset);
		super.setHeader("Content-Disposition", contentDisposition + ((filename != null) ? "; filename=" + filename : ""));
	}



	/**
	 * Spécifie l'encodage de la réponse.
	 * @param encoding Type mime de la réponse.
	 */
	public void setEncoding(Charset encoding) {
		super.setCharacterEncoding(encoding.toString());
	}



}
