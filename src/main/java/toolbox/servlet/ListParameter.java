package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link ListParameter} impl�mente la description d'un param�tre de servlet dont la valeur doit faire partie d'une liste.
 * @author Ludovic WALLE
 */
public class ListParameter extends Parameter {



	/**
	 * @param name Nom du param�tre.
	 * @param description Description du param�tre.
	 * @param values Valeurs valides.
	 */
	public ListParameter(String name, String description, String... values) {
		this(name, description, prepareValuesAndLabels(values));
	}



	/**
	 * @param name Nom du param�tre.
	 * @param description Description du param�tre.
	 * @param valuesAndLabels Valeurs+verbalisations valides.
	 */
	public ListParameter(String name, String description, ValueAndLabel... valuesAndLabels) {
		super(name, description);

		this.valuesAndLabels = valuesAndLabels.clone();
		if (this.valuesAndLabels.length == 0) {
			throw new RuntimeException("Il n'y a aucune valeur valide pour \"" + name + "\".");
		}
		for (int i = 0; i < this.valuesAndLabels.length; i++) {
			for (int j = i + 1; j < this.valuesAndLabels.length; j++) {
				if (this.valuesAndLabels[i].getValue().equals(this.valuesAndLabels[j].getValue())) {
					throw new RuntimeException("La valeur \"" + this.valuesAndLabels[i].getValue() + "\" est utilis�e plusieurs fois pour \"" + name + "\".");
				}
				if (this.valuesAndLabels[i].getLabel().equals(this.valuesAndLabels[j].getLabel())) {
					throw new RuntimeException("La verbalisation \"" + this.valuesAndLabels[i].getLabel() + "\" est utilis�e plusieurs fois pour \"" + name + "\".");
				}
			}
		}
		this.values = new String[this.valuesAndLabels.length];
		for (int i = 0; i < this.valuesAndLabels.length; i++) {
			values[i] = this.valuesAndLabels[i].getValue();
		}
	}



