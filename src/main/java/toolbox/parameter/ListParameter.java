package toolbox.parameter;

/**
 * La classe {@link BooleanParameter} représente une instance de paramètre de type chaine parmi une liste.
 * @author Ludovic WALLE
 */
public class ListParameter extends AbstractParameter<String, ListMold> {



	/**
	 * @param mold Moule du paramètre.
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public ListParameter(ListMold mold) throws ParameterException_InvalidValue {
		this(mold, null);
	}



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue
	 * @throws ParameterException_InvalidValue Si la valeur par défaut est invalide.
	 */
	public ListParameter(ListMold mold, String rawValue) throws ParameterException_InvalidValue {
		super(mold, rawValue);
	}



}
