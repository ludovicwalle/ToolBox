package toolbox.servlet;

import javax.servlet.http.*;

import toolbox.*;



/**
 * La classe {@link CustomizedResponse} �tend la classe {@link HttpServletRequest}, ajoutant des fonctionnalit�s pour d�finir les caract�ristiques du r�sultat.
 * @author Ludovic WALLE
 */
public class CustomizedResponse extends HttpServletResponseWrapper {



	/**
	 * @param response Donn�es de sortie de la servlet.
	 */
	public CustomizedResponse(HttpServletResponse response) {
		super(response);
	}



	/**
	 * Sp�cifie le type mime de la r�ponse.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : null, null);
	}



	/**
	 * Sp�cifie le type mime de la r�ponse.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 * @param charset Encodage de la r�ponse (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType, Charset charset) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : null, charset);
	}



	/**
	 * G�n�re la partie d'ent�te indiquant la forme d'une r�ponse de type fichier.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 * @param charset Encodage de la r�ponse (peut �tre <code>null</code>).
	 * @param contentDisposition Forme de la r�ponse de type fichier (ne doit pas �tre <code>null</code>).
	 * @param filename le nom du fichier (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(MimeType mimeType, Charset charset, ContentDisposition contentDisposition, String filename) {
		setContentHeaders((mimeType != null) ? mimeType.toString() : "application/octet-stream", charset);
		super.setHeader("Content-Disposition", contentDisposition + ((filename != null) ? "; filename=" + filename : ""));
	}



	/**
	 * Sp�cifie le type mime de la r�ponse.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(String mimeType) {
		setContentHeaders(mimeType, null);
	}



	/**
	 * Sp�cifie le type mime de la r�ponse.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 * @param charset Encodage de la r�ponse (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(String mimeType, Charset charset) {
		if ((mimeType != null) || (charset != null)) {
			super.setContentType(FormatTools.formatList("", ";", "", (mimeType != null) ? mimeType : null, (charset != null) ? "charset=" + charset : null));
		}
	}



	/**
	 * G�n�re la partie d'ent�te indiquant la forme d'une r�ponse de type fichier.
	 * @param mimeType Type mime de la r�ponse (peut �tre <code>null</code>).
	 * @param charset Encodage de la r�ponse (peut �tre <code>null</code>).
	 * @param contentDisposition Forme de la r�ponse de type fichier (ne doit pas �tre <code>null</code>).
	 * @param filename le nom du fichier (peut �tre <code>null</code>).
	 */
	public void setContentHeaders(String mimeType, Charset charset, ContentDisposition contentDisposition, String filename) {
		setContentHeaders((mimeType != null) ? mimeType : "application/octet-stream", charset);
		super.setHeader("Content-Disposition", contentDisposition + ((filename != null) ? "; filename=" + filename : ""));
	}



	/**
	 * Sp�cifie l'encodage de la r�ponse.
	 * @param encoding Type mime de la r�ponse.
	 */
	public void setEncoding(Charset encoding) {
		super.setCharacterEncoding(encoding.toString());
	}



}
