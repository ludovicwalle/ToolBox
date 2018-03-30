package toolbox;

import java.io.*;
import java.text.*;
import java.util.*;



/**
 * La classe {@link FormatTools} contient des méthodes statiques d'usage général relatives aux formatage de données.
 * @author Ludovic WALLE
 */
public class FormatTools {



	/**
	 * Constructeur privé pour interdire l'instanciation.
	 */
	private FormatTools() {}



	/**
	 * Retourne une chaine correspondant à la date en secondes indiquée.
	 * @param seconds Date absolue en secondes.
	 * @param subSecondDigits Nombre de chiffres après la virgule pour les secondes ([0-9]).
	 * @return Une chaine correspondant à la date.
	 */
	public static String formatDate(double seconds, int subSecondDigits) {
		if ((subSecondDigits < 0) || (subSecondDigits > 9)) {
			throw new RuntimeException("Le nombre de chiffres n'appartient pas à l'intervalle [0..9]: " + subSecondDigits);
		}
		return DATE_FORMAT.format(new Date((long) (seconds * 1000))) + ((subSecondDigits > 0) ? String.format("%11.9f", seconds - Math.floor(seconds)).substring(1, subSecondDigits + 1) : "");
	}



	/**
	 * Formate la durée indiquée en heures, minutes, secondes, fraction de seconde, avec une précision relative à la durée.
	 * @param seconds Durée à formatter en secondes.
	 * @return La valeur formatée.
	 */
	public static String formatDuration(double seconds) {
		if (seconds >= 3600) {
			return String.format("%d:%02d:%02d", (long) seconds / 3600, ((long) seconds / 60) % 60, (long) seconds % 60);
		} else if (seconds >= 60) {
			return String.format("%02d:%06.3f", (long) seconds / 60, (((long) (seconds * 1000)) % 60000) / 1000.0);
		} else if (seconds >= 1) {
			return String.format("%.3fs", seconds);
		} else if (seconds >= 0.001) {
			return String.format("%.3fms", seconds * 1000);
		} else if (seconds >= 0.000001) {
			return String.format("%.3fµs", seconds * 1000000);
		} else if (seconds > 0) {
			return String.format("%,.3fns", seconds * 1000000000);
		} else {
			return "0";
		}
	}



	/**
	 * Retourne une chaine contenant la description complète d'une exception.
	 * @param exception Exception.
	 * @return La chaine contenant la description complète d'une exception.
	 */
	public static String formatException(Throwable exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		if (exception == null) {
			return null;
		} else {
			exception.printStackTrace(printWriter);
			printWriter.flush();
			return stringWriter.toString();
		}
	}



	/**
	 * Retourne une chaine contenant la description complète d'une exception sur une ligne.
	 * @param exception Exception.
	 * @return La chaine contenant la description complète d'une exception sur une ligne.
	 */
	public static String formatExceptionOnOneLine(Throwable exception) {
		return formatOnOneLine(formatException(exception));
	}



	/**
	 * Concatène la représentation des objets non <code>null</code> indiqués sous forme de chaine, précédés, séparés et terminés par les chaines indiquées.<br>
	 * Si la collection est <code>null</code>, la méthode retourne <code>null</code>.<br>
	 * Si la collection est vide, ou ne contient que des <code>null</code>, la méthode retourne une chaine vide.<br>
	 * @param before Chaine précédant le premier objet (peut être vide ou <code>null</code>).
	 * @param between Chaine séparant les objets (peut être vide ou <code>null</code>).
	 * @param after Chaine suivant le dernier objet (peut être vide ou <code>null</code>).
	 * @param objects Collection d'objets (peut être vide ou <code>null</code>, et contenir des objets vides ou <code>null</code>).
	 * @return Les objets formatés.
	 */
	public static String formatList(String before, String between, String after, AbstractCollection<?> objects) {
		return formatList(before, between, after, (objects == null) ? null : objects.toArray(new Object[objects.size()]));
	}



