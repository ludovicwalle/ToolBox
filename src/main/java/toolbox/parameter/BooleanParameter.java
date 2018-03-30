package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type booléen.
 * @author Ludovic WALLE
 */
public class BooleanParameter extends AbstractParameter<Boolean, BooleanMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public BooleanParameter(BooleanMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue Valeur brute.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public BooleanParameter(BooleanMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