	/**
	 * Ajoute les informations sur le param�tre � la page d'aide indiqu�e.
	 * @param page Page d'aide � compl�ter.
	 */
	@Override public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription());
		page.appendListStart();
		for (ValueAndLabel valueAndLabel : getPossibleValuesAndLabels()) {
			if (valueAndLabel.getValue().equals(valueAndLabel.getLabel())) {
				page.appendItem(valueAndLabel.getValue());
			} else {
				page.appendItem(valueAndLabel.getValue(), valueAndLabel.getLabel());
			}
		}
		page.appendListStop();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du param�tre \"" + getName() + "\" ne fait pas partie de :");
		page.appendListStart();
		for (ValueAndLabel valueAndLabel : valuesAndLabels) {
			if (valueAndLabel.getValue().equals(valueAndLabel.getLabel())) {
				page.appendItem(valueAndLabel.getValue());
			} else {
				page.appendItem(valueAndLabel.getValue(), valueAndLabel.getLabel());
			}
		}
		page.appendListStop();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean check(String parameterValue) {
		for (String value : values) {
			if (parameterValue.equals(value)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Recherche un param�tre par sa valeur.
	 * @param value Valeur.
	 * @return Le param�tre correspondant � la valeur indiqu�e si elle existe, <code>null</code> sinon.
	 */
	public ValueAndLabel getPossibleValueAndLabel(String value) {
		for (ValueAndLabel valueAndLabel : valuesAndLabels) {
			if (valueAndLabel.getValue().equals(value)) {
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
	 * Retourne un tableau des {@link ValueAndLabel}s valides.<br>
	 * Le tableau retourn� est une copie.
	 * @return Un tableau des {@link ValueAndLabel}s valides.
	 */
	public ValueAndLabel[] getPossibleValuesAndLabels() {
		return valuesAndLabels.clone();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getValue(HttpServletRequest request) {
		String value;

		value = (String) super.getValue(request);
		if (getPossibleValueAndLabel(value) == null) {
			return null;
		} else {
			return value;
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return (String) super.getValue(request, valueWhenNoParameter, valueWhenParameterWithoutValue);
	}



	/**
	 * Retourne la valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, <code>null</code> sinon.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return La valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, <code>null</code> sinon.
	 */
	public ValueAndLabel getValueAndLabel(HttpServletRequest request) {
		return getPossibleValueAndLabel((String) super.getValue(request));
	}



	/**
	 * Retourne la valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, la valeur indiqu�e par d�faut sinon.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param valueWhenNoParameter Valeur � retourner si le param�tre n'existe pas dans les donn�es d'entr�e de la servlet.
	 * @param valueWhenParameterWithoutValue Valeur � retourner si le param�tre existe dans les donn�s d'entr�e de la servlet mais n'a pas de valeur.
	 * @return La valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, la valeur indiqu�e par d�faut sinon.
	 * @throws RuntimeException Si le param�tre appara�t plusieurs fois.
	 */
	public ValueAndLabel getValueAndLabel(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return getPossibleValueAndLabel(getValue(request, ((ValueAndLabel) valueWhenNoParameter).getValue(), ((ValueAndLabel) valueWhenParameterWithoutValue).getValue()));
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String[] getValues(HttpServletRequest request) {
		@SuppressWarnings("hiding") String[] values;

		if ((values = request.getParameterValues(getName())) == null) {
			return new String[0];
		} else {
			values = values.clone();
			for (int i = 0; i < values.length; i++) {
				if (getPossibleValueAndLabel(values[i]) == null) {
					values[i] = null;
				}
			}
			return values;
		}
	}



	/**
	 * Retourne un tableau contenant les valeurs du param�tre existantes et valides dans les donn�es d'entr�e de la servlet. Si une valeur est invalide, l'entr�e correspondante dans le tableau sera
	 * <code>null</code>.<br>
	 * Le tableau est vide si le param�tre n'apparait pas, jamais <code>null</code>.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return Un tableau contenant les valeurs du param�tre existantes et valides dans les donn�es d'entr�e de la servlet.
	 */
	public ValueAndLabel[] getValuesAndLabels(HttpServletRequest request) {
		@SuppressWarnings("hiding") String[] values;
		@SuppressWarnings("hiding") ValueAndLabel[] valuesAndLabels;

		values = getValues(request);
		valuesAndLabels = new ValueAndLabel[values.length];
		for (int i = 0; i < values.length; i++) {
			valuesAndLabels[i] = getPossibleValueAndLabel(values[i]);
		}
		return valuesAndLabels;
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre, sans valeur s�lectionn�e.<br>
	 * Le fragment est sur une seule ligne.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre, sans valeur s�lectionn�e.
	 */
	public String htmlSelect() {
		return htmlSelect("\t", -1, null);
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre, sans valeur s�lectionn�e.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne sans indentation.<br>
	 * Le caract�re utilis� pour l'indentation est la tabulation.
	 * @param indentationCount Profondeur de l'indentation.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre, sans valeur s�lectionn�e.
	 */
	public String htmlSelect(int indentationCount) {
		return htmlSelect("\t", indentationCount, null);
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne sans indentation.<br>
	 * Le caract�re utilis� pour l'indentation est la tabulation.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur s�lectionn�e, ou <code>null</code> si aucune valeur n'est s�lectionn�e.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 */
	public String htmlSelect(int indentationCount, String selected) {
		return htmlSelect("\t", indentationCount, selected);
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.<br>
	 * Le fragment est sur une seule ligne.
	 * @param selected Valeur s�lectionn�e, ou <code>null</code> si aucune valeur n'est s�lectionn�e.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 */
	public String htmlSelect(String selected) {
		return htmlSelect("", -1, selected);
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiqu�e.<br>
	 * @param indentationString Chaine � utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur s�lectionn�e, ou <code>null</code> si aucune valeur n'est s�lectionn�e.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 * @throws RuntimeException Si la valeur s�lectionn�e ne fait pas partie des valeurs autoris�es.
	 */
	public String htmlSelect(String indentationString, int indentationCount, String selected) {
		return htmlSelect(indentationString, indentationCount, selected, null);
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiqu�e.<br>
	 * @param indentationString Chaine � utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur s�lectionn�e, ou <code>null</code> si aucune valeur n'est s�lectionn�e.
	 * @param options Fragment HTML contenant les options � ajouter � la fin de la balise ouvrante de l'�l�ment select.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 * @throws RuntimeException Si la valeur s�lectionn�e ne fait pas partie des valeurs autoris�es.
	 */
	public String htmlSelect(String indentationString, int indentationCount, String selected, String options) {
		StringBuilder builder = new StringBuilder();
		String indentation;
		boolean found = (selected == null);
		String endOfLine = (indentationCount >= 0) ? "\n" : "";

		for (int i = 0; i < indentationCount; i++) {
			builder.append(indentationString);
		}
		indentation = builder.toString();

		builder.append("<select name=\"" + getName() + "\"" + (options == null ? "" : options) + ">" + endOfLine);
		for (ValueAndLabel valuesAndLabel : valuesAndLabels) {
			builder.append(indentation + indentationString + "<option value=\"" + CustomizedServlet.encodeForHtml(valuesAndLabel.getValue()) + "\"");
			if (valuesAndLabel.getValue().equals(selected)) {
				builder.append(" selected=\"selected\"");
				found = true;
			}
			builder.append(">" + CustomizedServlet.encodeForHtml(valuesAndLabel.getLabel()) + "</option>" + endOfLine);
		}
		builder.append(indentation + "</select>" + endOfLine);
		if ((selected != null) && !found) {
			throw new RuntimeException("La valeur \"" + selected + "\" ne fait pas partie des valeurs possibles pour \"" + getName() + "\".");
		}
		return builder.toString();
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" � s�lection multiple, correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiqu�e.<br>
	 * @param indentationCount Profondeur de l'indentation.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 */
	public String htmlSelectMultiple(int indentationCount) {
		return htmlSelectMultiple("\t", indentationCount, new String[0], "");
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" � s�lection multiple, correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiqu�e.<br>
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeurs s�lectionn�es, ou <code>null</code> si toutes les valeurs sont s�lectionn�es.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 * @throws RuntimeException Si une des valeurs s�lectionn�es ne fait pas partie des valeurs autoris�es.
	 */
	public String htmlSelectMultiple(int indentationCount, String[] selected) {
		return htmlSelectMultiple("\t", indentationCount, selected, "");
	}



	/**
	 * Pr�pare un fragment HTML contenant un �l�ment "select" � s�lection multiple, correspondant � ce param�tre.<br>
	 * Si la profondeur de l'indentation est strictement n�gative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiqu�e.<br>
	 * @param indentationString Chaine � utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeurs s�lectionn�es, ou <code>null</code> si toutes les valeurs sont s�lectionn�es.
	 * @param options Fragment HTML contenant les options � ajouter � la fin de la balise ouvrante de l'�l�ment select.
	 * @return Un fragment HTML contenant un �l�ment "select" correspondant � ce param�tre.
	 * @throws RuntimeException Si une des valeurs s�lectionn�es ne fait pas partie des valeurs autoris�es.
	 */
	public String htmlSelectMultiple(String indentationString, int indentationCount, String[] selected, String options) {
		StringBuilder builder = new StringBuilder();
		String indentation;
		int foundCount = 0;
		boolean found;
		String endOfLine = (indentationCount >= 0) ? "\n" : "";

		for (int i = 0; i < indentationCount; i++) {
			builder.append(indentationString);
		}
		indentation = builder.toString();

		builder.append("<select name=\"" + getName() + "\" multiple=\"multiple\"" + (options == null ? "" : options) + ">" + endOfLine);
		for (ValueAndLabel valuesAndLabel : valuesAndLabels) {
			builder.append(indentation + indentationString + "<option value=\"" + CustomizedServlet.encodeForHtml(valuesAndLabel.getValue()) + "\"");
			if (isSelected(valuesAndLabel.getValue(), selected)) {
				builder.append(" selected=\"selected\"");
				foundCount++;
			}
			builder.append(">" + CustomizedServlet.encodeForHtml(valuesAndLabel.getLabel()) + "</option>" + endOfLine);
		}
		builder.append(indentation + "</select>" + endOfLine);
		if ((selected != null) && (foundCount != selected.length)) {
			for (String selectedString : selected) {
				found = false;
				for (String value : values) {
					if (selectedString.equals(value)) {
						found = true;
						break;
					}
				}
				if (!found) {
					throw new RuntimeException("La valeur \"" + selectedString + "\" ne fait pas partie des valeurs possibles pour \"" + getName() + "\".");
				}
			}
		}
		return builder.toString();
	}



	/**
	 * Teste si la valeur indiqu�e est s�lectionn�e.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param value Valeur � tester.
	 * @return <code>true</code> si la valeur indiqu�e est s�lectionn�e, <code>false</code> sinon.
	 * @throws RuntimeException Si la valeur indiqu�e ne fait pas partie des valeurs autoris�es.
	 */
	public boolean isSelected(HttpServletRequest request, String value) {
		ValueAndLabel valueAndLabel;

		if ((valueAndLabel = getPossibleValueAndLabel(value)) == null) {
			throw new RuntimeException("La valeur est invalide pour le param�tre " + getName() + ": " + value);
		}
		return isSelected(request, valueAndLabel);

	}



	/**
	 * Teste si la valeur indiqu�e est s�lectionn�e.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param valueAndLabel Valeur � tester.
	 * @return <code>true</code> si la valeur indiqu�e est s�lectionn�e, <code>false</code> sinon.
	 */
	public boolean isSelected(HttpServletRequest request, ValueAndLabel valueAndLabel) {
		String[] selectedValues = request.getParameterValues(getName());

		if (selectedValues == null) {
			return false;
		}
		for (String selectedValue : selectedValues) {
			if (valueAndLabel.getValue().equals(selectedValue)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Teste si la valeur indiqu�e fait partie des valeurs indiqu�es.
	 * @param currentValue Valeur � tester.
	 * @param selectedValues Valeurs s�lectionn�es, ou <code>null</code> si toutes les valeurs sont s�lectionn�es.
	 * @return <code>true</code> si la valeur � tester fait partie des valeurs s�lectionn�es, false sinon.
	 */
	private static boolean isSelected(String currentValue, String[] selectedValues) {
		if (selectedValues == null) {
			return true;
		}
		for (String selectedValue : selectedValues) {
			if (currentValue.equals(selectedValue)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Cr�e un tableau contenant des valeurs+verbalisations o� la verbalisation est la m�me que la valeur.
	 * @param values Valeurs
	 * @return Retourne un tableau contenant les valeurs+verbalisations.
	 */
	private static ValueAndLabel[] prepareValuesAndLabels(String... values) {
		ValueAndLabel[] valuesAndLabels = new ValueAndLabel[values.length];
		for (int i = 0; i < values.length; i++) {
			valuesAndLabels[i] = new ValueAndLabel(values[i]);
		}
		return valuesAndLabels;
	}



	/**
	 * Tableau des valeurs.
	 */
	private final String[] values;



	/**
	 * Tableau des valeurs+verbalisations.
	 */
	private final ValueAndLabel[] valuesAndLabels;



}
