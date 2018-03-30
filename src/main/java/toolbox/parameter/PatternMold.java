package toolbox.parameter;

import java.util.regex.*;



/**
 * La classe {@link PatternMold} impl�mente le mod�le d'un param�tre dont la valeur doit respecter un mod�le de syntaxe.
 * @author Ludovic WALLE
 */
public class PatternMold extends AbstractMold<String> {



	/**
	 * @param name Nom du param�tre (s�quence non vide de caract�res {@value #NAMES_CHARS}).
	 * @param description Description du param�tre (devrait ne contenir qu'une seule ligne de texte, commencer par une majuscule et se terminer par un point) (peut �tre <code>null</code> ou vide).
	 * @param valueRequired Indicateur valeur obligatoire si le param�tre est sp�cifi�.
	 * @param defaultValue Valeur par d�faut du param�tre (peut �tre <code>null</code>).
	 * @param patternString Chaine d�crivant le mod�le de la valeur.
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
			throw new ParameterException_InvalidValue(this, rawValue, "La valeur \"" + rawValue + "\" du param�tre \"" + getName() + "\" ne respecte pas le mod�le de syntaxe :" + pattern.pattern());
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected String delegateGetValueDescription() {
		return "La valeur doit respecter le mod�le de syntaxe: " + pattern.pattern();
	}



	/**
	 * Retourne la chaine d�crivant le mod�le de la valeur.
	 * @return La chaine d�crivant le mod�le de la valeur.
	 */
	public String getPatternString() {
		return pattern.toString();
	}



	/** {@inheritDoc} */
	@Override public PatternParameter mold(String rawValue) throws ParameterException_InvalidValue {
		return new PatternParameter(this, rawValue);
	}



	/**
	 * Mod�le de la valeur.
	 */
	private final Pattern pattern;



}
