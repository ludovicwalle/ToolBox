package toolbox.servlet;

import toolbox.*;



/**
 * La classe {@link MethodDescription} contient la description d'une servlet pour une m�thode.
 * @author Ludovic WALLE
 */
public class MethodDescription implements Comparable<MethodDescription> {



	/**
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param other Autre description.
	 */
	public MethodDescription(Method[] methods, MethodDescription other) {
		this(methods, other.textualDescription, other.checker);
	}



	/**
	 * Aucun param�tre.
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut �tre <code>null</code>).
	 */
	public MethodDescription(Method[] methods, String textualDescription) {
		this(methods, textualDescription, new Checker(true));
	}



	/**
	 * Les param�tres seront consid�r�s comme tous obligatoires et non r�p�titifs, ou tous facultatifs non r�p�titifs.
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut �tre <code>null</code>).
	 * @param required Indication d'obligation de pr�sence des param�tres: tous obligatoire si <code>true</code>, tous facultatifs si <code>false</code>.
	 * @param parameters Description des param�tres.
	 */
	public MethodDescription(Method[] methods, String textualDescription, boolean required, Parameter... parameters) {
		this(methods, textualDescription, new Checker(required, parameters));
	}



	/**
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut �tre <code>null</code>).
	 * @param checker V�rificateur de param�tres.
	 */
	private MethodDescription(Method[] methods, String textualDescription, Checker checker) {
		this.textualDescription = (textualDescription == null) ? "" : textualDescription;
		this.checker = checker;
		if (methods.length == 0) {
			throw new RuntimeException("Aucune m�thode pour la description: " + textualDescription);
		}
		for (Method method : methods) {
			if (method == null) {
				throw new RuntimeException("M�thode null pour la description: " + textualDescription);
			}
		}
		this.methods = methods.clone();
	}



	/**
	 * Les param�tres seront d�duits de la chaine de combinaisons, et seront tous de la classe {@link Parameter}.
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut �tre <code>null</code>).
	 * @param combinationsString Chaine d�crivant les combinaisons de param�tres autoris�es.
	 */
	public MethodDescription(Method[] methods, String textualDescription, String combinationsString) {
		this(methods, textualDescription, new Checker(combinationsString));
	}



	/**
	 * Les param�tres sont r�f�renc�s dans la chaine de combinaison et �num�r�s. Tous les param�tres de la chaine de combinaison doivent �tre �num�r�s, et, r�ciproquement, tous les param�tres �num�r�s
	 * doivent figurer dans la chaine de combinaisons.
	 * @param methods M�thodes HTTP d�crites (ne doit pas �tre <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut �tre <code>null</code>).
	 * @param combinationsString Chaine d�crivant les combinaisons de param�tres autoris�es.
	 * @param parameters Description des param�tres.
	 */
	public MethodDescription(Method[] methods, String textualDescription, String combinationsString, Parameter... parameters) {
		this(methods, textualDescription, new Checker(combinationsString, parameters));
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int compareTo(MethodDescription other) {
		int compare;

		for (int i = 0; i < OtherTools.min(methods.length, other.methods.length); i++) {
			if ((compare = methods[i].compareTo(other.methods[i])) != 0) {
				return compare;
			}
		}
		return other.methods.length - methods.length;
	}



	/**
	 * Retourne le v�rificateur de param�tres.
	 * @return Le v�rificateur de param�tres (jamais <code>null</code>).
	 */
	public Checker getChecker() {
		return checker;
	}



	/**
	 * Retourne un tableau contenant les m�thodes HTTP d�crites.
	 * @return Un tableau contenant les m�thodes HTTP d�crites (jamais <code>null</code>).
	 */
	public Method[] getMethods() {
		return methods.clone();
	}



	/**
	 * Retourne la description textuelle de la servlet pour cette m�thode.<br>
	 * La description textuelle peut �tre vide, mais jamais <code>null</code>.
	 * @return La description textuelle de la servlet pour cette m�thode.
	 */
	public String getTextualDescription() {
		return textualDescription;
	}



	/**
	 * V�rificateur de param�tres.
	 */
	private final Checker checker;



	/**
	 * M�thodes HTTP d�crites.
	 */
	private final Method[] methods;



	/**
	 * Description de la servlet pour l'aide.
	 */
	private final String textualDescription;



}