	/**
	 * Concatène les nombres indiqués sous forme de chaine, précédés, séparés et terminés par les chaines indiquées.<br>
	 * Si aucun nombre n'est indiqué, la méthode retourne une chaine vide.<br>
	 * @param before Chaine précédant le premier objet (peut être vide ou <code>null</code>).
	 * @param between Chaine séparant les objets (peut être vide ou <code>null</code>).
	 * @param after Chaine suivant le dernier objet (peut être vide ou <code>null</code>).
	 * @param numbers Nombres (peut être vide ou <code>null</code>).
	 * @return Les objets formatés.
	 */
	@SafeVarargs public static String formatList(String before, String between, String after, int... numbers) {
		StringBuilder builder = new StringBuilder();
		int i = 0;

		if ((numbers == null) || (numbers.length == 0)) {
			return "";
		} else {
			builder.append((before == null) ? "" : before);
			builder.append(numbers[i++]);
			between = (between == null) ? "" : between;
			for (; i < numbers.length; i++) {
				builder.append(between);
				builder.append(numbers[i]);
			}
			builder.append((after == null) ? "" : after);
			return builder.toString();
		}
	}



	/**
	 * Concatène la représentation des objets non <code>null</code> indiqués sous forme de chaine, précédés, séparés et terminés par les chaines indiquées.<br>
	 * Si aucun objet n'est indiqué, ou que tous les objets sont <code>null</code>, la méthode retourne une chaine vide.<br>
	 * @param <O> Type des objets à formatter.
	 * @param before Chaine précédant le premier objet (peut être vide ou <code>null</code>).
	 * @param between Chaine séparant les objets (peut être vide ou <code>null</code>).
	 * @param after Chaine suivant le dernier objet (peut être vide ou <code>null</code>).
	 * @param objects Objets (peut être vide ou <code>null</code>).
	 * @return Les objets formatés.
	 */
	@SafeVarargs public static <O extends Object> String formatList(String before, String between, String after, O... objects) {
		StringBuilder builder = new StringBuilder();
		int i;

		if ((objects == null) || (objects.length == 0)) {
			return "";
		} else {
			for (i = 0; (i < objects.length) && (objects[i] == null); i++) {
			}
			if (i < objects.length) {
				builder.append((before == null) ? "" : before);
				builder.append(objects[i++]);
				between = (between == null) ? "" : between;
				for (; i < objects.length; i++) {
					if (objects[i] != null) {
						builder.append(between);
						builder.append(objects[i].toString());
					}
				}
				builder.append((after == null) ? "" : after);
			}
			return builder.toString();
		}
	}



	/**
	 * Concatène la représentation des objets non <code>null</code> indiqués sous forme de chaine, précédés, séparés et terminés par les chaines indiquées.<br>
	 * Si l'ensemble est <code>null</code>, la méthode retourne <code>null</code>.<br>
	 * Si l'ensemble est vide, ou ne contient que des <code>null</code>, la méthode retourne une chaine vide.<br>
	 * @param before Chaine précédant le premier objet (peut être vide ou <code>null</code>).
	 * @param between Chaine séparant les objets (peut être vide ou <code>null</code>).
	 * @param after Chaine suivant le dernier objet (peut être vide ou <code>null</code>).
	 * @param objects Ensemble d'objets (peut être vide ou <code>null</code>).
	 * @return Les objets formatés.
	 */
	public static String formatList(String before, String between, String after, Set<?> objects) {
		return formatList(before, between, after, (objects == null) ? null : objects.toArray(new Object[objects.size()]));
	}



	/**
	 * Encode la chaine pour qu'elle ne contienne plus de fins de lignes.<br>
	 * Les \ sont doublés, puis les LF sont remplacés par \n.
	 * @param string Chaine à traiter.
	 * @return La chaine d'une seule ligne.
	 */
	public static String formatOnOneLine(String string) {
		return (string == null) ? null : string.replace("\\", "\\\\").replace("\r\n", "\\n").replace("\r", "\\r").replace("\n", "\\n");
	}



	/**
	 * Formate la valeur indiquée avec des unités (k, m, µ, ...).
	 * @param value Valeur à formatter.
	 * @return La valeur formatée.
	 */
	public static String formatWithUnit(double value) {
		if (value == 0) {
			return "0";
		} else if (value >= 1000000000) {
			return String.format("%,.3fG", value / 1000000000);
		} else if ((value) >= 1000000) {
			return String.format("%.3fM", value / 1000000);
		} else if ((value) >= 1000) {
			return String.format("%,.0f", value);
		} else if ((value) >= 1) {
			return String.format("%.3f", value);
		} else if ((value) >= 0.001) {
			return String.format("%.3fm", value * 1000);
		} else if ((value) >= 0.000001) {
			return String.format("%.3fµ", value * 1000000);
		} else {
			return String.format("%,.3fn", value * 1000000000);
		}
	}



	/**
	 * Format de date avec une précision de la seconde.
	 */
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");



}
