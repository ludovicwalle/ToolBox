package toolbox;

import java.util.*;



/**
 * La classe {@link ArrayIterator} implémente un itérateur générique de tableau.
 * @author Ludovic WALLE
 * @param <T> Type des éléments du tableau.
 */
public class ArrayIterator<T> implements Iterator<T>, Iterable<T> {



	/**
	 * @param array Tableau.
	 */
	public ArrayIterator(T[] array) {
		this.array = array;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean hasNext() {
		return index < array.length;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Iterator<T> iterator() {
		return this;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public T next() {
		try {
			return array[index++];
		} catch (ArrayIndexOutOfBoundsException exception) {
			throw new NoSuchElementException();
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void remove() {
		throw new UnsupportedOperationException();
	}



	/**
	 * Tableau.
	 */
	private final T[] array;



	/**
	 * Index pour le parcours du tableau.
	 */
	private int index = 0;



}
