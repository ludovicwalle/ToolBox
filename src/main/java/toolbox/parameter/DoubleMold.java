package toolbox.parameter;

/**
 * La classe {@link DoubleMold} impl�mente le mod�le d'un param�tre dont la valeur est un double.
 * @author Ludovic WALLE
 */
public class DoubleMold extends AbstractMold<Double> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 */
	public DoubleMold(String name, String description, boolean valueRequired, double defaultValue) {
		super(name, description, valueRequired, String.valueOf(defaultValue));
	}



	/** {@inheritDoc} */
	@Override protected Double delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		try {
			return Double.parseDouble(rawValue);
		} catch (NumberFormatException exception) {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du param�tre \"" + getName() + "\" n'est pas un nombre r�el.");
		}
	}



	/** {@inheritDoc} */
	@Override protected String delegateGetValueDescription() {
		return "Nombre r�el";
	}



	/** {@inheritDoc} */
	@Override public DoubleParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new DoubleParameter(this, rawValue);
	}



}
