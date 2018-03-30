package toolbox.parameter;

/**
 * La classe {@link AbstractParameter} rep�sente une instance de param�tre.
 * @author Ludovic WALLE
 * @param <O> Classe de la valeur du param�tre.
 * @param <M> Moule du param�tre.
 */
public abstract class AbstractParameter<O extends Object, M extends AbstractMold<O>> implements Parameter {



	/**
	 * @param mold Moule du param�tre.
	 * @param rawValue Valeur brute du param�tre, telle qu'elle est sp�cifi�e (peut �tre <code>null</code>).
	 * @throws ParameterException_InvalidValue
	 */
	public AbstractParameter(M mold, String rawValue) throws ParameterException_InvalidValue {
		if (((rawValue == null) || rawValue.isEmpty()) && mold.valueIsRequired()) {
			throw new ParameterException_InvalidValue(mold, rawValue, "La valeur du param�tre \"" + getName() + "\" est obligatoire mais n'est pas sp�cifi�e.");
		}
		this.value = mold.computeValue(rawValue);
		this.mold = mold;
		this.rawValue = rawValue;
	}



	/**
	 * Retourne la valeur par d�faut du param�tre.
	 * @return La valeur par d�faut du param�tre.
	 */
	@Override public final O getDefaultValue() {
		return mold.getDefaultValue();
	}



	/**
	 * Retourne le moule du param�tre.
	 * @return Le moule du param�tre.
	 */
	@Override public final M getMold() {
		return mold;
	}



	/**
	 * Retourne le nom du param�tre.
	 * @return Le nom du param�tre.
	 */
	@Override public final String getName() {
		return mold.getName();
	}



	/**
	 * Retourne la valeur brute du param�tre:
	 * <ul>
	 * <li><code>null</code> si le param�tre est sp�cifi� mais sans valeur,
	 * <li>la valeur telle qu'elle sp�cifi�e si le param�tre est sp�cifi� avec une valeur.
	 * </ul>
	 * @return La valeur brute du param�tre.
	 */
	@Override public final String getRawValue() {
		return rawValue;
	}



	/**
	 * Retourne la valeur du param�tre:
	 * <ul>
	 * <li><code>null</code> si le param�tre est sp�cifi� mais sans valeur,
	 * <li>la valeur du param�tre si il est sp�cifi� avec une valeur.
	 * </ul>
	 * .
	 * @return La valeur du param�tre.
	 */
	@Override public final O getValue() {
		return value;
	}



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
	@Override public final O getValueOrDefault() {
		if (hasValue()) {
			return getValue();
		} else {
			return getDefaultValue();
		}
	}



	/**
	 * Teste si le param�tre a une valeur sp�cifi�e.
	 * @return <code>true</code> si le param�tre a une valeur sp�cifi�e, <code>false</code> sinon..
	 */
	@Override public final boolean hasValue() {
		return rawValue != null;
	}



	/**
	 * Moule associ� au param�tre (jamais <code>null</code>).
	 */
	private final M mold;



	/**
	 * Valeur brute. Elle est:
	 * <ul>
	 * <li><code>null</code> si elle n'est pas sp�cifi�e;
	 * <li>vide si elle est sp�cifi�e mais n'a pas de valeur;
	 * <li>telle qu'elle sp�cifi�e si elle est sp�cifi�e et a une valeur.
	 * </ul>
	 */
	private final String rawValue;



	/**
	 * Valeur du param�tre.
	 */
	private final O value;



}
