package toolbox.servlet;

import java.util.regex.*;

import javax.servlet.http.*;



/**
 * La classe {@link PatternParameter} impl�mente la description d'un param�tre de servlet dont la valeur doit respecter un mod�le de syntaxe.
 * @author Ludovic WALLE
 */
public class PatternParameter extends Parameter {



	/**
	 * @param name Nom du param�tre.
	 * @param description Description du param�tre.
	 * @param patternString Chaine d�crivant le mod�le de la valeur.
	 */
	public PatternParameter(String name, String description, String patternString) {
		super(name, description);
		this.pattern = Pattern.compile(patternString);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription(), "La valeur doit respecter le mod�le de syntaxe: " + pattern.pattern());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du param�tre \"" + getName() + "\" ne respecte pas le mod�le de syntaxe :" + pattern.pattern());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean check(String parameterValue) {
		if (pattern.matcher(parameterValue).matches()) {
			return true;
		}
		return false;
	}



	/**
	 * Retourne la chaine d�crivant le mod�le de la valeur.
	 * @return La chaine d�crivant le mod�le de la valeur.
	 */
	public String getPatternString() {
		return pattern.toString();
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



	/**
	 * Mod�le de la valeur.
	 */
	private final Pattern pattern;



}
