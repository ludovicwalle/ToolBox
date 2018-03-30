package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type réel.
 * @author Ludovic WALLE
 */
public class DoubleParameter extends AbstractParameter<Double, DoubleMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public DoubleParameter(DoubleMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public DoubleParameter(DoubleMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
