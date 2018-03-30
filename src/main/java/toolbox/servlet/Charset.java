package toolbox.servlet;



/**
 * La classe {@link Charset} recense les encodages dont on a eu besoin.
 * @author Ludovic WALLE
 */
public enum Charset {
    /**  */
	ISO_8859_1("ISO-8859-1"),
	/**  */
	US_ASCII("US-ASCII"),
	/**  */
	UTF_8("UTF-8");



	/**
	 * @param name Nom de l'encodage.
	 */
	private Charset(String name) {
		this.name = name;
		this.charset = java.nio.charset.Charset.forName(name);
	}



	/**
	 * Retourne l'encodage.
	 * @return L'encodage.
	 */
	public java.nio.charset.Charset getCharset() {
		return charset;
	}



	/**
	 * Retourne le nom de l'encodage.
	 * @return Le nom de l'encodage.
	 */
	@Override public String toString() {
		return name;
	}



	/**
	 * Encodage.
	 */
	private final java.nio.charset.Charset charset;



	/**
	 * Nom de l'encodage.
	 */
	private final String name;



}
