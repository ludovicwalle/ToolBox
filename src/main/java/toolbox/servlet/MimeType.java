package toolbox.servlet;



/**
 * La classe {@link MimeType} recense les types mime de contenu des réponses dont on a eu besoin. Ces types sont utilisées dans l'entête HTTP <code>ContentType</code>.<br>
 * Excel: {@link "http://filext.com/faq/office_mime_types.php"}
 * @author Ludovic WALLE
 */
public enum MimeType {
	/** Données binaires */
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
	 * @param string Chaine à utiliser pour le type de contenu des réponses.
	 */
	private MimeType(String string) {
		this.string = string;
	}



	/**
	 * Retourne la chaine à utiliser pour le type de contenu des réponses.
	 * @return La chaine à utiliser pour le type de contenu des réponses.
	 */
	@Override public String toString() {
		return string;
	}



	/**
	 * Chaine à utiliser pour le type de contenu des réponses.
	 */
	private final String string;



}
