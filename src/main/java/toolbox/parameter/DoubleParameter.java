package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} repr�sente une instance de param�tre de type r�el.
 * @author Ludovic WALLE
 */
public class DoubleParameter extends AbstractParameter<Double, DoubleMold> {



	/**
	 * @param mold Moule du param�tre.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public DoubleParameter(DoubleMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public DoubleParameter(DoubleMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
