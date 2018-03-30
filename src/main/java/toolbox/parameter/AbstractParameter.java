package toolbox.parameter;

/**
 * La classe {@link AbstractParameter} repésente une instance de paramètre.
 * @author Ludovic WALLE
 * @param <O> Classe de la valeur du paramètre.
 * @param <M> Moule du paramètre.
 */
public abstract class AbstractParameter<O extends Object, M extends AbstractMold<O>> implements Parameter {



	/**
	 * @param mold Moule du paramètre.
	 * @param rawValue Valeur brute du paramètre, telle qu'elle est spécifiée (peut être <code>null</code>).
	 * @throws ParameterException_InvalidValue
	 */
	public AbstractParameter(M mold, String rawValue) throws ParameterException_InvalidValue {
		if (((rawValue == null) || rawValue.isEmpty()) && mold.valueIsRequired()) {
			throw new ParameterException_InvalidValue(mold, rawValue, "La valeur du paramètre \"" + getName() + "\" est obligatoire mais n'est pas spécifiée.");
		}
		this.value = mold.computeValue(rawValue);
		this.mold = mold;
		this.rawValue = rawValue;
	}



	/**
	 * Retourne la valeur par défaut du paramètre.
	 * @return La valeur par défaut du paramètre.
	 */
	@Override public final O getDefaultValue() {
		return mold.getDefaultValue();
	}



	/**
	 * Retourne le moule du paramètre.
	 * @return Le moule du paramètre.
	 */
	@Override public final M getMold() {
		return mold;
	}



	/**
	 * Retourne le nom du paramètre.
	 * @return Le nom du paramètre.
	 */
	@Override public final String getName() {
		return mold.getName();
	}



	/**
	 * Retourne la valeur brute du paramètre:
	 * <ul>
	 * <li><code>null</code> si le paramètre est spécifié mais sans valeur,
	 * <li>la valeur telle qu'elle spécifiée si le paramètre est spécifié avec une valeur.
	 * </ul>
	 * @return La valeur brute du paramètre.
	 */
	@Override public final String getRawValue() {
		return rawValue;
	}



	/**
	 * Retourne la valeur du paramètre:
	 * <ul>
	 * <li><code>null</code> si le paramètre est spécifié mais sans valeur,
	 * <li>la valeur du paramètre si il est spécifié avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du paramètre.
	 */
	@Override public final O getValue() {
		return value;
	}



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
	@Override public final O getValueOrDefault() {
		if (hasValue()) {
			return getValue();
		} else {
			return getDefaultValue();
		}
	}



	/**
	 * Teste si le paramètre a une valeur spécifiée.
	 * @return <code>true</code> si le paramètre a une valeur spécifiée, <code>false</code> sinon..
	 */
	@Override public final boolean hasValue() {
		return rawValue != null;
	}



	/**
	 * Moule associé au paramètre (jamais <code>null</code>).
	 */
	private final M mold;



	/**
	 * Valeur brute. Elle est:
	 * <ul>
	 * <li><code>null</code> si elle n'est pas spécifiée;
	 * <li>vide si elle est spécifiée mais n'a pas de valeur;
	 * <li>telle qu'elle spécifiée si elle est spécifiée et a une valeur.
	 * </ul>
	 */
	private final String rawValue;



	/**
	 * Valeur du paramètre.
	 */
	private final O value;



}
