package toolbox.parameter;

/**
 * La classe {@link AbstractMold} représente un moule de paramètre, regroupant des informations telles que son nom, sa description, le type de la valeur, ...<br>
 * Un moule de paramètre ne contient pas de valeur.<br>
 * Le nom du paramètre doit être constitué uniquement des caractères <code>{@value #NAMES_CHARS}</code> ou être vide (paramètre anonyme).
 * @author Ludovic WALLE
 * @param <O> Classe de la valeur du paramètre.
 */
public abstract class AbstractMold<O extends Object> implements Mold {



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut (peut être <code>null</code>).
	 */
	public AbstractMold(String name, String description, boolean valueRequired, String defaultValue) {
		if (!name.matches("[" + NAMES_CHARS + "]*")) {
			throw new RuntimeException("La syntaxe du nom de paramètre est invalide: " + name);
		}
		this.name = name;
		this.description = description;
		this.valueRequired = valueRequired;
		try {
			this.defaultValue = computeValue(defaultValue);
		} catch (ParameterException_InvalidValue exception) {
			throw new RuntimeException("La valeur par défaut est invalide.", exception);
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final O computeValue(String rawValue) throws ParameterException_InvalidValue {
		if ((rawValue == null) || rawValue.isEmpty()) {
			return null;
		} else {
			return delegateComputeValue(rawValue);
		}
	}



	/**
	 * Calcule la valeur correspondant à la valeur brute indiquée.
	 * @param rawValue Valeur brute (ne doit pas être <code>null</code> ni vide).
	 * @return La valeur de l'objet.
	 * @throws ParameterException_InvalidValue Si la valeur est invalide.
	 */
	protected abstract O delegateComputeValue(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Retourne la description de la valeur.<br>
	 * La description devrait commencer par une majuscule, ne pas tenir compte de l'obligation de spécifier une valeur, et ne pas se terminer par un point.
	 * @return La description de la valeur.
	 */
	protected abstract String delegateGetValueDescription();



	/**
	 * Teste si l'objet indiqué est égal à cet objet.<br>
	 * Il sont égaux si leurs noms sont égaux.
	 * @param object Objet à comparer.
	 * @return <code>true</code> si les objets sont égaux, <code>false</code> sinon.
	 */
	@Override public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked") AbstractMold<O> other = (AbstractMold<O>) object;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final O getDefaultValue() {
		return defaultValue;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final String getDescription() {
		return description;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final String getName() {
		return name;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final String getValueDescription() {
		return delegateGetValueDescription() + (valueRequired ? "" : " (peut être omis)") + ".";
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		return name.hashCode();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final boolean isValid(String rawValue) {
		try {
			delegateComputeValue(rawValue);
			return true;
		} catch (ParameterException_InvalidValue exception) {
			return false;
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public final boolean valueIsRequired() {
		return valueRequired;
	}



	/**
	 * Valeur par défaut, ou <code>null</code> si il n'y en a pas.<br>
	 */
	private final O defaultValue;



	/**
	 * Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 */
	private final String description;



	/**
	 * Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 */
	private final String name;



	/**
	 * Indicateur valeur obligatoire.
	 */
	private final boolean valueRequired;



}
