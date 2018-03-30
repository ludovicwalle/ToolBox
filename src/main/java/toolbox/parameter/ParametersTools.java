package toolbox.parameter;

import java.util.*;



/**
 * La classe {@link ParametersTools} implémente différentes méthodes utiles pour la synchronisation.
 * @author Ludovic WALLE
 */
public class ParametersTools {



	/**
	 * Extrait une propriété booléenne facultative.
	 * @param key Nom de la propriété.
	 * @param defaultValue Valeur par défaut.
	 * @param properties Propriétés.
	 * @return La valeur booléenne.
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
	 * Extrait une propriété booléenne facultative.
	 * @param key Nom de la propriété.
	 * @param properties Propriétés.
	 * @return La valeur booléenne, ou <code>false</code> si elle n'est pas définie.
	 */
	public static boolean getOptionalBooleanProperty(String key, Properties properties) {
		return getOptionalBooleanProperty(key, false, properties);
	}



	/**
	 * Extrait une propriété numérique facultative.
	 * @param key Nom de la propriété.
	 * @param defaultValue Valeur par défaut.
	 * @param properties Propriétés.
	 * @return La valeur numérique.
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
	 * Extrait une propriété textuelle facultative.
	 * @param key Nom de la propriété.
	 * @param properties Propriétés.
	 * @return La valeur textuelle, ou <code>null</code> si elle n'est pas définie.
	 */
	public static String getOptionalStringProperty(String key, Properties properties) {
		return getOptionalStringProperty(key, null, properties);
	}



	/**
	 * Extrait une propriété textuelle facultative.
	 * @param key Nom de la propriété.
	 * @param defaultValue Valeur par défaut.
	 * @param properties Propriétés.
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
	 * Extrait une propriété booléenne obligatoire.
	 * @param key Nom de la propriété.
	 * @param properties Propriétés.
	 * @return La valeur textuelle.
	 * @throws RuntimeException Si la propriété n'est pas définie.
	 */
	public static boolean getRequiredBooleanProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propriété " + key + " n'est pas définie.");
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
	 * Extrait une propriété numérique obligatoire.
	 * @param key Nom de la propriété.
	 * @param properties Propriétés.
	 * @return La valeur numérique.
	 * @throws RuntimeException Si la propriété n'est pas définie.
	 */
	public static int getRequiredIntProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propriété " + key + " n'est pas définie.");
		}
		return Integer.parseInt(value);
	}



	/**
	 * Extrait une propriété textuelle obligatoire.
	 * @param key Nom de la propriété.
	 * @param properties Propriétés.
	 * @return La valeur textuelle.
	 * @throws RuntimeException Si la propriété n'est pas définie.
	 */
	public static String getRequiredStringProperty(String key, Properties properties) {
		String value;

		if ((value = properties.getProperty(key)) == null) {
			throw new RuntimeException("La propriété " + key + " n'est pas définie.");
		}
		return value;
	}



}
