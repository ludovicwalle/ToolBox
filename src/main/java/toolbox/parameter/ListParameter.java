package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} repr�sente une instance de param�tre de type chaine parmi une liste.
 * @author Ludovic WALLE
 */
public class ListParameter extends AbstractParameter<String, ListMold> {



	/**
	 * @param mold Moule du param�tre.
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public ListParameter(ListMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par d�faut est invalide.
	 */
	public ListParameter(ListMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
