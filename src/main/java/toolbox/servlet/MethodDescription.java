package toolbox.servlet;

import toolbox.*;



/**
 * La classe {@link MethodDescription} contient la description d'une servlet pour une méthode.
 * @author Ludovic WALLE
 */
public class MethodDescription implements Comparable<MethodDescription> {



	/**
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param other Autre description.
	 */
	public MethodDescription(Method[] methods, MethodDescription other) {
		this(methods, other.textualDescription, other.checker);
	}



	/**
	 * Aucun paramètre.
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut être <code>null</code>).
	 */
	public MethodDescription(Method[] methods, String textualDescription) {
		this(methods, textualDescription, new Checker(true));
	}



	/**
	 * Les paramètres seront considérés comme tous obligatoires et non répétitifs, ou tous facultatifs non répétitifs.
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut être <code>null</code>).
	 * @param required Indication d'obligation de présence des paramètres: tous obligatoire si <code>true</code>, tous facultatifs si <code>false</code>.
	 * @param parameters Description des paramètres.
	 */
	public MethodDescription(Method[] methods, String textualDescription, boolean required, Parameter... parameters) {
		this(methods, textualDescription, new Checker(required, parameters));
	}



	/**
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut être <code>null</code>).
	 * @param checker Vérificateur de paramètres.
	 */
	private MethodDescription(Method[] methods, String textualDescription, Checker checker) {
		this.textualDescription = (textualDescription == null) ? "" : textualDescription;
		this.checker = checker;
		if (methods.length == 0) {
			throw new RuntimeException("Aucune méthode pour la description: " + textualDescription);
		}
		for (Method method : methods) {
			if (method == null) {
				throw new RuntimeException("Méthode null pour la description: " + textualDescription);
			}
		}
		this.methods = methods.clone();
	}



	/**
	 * Les paramètres seront déduits de la chaine de combinaisons, et seront tous de la classe {@link Parameter}.
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut être <code>null</code>).
	 * @param combinationsString Chaine décrivant les combinaisons de paramètres autorisées.
	 */
	public MethodDescription(Method[] methods, String textualDescription, String combinationsString) {
		this(methods, textualDescription, new Checker(combinationsString));
	}



	/**
	 * Les paramètres sont référencés dans la chaine de combinaison et énumérés. Tous les paramètres de la chaine de combinaison doivent être énumérés, et, réciproquement, tous les paramètres énumérés
	 * doivent figurer dans la chaine de combinaisons.
	 * @param methods Méthodes HTTP décrites (ne doit pas être <code>null</code>, ni vide, ni contenir de <code>null</code>).
	 * @param textualDescription Description de la servlet pour la page d'aide (peut être <code>null</code>).
	 * @param combinationsString Chaine décrivant les combinaisons de paramètres autorisées.
	 * @param parameters Description des paramètres.
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
	 * Retourne le vérificateur de paramètres.
	 * @return Le vérificateur de paramètres (jamais <code>null</code>).
	 */
	public Checker getChecker() {
		return checker;
	}



	/**
	 * Retourne un tableau contenant les méthodes HTTP décrites.
	 * @return Un tableau contenant les méthodes HTTP décrites (jamais <code>null</code>).
	 */
	public Method[] getMethods() {
		return methods.clone();
	}



	/**
	 * Retourne la description textuelle de la servlet pour cette méthode.<br>
	 * La description textuelle peut être vide, mais jamais <code>null</code>.
	 * @return La description textuelle de la servlet pour cette méthode.
	 */
	public String getTextualDescription() {
		return textualDescription;
	}



	/**
	 * Vérificateur de paramètres.
	 */
	private final Checker checker;



	/**
	 * Méthodes HTTP décrites.
	 */
	private final Method[] methods;



	/**
	 * Description de la servlet pour l'aide.
	 */
	private final String textualDescription;



}
