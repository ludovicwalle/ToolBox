package toolbox.servlet;

import java.util.regex.*;

import javax.servlet.http.*;



/**
 * La classe {@link Parameter} implémente la description d'un paramètre de servlet.<br>
 * Le nom du paramètre doit être constitué uniquement des caractères: {@value #NAME_PATTERN}.
 * @author Ludovic WALLE
 */
public abstract class Parameter implements Comparable<Parameter> {



	/**
	 * @param name Nom du paramètre.
	 * @param description Description du paramètre.
	 */
	public Parameter(String name, String description) {
		if (!NAME_PATTERN.matcher(name).matches()) {
			throw new RuntimeException("La syntaxe du nom de paramètre est invalide: " + name);
		}
		this.name = name;
		this.description = (description == null) ? "" : description;
	}



	/**
	 * Ajoute les informations sur le paramètre à la page d'aide indiquée.
	 * @param page Page d'aide à compléter.
	 */
	public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription());
	}



	/**
	 * Ajoute la description de la valeur invalide indiquée à la page indiquée.
	 * @param page Page d'aide à compléter.
	 * @param value Valeur du paramètre.
	 */
	public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du paramètre \"" + getName() + "\" est invalide.");
	}



	/**
	 * Teste si la valeur indiquée (éventuellement <code>null</code>) est valide.<br>
	 * Cette méthode est destinée à être surchargée dans les classes dérivées. Par défaut, elle retourne toujours <code>true</code>.
	 * @param parameterValue Valeur du paramètre à tester.
	 * @return <code>true</code> si la valeur est valide, <code>false</code> sinon.
	 */
	@SuppressWarnings("static-method") public boolean check(@SuppressWarnings("unused") String parameterValue) {
		return true;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int compareTo(Parameter other) {
		return name.compareTo(other.name);
	}



	/**
	 * Teste si l'objet indiqué est égal à cet objet.<br>
	 * Il sont égaux si tous leurs champs sont égaux.
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
		Parameter other = (Parameter) object;
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
	 * Teste si le paramètre existe dans les données d'entrée de la servlet, même sans valeur.
	 * @param request Données d'entrée de la servlet.
	 * @return <code>true</code> si le paramètre existe, <code>false</code> sinon.
	 */
	public boolean exists(HttpServletRequest request) {
		return request.getParameterValues(name) != null;
	}



	/**
	 * Teste si le paramètre existe dans les données d'entrée de la servlet, avec au moins une valeur non vide.<br>
	 * @param request Données d'entrée de la servlet.
	 * @return <code>true</code> si le paramètre existe, <code>false</code> sinon.
	 */
	public boolean existsWithValue(HttpServletRequest request) {
		if (request.getParameterValues(name) == null) {
			return false;
		}
		for (String value : request.getParameterValues(name)) {
			if ((value != null) && (value.length() > 0)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Retourne la description du paramètre.
	 * @return La description du paramètre.
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * Retourne le nom du paramètre.
	 * @return Le nom du paramètre.
	 */
	public String getName() {
		return name;
	}



	/**
	 * Retourne la valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, <code>null</code> sinon, sauf pour les paramètres {@link BooleanParameter}, où la valeur
	 * retournée est {@link Boolean#FALSE}.
	 * @param request Données d'entrée de la servlet.
	 * @return La valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, <code>null</code> sinon, sauf pour les paramètres {@link BooleanParameter}, où la valeur
	 *         retournée est {@link Boolean#FALSE}.
	 * @throws RuntimeException Si le paramètre apparaît plusieurs fois.
	 */
	public Object getValue(HttpServletRequest request) throws RuntimeException {
		return getValue(request, null, null);
	}



	/**
	 * Retourne la valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, la valeur indiquée par défaut sinon.
	 * @param request Données d'entrée de la servlet.
	 * @param valueWhenNoParameter Valeur à retourner si le paramètre n'existe pas dans les données d'entrée de la servlet.
	 * @param valueWhenParameterWithoutValue Valeur à retourner si le paramètre existe dans les donnés d'entrée de la servlet mais n'a pas de valeur.
	 * @return La valeur du paramètre si elle existe dans les données d'entrée de la servlet et est valide, la valeur indiquée par défaut sinon.
	 * @throws RuntimeException Si le paramètre apparaît plusieurs fois.
	 */
	public Object getValue(HttpServletRequest request, Object valueWhenNoParameter, Object valueWhenParameterWithoutValue) throws RuntimeException {
		String[] values;

		values = request.getParameterValues(name);
		if (values == null) {
			return valueWhenNoParameter;
		} else if (values.length == 1) {
			if ((values[0] == null) || (values[0].length() <= 0)) {
				return valueWhenParameterWithoutValue;
			} else {
				return getValue(values[0]);
			}
		} else {
			throw new RuntimeException("Le paramètre est multi-occurrent: " + name);
		}
	}



	/**
	 * Retourne un {@link Integer} correspondant à la valeur indiquée, si elle est valide (un entier dans la plage autorisée), <code>null</code> sinon.
	 * @param parameterValue Valeur du paramètre.
	 * @return Un {@link Integer} correspondant à la valeur indiquée, si elle est valide (un entier dans la plage autorisée), <code>null</code> sinon.
	 */
	@SuppressWarnings("static-method") public Object getValue(String parameterValue) {
		return parameterValue;
	}



	/**
	 * Retourne un tableau contenant les valeurs du paramètre existantes et valides dans les données d'entrée de la servlet. Si une valeur est invalide, l'entrée correspondante dans le tableau sera
	 * <code>null</code>.<br>
	 * Le tableau est vide si le paramètre n'apparait pas, jamais <code>null</code>.
	 * @param request Données d'entrée de la servlet.
	 * @return Un tableau contenant les valeurs du paramètre existantes et valides dans les données d'entrée de la servlet.
	 */
	public abstract Object[] getValues(HttpServletRequest request);



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		return name.hashCode();
	}



	/**
	 * Description du paramètre.
	 */
	private final String description;



	/**
	 * Nom du paramètre.
	 */
	private final String name;



	/**
	 * Caractères autorisés dans les noms de paramètres.
	 */
	public static final String NAME_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";



	/**
	 * Modèle de syntaxe des noms de paramètres.
	 */
	public static final Pattern NAME_PATTERN = Pattern.compile("[" + NAME_CHARACTERS + "]+");



}
