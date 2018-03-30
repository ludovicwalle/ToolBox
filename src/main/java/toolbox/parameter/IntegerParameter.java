package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type entier.
 * @author Ludovic WALLE
 */
public class IntegerParameter extends AbstractParameter<Integer, IntegerMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public IntegerParameter(IntegerMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public IntegerParameter(IntegerMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
