package toolbox;



/**
 * La classe {@link CircularStorage} implemente un stockage circulaire d'�l�ments.
 * @author Ludovic WALLE
 * @param <T> Type des �l�ments stock�s.
 */
public class CircularStorage<T> {



	/**
	 * @param size Nombre d'�l�ments � stocker.
	 */
	public CircularStorage(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Le nombre d'�lements doit �tre strictement positif.");
		}
		elements = new Object[size];
	}



	/**
	 * Stocke les �l�ments indiqu�s.
	 * @param elements Elements � stocker.
	 */
	public void add(@SuppressWarnings({"unchecked", "hiding"}) T... elements) {
		if ((elements != null) && (elements.length > 0)) {
			if (elements.length == 1) {
				this.elements[firstIndex] = elements[0];
				if (count < this.elements.length) {
					count++;
				} else {
					firstIndex = (firstIndex + 1) % this.elements.length;
				}
				nextIndex = (nextIndex + 1) % this.elements.length;
			} else if (elements.length >= this.elements.length) {
				System.arraycopy(elements, elements.length - this.elements.length, this.elements, 0, this.elements.length);
				count = this.elements.length;
				firstIndex = 0;
				nextIndex = 0;
			} else {
				if ((nextIndex + elements.length) <= this.elements.length) {
					System.arraycopy(elements, 0, this.elements, nextIndex, elements.length);
				} else {
					System.arraycopy(elements, 0, this.elements, nextIndex, this.elements.length - nextIndex);
					System.arraycopy(elements, elements.length - (this.elements.length - nextIndex), this.elements, 0, elements.length - (this.elements.length - nextIndex));
				}
				if ((count + elements.length) < this.elements.length) {
					count += elements.length;
				} else {
					count = this.elements.length;
					firstIndex = (firstIndex + count + elements.length) % this.elements.length;
				}
				nextIndex = (nextIndex + elements.length) % this.elements.length;
			}
		}
	}



	/**
	 * Teste si les �l�ments stock�s sont les m�mes que ceux indiqu�s (m�me nombre d'�l�ments, �gaux selon {@link Object#equals(Object)}, dans le m�me ordre).<br>
	 * Le nombre d'�l�ments est celui r�ellement stock�, et il peut donc �tre inf�rieur � la taille du stockage ciculaire.
	 * @param elements El�ments � comparer (ne doit pas �tre <code>null</code>).
	 * @return <code>true</code> si les �l�ments stock�s sont les m�mes, <code>false</code> sinon.
	 */
	public boolean equals(@SuppressWarnings({"unchecked", "hiding"}) T... elements) {
		if (elements.length != count) {
			return false;
		} else {
			for (int i = 0; i < count; i++) {
				if (elements[i] != this.elements[(i + firstIndex) % elements.length]) {
					return false;
				}
			}
			return true;
		}
	}



	/**
	 * Retourne un tableau contenant les objets stock�s, dans l'ordre
	 * @return Un tableau contenant les objets stock�s, dans l'ordre.
	 */
	@SuppressWarnings("unchecked") public T[] toArray() {
		T[] array;
		int iArray = 0;

		array = (T[]) new Object[count];
		for (int i = 0; i < count; i++) {
			array[iArray++] = (T) elements[(i + firstIndex) % elements.length];
		}
		return array;
	}



	/**
	 * Nombre d'�l�ments stock�s.
	 */
	private int count = 0;



	/**
	 * Tableau contenant les �l�ments stock�s.
	 */
	private final Object[] elements;



	/**
	 * Index du premier �l�ment stock�.
	 */
	private int firstIndex = 0;



	/**
	 * Index du prochain �l�ment � stocker.
	 */
	private int nextIndex = 0;



}
