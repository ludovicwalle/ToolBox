package toolbox.servlet;



/**
 * La classe {@link MimeType} recense les types mime de contenu des r�ponses dont on a eu besoin. Ces types sont utilis�es dans l'ent�te HTTP <code>ContentType</code>.<br>
 * Excel: {@link "http://filext.com/faq/office_mime_types.php"}
 * @author Ludovic WALLE
 */
public enum MimeType {
	/** Donn�es binaires */
	BYTES("application/octet-stream"),
	/** Document CSV. */
	CSV("text/csv"),
	/** Document Excel (.xls, .xlt, .xla). */
	EXCEL("application/vnd.ms-excel"),
	/** Document Excel (.xlsx). */
	EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	/** Page HTML. */
	HTML("text/html"),
	/** Document XML. */
	JAVASCRIPT("text/javascript"),
	/** Document Json. */
	JSON("application/json"),
	/** Page XHTML. */
	MULTIPART("application/octet-stream"),
	/** Document texte. */
	PLAIN("text/plain"),
	/** Page XHTML. */
	XHTML("application/xhtml+xml"),
	/** Document XML. */
	XML("text/xml");



	/**
	 * @param string Chaine � utiliser pour le type de contenu des r�ponses.
	 */
	private MimeType(String string) {
		this.string = string;
	}



	/**
	 * Retourne la chaine � utiliser pour le type de contenu des r�ponses.
	 * @return La chaine � utiliser pour le type de contenu des r�ponses.
	 */
	@Override public String toString() {
		return string;
	}



	/**
	 * Chaine � utiliser pour le type de contenu des r�ponses.
	 */
	private final String string;



}
