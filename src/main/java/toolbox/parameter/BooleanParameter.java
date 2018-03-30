package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} repr�sente une instance de param�tre de type bool�en.
 * @author Ludovic WALLE
 */
public class BooleanParameter extends AbstractParameter<Boolean, BooleanMold> {



	/**
	 * @param mold Moule du param�tre.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public BooleanParameter(BooleanMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue Valeur brute.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public BooleanParameter(BooleanMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
