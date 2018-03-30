package toolbox.parameter;


import toolbox.*;



/**
 * La classe {@link ListMold} impl�mente le mod�le d'un param�tre dont la valeur doit faire partie d'une liste.
 * @author Ludovic WALLE
 */
public class ListMold extends AbstractMold<String> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 * @param valuesAndLabels Valeurs et verbalisations valides.
	 */
	public ListMold(String name, String description, boolean valueRequired, String defaultValue, NamedString... valuesAndLabels) {
		super(name, description, valueRequired, defaultValue);

		this.valuesAndLabels = valuesAndLabels.clone();
		if (this.valuesAndLabels.length == 0) {
			throw new RuntimeException("Il n'y a aucune valeur valide pour \"" + name + "\".");
		}
		for (int i = 0; i < this.valuesAndLabels.length; i++) {
			for (int j = i + 1; j < this.valuesAndLabels.length; j++) {
				if (this.valuesAndLabels[i].getName().equals(this.valuesAndLabels[j].getName())) {
					throw new RuntimeException("La valeur \"" + this.valuesAndLabels[i].getName() + "\" est utilis�e plusieurs fois pour \"" + name + "\".");
				}
				if (this.valuesAndLabels[i].getString().equals(this.valuesAndLabels[j].getString())) {
					throw new RuntimeException("La verbalisation \"" + this.valuesAndLabels[i].getString() + "\" est utilis�e plusieurs fois pour \"" + name + "\".");
				}
			}
		}
		this.values = new String[this.valuesAndLabels.length];
		for (int i = 0; i < this.valuesAndLabels.length; i++) {
			values[i] = this.valuesAndLabels[i].getName();
		}
	}



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 * @param values Valeurs valides.
	 */
	public ListMold(String name, String description, boolean valueRequired, String defaultValue, String... values) {
		this(name, description, valueRequired, defaultValue, prepareValuesAndLabels(values));
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateComputeValue(java.lang.String rawValue) throws ParameterException_InvalidValue {
		for (NamedString valueAndLabel : valuesAndLabels) {
			if (valueAndLabel.getName().equals(rawValue)) {
				return null;
			}
		}
		throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du param�tre \"" + getName() + "\" ne fait pas partie des valeurs autoris�es.");
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "Une des valeurs autoris�es";
	}



	/**
	 * Recherche un param�tre par sa valeur.
	 * @param value Valeur.
	 * @return Le param�tre correspondant � la valeur indiqu�e si elle existe, <code>null</code> sinon.
	 */
	public NamedString getPossibleValueAndLabel(String value) {
		for (NamedString valueAndLabel : valuesAndLabels) {
			if (valueAndLabel.getName().equals(value)) {
				return valueAndLabel;
			}
		}
		return null;
	}



	/**
	 * Retourne un tableau des valeurs valides.<br>
	 * Le tableau retourn� est une copie.
	 * @return Un tableau des valeurs valides.
	 */
	public String[] getPossibleValues() {
		return values.clone();
	}



	/**
	 * Retourne un tableau des valeurs et verbalisations valides.<br>
	 * Le tableau retourn� est une copie.
	 * @return Un tableau des valeurs et verbalisations valides.
	 */
	public NamedString[] getPossibleValuesAndLabels() {
		return valuesAndLabels.clone();
	}



	/** {@inheritDoc} */
	@Override public ListParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new ListParameter(this, rawValue);
	}



	/**
	 * Cr�e un tableau contenant des valeurs et verbalisations o� la verbalisation est la m�me que la valeur.
	 * @param values Valeurs
	 * @return Retourne un tableau contenant les valeurs et verbalisations.
	 */
	private static NamedString[] prepareValuesAndLabels(String... values) {
		NamedString[] valuesAndLabels = new NamedString[values.length];
		for (int i = 0; i < values.length; i++) {
			valuesAndLabels[i] = new NamedString(values[i], null);
		}
		return valuesAndLabels;
	}



	/**
	 * Tableau des valeurs autoris�es.
	 */
	private final String[] values;



	/**
	 * Tableau des valeurs et verbalisations autoris�es.
	 */
	private final NamedString[] valuesAndLabels;



}
