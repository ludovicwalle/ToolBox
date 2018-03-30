package toolbox.parameter;



/**
 * La classe {@link Mold} repr�sente un moule de param�tre, regroupant des informations telles que son nom, sa description, le type de la valeur, ...<br>
 * Un moule de param�tre ne contient pas de valeur mais permet de cr�er des param�tres.<br>
 * Le nom du param�tre doit �tre constitu� uniquement des caract�res: {@value AbstractMold#NAMES_CHARS}.
 * @author Ludovic WALLE
 */
public interface Mold {



	/**
	 * Calcule la valeur correspondant � la valeur brute indiqu�e.
	 * @param rawValue Valeur brute.
	 * @return La valeur de l'objet, ou <code>null</code> si la valeur brute est <code>null</code> ou vide.
	 * @throws ParameterException_InvalidValue Si la valeur brute est invalide.
	 */
	public Object computeValue(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Retourne la valeur par d�faut, ou <code>null</code> si il n'y en a pas.
	 * @return La valeur par d�faut, ou <code>null</code> si il n'y en a pas.
	 */
	public abstract Object getDefaultValue();



	/**
	 * Retourne la description du param�tre.
	 * @return La description du param�tre.
	 */
	public abstract String getDescription();



	/**
	 * Retourne le nom du param�tre.
	 * @return Le nom du param�tre.
	 */
	public abstract String getName();



	/**
	 * Retourne la description de la valeur du param�tre.<br>
	 * La description de la valeur devrait contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point. Elle peut �tre <code>null</code>.
	 * @return La description de la valeur du param�tre.
	 */
	public abstract String getValueDescription();



	/**
	 * Teste si la valeur brute indiqu�e est valide.
	 * @param rawValue Valeur brute.
	 * @return <code>true</code> si la valeur brute indiqu�e est valide, <code>false</code> sinon.
	 */
	boolean isValid(String rawValue);



	/**
	 * Cr�e un nouveau param�tre avec la valeur brute indiqu�e.
	 * @param rawValue Valeur brute.
	 * @return Le nouveau param�tre avec la valeur brute indiqu�e.
	 * @throws ParameterException_InvalidValue Si la valeur brute est invalide.
	 */
	public abstract Parameter mold(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Teste si une valeur est requise lorsque le param�tre est sp�cifi�.
	 * @return <code>true</code> si une valeur est requise lorsque le param�tre est sp�cifi�, <code>false</code> sinon.
	 */
	public boolean valueIsRequired();



	/**
	 * Caract�res utilisables dans les noms de param�tres.
	 */
	public static final String NAMES_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";



}
