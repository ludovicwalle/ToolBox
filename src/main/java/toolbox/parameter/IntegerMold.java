package toolbox.parameter;

/**
 * La classe {@link IntegerMold} impl�mente le mod�le d'un param�tre dont la valeur est un entier.
 * @author Ludovic WALLE
 */
public class IntegerMold extends AbstractMold<Integer> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 */
	public IntegerMold(String name, String description, boolean valueRequired, int defaultValue) {
		this(name, description, valueRequired, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 * @param min Valeur minimale autoris�e.
	 * @param max Valeur maximale autoris�e.
	 */

	public IntegerMold(String name, String description, boolean valueRequired, int defaultValue, int min, int max) {
		super(name, description, valueRequired, String.valueOf(defaultValue));
		if (min > max) {
			throw new RuntimeException("La valeur minimale " + min + " est strictement sup�rieure � la valeur maximale " + name + "\".");
		}
		this.min = min;
		this.max = max;
	}



	/** {@inheritDoc} */
	@Override protected Integer delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		try {
			return Integer.parseInt(rawValue);
		} catch (NumberFormatException exception) {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du param�tre \"" + getName() + "\" n'est pas un entier dans l'intervalle [" + min + " et " + max + "].");
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "Entier dans l'intervalle [" + min + ".." + max + "]";
	}



	/**
	 * Retourne la valeur maximale autoris�e.
	 * @return La valeur maximale autoris�e.
	 */
	public int getMax() {
		return max;
	}



	/**
	 * Retourne la valeur minimale autoris�e.
	 * @return La valeur minimale autoris�e.
	 */
	public int getMin() {
		return min;
	}



	/** {@inheritDoc} */
	@Override public IntegerParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new IntegerParameter(this, rawValue);
	}



	/**
	 * Valeur maximale autoris�e.
	 */
	private final int max;



	/**
	 * Valeur minimale autoris�e.
	 */
	private final int min;



}
