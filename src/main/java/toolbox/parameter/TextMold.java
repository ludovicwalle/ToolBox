package toolbox.parameter;

/**
 * La classe {@link TextMold} impl�mente le mod�le d'un param�tre dont la valeur est textuelle.
 * @author Ludovic WALLE
 */
public class TextMold extends AbstractMold<String> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 */
	public TextMold(String name, String description) {
		super(name, description, false, "");
	}



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
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
		return "Une chaine de caract�res";
	}



	/** {@inheritDoc} */
	@Override public TextParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new TextParameter(this, rawValue);
	}



}
