package toolbox;



/**
 * La classe {@link CharArraySequence} permet de parcourir un tableau de caract�re en tant que {@link CharSequence}.
 * @author Ludovic WALLE
 */
public class CharArraySequence implements CharSequence {



	/**
	 * @param chars Tableau de caract�res.
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
	 * Sp�cifie le tableau de caract�res � utiliser.
	 * @param chars Caract�res � utiliser.
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
	 * Tableau de caract�res.
	 */
	private char[] chars;



}
