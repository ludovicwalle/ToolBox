package toolbox;

import java.lang.reflect.*;
import java.util.*;



/**
 * La classe {@link #ArraysTools} contient des méthodes statiques d'usage général relatives aux tableaux.
 * @author walle
 */
public abstract class ArraysTools {



	/**
	 * Constructeur privé pour interdire l'instanciation.
	 */
	private ArraysTools() {}



	/**
	 * Ajoute les entiers indiqués à la fin du tableau.<br>
	 * Le tableau retourné est celui indiqué si il n'y avait rien à ajouter, un nouveau tableau sinon. Les tableaux ne sont jamais modifiés.
	 * @param array Premier tableau (ne doit pas être <code>null</code>).
	 * @param ints Entiers à ajouter, ou un tableau d'entiers (ne doit pas être <code>null</code>).
	 * @return Un tableau contenant tous les entiers.
	 */
	public static final int[] append(int[] array, int... ints) {
		int[] arrayAndInts;

		if (ints.length > 0) {
			arrayAndInts = new int[array.length + ints.length];
			System.arraycopy(array, 0, arrayAndInts, 0, array.length);
			System.arraycopy(ints, 0, arrayAndInts, array.length, ints.length);
			return arrayAndInts;
		} else if (array == null) {
			throw new NullPointerException();
		} else {
			return array;
		}
	}



	/**
	 * Ajoute les objets indiqués à la fin du tableau.<br>
	 * Le tableau retourné est celui indiqué si il n'y avait rien à ajouter, un nouveau tableau sinon. Les tableaux passés en paramètre ne sont jamais modifiés. Le type des éléments à ajouter doit
	 * être le même que celui des éléments du tableau.
	 * @param <T> Classe des éléments du tableau.
	 * @param array Tableau (ne doit pas être <code>null</code>).
	 * @param objects Objets à ajouter ou un tableau d'objets (ne doit pas être <code>null</code>).
	 * @return Un tableau contenant tous les objets.
	 */
	public static final <T> T[] append(T[] array, @SuppressWarnings("unchecked") T... objects) {
		T[] arrayAndObjects;

		if (array.getClass().getComponentType() != objects.getClass().getComponentType()) {
			throw new ArrayStoreException("La classe des éléments à ajouter au tableau (" + objects.getClass().getComponentType().getCanonicalName() + ") est différente de celle des éléments du tableau (" + array.getClass().getComponentType().getCanonicalName() + ").");
		} else if (objects.length > 0) {
			arrayAndObjects = newArray(array, array.length + objects.length);
			System.arraycopy(array, 0, arrayAndObjects, 0, array.length);
			System.arraycopy(objects, 0, arrayAndObjects, array.length, objects.length);
			return arrayAndObjects;
		} else {
			return array;
		}
	}



	/**
	 * Convertit le tableau indiqué en tableau d'éléments de la classe indiquée.
	 * @param <T> Classe des éléments du tableau.
	 * @param array Tableau à convertir (peut être <code>null</code>).
	 * @param arrayElementClass Classe des éléments du tableau converti (peut être <code>null</code> si le tableau est <code>null</code>, ne doit pas être <code>null</code> sinon).
	 * @return Un nouveau tableau converti.
	 */
	@SuppressWarnings("unchecked") public static final <T> T[] castArray(T[] array, Class<?> arrayElementClass) {
		T[] castedArray;

		if (array == null) {
			return null;
		} else if (array.getClass().getComponentType().equals(arrayElementClass)) {
			return array;
		} else {
			castedArray = (T[]) Array.newInstance(arrayElementClass, array.length);
			System.arraycopy(array, 0, castedArray, 0, array.length);
			return castedArray;
		}
	}



