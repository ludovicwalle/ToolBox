package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} repr�sente une instance de param�tre de type chaine.
 * @author Ludovic WALLE
 */
public class TextParameter extends AbstractParameter<String, TextMold> {



	/**
	 * @param mold Moule du param�tre.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public TextParameter(TextMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public TextParameter(TextMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
