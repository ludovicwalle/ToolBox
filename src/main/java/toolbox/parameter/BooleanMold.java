package toolbox.parameter;

/**
 * La classe {@link BooleanMold} impl�mente le mod�le d'un param�tre dont la valeur est bool�enne.
 * @author Ludovic WALLE
 */
public class BooleanMold extends AbstractMold<Boolean> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 */
	public BooleanMold(String name, String description) {
		this(name, description, false, false);
	}



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 */
	public BooleanMold(String name, String description, boolean valueRequired, Boolean defaultValue) {
		super(name, description, valueRequired, (defaultValue == null) ? null : (defaultValue ? "True" : "False"));
	}



	/** {@inheritDoc} */
	@Override protected Boolean delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		if (rawValue.isEmpty() || rawValue.equals("True")) {
			return Boolean.TRUE;
		} else if (rawValue.equals("False")) {
			return Boolean.FALSE;
		} else {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du param�tre \"" + getName() + "\" n'est ni \"True\" ni \"False\".");
		}
	}



	/** {@inheritDoc} */
	@Override protected String delegateGetValueDescription() {
		return "\"True\" ou \"False\"";
	}



	/** {@inheritDoc} */
	@Override public BooleanParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new BooleanParameter(this, rawValue);
	}



}
