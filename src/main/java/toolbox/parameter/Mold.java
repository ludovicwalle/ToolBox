package toolbox.parameter;



/**
 * La classe {@link Mold} représente un moule de paramètre, regroupant des informations telles que son nom, sa description, le type de la valeur, ...<br>
 * Un moule de paramètre ne contient pas de valeur mais permet de créer des paramètres.<br>
 * Le nom du paramètre doit être constitué uniquement des caractères: {@value AbstractMold#NAMES_CHARS}.
 * @author Ludovic WALLE
 */
public interface Mold {



	/**
	 * Calcule la valeur correspondant à la valeur brute indiquée.
	 * @param rawValue Valeur brute.
	 * @return La valeur de l'objet, ou <code>null</code> si la valeur brute est <code>null</code> ou vide.
	 * @throws ParameterException_InvalidValue Si la valeur brute est invalide.
	 */
	public Object computeValue(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Retourne la valeur par défaut, ou <code>null</code> si il n'y en a pas.
	 * @return La valeur par défaut, ou <code>null</code> si il n'y en a pas.
	 */
	public abstract Object getDefaultValue();



	/**
	 * Retourne la description du paramètre.
	 * @return La description du paramètre.
	 */
	public abstract String getDescription();



	/**
	 * Retourne le nom du paramètre.
	 * @return Le nom du paramètre.
	 */
	public abstract String getName();



	/**
	 * Retourne la description de la valeur du paramètre.<br>
	 * La description de la valeur devrait contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point. Elle peut être <code>null</code>.
	 * @return La description de la valeur du paramètre.
	 */
	public abstract String getValueDescription();



	/**
	 * Teste si la valeur brute indiquée est valide.
	 * @param rawValue Valeur brute.
	 * @return <code>true</code> si la valeur brute indiquée est valide, <code>false</code> sinon.
	 */
	boolean isValid(String rawValue);



	/**
	 * Crée un nouveau paramètre avec la valeur brute indiquée.
	 * @param rawValue Valeur brute.
	 * @return Le nouveau paramètre avec la valeur brute indiquée.
	 * @throws ParameterException_InvalidValue Si la valeur brute est invalide.
	 */
	public abstract Parameter mold(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Teste si une valeur est requise lorsque le paramètre est spécifié.
	 * @return <code>true</code> si une valeur est requise lorsque le paramètre est spécifié, <code>false</code> sinon.
	 */
	public boolean valueIsRequired();



	/**
	 * Caractères utilisables dans les noms de paramètres.
	 */
	public static final String NAMES_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";



}
