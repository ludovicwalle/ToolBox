package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link DoubleParameter} implémente la description d'un paramètre de servlet dont la valeur est un double.
 * @author Ludovic WALLE
 */
public class DoubleParameter extends Parameter {



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 * @param canBeNull Indicateur de valeur nulle autorisée.
	 */
	public DoubleParameter(String name, String description, boolean canBeNull) {
		super(name, description);
		this.canBeNull = canBeNull;
	}



	/**
	 * Ajoute les informations sur le paramètre à la page d'aide indiquée.
	 * @param page Page d'aide à compléter.
	 */
	@Override public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du paramètre \"" + getName() + "\" n'est pas un double.");
	}



	/**
	 * Retourne l'indicateur de valeur nulle autorisée.
	 * @return L'indicateur de valeur nulle autorisée.
	 */
	public boolean canBeNull() {
		return canBeNull;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean check(String parameterValue) {
		try {
			if (canBeNull && parameterValue.equals("")) {
				return true;
			} else if (parameterValue.matches("\\A(?:(?:0)|(?:-?[1-9][0-9]*)|(?:-?(?:0|[1-9][0-9]*)\\.[0-9]+))\\z")) {
				Double.parseDouble(parameterValue);
				return true;
			}
		} catch (NumberFormatException exception) {
		}
		return false;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Double getValue(HttpServletRequest request) {
		return (Double) super.getValue(request);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Double getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return (Double) super.getValue(request, valueWhenNoParameter, valueWhenParameterWithoutValue);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Double getValue(String parameterValue) {
		try {
			return (parameterValue == null) ? null : Double.parseDouble(parameterValue);
		} catch (NumberFormatException exception) {
		}
		return null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Double[] getValues(HttpServletRequest request) {
		String[] strings;
		Double[] doubles;

		strings = request.getParameterValues(getName());
		if (strings == null) {
			doubles = new Double[0];
		} else {
			doubles = new Double[strings.length];
			for (int i = 0; i < strings.length; i++) {
				check(strings[i]);
				doubles[i] = getValue(strings[i]);
			}
		}
		return doubles;
	}



	/**
	 * Indicateur de valeur nulle autorisée.
	 */
	private final boolean canBeNull;



}
