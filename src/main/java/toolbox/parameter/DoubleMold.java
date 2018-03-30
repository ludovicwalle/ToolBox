package toolbox.parameter;

/**
 * La classe {@link DoubleMold} implémente le modèle d'un paramètre dont la valeur est un double.
 * @author Ludovic WALLE
 */
public class DoubleMold extends AbstractMold<Double> {



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut du paramètre (peut être <code>null</code>).
	 */
	public DoubleMold(String name, String description, boolean valueRequired, double defaultValue) {
		super(name, description, valueRequired, String.valueOf(defaultValue));
	}



	/** {@inheritDoc} */
	@Override protected Double delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		try {
			return Double.parseDouble(rawValue);
		} catch (NumberFormatException exception) {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du paramètre \"" + getName() + "\" n'est pas un nombre réel.");
		}
	}



	/** {@inheritDoc} */
	@Override protected String delegateGetValueDescription() {
		return "Nombre réel";
	}



	/** {@inheritDoc} */
	@Override public DoubleParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new DoubleParameter(this, rawValue);
	}



}
