package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type chaine.
 * @author Ludovic WALLE
 */
public class TextParameter extends AbstractParameter<String, TextMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public TextParameter(TextMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public TextParameter(TextMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
