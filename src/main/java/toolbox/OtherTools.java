package toolbox;

import java.io.*;
import java.net.*;



/**
 * La classe {@link #OtherTools} contient des méthodes statiques d'usage général.
 * @author walle
 */
public class OtherTools {



	/**
	 * Constructeur privé pour interdire l'instanciation.
	 */
	private OtherTools() {}



	/**
	 * Compare les deux objets comparables indiqués. Les objets peuvent être <code>null</code>. Les <code>null</code> sont en premier.
	 * @param <T> Type des objets.
	 * @param one Premier objet (peut être <code>null</code>).
	 * @param two Deuxième objet (peut être <code>null</code>).
	 * @return Le resultat de {@link Comparable#compareTo(Object)} si les deux objets sont non <code>null</code>, -1 si seul le premier est <code>null</code>, 1 si seul le deuxième est
	 *         <code>null</code>, 0 si ils sont tous les deux <code>null</code>.
	 */
	public static final <T extends Comparable<T>> int compare(T one, T two) {
		if (one == two) {
			return 0;
		} else if (one == null) {
			return -1;
		} else if (two == null) {
			return 1;
		} else {
			return one.compareTo(two);
		}
	}



	/**
	 * Calcule le nombre de chiffre qu'il y a dans un nombre. Si la valeur est négative, le signe est comptabilisé comme un chiffre.
	 * @param value Nombre dont il faut calculer le nombre de chiffres.
	 * @return Le nombre de chiffre qu'il y a dans un nombre.
	 */
	public static final int digitCount(int value) {
		int digits = 1;

		if (value < 0) {
			digits++;
			value = -value;
		}
		while (value >= 10) {
			digits++;
			value /= 10;
		}
		return digits;
	}



	/**
	 * Calcule le temps écoulé en secondes depuis la date indiquée.
	 * @param start Date de départ en microsecondes, obtenue par {@link System#nanoTime()}.
	 * @return Le temps écoulé en secondes.
	 */
	public static double elapsed(double start) {
		return (System.nanoTime() - start) / 1000000000;
	}



	/**
	 * Affiche l'exception indiquée et interromp le traitement.
	 * @param exception Exception fatale.
	 */
	public static void fatal(Throwable exception) {
		printException(exception);
		System.exit(1);
	}



	/**
	 * Retourne le nom de la machine locale, ou <code>null</code> si il n'est pas disponible.
	 * @return Le nom de la machine locale, ou <code>null</code> si il n'est pas disponible.
	 */
	public static String getHostName() {
		if (!hostNameComputed) {
			synchronized (OtherTools.class) {
				if (!hostNameComputed) {
					try {
						InetAddress localMachine = InetAddress.getLocalHost();
						hostName = localMachine.getHostName();
					} catch (UnknownHostException exception) {
						hostName = null;
					}
					hostNameComputed = true;
				}
			}
		}
		return hostName;
	}



	/**
	 * Retourne un extrait de la chaine.<br>
	 * L'extrait commence au caractère en position <code>offset - before</code> et se termine au caractère en position <code>offset + after</code>. Il est encadré par des <code>...</code> pour
	 * indiquer que l'extrait est tronqué, au début (sauf dans le cas où l'extrait atteint le début de la chaine), et à la fin (sauf dans le cas où l'extrait atteint la fin de la chaine).
	 * @param string La chaine dont on veut extraire une partie (ne doit pas être <code>null</code> ).
	 * @param offset La position dans la chaine (doit être positif ou nul et strictement inférieur à la longueur de la chaine).
	 * @param before Le nombre de caractères à extraire avant la position dans la chaine (doit être positif ou nul).
	 * @param after Le nombre de caractères à extraire après la position dans la chaine (doit être positif ou nul).
	 * @return Un extrait de la chaine.
	 */
	public static final String getStringFragment(String string, int offset, int before, int after) {
		int first;
		String textBefore;
		int last;
		String textAfter;

		if (before < 0) {
			throw new RuntimeException("Le nombre de caractères à extraire doit être positif ou nul: " + String.valueOf(before));
		}
		if (after < 0) {
			throw new RuntimeException("Le nombre de caractères à extraire doit être positif ou nul: " + String.valueOf(after));
		}
		int maximum = string.length() - 1;
		if ((offset < 0) || (offset > maximum)) {
			throw new RuntimeException("La position " + offset + " est en dehors de la chaine.");
		}
		if ((offset - before) > 0) {
			first = offset - before;
			textBefore = "...";
		} else {
			first = 0;
			textBefore = "";
		}
		if ((offset + after) < string.length()) {
			last = offset + after;
			textAfter = "...";
		} else {
			last = string.length();
			textAfter = "";
		}
		return textBefore + string.substring(first, last) + textAfter;
	}



	/**
	 * Retourne le plus grand des nombres indiqués.
	 * @param numbers Nombres (au moins un).
	 * @return Le plus grand des nombres indiqués.
	 */
	public static int max(int... numbers) {
		int max;

		max = numbers[0];
		for (int number : numbers) {
			if (number > max) {
				max = number;
			}
		}
		return max;
	}



	/**
	 * Retourne le plus petit des nombres indiqués.
	 * @param numbers Nombres (au moins un).
	 * @return Le plus petit des nombres indiqués.
	 */
	public static int min(int... numbers) {
		int min;

		min = numbers[0];
		for (int number : numbers) {
			if (number < min) {
				min = number;
			}
		}
		return min;
	}



	/**
	 * Affiche l'exception indiquée sur la sortie indiquée.
	 * @param stream Flux de sortie.
	 * @param exception Exception à afficher.
	 */
	public static final void printException(PrintStream stream, Throwable exception) {
		StackTraceElement[] trace = exception.getStackTrace();

		stream.println(exception);
		for (StackTraceElement element : trace) {
			stream.println("\tat " + element);
		}
		if ((exception.getCause() != null) && (exception.getCause() != exception)) {
			printException(stream, exception.getCause());
		}
	}



	/**
	 * Affiche l'exception indiquée sur la sortie standard.
	 * @param exception Exception à afficher.
	 */
	public static final void printException(Throwable exception) {
		StackTraceElement[] trace = exception.getStackTrace();

		System.err.println(exception);
		for (StackTraceElement element : trace) {
			System.err.println("\tat " + element);
		}
		if ((exception.getCause() != null) && (exception.getCause() != exception)) {
			printException(exception.getCause());
		}
	}



	/**
	 * Attend le nombre de millisecondes indiqué.
	 * @param milliseconds Nombre de millisecondes à attendre.
	 */
	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException exception) {
		}
	}



	/**
	 * Nom de la machine locale, ou <code>null</code> si il n'est pas disponible, significatif uniquement si {@link #hostNameComputed} est <code>true</code>.
	 */
	private static String hostName;



	/**
	 * Indicateur de nom de machine locale déjà calculé.
	 */
	private static boolean hostNameComputed = false;



}
