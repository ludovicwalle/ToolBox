package toolbox.servlet;



/**
 * La classe {@link MimeType} recense les formes de réponses de type de fichier dont on a eu besoin. Ces types sont utilisées dans l'entête HTTP <code>ContentDisposition</code>.
 * @author Ludovic WALLE
 */
public enum ContentDisposition {
    /** Fichier attaché. */
	ATTACHMENT("attachment"),
	/** Fichier inclus. */
	INLINE("inline");



	/**
	 * @param string Chaine à utiliser pour la forme de réponse de type de fichier.
	 */
	private ContentDisposition(String string) {
		this.string = string;
	}



	/**
	 * Retourne la chaine à utiliser pour la forme de réponse de type de fichier.
	 * @return La chaine à utiliser pour la forme de réponse de type de fichier.
	 */
	@Override public String toString() {
		return string;
	}



	/**
	 * Chaine à utiliser pour la forme de réponse de type de fichier.
	 */
	private final String string;



}
