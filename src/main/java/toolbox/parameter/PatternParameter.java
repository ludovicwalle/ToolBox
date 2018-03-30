package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} repr�sente une instance de param�tre de type chaine respectant un mod�le.
 * @author Ludovic WALLE
 */
public class PatternParameter extends AbstractParameter<String, PatternMold> {



	/**
	 * @param mold Moule du param�tre.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public PatternParameter(PatternMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public PatternParameter(PatternMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
