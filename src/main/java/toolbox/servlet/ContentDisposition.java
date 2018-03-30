package toolbox.servlet;



/**
 * La classe {@link MimeType} recense les formes de r�ponses de type de fichier dont on a eu besoin. Ces types sont utilis�es dans l'ent�te HTTP <code>ContentDisposition</code>.
 * @author Ludovic WALLE
 */
public enum ContentDisposition {
    /** Fichier attach�. */
	ATTACHMENT("attachment"),
	/** Fichier inclus. */
	INLINE("inline");



	/**
	 * @param string Chaine � utiliser pour la forme de r�ponse de type de fichier.
	 */
	private ContentDisposition(String string) {
		this.string = string;
	}



	/**
	 * Retourne la chaine � utiliser pour la forme de r�ponse de type de fichier.
	 * @return La chaine � utiliser pour la forme de r�ponse de type de fichier.
	 */
	@Override public String toString() {
		return string;
	}



	/**
	 * Chaine � utiliser pour la forme de r�ponse de type de fichier.
	 */
	private final String string;



}
