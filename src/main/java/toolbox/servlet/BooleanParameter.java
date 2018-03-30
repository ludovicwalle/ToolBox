package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link BooleanParameter} implémente la description d'un paramètre de servlet dont la valeur est booléenne.
 * @author Ludovic WALLE
 */
public class BooleanParameter extends Parameter {



	/**
	 * Paramètre booléen avec une valeur par défaut à faux.
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 */
	public BooleanParameter(String name, String description) {
		this(name, description, Boolean.FALSE);
	}



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 * @param defaultValue Valeur par défaut.
	 */
	public BooleanParameter(String name, String description, boolean defaultValue) {
		super(name, description);
		this.defaultValue = defaultValue;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription(), "Seule la présence du paramètre compte (présent = vrai, absent = faux), la valeur est ignorée.");
	}



	/**
	 * Retourne la valeur si le paramètre n'est pas défini dans les données d'entrée.
	 * @return La valeur si le paramètre n'est pas défini dans les données d'entrée.
	 */
	public boolean getDefaultValue() {
		return defaultValue;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Boolean getValue(HttpServletRequest request) {
		return super.getValue(request) != null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Boolean getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return (Boolean) super.getValue(request, valueWhenNoParameter, valueWhenParameterWithoutValue);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Boolean getValue(String parameterValue) {
		return parameterValue != null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Boolean[] getValues(HttpServletRequest request) {
		String[] strings;
		Boolean[] booleans;

		strings = request.getParameterValues(getName());
		if (strings == null) {
			booleans = new Boolean[0];
		} else {
			booleans = new Boolean[strings.length];
			for (int i = 0; i < strings.length; i++) {
				check(strings[i]);
				booleans[i] = (getValue(strings[i]) != null);
			}
		}
		return booleans;
	}



	/**
	 * Valeur si le paramètre n'est pas défini dans les données d'entrée.
	 */
	private final Boolean defaultValue;



}
