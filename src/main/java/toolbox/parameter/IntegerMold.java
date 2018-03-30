package toolbox.parameter;

/**
 * La classe {@link IntegerMold} implémente le modèle d'un paramètre dont la valeur est un entier.
 * @author Ludovic WALLE
 */
public class IntegerMold extends AbstractMold<Integer> {



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut du paramètre (peut être <code>null</code>).
	 */
	public IntegerMold(String name, String description, boolean valueRequired, int defaultValue) {
		this(name, description, valueRequired, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut du paramètre (peut être <code>null</code>).
	 * @param min Valeur minimale autorisée.
	 * @param max Valeur maximale autorisée.
	 */

	public IntegerMold(String name, String description, boolean valueRequired, int defaultValue, int min, int max) {
		super(name, description, valueRequired, String.valueOf(defaultValue));
		if (min > max) {
			throw new RuntimeException("La valeur minimale " + min + " est strictement supérieure à la valeur maximale " + name + "\".");
		}
		this.min = min;
		this.max = max;
	}



	/** {@inheritDoc} */
	@Override protected Integer delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		try {
			return Integer.parseInt(rawValue);
		} catch (NumberFormatException exception) {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du paramètre \"" + getName() + "\" n'est pas un entier dans l'intervalle [" + min + " et " + max + "].");
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "Entier dans l'intervalle [" + min + ".." + max + "]";
	}



	/**
	 * Retourne la valeur maximale autorisée.
	 * @return La valeur maximale autorisée.
	 */
	public int getMax() {
		return max;
	}



	/**
	 * Retourne la valeur minimale autorisée.
	 * @return La valeur minimale autorisée.
	 */
	public int getMin() {
		return min;
	}



	/** {@inheritDoc} */
	@Override public IntegerParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new IntegerParameter(this, rawValue);
	}



	/**
	 * Valeur maximale autorisée.
	 */
	private final int max;



	/**
	 * Valeur minimale autorisée.
	 */
	private final int min;



}
