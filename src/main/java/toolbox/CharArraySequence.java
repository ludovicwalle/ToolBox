package toolbox;



/**
 * La classe {@link CharArraySequence} permet de parcourir un tableau de caractère en tant que {@link CharSequence}.
 * @author Ludovic WALLE
 */
public class CharArraySequence implements CharSequence {



	/**
	 * @param chars Tableau de caractères.
	 */
	public CharArraySequence(char[] chars) {
		this.chars = chars;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public char charAt(int index) {
		return chars[index];
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int length() {
		return chars.length;
	}



	/**
	 * Spécifie le tableau de caractères à utiliser.
	 * @param chars Caractères à utiliser.
	 * @return Cet objet.
	 */
	public CharArraySequence setChars(char[] chars) {
		this.chars = chars;
		return this;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public CharSequence subSequence(int start, int end) {
		return new String(chars, start, end - start);
	}



	/**
	 * Tableau de caractères.
	 */
	private char[] chars;



}
