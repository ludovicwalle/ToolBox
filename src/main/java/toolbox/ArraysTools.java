package toolbox;

import java.lang.reflect.*;
import java.util.*;



/**
 * La classe {@link #ArraysTools} contient des m�thodes statiques d'usage g�n�ral relatives aux tableaux.
 * @author walle
 */
public abstract class ArraysTools {



	/**
	 * Constructeur priv� pour interdire l'instanciation.
	 */
	private ArraysTools() {}



	/**
	 * Ajoute les entiers indiqu�s � la fin du tableau.<br>
	 * Le tableau retourn� est celui indiqu� si il n'y avait rien � ajouter, un nouveau tableau sinon. Les tableaux ne sont jamais modifi�s.
	 * @param array Premier tableau (ne doit pas �tre <code>null</code>).
	 * @param ints Entiers � ajouter, ou un tableau d'entiers (ne doit pas �tre <code>null</code>).
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
	 * Ajoute les objets indiqu�s � la fin du tableau.<br>
	 * Le tableau retourn� est celui indiqu� si il n'y avait rien � ajouter, un nouveau tableau sinon. Les tableaux pass�s en param�tre ne sont jamais modifi�s. Le type des �l�ments � ajouter doit
	 * �tre le m�me que celui des �l�ments du tableau.
	 * @param <T> Classe des �l�ments du tableau.
	 * @param array Tableau (ne doit pas �tre <code>null</code>).
	 * @param objects Objets � ajouter ou un tableau d'objets (ne doit pas �tre <code>null</code>).
	 * @return Un tableau contenant tous les objets.
	 */
	public static final <T> T[] append(T[] array, @SuppressWarnings("unchecked") T... objects) {
		T[] arrayAndObjects;

		if (array.getClass().getComponentType() != objects.getClass().getComponentType()) {
			throw new ArrayStoreException("La classe des �l�ments � ajouter au tableau (" + objects.getClass().getComponentType().getCanonicalName() + ") est diff�rente de celle des �l�ments du tableau (" + array.getClass().getComponentType().getCanonicalName() + ").");
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
	 * Convertit le tableau indiqu� en tableau d'�l�ments de la classe indiqu�e.
	 * @param <T> Classe des �l�ments du tableau.
	 * @param array Tableau � convertir (peut �tre <code>null</code>).
	 * @param arrayElementClass Classe des �l�ments du tableau converti (peut �tre <code>null</code> si le tableau est <code>null</code>, ne doit pas �tre <code>null</code> sinon).
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
	 * Compare les tableaux indiqu�s.<br>
	 * Si les tableaux font la m�me taille et que les �l�ments qu'ils contiennent aux m�mes indices sont �gaux, les tableaux sont consid�r�s comme �gaux.<br>
	 * Si tous les �l�ments aux indices existant dans les deux tableaux sont �gaux, le tableau le plus court est consid�r� comme inf�fieur au tableau le plus long. Si des �l�ments aux indices existant
	 * dans les deux tableaux ne sont pas �gaux, on retourne le r�sultat de la comparaison du premier d'entre eux (plus petit indice).
	 * @param <T> Type des �l�ments du tableau.
	 * @param one Premier tableau.
	 * @param two Deuxi�me tableau.
	 * @return Un nombre n�gatif si le premier tableau est inf�rieur au deuxi�me, un nombre positif si le deuxi�me tableau est sup�rieur au premier, 0 si les tableaux sont �gaux.
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
	 * Retourne un nouveau tableau contenant la concat�nation des tableaux indiqu�s.<br>
	 * Les tableaux en entr�e ne sont jamais modifi�s.<br>
	 * Les tableaux <code>null</code> sont ignor�s.
	 * @param arrays Tableaux � concat�ner (peuvent �tre <code>null</code>).
	 * @return Un nouveau tableau contenant la concat�nation des tableaux.
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
	 * Retourne un nouveau tableau contenant la concat�nation des tableaux indiqu�s.<br>
	 * Les tableaux en entr�e ne sont jamais modifi�s.<br>
	 * Les tableaux <code>null</code> sont ignor�s.
	 * @param <T> Classe des �l�ments du tableau de tableaux.
	 * @param arrays Tableaux � concat�ner.
	 * @return Un nouveau tableau contenant la concat�nation des tableaux.
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
					throw new ArrayStoreException("La classe des �l�ments � des tableaux sont diff�rentes (" + arrayElementClass.getCanonicalName() + " / " + array.getClass().getComponentType().getCanonicalName() + ").");
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
	 * Teste si l'objet indiqu� est dans le tableau indiqu�.<br>
	 * La comparaison se fait par {@link #equals(Object)}, sauf si l'objet recherch� est <code>null</code>.
	 * @param <T> Classe des �l�ments du tableau.
	 * @param array Tableau (ne doit pas �tre <code>null</code>, peut contenir des <code>null</code> s).
	 * @param searchedObject Objet recherch� (peut �tre <code>null</code>).
	 * @return <code>true</code> si l'object recherch� est dans le tableau, <code>false</code> sinon.
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
	 * Cr�e un nouveau tableau du m�me type que celui du tableau indiqu�, et de la taille indiqu�e.
	 * @param <T> Classe des �l�ments du tableau.
	 * @param array Tableau dont le type sert de r�f�rence (ne doit pas �tre <code>null</code>).
	 * @param length Taille du tableau � cr�er (doit �tre positif ou nul).
	 * @return Le nouveau tableau.
	 */
	@SuppressWarnings("unchecked") public static final <T> T[] newArray(T[] array, int length) {
		return (T[]) Array.newInstance(array.getClass().getComponentType(), length);
	}



	/**
	 * Cr�e un tableau de la taille indiqu� et dont toutes les cases contiennent la valeur indiqu�e.
	 * @param value Valeur � mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit �tre positif ou nul).
	 * @return Le tableau initialis�.
	 */
	public static final boolean[] newInitializedArray(boolean value, int size) {
		boolean[] array;

		array = new boolean[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Cr�e un tableau de la taille indiqu� et dont toutes les cases contiennent la valeur indiqu�e.
	 * @param value Valeur � mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit �tre positif ou nul).
	 * @return Le tableau initialis�.
	 */
	public static final char[] newInitializedArray(char value, int size) {
		char[] array;

		array = new char[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Cr�e un tableau de la taille indiqu� et dont toutes les cases contiennent la valeur indiqu�e.
	 * @param value Valeur � mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit �tre positif ou nul).
	 * @return Le tableau initialis�.
	 */
	public static final int[] newInitializedArray(int value, int size) {
		int[] array;

		array = new int[size];
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Cr�e un tableau de la taille indiqu� et dont toutes les cases contiennent la valeur indiqu�e.
	 * @param <T> Classe des �l�ments du tableau.
	 * @param value Valeur � mettre dans toutes les cases du tableau.
	 * @param size Taille du tableau (doit �tre positif ou nul).
	 * @return Le tableau initialis�.
	 */
	@SuppressWarnings("unchecked") public static final <T> T[] newInitializedArray(T value, int size) {
		T[] array;

		array = (T[]) Array.newInstance(value.getClass(), size);
		for (int i = 0; i < array.length; array[i++] = value) {
		}
		return array;
	}



	/**
	 * Trie le tableau indiqu�.
	 * @param <T> Type des �l�ments du tableau.
	 * @param array Tableau (peut �tre <code>null</code>).
	 * @return Le tableau, tri�, ou <code>null</code> si le tableau indiqu� �tait <code>null</code>.
	 */
	public static final <T> T[] sort(T[] array) {
		if (array != null) {
			Arrays.sort(array);
		}
		return array;
	}



	/**
	 * Trie le tableau en �liminant les doublons.
	 * @param array Tableau (peut �tre <code>null</code>).
	 * @return Un tableau tri� et sans doublons, ou <code>null</code> si le tableau indiqu� �tait <code>null</code>.
	 */
	public static final int[] sortAndRemoveDoubles(int[] array) {
		return sortAndRemoveDoubles(array, array.length);
	}



	/**
	 * Trie les premiers �l�ments du tableau en �liminant les doublons.
	 * @param array Tableau (peut �tre <code>null</code>).
	 * @param count Nombre d'�l�ments, doit �tre positif ou nul et inf�rieur ou �gal au nombre d'�l�ments du tableau.
	 * @return Un tableau tri� et sans doublons, ou <code>null</code> si le tableau indiqu� �tait <code>null</code>.
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
	 * Trie le tableau en �liminant les doublons.
	 * @param <T> Type des �l�ment du tableau.
	 * @param array Tableau (peut �tre <code>null</code>).
	 * @return Un tableau tri� et sans doublons, ou <code>null</code> si le tableau indiqu� �tait <code>null</code>.
	 */
	public static final <T> T[] sortAndRemoveDoubles(T[] array) {
		return (array == null) ? null : sortAndRemoveDoubles(array, array.length);
	}



	/**
	 * Trie les premiers �l�ments du tableau en �liminant les doublons.
	 * @param <T> Type des �l�ment du tableau.
	 * @param array Tableau (peut �tre <code>null</code>).
	 * @param count Nombre d'�l�ments, doit �tre positif ou nul et inf�rieur ou �gal au nombre d'�l�ments du tableau.
	 * @return Un tableau tri� et sans doublons, ou <code>null</code> si le tableau indiqu� �tait <code>null</code>.
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
