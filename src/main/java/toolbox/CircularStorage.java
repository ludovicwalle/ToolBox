package toolbox;



/**
 * La classe {@link CircularStorage} implemente un stockage circulaire d'éléments.
 * @author Ludovic WALLE
 * @param <T> Type des éléments stockés.
 */
public class CircularStorage<T> {



	/**
	 * @param size Nombre d'éléments à stocker.
	 */
	public CircularStorage(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Le nombre d'élements doit être strictement positif.");
		}
		elements = new Object[size];
	}



	/**
	 * Stocke les éléments indiqués.
	 * @param elements Elements à stocker.
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
	 * Teste si les éléments stockés sont les mêmes que ceux indiqués (même nombre d'éléments, égaux selon {@link Object#equals(Object)}, dans le même ordre).<br>
	 * Le nombre d'éléments est celui réellement stocké, et il peut donc être inférieur à la taille du stockage ciculaire.
	 * @param elements Eléments à comparer (ne doit pas être <code>null</code>).
	 * @return <code>true</code> si les éléments stockés sont les mêmes, <code>false</code> sinon.
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
	 * Retourne un tableau contenant les objets stockés, dans l'ordre
	 * @return Un tableau contenant les objets stockés, dans l'ordre.
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
	 * Nombre d'éléments stockés.
	 */
	private int count = 0;



	/**
	 * Tableau contenant les éléments stockés.
	 */
	private final Object[] elements;



	/**
	 * Index du premier élément stocké.
	 */
	private int firstIndex = 0;



	/**
	 * Index du prochain élément à stocker.
	 */
	private int nextIndex = 0;



}
