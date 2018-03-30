package toolbox.parameter;

import java.util.regex.*;



/**
 * La classe {@link PatternMold} implémente le modèle d'un paramètre dont la valeur doit respecter un modèle de syntaxe.
 * @author Ludovic WALLE
 */
public class PatternMold extends AbstractMold<String> {



	/**
	 * @param name Nom du paramètre (séquence non vide de caractères {@value #NAMES_CHARS}).
	 * @param description Description du paramètre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut être <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le paramètre est spécifié.
	 * @param defaultValue Valeur par défaut du paramètre (peut être <code>null</code>).
	 * @param patternString Chaine décrivant le modèle de la valeur.
	 */
	public PatternMold(String name, String description, boolean valueRequired, String defaultValue, String patternString) {
		super(name, description, valueRequired, defaultValue);
		this.pattern = Pattern.compile(patternString);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateComputeValue(String rawValue) throws ParameterException_InvalidValue {
		if (pattern.matcher(rawValue).matches()) {
			return rawValue;
		} else {
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du paramètre \"" + getName() + "\" ne respecte pas le modèle de syntaxe :" + pattern.pattern());
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "La valeur doit respecter le modèle de syntaxe: " + pattern.pattern();
	}



	/**
	 * Retourne la chaine décrivant le modèle de la valeur.
	 * @return La chaine décrivant le modèle de la valeur.
	 */
	public String getPatternString() {
		return pattern.toString();
	}



	/** {@inheritDoc} */
	@Override public PatternParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new PatternParameter(this, rawValue);
	}



	/**
	 * Modèle de la valeur.
	 */
	private final Pattern pattern;



}
