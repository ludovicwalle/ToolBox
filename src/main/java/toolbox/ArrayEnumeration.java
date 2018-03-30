package toolbox;

import java.util.*;



/**
 * La classe {@link ArrayEnumeration} impl�mente une �num�ration g�n�rique de tableau.
 * @author Ludovic WALLE
 * @param <T> Type des �l�ments du tableau.
 */
public class ArrayEnumeration<T> implements Enumeration<T> {



	/**
	 * @param array Tableau.
	 */
	public ArrayEnumeration(T[] array) {
		this.array = array;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean hasMoreElements() {
		return index < array.length;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public T nextElement() {
		return array[index++];
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
