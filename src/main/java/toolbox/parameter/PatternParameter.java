package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type chaine respectant un modèle.
 * @author Ludovic WALLE
 */
public class PatternParameter extends AbstractParameter<String, PatternMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public PatternParameter(PatternMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public PatternParameter(PatternMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
