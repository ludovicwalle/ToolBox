package toolbox.counter;

import java.util.*;

import toolbox.*;



/**
 * Compteur, avec date de démarrage et valeur maximale, qui peut être mis en pause, utilisable dans un {@link Timer}.
 */
public abstract class Counter extends TimerTask {



	/**
	 */
	public Counter() {
		this.clock = new NanoClock();
	}



	/**
	 * @param clock Horloge.
	 */
	public Counter(final NanoClock clock) {
		this.clock = clock;
	}



	/**
	 * Affiche le compteur.<br>
	 * @return <code>true</code> si le compteur a été affiché, <code>false</code> sinon.
	 */
	protected abstract boolean delegate_show();



	/**
	 * Retourne la valeur du compteur (>= 0).
	 * @return La valeur du compteur (>= 0).
	 */
	public int getCount() {

		return count;
	}



	/**
	 * Retourne le temps écoulé en secondes depuis le premier démarrage.
	 * @return Le temps écoulé en secondes depuis le premier démarrage.
	 */
	public double getElapsed() {
		return previouslyElapsed + ((start > 0) ? (clock.absolute() - start) : 0);
	}



	/**
	 * Retourne la valeur maximale, ou -1 si elle n'est pas définie.
	 * @return la valeur maximale, ou -1 si elle n'est pas définie.
	 */
	public int getExpected() {
		return expected;
	}



	/**
	 * Spécifie le préfixe de la ligne d'affichage du compteur.
	 * @return Le préfixe de la ligne d'affichage du compteur.
	 */
	public String getPrefix() {
		return prefix;
	}



	/**
	 * Retourne la date de dernier démarrage en secondes, ou 0 si le compteur n'est pas démarré.
	 * @return La date de dernier démarrage en secondes, ou 0 si le compteur n'est pas démarré.
	 */
	public double getStart() {
		return start;
	}



	/**
	 * Incrémente le compteur.
	 */
	public void increment() {
		count++;
	}



	/**
	 * Incrémente le compteur de la valeur indiquée.
	 * @param count Valeur à ajouter au compteur (doit être >= 0).
	 */
	public void increment(@SuppressWarnings("hiding") int count) {
		if (count < 0) {
			throw new IllegalArgumentException(String.valueOf(count));
		}
		this.count += count;
	}



	/**
	 * Teste si le compteur est démarré.
	 * @return <code>true</code> si le compteur est démarré, <code>false</code> sinon.
	 */
	public boolean isStarted() {
		return start != 0;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final void run() {
		delegate_show();
		shown = true;
	}



	/**
	 * Spécifie la valeur maximale.
	 * @param maximum La valeur maximale, ou une valeur négative si elle est indéfinie.
	 */
	public void setExpected(final int maximum) {
		this.expected = (maximum < 0) ? -1 : maximum;
	}



	/**
	 * Spécifie le préfixe de la ligne d'affichage du compteur.
	 * @param prefix Préfixe de la ligne d'affichage du compteur.
	 */
	public void setPrefix(final String prefix) {
		this.prefix = (prefix == null) ? "" : prefix;
	}



	/**
	 * Affiche le compteur à la périodicité indiquée.<br>
	 * Si la périodicité est négative ou nulle, l'affichage est supprimé.
	 * @param milliseconds Temps entre chaque affichage, en millisecondes.
	 */
	public void showEvery(int milliseconds) {
		synchronized (clock) {
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			if (milliseconds > 0) {
				timer = new Timer("Counter", true);
				timer.schedule(this, milliseconds, milliseconds);
			}
		}
	}



	/**
	 * Démarre le compteur.
	 */
	public void start() {
		if (start == 0) {
			start = clock.absolute();
		}
	}



	/**
	 * Arrete le compteur en l'affichant,
	 * @return <code>true</code> si le compteur a été affiché, <code>false</code> sinon.
	 */
	public boolean stop() {
		if (start > 0) {
			delegate_show();
			shown = true;
			previouslyElapsed += clock.absolute() - start;
			start = 0;
		}
		return shown;
	}



	/**
	 * Arrete le compteur sans l'afficher.
	 * @return <code>true</code> si le compteur a été affiché, <code>false</code> sinon.
	 */
	public boolean stopSilently() {
		if (start > 0) {
			previouslyElapsed += clock.absolute() - start;
			start = 0;
		}
		return shown;
	}



	/**
	 * Retourne une chaine décrivant l'état de progression du compteur.
	 */
	@Override public String toString() {
		double elapsed;
		@SuppressWarnings("hiding") double lastElapsed = this.lastElapsed;
		@SuppressWarnings("hiding") int count = this.count;
		@SuppressWarnings("hiding") int lastCount = this.lastCount;
		StringBuilder string = new StringBuilder();

		elapsed = getElapsed();
		count = getCount();
		lastElapsed = this.lastElapsed;
		lastCount = this.lastCount;
		this.lastElapsed = elapsed;
		this.lastCount = count;

		if (!prefix.isEmpty()) {
			string.append(String.format("%s: ", prefix));
		}

		string.append(String.format("%s / %,d", FormatTools.formatDuration(elapsed), count));
		if (count > 0) {
			if (expected > 0) {
				string.append(String.format(" (%.3f%%) => %s / %,d (100%%)", (count * 100.0) / expected, FormatTools.formatDuration(((elapsed * expected) / count)), expected));
				if (start > 0) {
					string.append(String.format(" => %s", FormatTools.formatDate(start + ((elapsed * expected) / count), 3)));
				}
			}

			if ((elapsed > 0) && (elapsed > lastElapsed)) {
				string.append(String.format(" [%s/s ~%s/s]", FormatTools.formatWithUnit((count - lastCount) / (elapsed - lastElapsed)), FormatTools.formatWithUnit(count / elapsed)));
			}
		}
		return string.toString();
	}



	/**
	 * Horloge.
	 */
	private final NanoClock clock;



	/**
	 * Compteur.
	 */
	private int count = 0;



	/**
	 * Valeur maximale attendue du compteur ou une valeur strictement négative si elle est indéfinie.
	 */
	private int expected = -1;



	/**
	 * Compteur lors du dernier appel à {@link #toString()}.
	 */
	private int lastCount = 0;



	/**
	 * Temps écoulé lors du dernier appel à {@link #toString()}.
	 */
	private double lastElapsed;



	/**
	 * Préfixe de la ligne d'affichage du compteur (jamais <code>null</code>).
	 */
	private String prefix = "";



	/**
	 * Temps précedement écoulé (entre {@link #start()} et {@link #stop()} )
	 */
	private double previouslyElapsed = 0;



	/**
	 * Indicateur de compteur affiché.
	 */
	private boolean shown = false;



	/**
	 * Date de dernier démarrage en secondes, ou 0 si le compteur n'est pas démarré.
	 */
	private double start = 0;



	/**
	 * Timer pour l'affichage périodique.
	 */
	private Timer timer = null;



}
