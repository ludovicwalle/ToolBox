package toolbox.counter;

import java.util.*;

import toolbox.*;



/**
 * Compteur, avec date de d�marrage et valeur maximale, qui peut �tre mis en pause, utilisable dans un {@link Timer}.
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
	 * @return <code>true</code> si le compteur a �t� affich�, <code>false</code> sinon.
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
	 * Retourne le temps �coul� en secondes depuis le premier d�marrage.
	 * @return Le temps �coul� en secondes depuis le premier d�marrage.
	 */
	public double getElapsed() {
		return previouslyElapsed + ((start > 0) ? (clock.absolute() - start) : 0);
	}



	/**
	 * Retourne la valeur maximale, ou -1 si elle n'est pas d�finie.
	 * @return la valeur maximale, ou -1 si elle n'est pas d�finie.
	 */
	public int getExpected() {
		return expected;
	}



	/**
	 * Sp�cifie le pr�fixe de la ligne d'affichage du compteur.
	 * @return Le pr�fixe de la ligne d'affichage du compteur.
	 */
	public String getPrefix() {
		return prefix;
	}



	/**
	 * Retourne la date de dernier d�marrage en secondes, ou 0 si le compteur n'est pas d�marr�.
	 * @return La date de dernier d�marrage en secondes, ou 0 si le compteur n'est pas d�marr�.
	 */
	public double getStart() {
		return start;
	}



	/**
	 * Incr�mente le compteur.
	 */
	public void increment() {
		count++;
	}



	/**
	 * Incr�mente le compteur de la valeur indiqu�e.
	 * @param count Valeur � ajouter au compteur (doit �tre >= 0).
	 */
	public void increment(@SuppressWarnings("hiding") int count) {
		if (count < 0) {
			throw new IllegalArgumentException(String.valueOf(count));
		}
		this.count += count;
	}



	/**
	 * Teste si le compteur est d�marr�.
	 * @return <code>true</code> si le compteur est d�marr�, <code>false</code> sinon.
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
	 * Sp�cifie la valeur maximale.
	 * @param maximum La valeur maximale, ou une valeur n�gative si elle est ind�finie.
	 */
	public void setExpected(final int maximum) {
		this.expected = (maximum < 0) ? -1 : maximum;
	}



	/**
	 * Sp�cifie le pr�fixe de la ligne d'affichage du compteur.
	 * @param prefix Pr�fixe de la ligne d'affichage du compteur.
	 */
	public void setPrefix(final String prefix) {
		this.prefix = (prefix == null) ? "" : prefix;
	}



	/**
	 * Affiche le compteur � la p�riodicit� indiqu�e.<br>
	 * Si la p�riodicit� est n�gative ou nulle, l'affichage est supprim�.
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
	 * D�marre le compteur.
	 */
	public void start() {
		if (start == 0) {
			start = clock.absolute();
		}
	}



	/**
	 * Arrete le compteur en l'affichant,
	 * @return <code>true</code> si le compteur a �t� affich�, <code>false</code> sinon.
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
	 * @return <code>true</code> si le compteur a �t� affich�, <code>false</code> sinon.
	 */
	public boolean stopSilently() {
		if (start > 0) {
			previouslyElapsed += clock.absolute() - start;
			start = 0;
		}
		return shown;
	}



	/**
	 * Retourne une chaine d�crivant l'�tat de progression du compteur.
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
	 * Valeur maximale attendue du compteur ou une valeur strictement n�gative si elle est ind�finie.
	 */
	private int expected = -1;



	/**
	 * Compteur lors du dernier appel � {@link #toString()}.
	 */
	private int lastCount = 0;



	/**
	 * Temps �coul� lors du dernier appel � {@link #toString()}.
	 */
	private double lastElapsed;



	/**
	 * Pr�fixe de la ligne d'affichage du compteur (jamais <code>null</code>).
	 */
	private String prefix = "";



	/**
	 * Temps pr�cedement �coul� (entre {@link #start()} et {@link #stop()} )
	 */
	private double previouslyElapsed = 0;



	/**
	 * Indicateur de compteur affich�.
	 */
	private boolean shown = false;



	/**
	 * Date de dernier d�marrage en secondes, ou 0 si le compteur n'est pas d�marr�.
	 */
	private double start = 0;



	/**
	 * Timer pour l'affichage p�riodique.
	 */
	private Timer timer = null;



}
