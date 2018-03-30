package toolbox.servlet;

import javax.servlet.http.*;



/**
 * La classe {@link IntegerParameter} implémente la description d'un paramètre de servlet dont la valeur est un entier.
 * @author Ludovic WALLE
 */
public class IntegerParameter extends Parameter {



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 * @param canBeNull Indicateur de valeur nulle autorisée.
	 */
	public IntegerParameter(String name, String description, boolean canBeNull) {
		this(name, description, canBeNull, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 * @param canBeNull Indicateur de valeur nulle autorisée.
	 * @param min Valeur minimale autorisée.
	 * @param max Valeur maximale autorisée.
	 */
	public IntegerParameter(String name, String description, boolean canBeNull, int min, int max) {
		super(name, description);
		this.canBeNull = canBeNull;
		if (min > max) {
			throw new RuntimeException("La valeur minimale " + min + " est strictement supérieure à la valeur maximale " + name + "\".");
		}
		this.min = min;
		this.max = max;
	}



	/**
	 * Ajoute les informations sur le paramètre à la page d'aide indiquée.
	 * @param page Page d'aide à compléter.
	 */
	@Override public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription(), validRange());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du paramètre \"" + getName() + "\" n'est pas un entier compris entre " + min + " et " + max + ".");
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
		int value;

		try {
			if (canBeNull && parameterValue.equals("")) {
				return true;
			} else if (parameterValue.matches("\\A(?:0|-?[1-9][0-9]*)\\z")) {
				value = Integer.parseInt(parameterValue);
				return (min <= value) && (value <= max);
			}
		} catch (NumberFormatException exception) {
		}
		return false;
	}



	/**
	 * Retourne la valeur maximale autorisée.
	 * @return La valeur maximale autorisée.
	 */
	public int getMax() {
		return max;
	}



	/**
	 * Retourne la valeur minimale autorisée.
	 * @return La valeur minimale autorisée.
	 */
	public int getMin() {
		return min;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Integer getValue(HttpServletRequest request) {
		return (Integer) super.getValue(request);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Integer getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) {
		return (Integer) super.getValue(request, valueWhenNoParameter, valueWhenParameterWithoutValue);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Integer getValue(String parameterValue) {
		int value;

		try {
			value = Integer.parseInt(parameterValue);
			if ((value >= min) && (value <= max)) {
				return Integer.valueOf(value);
			}
		} catch (NumberFormatException exception) {
		}
		return null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Integer[] getValues(HttpServletRequest request) {
		String[] strings;
		Integer[] integers;

		strings = request.getParameterValues(getName());
		if (strings == null) {
			integers = new Integer[0];
		} else {
			integers = new Integer[strings.length];
			for (int i = 0; i < strings.length; i++) {
				check(strings[i]);
				integers[i] = getValue(strings[i]);
			}
		}
		return integers;
	}



	/**
	 * Retourne une chaine décrivant la plage des valeurs valides.
	 * @return Une chaine décrivant la plage des valeurs valides.
	 */
	public String validRange() {
		return "[" + min + ".." + max + "]" + (canBeNull ? " ou vide" : "");
	}



	/**
	 * Indicateur de valeur nulle autorisée.
	 */
	private final boolean canBeNull;



	/**
	 * Valeur maximale autorisée.
	 */
	private final int max;



	/**
	 * Valeur minimale autorisée.
	 */
	private final int min;



}
