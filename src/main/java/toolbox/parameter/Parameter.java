package toolbox.parameter;



/**
 * La classe {@link Parameter} rep�sente une instance de param�tre.
 * @author Ludovic WALLE
 */
public interface Parameter {



	/**
	 * Retourne la valeur par d�faut du param�tre.
	 * @return La valeur par d�faut du param�tre.
	 */
	public abstract Object getDefaultValue();



	/**
	 * Retourne le moule du param�tre.
	 * @return Le moule du param�tre.
	 */
	public abstract Mold getMold();



	/**
	 * Retourne le nom du param�tre.
	 * @return Le nom du param�tre.
	 */
	public abstract String getName();



	/**
	 * Retourne la valeur brute du param�tre:
	 * <ul>
	 * <li><code>null</code> si le param�tre est sp�cifi� mais sans valeur,
	 * <li>la valeur telle qu'elle sp�cifi�e si le param�tre est sp�cifi� avec une valeur.
	 * </ul>
	 * @return La valeur brute du param�tre.
	 */
	public abstract String getRawValue();



	/**
	 * Retourne la valeur du param�tre:
	 * <ul>
	 * <li><code>null</code> si le param�tre est sp�cifi� mais sans valeur,
	 * <li>la valeur du param�tre si il est sp�cifi� avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du param�tre.
	 */
	public abstract Object getValue();



	/**
	 * Retourne la valeur du param�tre:
	 * <ul>
	 * <li><code>null</code> si le param�tre est sp�cifi� sans valeur et n'a pas de valeur par d�faut;
	 * <li>la valeur par d�faut si le param�tre est sp�cifi� sans valeur mais a une valeur par d�faut;
	 * <li>la valeur du param�tre si il est sp�cifi� avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du param�tre.
	 */
	public abstract Object getValueOrDefault();



	/**
	 * Teste si le param�tre a une valeur sp�cifi�e.
	 * @return <code>true</code> si le param�tre a une valeur sp�cifi�e, <code>false</code> sinon..
	 */
	public abstract boolean hasValue();



}

