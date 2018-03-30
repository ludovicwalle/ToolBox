package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link TextParameter} implémente la description d'un paramètre de servlet dont la valeur est textuelle.
 * @author Ludovic WALLE
 */
public class TextParameter extends Parameter {



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 */
	public TextParameter(String name, String description) {
		super(name, description);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getValue(HttpServletRequest request) {
		return (String) super.getValue(request);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return (String) super.getValue(request, valueWhenNoParameter, valueWhenParameterWithoutValue);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String[] getValues(HttpServletRequest request) {
		String[] values;

		if ((values = request.getParameterValues(getName())) == null) {
			values = new String[0];
		}
		return values;
	}



}