	/**
	 * Compare les tableaux indiqués.<br>
	 * Si les tableaux font la même taille et que les éléments qu'ils contiennent aux mêmes indices sont égaux, les tableaux sont considérés comme égaux.<br>
	 * Si tous les éléments aux indices existant dans les deux tableaux sont égaux, le tableau le plus court est considéré comme inféfieur au tableau le plus long. Si des éléments aux indices existant
	 * dans les deux tableaux ne sont pas égaux, on retourne le résultat de la comparaison du premier d'entre eux (plus petit indice).
	 * @param <T> Type des éléments du tableau.
	 * @param one Premier tableau.
	 * @param two Deuxième tableau.
	 * @return Un nombre négatif si le premier tableau est inférieur au deuxième, un nombre positif si le deuxième tableau est supérieur au premier, 0 si les tableaux sont égaux.
	 */
	public static final <T extends Comparable<T>> int compare(T[] one, T[] two) {
		int compare;

		for (int i = 0; i < OtherTools.min(one.length, two.length); i++) {
			if ((compare = one[i].compareTo(two[i])) != 0) {
				return compare;
			}
		}
		return two.length - one.length;
	}



	/**
	 * Retourne un nouveau tableau contenant la concaténation des tableaux indiqués.<br>
	 * Les tableaux en entrée ne sont jamais modifiés.<br>
	 * Les tableaux <code>null</code> sont ignorés.
	 * @param arrays Tableaux à concaténer (peuvent être <code>null</code>).
	 * @return Un nouveau tableau contenant la concaténation des tableaux.
	 */
	public static final byte[] concat(byte[]... arrays) {
		int length = 0;
		int offset = 0;
		byte[] concatened;

		for (byte[] array : arrays) {
			if (array != null) {
				length += array.length;
			}
		}
		concatened = new byte[length];
		for (byte[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, concatened, offset, array.length);
				offset += array.length;
			}
		}
		return concatened;
	}



	/**
	 * Retourne un nouveau tableau contenant la concaténation des tableaux indiqués.<br>
	 * Les tableaux en entrée ne sont jamais modifiés.<br>
	 * Les tableaux <code>null</code> sont ignorés.
	 * @param <T> Classe des éléments du tableau de tableaux.
	 * @param arrays Tableaux à concaténer.
	 * @return Un nouveau tableau contenant la concaténation des tableaux.
	 */
	public static final <T> T[] concat(@SuppressWarnings("unchecked") T[]... arrays) {
		int length = 0;
		int offset = 0;
		T[] concatened;
		Class<?> arrayElementClass = null;

		for (T[] array : arrays) {
			if (array != null) {
				length += array.length;
				if (arrayElementClass == null) {
					arrayElementClass = array.getClass().getComponentType();
				} else if (array.getClass().getComponentType() != arrayElementClass) {
					throw new ArrayStoreException("La classe des éléments à des tableaux sont différentes (" + arrayElementClass.getCanonicalName() + " / " + array.getClass().getComponentType().getCanonicalName() + ").");
				}
			}
		}
		concatened = newArray(arrays[0], length);
		for (T[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, concatened, offset, array.length);
				offset += array.length;
			}
		}
		return concatened;
	}



	/**
	 * Teste si l'objet indiqué est dans le tableau indiqué.<br>
	 * La comparaison se fait par {@link #equals(Object)}, sauf si l'objet recherché est <code>null</code>.
	 * @param <T> Classe des éléments du tableau.
	 * @param array Tableau (ne doit pas être <code>null</code>, peut contenir des <code>null</code> s).
	 * @param searchedObject Objet recherché (peut être <code>null</code>).
	 * @return <code>true</code> si l'object recherché est dans le tableau, <code>false</code> sinon.
	 */
	public static final <T> boolean contains(T[] array, T searchedObject) {
		if (searchedObject == null) {
			for (T object : array) {
				if (object == null) {
					return true;
				}
			}
		} else {
			for (T object : array) {
				if (searchedObject.equals(object)) {
					return true;
				}
			}
		}
		return false;
	}



	/**
	 * Crée un nouveau tableau du même type que celui du tableau indiqué, et de la taille indiquée.
	 * @param <T> Classe des éléments du tableau.
	 * @param array Tableau dont le type sert de référence (ne doit pas être <code>null</code>).
	 * @param length Taille du tableau à créer (doit être positif ou nul).
	 * @return Le nouveau tableau.
	 */
	@SuppressWarnings("unchecked") public static final <T> T[] newArray(T[] array, int length) {
		return (T[]) Array.newInstance(array.getClass().getComponentType(), length);
	}



	/**
	 * Crée un tableau de la taille indiqué et dont toutes les cases contiennent la valeur indiquée.
	 * @param value Valeur à mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit être positif ou nul).
	 * @return Le tableau initialisé.
	 */
	public static final boolean[] newInitializedArray(boolean value, int size) {
		boolean[] array;

		array = new boolean[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Crée un tableau de la taille indiqué et dont toutes les cases contiennent la valeur indiquée.
	 * @param value Valeur à mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit être positif ou nul).
	 * @return Le tableau initialisé.
	 */
	public static final char[] newInitializedArray(char value, int size) {
		char[] array;

		array = new char[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Crée un tableau de la taille indiqué et dont toutes les cases contiennent la valeur indiquée.
	 * @param value Valeur à mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit être positif ou nul).
	 * @return Le tableau initialisé.
	 */
	public static final int[] newInitializedArray(int value, int size) {
		int[] array;

		array = new int[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Crée un tableau de la taille indiqué et dont toutes les cases contiennent la valeur indiquée.
	 * @param <T> Classe des éléments du tableau.
	 * @param value Valeur à mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit être positif ou nul).
	 * @return Le tableau initialisé.
	 */
	@SuppressWarnings("unchecked") public static final <T> T[] newInitializedArray(T value, int size) {
		T[] array;

		array = (T[]) Array.newInstance(value.getClass(), size);
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Trie le tableau indiqué.
	 * @param <T> Type des éléments du tableau.
	 * @param array Tableau (peut être <code>null</code>).
	 * @return Le tableau, trié, ou <code>null</code> si le tableau indiqué était <code>null</code>.
	 */
	public static final <T> T[] sort(T[] array) {
		if (array != null) {
			Arrays.sort(array);
		}
		return array;
	}



	/**
	 * Trie le tableau en éliminant les doublons.
	 * @param array Tableau (peut être <code>null</code>).
	 * @return Un tableau trié et sans doublons, ou <code>null</code> si le tableau indiqué était <code>null</code>.
	 */
	public static final int[] sortAndRemoveDoubles(int[] array) {
		return sortAndRemoveDoubles(array, array.length);
	}



	/**
	 * Trie les premiers éléments du tableau en éliminant les doublons.
	 * @param array Tableau (peut être <code>null</code>).
	 * @param count Nombre d'éléments, doit être positif ou nul et inférieur ou égal au nombre d'éléments du tableau.
	 * @return Un tableau trié et sans doublons, ou <code>null</code> si le tableau indiqué était <code>null</code>.
	 */
	public static final int[] sortAndRemoveDoubles(int[] array, int count) {
		int uniqueCount;

		if ((array != null) && (count > 1)) {
			array = Arrays.copyOf(array, count);
			Arrays.sort(array);
			uniqueCount = 1;
			for (int i = 1; i < count; i++) {
				if (array[i] != array[i - 1]) {
					array[uniqueCount++] = array[i];
				}
			}
			if (array.length > uniqueCount) {
				array = Arrays.copyOf(array, uniqueCount);
			}
		}
		return array;
	}



	/**
	 * Trie le tableau en éliminant les doublons.
	 * @param <T> Type des élément du tableau.
	 * @param array Tableau (peut être <code>null</code>).
	 * @return Un tableau trié et sans doublons, ou <code>null</code> si le tableau indiqué était <code>null</code>.
	 */
	public static final <T> T[] sortAndRemoveDoubles(T[] array) {
		return (array == null) ? null : sortAndRemoveDoubles(array, array.length);
	}



	/**
	 * Trie les premiers éléments du tableau en éliminant les doublons.
	 * @param <T> Type des élément du tableau.
	 * @param array Tableau (peut être <code>null</code>).
	 * @param count Nombre d'éléments, doit être positif ou nul et inférieur ou égal au nombre d'éléments du tableau.
	 * @return Un tableau trié et sans doublons, ou <code>null</code> si le tableau indiqué était <code>null</code>.
	 */
	public static final <T> T[] sortAndRemoveDoubles(T[] array, int count) {
		int uniqueCount;

		if ((array != null) && (count > 1)) {
			array = Arrays.copyOf(array, count);
			Arrays.sort(array);
			uniqueCount = 1;
			for (int i = 1; i < count; i++) {
				if (!array[i].equals(array[i - 1])) {
					array[uniqueCount++] = array[i];
				}
			}
			if (array.length > uniqueCount) {
				array = Arrays.copyOf(array, uniqueCount);
			}
		}
		return array;
	}



}
