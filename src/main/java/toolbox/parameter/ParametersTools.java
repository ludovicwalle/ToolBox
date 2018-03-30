package toolbox.parameter;

import java.util.*;



/**
 * La classe {@link ParametersTools} impl�mente diff�rentes m�thodes utiles pour la synchronisation.
 * @author Ludovic WALLE
 */
public class ParametersTools {



	/**
	 * Extrait une propri�t� bool�enne facultative.
	 * @param key Nom de la propri�t�.
	 * @param defaultValue Valeur par d�faut.
	 * @param properties Propri�t�s.
	 * @return La valeur bool�enne.
	 */
	public static boolean getOptionalBooleanProperty(String key, boolean defaultValue, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			return defaultValue;
		}
		if (value.equalsIgnoreCase("true")) {
			return true;
		} else if (value.equalsIgnoreCase("false")) {
			return false;
		} else {
			throw new RuntimeException("La valeur de " + key + " n'est pas \"true\" ou \"false\".");
		}
	}



	/**
	 * Extrait une propri�t� bool�enne facultative.
	 * @param key Nom de la propri�t�.
	 * @param properties Propri�t�s.
	 * @return La valeur bool�enne, ou <code>false</code> si elle n'est pas d�finie.
	 */
	public static boolean getOptionalBooleanProperty(String key, Properties properties) {
		return getOptionalBooleanProperty(key, false, properties);
	}



	/**
	 * Extrait une propri�t� num�rique facultative.
	 * @param key Nom de la propri�t�.
	 * @param defaultValue Valeur par d�faut.
	 * @param properties Propri�t�s.
	 * @return La valeur num�rique.
	 */
	public static int getOptionalIntProperty(String key, int defaultValue, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}



	/**
	 * Extrait une propri�t� textuelle facultative.
	 * @param key Nom de la propri�t�.
	 * @param properties Propri�t�s.
	 * @return La valeur textuelle, ou <code>null</code> si elle n'est pas d�finie.
	 */
	public static String getOptionalStringProperty(String key, Properties properties) {
		return getOptionalStringProperty(key, null, properties);
	}



	/**
	 * Extrait une propri�t� textuelle facultative.
	 * @param key Nom de la propri�t�.
	 * @param defaultValue Valeur par d�faut.
	 * @param properties Propri�t�s.
	 * @return La valeur textuelle.
	 */
	public static String getOptionalStringProperty(String key, String defaultValue, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			return defaultValue;
		} else {
			return value;
		}
	}



	/**
	 * Extrait une propri�t� bool�enne obligatoire.
	 * @param key Nom de la propri�t�.
	 * @param properties Propri�t�s.
	 * @return La valeur textuelle.
	 * @throws RuntimeException Si la propri�t� n'est pas d�finie.
	 */
	public static boolean getRequiredBooleanProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propri�t� " + key + " n'est pas d�finie.");
		}
		if (value.equalsIgnoreCase("true")) {
			return true;
		} else if (value.equalsIgnoreCase("false")) {
			return false;
		} else {
			throw new RuntimeException("La valeur de " + key + " n'est pas \"true\" ou \"false\".");
		}
	}



	/**
	 * Extrait une propri�t� num�rique obligatoire.
	 * @param key Nom de la propri�t�.
	 * @param properties Propri�t�s.
	 * @return La valeur num�rique.
	 * @throws RuntimeException Si la propri�t� n'est pas d�finie.
	 */
	public static int getRequiredIntProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propri�t� " + key + " n'est pas d�finie.");
		}
		return Integer.parseInt(value);
	}



	/**
	 * Extrait une propri�t� textuelle obligatoire.
	 * @param key Nom de la propri�t�.
	 * @param properties Propri�t�s.
	 * @return La valeur textuelle.
	 * @throws RuntimeException Si la propri�t� n'est pas d�finie.
	 */
	public static String getRequiredStringProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propri�t� " + key + " n'est pas d�finie.");
		}
		return value;
	}



}
