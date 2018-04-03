package toolbox.json;



/**
 * La classe {@link JsonAtomic} .
 * @author Ludovic WALLE
 */
public abstract class JsonAtomic extends Json {



	/**
	 * Retourne la valeur Json. La valeur peut être de type Boolean, Integer, Double, String ou <code>null</code>.
	 * @return La valeur Json.
	 */
	public abstract Object getValue();



}
