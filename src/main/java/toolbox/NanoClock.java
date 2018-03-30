package toolbox;

import java.util.*;



/**
 * La classe {@link NanoClock} implémente une horloge dont la précision atteint la nanoseconde, en combinant les méthodes {@link System#nanoTime()} (pour la précision, jusqu'à la nanoseconde) et
 * {@link System#currentTimeMillis()} (pour avoir une date absolue correcte, mais dont la précision est limitée à la milliseconde, et la résolution pratique semble être d'environ 15 millisecondes).
 * @author Ludovic WALLE
 */
public class NanoClock {



	/** */
	public NanoClock() {
		nanoTimeOffset = (System.currentTimeMillis() * MILLISECONDS_TO_SECONDS) - (System.nanoTime() * NANOSECONDS_TO_SECONDS) - (TimeZone.getTimeZone((System.getenv("TZ") == null) ? "GMT" : System.getenv("TZ")).getRawOffset() * MILLISECONDS_TO_SECONDS);
	}



	/**
	 * Retourne la date absolue en secondes
	 * @return La date absolue en secondes.
	 */
	public double absolute() {
		return absolute(System.nanoTime() * NANOSECONDS_TO_SECONDS);
	}



	/**
	 * Retourne la date absolue en secondes correspondant à la date selon {@link System#nanoTime()} en secondes indiquée.
	 * @param nanoTimeSeconds Date en secondes selon {@link System#nanoTime()}.
	 * @return La date absolue en secondes.
	 */
	public double absolute(double nanoTimeSeconds) {
		return nanoTimeSeconds + nanoTimeOffset;
	}



	/**
	 * Retourne le décalage de l'horloge.
	 * @return Le décalage de l'horloge.
	 */
	public double getNanoTimeOffset() {
		return nanoTimeOffset;
	}



	/**
	 * Change l'heure de l'horloge. Seul le décalage {@link #nanoTimeOffset} est modifié.
	 * @param newTime La nouvelle date absolue en secondes.
	 * @return L'ancienne date absolue en secondes.
	 */
	public double set(Double newTime) {
		double oldTime;
		double nanoTimeSeconds;

		nanoTimeSeconds = System.nanoTime() * NANOSECONDS_TO_SECONDS;
		oldTime = absolute(nanoTimeSeconds);
		nanoTimeOffset = newTime - nanoTimeSeconds;
		return oldTime;
	}



	/**
	 * Retourne une chaine correspondant à la date selon {@link System#nanoTime()} en secondes indiquée, avec une précision de la seconde (0 chiffres après la seconde).
	 * @param nanoTimeSeconds Date en secondes selon {@link System#nanoTime()}.
	 * @param subSecondDigits Nombre de chiffres après la virgule pour les secondes ([0-9]).
	 * @return Une chaine correspondant à la date en secondes.
	 */
	public String toString(double nanoTimeSeconds, int subSecondDigits) {
		return FormatTools.formatDate(absolute(nanoTimeSeconds), subSecondDigits);
	}



	/**
	 * Retourne une chaine correspondant à la date courante, avec une précision de la seconde (0 chiffres après la seconde).
	 * @param subSecondDigits Nombre de chiffres après la virgule pour les secondes ([0-9]).
	 * @return Une chaine correspondant à la date courante.
	 */
	public String toString(int subSecondDigits) {
		return toString(System.nanoTime() * NANOSECONDS_TO_SECONDS, subSecondDigits);
	}



	/**
	 * Décalage de entre {@link System#nanoTime()} et {@link System#currentTimeMillis()}.<br>
	 * La résolution de la méthode {@link System#nanoTime()} est la nanoseconde, au moins en théorie, mais son origine est arbitraire. Il est possible de la déterminer, par exemple à l'aide de la
	 * méthode {@link System#currentTimeMillis()}, dont l'origine est fixe (01/01/1970) et la résolution est la milliseconde, bien qu'elle semble limitée en pratique à environ 15 millisecondes.
	 */
	private double nanoTimeOffset;



	/**
	 * Horloge par défaut.
	 */
	public final static NanoClock DEFAULT_CLOCK = new NanoClock();



	/**
	 * Coefficient de conversion de microsecondes en millisecondes.
	 */
	public final static double MICROSECONDS_TO_MILLISECONDS = 0.001;



	/**
	 * Coefficient de conversion de microsecondes en nanosecondes.
	 */
	public final static int MICROSECONDS_TO_NANOSECONDS = 1000;



	/**
	 * Coefficient de conversion de microsecondes en secondes.
	 */
	public final static double MICROSECONDS_TO_SECONDS = 0.000001;



	/**
	 * Coefficient de conversion de millisecondes en microsecondes.
	 */
	public final static int MILLISECONDS_TO_MICROSECONDS = 1000;



	/**
	 * Coefficient de conversion de millisecondes en nanosecondes.
	 */
	public final static int MILLISECONDS_TO_NANOSECONDS = 1000000;



	/**
	 * Coefficient de conversion de millisecondes en secondes.
	 */
	public final static double MILLISECONDS_TO_SECONDS = 0.001;



	/**
	 * Coefficient de conversion de nanosecondes en microsecondes.
	 */
	public final static double NANOSECONDS_TO_MICROSECONDS = 0.001;



	/**
	 * Coefficient de conversion de nanosecondes en millisecondes.
	 */
	public final static double NANOSECONDS_TO_MILLISECONDS = 0.000001;



	/**
	 * Coefficient de conversion de nanosecondes en secondes.
	 */
	public final static double NANOSECONDS_TO_SECONDS = 0.000000001;



	/**
	 * Coefficient de conversion de secondes en microsecondes.
	 */
	public final static int SECONDS_TO_MICROSECONDS = 1000000;



	/**
	 * Coefficient de conversion de secondes en millisecondes.
	 */
	public final static int SECONDS_TO_MILLISECONDS = 1000;



	/**
	 * Coefficient de conversion de secondes en nanosecondes.
	 */
	public final static int SECONDS_TO_NANOSECONDS = 1000000000;



}
