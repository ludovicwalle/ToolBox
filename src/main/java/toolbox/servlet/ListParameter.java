package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link ListParameter} implémente la description d'un paramètre de servlet dont la valeur doit faire partie d'une liste.
 * @author Ludovic WALLE
 */
public class ListParameter extends Parameter {



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 * @param values Valeurs valides.
	 */
	public ListParameter(String name, String description, String... values) {
		this(name, description, prepareValuesAndLabels(values));
	}



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
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
					throw new RuntimeException("La valeur \"" + this.valuesAndLabels[i].getValue() + "\" est utilisée plusieurs fois pour \"" + name + "\".");
				}
				if (this.valuesAndLabels[i].getLabel().equals(this.valuesAndLabels[j].getLabel())) {
					throw new RuntimeException("La verbalisation \"" + this.valuesAndLabels[i].getLabel() + "\" est utilisée plusieurs fois pour \"" + name + "\".");
				}
			}
		}
		this.values = new String[this.valuesAndLabels.length];
		for (int i = 0; i < this.valuesAndLabels.length; i++) {
			values[i] = this.valuesAndLabels[i].getValue();
		}
	}



	/**
	 * Ajoute les informations sur le paramètre à la page d'aide indiquée.
	 * @param page Page d'aide à compléter.
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
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du paramètre \"" + getName() + "\" ne fait pas partie de :");
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
	 * Recherche un paramètre par sa valeur.
	 * @param value Valeur.
	 * @return Le paramètre correspondant à la valeur indiquée si elle existe, <code>null</code> sinon.
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
	 * Le tableau retourné est une copie.
	 * @return Un tableau des valeurs valides.
	 */
	public String[] getPossibleValues() {
		return values.clone();
	}



	/**
	 * Retourne un tableau des {@link ValueAndLabel}s valides.<br>
	 * Le tableau retourné est une copie.
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
	 * Retourne la valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, <code>null</code> sinon.
	 * @param request Données d'entrée de la servlet.
	 * @return La valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, <code>null</code> sinon.
	 */
	public ValueAndLabel getValueAndLabel(HttpServletRequest request) {
		return getPossibleValueAndLabel((String) super.getValue(request));
	}



	/**
	 * Retourne la valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, la valeur indiquée par défaut sinon.
	 * @param request Données d'entrée de la servlet.
	 * @param valueWhenNoParameter Valeur à retourner si le paramètre n'existe pas dans les données d'entrée de la servlet.
	 * @param valueWhenParameterWithoutValue Valeur à retourner si le paramètre existe dans les donnés d'entrée de la servlet mais n'a pas de valeur.
	 * @return La valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, la valeur indiquée par défaut sinon.
	 * @throws RuntimeException Si le paramètre apparaît plusieurs fois.
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
	 * Retourne un tableau contenant les valeurs du paramètre existantes et valides dans les données d'entrée de la servlet. Si une valeur est invalide, l'entrée correspondante dans le tableau sera
	 * <code>null</code>.<br>
	 * Le tableau est vide si le paramètre n'apparait pas, jamais <code>null</code>.
	 * @param request Données d'entrée de la servlet.
	 * @return Un tableau contenant les valeurs du paramètre existantes et valides dans les données d'entrée de la servlet.
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
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre, sans valeur sélectionnée.<br>
	 * Le fragment est sur une seule ligne.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre, sans valeur sélectionnée.
	 */
	public String htmlSelect() {
		return htmlSelect("\t", -1, null);
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre, sans valeur sélectionnée.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne sans indentation.<br>
	 * Le caractère utilisé pour l'indentation est la tabulation.
	 * @param indentationCount Profondeur de l'indentation.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre, sans valeur sélectionnée.
	 */
	public String htmlSelect(int indentationCount) {
		return htmlSelect("\t", indentationCount, null);
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne sans indentation.<br>
	 * Le caractère utilisé pour l'indentation est la tabulation.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur sélectionnée, ou <code>null</code> si aucune valeur n'est sélectionnée.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 */
	public String htmlSelect(int indentationCount, String selected) {
		return htmlSelect("\t", indentationCount, selected);
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre.<br>
	 * Le fragment est sur une seule ligne.
	 * @param selected Valeur sélectionnée, ou <code>null</code> si aucune valeur n'est sélectionnée.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 */
	public String htmlSelect(String selected) {
		return htmlSelect("", -1, selected);
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiquée.<br>
	 * @param indentationString Chaine à utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur sélectionnée, ou <code>null</code> si aucune valeur n'est sélectionnée.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 * @throws RuntimeException Si la valeur sélectionnée ne fait pas partie des valeurs autorisées.
	 */
	public String htmlSelect(String indentationString, int indentationCount, String selected) {
		return htmlSelect(indentationString, indentationCount, selected, null);
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiquée.<br>
	 * @param indentationString Chaine à utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeur sélectionnée, ou <code>null</code> si aucune valeur n'est sélectionnée.
	 * @param options Fragment HTML contenant les options à ajouter à la fin de la balise ouvrante de l'élément select.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 * @throws RuntimeException Si la valeur sélectionnée ne fait pas partie des valeurs autorisées.
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
	 * Prépare un fragment HTML contenant un élément "select" à sélection multiple, correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiquée.<br>
	 * @param indentationCount Profondeur de l'indentation.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 */
	public String htmlSelectMultiple(int indentationCount) {
		return htmlSelectMultiple("\t", indentationCount, new String[0], "");
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" à sélection multiple, correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiquée.<br>
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeurs sélectionnées, ou <code>null</code> si toutes les valeurs sont sélectionnées.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 * @throws RuntimeException Si une des valeurs sélectionnées ne fait pas partie des valeurs autorisées.
	 */
	public String htmlSelectMultiple(int indentationCount, String[] selected) {
		return htmlSelectMultiple("\t", indentationCount, selected, "");
	}



	/**
	 * Prépare un fragment HTML contenant un élément "select" à sélection multiple, correspondant à ce paramètre.<br>
	 * Si la profondeur de l'indentation est strictement négative, le fragment est sur une seule ligne, sinon il est sur plusieurs lignes, avec l'indentation indiquée.<br>
	 * @param indentationString Chaine à utiliser pour l'indentation du fragment HTML.
	 * @param indentationCount Profondeur de l'indentation.
	 * @param selected Valeurs sélectionnées, ou <code>null</code> si toutes les valeurs sont sélectionnées.
	 * @param options Fragment HTML contenant les options à ajouter à la fin de la balise ouvrante de l'élément select.
	 * @return Un fragment HTML contenant un élément "select" correspondant à ce paramètre.
	 * @throws RuntimeException Si une des valeurs sélectionnées ne fait pas partie des valeurs autorisées.
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
	 * Teste si la valeur indiquée est sélectionnée.
	 * @param request Données d'entrée de la servlet.
	 * @param value Valeur à tester.
	 * @return <code>true</code> si la valeur indiquée est sélectionnée, <code>false</code> sinon.
	 * @throws RuntimeException Si la valeur indiquée ne fait pas partie des valeurs autorisées.
	 */
	public boolean isSelected(HttpServletRequest request, String value) {
		ValueAndLabel valueAndLabel;

		if ((valueAndLabel = getPossibleValueAndLabel(value)) == null) {
			throw new RuntimeException("La valeur est invalide pour le paramètre " + getName() + ": " + value);
		}
		return isSelected(request, valueAndLabel);

	}



	/**
	 * Teste si la valeur indiquée est sélectionnée.
	 * @param request Données d'entrée de la servlet.
	 * @param valueAndLabel Valeur à tester.
	 * @return <code>true</code> si la valeur indiquée est sélectionnée, <code>false</code> sinon.
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
	 * Teste si la valeur indiquée fait partie des valeurs indiquées.
	 * @param currentValue Valeur à tester.
	 * @param selectedValues Valeurs sélectionnées, ou <code>null</code> si toutes les valeurs sont sélectionnées.
	 * @return <code>true</code> si la valeur à tester fait partie des valeurs sélectionnées, false sinon.
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
	 * Crée un tableau contenant des valeurs+verbalisations où la verbalisation est la même que la valeur.
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
