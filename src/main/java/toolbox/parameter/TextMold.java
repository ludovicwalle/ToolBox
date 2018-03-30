package toolbox.parameter;

/**
 * La classe {@link TextMold} implémente le modèle d'un paramètre dont la valeur est textuelle.
 * @author Ludovic WALLE
 */
public class TextMold extends AbstractMold<String> {



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 */
	public TextMold(String name, String description) {
		super(name, description, false, "");
	}



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut du paramètre (peut être <code>null</code>).
	 */
	public TextMold(String name, String description, boolean valueRequired, String defaultValue) {
		super(name, description, valueRequired, defaultValue);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		return rawValue;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "Une chaine de caractères";
	}



	/** {@inheritDoc} */
	@Override public TextParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new TextParameter(this, rawValue);
	}



}
