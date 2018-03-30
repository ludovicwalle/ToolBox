package toolbox;



/**
 * La classe {@link CharCircularStorage} implemente un stockage circulaire de caractères.
 * @author Ludovic WALLE
 */
public class CharCircularStorage {



	/**
	 * @param size Nombre de caractères à stocker.
	 */
	public CharCircularStorage(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Le nombre de caractères doit être strictement positif.");
		}
		chars = new char[size];
	}



	/**
	 * Stocke les caractères indiqués.
	 * @param chars Caractères à stocker.
	 */
	public void add(@SuppressWarnings("hiding") char... chars) {
		if ((chars != null) && (chars.length > 0)) {
			if (chars.length == 1) {
				this.chars[firstIndex] = chars[0];
				if (count < this.chars.length) {
					count++;
				} else {
					firstIndex = (firstIndex + 1) % this.chars.length;
				}
				nextIndex = (nextIndex + 1) % this.chars.length;
			} else if (chars.length >= this.chars.length) {
				System.arraycopy(chars, chars.length - this.chars.length, this.chars, 0, this.chars.length);
				count = this.chars.length;
				firstIndex = 0;
				nextIndex = 0;
			} else {
				if ((nextIndex + chars.length) <= this.chars.length) {
					System.arraycopy(chars, 0, this.chars, nextIndex, chars.length);
				} else {
					System.arraycopy(chars, 0, this.chars, nextIndex, this.chars.length - nextIndex);
					System.arraycopy(chars, chars.length - (this.chars.length - nextIndex), this.chars, 0, chars.length - (this.chars.length - nextIndex));
				}
				if ((count + chars.length) < this.chars.length) {
					count += chars.length;
				} else {
					count = this.chars.length;
					firstIndex = (firstIndex + count + chars.length) % this.chars.length;
				}
				nextIndex = (nextIndex + chars.length) % this.chars.length;
			}
		}
	}



	/**
	 * Stocke le caractère indiqué.
	 * @param aChar Caractère à stocker.
	 */
	public void add(char aChar) {
		this.chars[nextIndex] = aChar;
		if (count < this.chars.length) {
			count++;
		} else {
			firstIndex = (firstIndex + 1) % this.chars.length;
		}
		nextIndex = (nextIndex + 1) % this.chars.length;
	}



	/**
	 * Teste si les caractèrse stockés sont les mêmes que ceux indiqués (même nombre de caractères, identiques, dans le même ordre).<br>
	 * Le nombre de caractères est celui réellement stocké, et il peut donc être inférieur à la taille du stockage ciculaire.
	 * @param chars Caractères à comparer (ne doit pas être <code>null</code>).
	 * @return <code>true</code> si les caractères stockés sont les mêmes, <code>false</code> sinon.
	 */
	public boolean equals(@SuppressWarnings("hiding") char... chars) {
		if (chars.length != count) {
			return false;
		} else {
			for (int i = 0; i < count; i++) {
				if (chars[i] != this.chars[(i + firstIndex) % chars.length]) {
					return false;
				}
			}
			return true;
		}
	}



	/**
	 * Retourne une chaine contenant les caractères stockés, dans l'ordre.
	 */
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < count; i++) {
			builder.append(chars[(i + firstIndex) % chars.length]);
		}
		return builder.toString();
	}



	/**
	 * Tableau contenant les caractères stockés.
	 */
	private final char[] chars;



	/**
	 * Nombre de caractères stockés.
	 */
	private int count = 0;



	/**
	 * Index du premier caractère stocké.
	 */
	private int firstIndex = 0;



	/**
	 * Index du prochain caractère à stocker.
	 */
	private int nextIndex = 0;



}
