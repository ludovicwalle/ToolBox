package toolbox.parameter;

/**
 * La classe {@link ParameterException} est la classe racine de toutes les exceptions liées aux paramètres.
 * @author Ludovic WALLE
 */
public abstract class ParameterException extends Exception {



	/**
	 * @return .
	 */
	public String getFormattedMessage() {
		return getMessage() + "\n";
	}



}
