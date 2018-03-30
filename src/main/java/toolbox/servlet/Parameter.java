package toolbox.servlet;

import java.util.regex.*;

import javax.servlet.http.*;



/**
 * La classe {@link Parameter} impl�mente la description d'un param�tre de servlet.<br>
 * Le nom du param�tre doit �tre constitu� uniquement des caract�res: {@value #NAME_PATTERN}.
 * @author Ludovic WALLE
 */
public abstract class Parameter implements Comparable<Parameter> {



	/**
	 * @param name Nom du param�tre.
	 * @param description Description du param�tre.
	 */
	public Parameter(String name, String description) {
		if (!NAME_PATTERN.matcher(name).matches()) {
			throw new RuntimeException("La syntaxe du nom de param�tre est invalide: " + name);
		}
		this.name = name;
		this.description = (description == null) ? "" : description;
	}



	/**
	 * Ajoute les informations sur le param�tre � la page d'aide indiqu�e.
	 * @param page Page d'aide � compl�ter.
	 */
	public void appendHelp(Page page) {
		page.appendItem(getName(), getDescription());
	}



	/**
	 * Ajoute la description de la valeur invalide indiqu�e � la page indiqu�e.
	 * @param page Page d'aide � compl�ter.
	 * @param value Valeur du param�tre.
	 */
	public void appendInvalidValue(Page page, String value) {
		page.appendSection("Erreur", "La valeur \"" + CustomizedServlet.encodeForHtml(value) + "\" du param�tre \"" + getName() + "\" est invalide.");
	}



	/**
	 * Teste si la valeur indiqu�e (�ventuellement <code>null</code>) est valide.<br>
	 * Cette m�thode est destin�e � �tre surcharg�e dans les classes d�riv�es. Par d�faut, elle retourne toujours <code>true</code>.
	 * @param parameterValue Valeur du param�tre � tester.
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
	 * Teste si l'objet indiqu� est �gal � cet objet.<br>
	 * Il sont �gaux si tous leurs champs sont �gaux.
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
	 * Teste si le param�tre existe dans les donn�es d'entr�e de la servlet, m�me sans valeur.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return <code>true</code> si le param�tre existe, <code>false</code> sinon.
	 */
	public boolean exists(HttpServletRequest request) {
		return request.getParameterValues(name) != null;
	}



	/**
	 * Teste si le param�tre existe dans les donn�es d'entr�e de la servlet, avec au moins une valeur non vide.<br>
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return <code>true</code> si le param�tre existe, <code>false</code> sinon.
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
	 * Retourne la description du param�tre.
	 * @return La description du param�tre.
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * Retourne le nom du param�tre.
	 * @return Le nom du param�tre.
	 */
	public String getName() {
		return name;
	}



	/**
	 * Retourne la valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, <code>null</code> sinon, sauf pour les param�tres {@link BooleanParameter}, o� la valeur
	 * retourn�e est {@link Boolean#FALSE}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return La valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, <code>null</code> sinon, sauf pour les param�tres {@link BooleanParameter}, o� la valeur
	 *         retourn�e est {@link Boolean#FALSE}.
	 * @throws RuntimeException Si le param�tre appara�t plusieurs fois.
	 */
	public Object getValue(HttpServletRequest request) throws RuntimeException {
		return getValue(request, null, null);
	}



	/**
	 * Retourne la valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, la valeur indiqu�e par d�faut sinon.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param valueWhenNoParameter Valeur � retourner si le param�tre n'existe pas dans les donn�es d'entr�e de la servlet.
	 * @param valueWhenParameterWithoutValue Valeur � retourner si le param�tre existe dans les donn�s d'entr�e de la servlet mais n'a pas de valeur.
	 * @return La valeur du param�tre si elle existe dans les donn�es d'entr�e de la servlet et est valide, la valeur indiqu�e par d�faut sinon.
	 * @throws RuntimeException Si le param�tre appara�t plusieurs fois.
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
			throw new RuntimeException("Le param�tre est multi-occurrent: " + name);
		}
	}



	/**
	 * Retourne un {@link Integer} correspondant � la valeur indiqu�e, si elle est valide (un entier dans la plage autoris�e), <code>null</code> sinon.
	 * @param parameterValue Valeur du param�tre.
	 * @return Un {@link Integer} correspondant � la valeur indiqu�e, si elle est valide (un entier dans la plage autoris�e), <code>null</code> sinon.
	 */
	@SuppressWarnings("static-method") public Object getValue(String parameterValue) {
		return parameterValue;
	}



	/**
	 * Retourne un tableau contenant les valeurs du param�tre existantes et valides dans les donn�es d'entr�e de la servlet. Si une valeur est invalide, l'entr�e correspondante dans le tableau sera
	 * <code>null</code>.<br>
	 * Le tableau est vide si le param�tre n'apparait pas, jamais <code>null</code>.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @return Un tableau contenant les valeurs du param�tre existantes et valides dans les donn�es d'entr�e de la servlet.
	 */
	public abstract Object[] getValues(HttpServletRequest request);



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		return name.hashCode();
	}



	/**
	 * Description du param�tre.
	 */
	private final String description;



	/**
	 * Nom du param�tre.
	 */
	private final String name;



	/**
	 * Caract�res autoris�s dans les noms de param�tres.
	 */
	public static final String NAME_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";



	/**
	 * Mod�le de syntaxe des noms de param�tres.
	 */
	public static final Pattern NAME_PATTERN = Pattern.compile("[" + NAME_CHARACTERS + "]+");



}
