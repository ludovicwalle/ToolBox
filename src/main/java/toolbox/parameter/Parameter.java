package toolbox.parameter;



/**
 * La classe {@link Parameter} repésente une instance de paramètre.
 * @author Ludovic WALLE
 */
public interface Parameter {



	/**
	 * Retourne la valeur par défaut du paramètre.
	 * @return La valeur par défaut du paramètre.
	 */
	public abstract Object getDefaultValue();



	/**
	 * Retourne le moule du paramètre.
	 * @return Le moule du paramètre.
	 */
	public abstract Mold getMold();



	/**
	 * Retourne le nom du paramètre.
	 * @return Le nom du paramètre.
	 */
	public abstract String getName();



	/**
	 * Retourne la valeur brute du paramètre:
	 * <ul>
	 * <li><code>null</code> si le paramètre est spécifié mais sans valeur,
	 * <li>la valeur telle qu'elle spécifiée si le paramètre est spécifié avec une valeur.
	 * </ul>
	 * @return La valeur brute du paramètre.
	 */
	public abstract String getRawValue();



	/**
	 * Retourne la valeur du paramètre:
	 * <ul>
	 * <li><code>null</code> si le paramètre est spécifié mais sans valeur,
	 * <li>la valeur du paramètre si il est spécifié avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du paramètre.
	 */
	public abstract Object getValue();



	/**
	 * Retourne la valeur du paramètre:
	 * <ul>
	 * <li><code>null</code> si le paramètre est spécifié sans valeur et n'a pas de valeur par défaut;
	 * <li>la valeur par défaut si le paramètre est spécifié sans valeur mais a une valeur par défaut;
	 * <li>la valeur du paramètre si il est spécifié avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du paramètre.
	 */
	public abstract Object getValueOrDefault();



	/**
	 * Teste si le paramètre a une valeur spécifiée.
	 * @return <code>true</code> si le paramètre a une valeur spécifiée, <code>false</code> sinon..
	 */
	public abstract boolean hasValue();



}

