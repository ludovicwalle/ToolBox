package toolbox.parameter;

/**
 * La classe {@link AbstractMold} repr�sente un moule de param�tre, regroupant des informations telles que son nom, sa description, le type de la valeur, ...<br>
 * Un moule de param�tre ne contient pas de valeur.<br>
 * Le nom du param�tre doit �tre constitu� uniquement des caract�res <code>{@value #NAMES_CHARS}</code> ou �tre vide (param�tre anonyme).
 * @author Ludovic WALLE
 * @param <O> Classe de la valeur du param�tre.
 */
public abstract class AbstractMold<O extends Object> implements Mold {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut (peut �tre <code>null</code>).
	 */
	public AbstractMold(String name, String description, boolean valueRequired, String defaultValue) {
		if (!name.matches("[" + NAMES_CHARS + "]*")) {
			throw new RuntimeException("La syntaxe du nom de param�tre est invalide: " + name);
		}
		this.name = name;
		this.description = description;
		this.valueRequired = valueRequired;
		try {
			this.defaultValue = computeValue(defaultValue);
		} catch (ParameterException_InvalidValue exception) {
			throw new RuntimeException("La valeur par d�faut est invalide.", exception);
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
	 * Calcule la valeur correspondant � la valeur brute indiqu�e.
	 * @param rawValue Valeur brute (ne doit pas �tre <code>null</code> ni vide).
	 * @return La valeur de l'objet.
	 * @throws ParameterException_InvalidValue Si la valeur est invalide.
	 */
	protected abstract O delegateComputeValue(String rawValue) throws ParameterException_InvalidValue;



	/**
	 * Retourne la description de la valeur.<br>
	 * La description devrait commencer par une majuscule, ne pas tenir compte de l'obligation de sp�cifier une valeur, et ne pas se terminer par un point.
	 * @return La description de la valeur.
	 */
	protected abstract String delegateGetValueDescription();



	/**
	 * Teste si l'objet indiqu� est �gal � cet objet.<br>
	 * Il sont �gaux si leurs noms sont �gaux.
	 * @param object Objet � comparer.
	 * @return <code>true</code> si les objets sont �gaux, <code>false</code> sinon.
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
		return delegateGetValueDescription() + (valueRequired ? "" : " (peut �tre omis)") + ".";
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
	 * Valeur par d�faut, ou <code>null</code> si il n'y en a pas.<br>
	 */
	private final O defaultValue;



	/**
	 * Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 */
	private final String description;



	/**
	 * Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 */
	private final String name;



	/**
	 * Indicateur valeur obligatoire.
	 */
	private final boolean valueRequired;



}
