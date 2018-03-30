package toolbox;



/**
 * La classe {@link CharCircularStorage} implemente un stockage circulaire de caract�res.
 * @author Ludovic WALLE
 */
public class CharCircularStorage {



	/**
	 * @param size Nombre de caract�res � stocker.
	 */
	public CharCircularStorage(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Le nombre de caract�res doit �tre strictement positif.");
		}
		chars = new char[size];
	}



	/**
	 * Stocke les caract�res indiqu�s.
	 * @param chars Caract�res � stocker.
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
	 * Stocke le caract�re indiqu�.
	 * @param aChar Caract�re � stocker.
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
	 * Teste si les caract�rse stock�s sont les m�mes que ceux indiqu�s (m�me nombre de caract�res, identiques, dans le m�me ordre).<br>
	 * Le nombre de caract�res est celui r�ellement stock�, et il peut donc �tre inf�rieur � la taille du stockage ciculaire.
	 * @param chars Caract�res � comparer (ne doit pas �tre <code>null</code>).
	 * @return <code>true</code> si les caract�res stock�s sont les m�mes, <code>false</code> sinon.
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
	 * Retourne une chaine contenant les caract�res stock�s, dans l'ordre.
	 */
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < count; i++) {
			builder.append(chars[(i + firstIndex) % chars.length]);
		}
		return builder.toString();
	}



	/**
	 * Tableau contenant les caract�res stock�s.
	 */
	private final char[] chars;



	/**
	 * Nombre de caract�res stock�s.
	 */
	private int count = 0;



	/**
	 * Index du premier caract�re stock�.
	 */
	private int firstIndex = 0;



	/**
	 * Index du prochain caract�re � stocker.
	 */
	private int nextIndex = 0;



}
