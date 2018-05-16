package toolbox.json;



/**
 * La classe {@link JsonAtomic} est un anc�tre commun abstrait � tous les �l�ments Json atomiques (bool�ens, nombres, chaines).
 * @author Ludovic WALLE
 */
public abstract class JsonAtomic extends Json {



	/**
	 * Retourne la valeur Json. La valeur peut �tre de type Boolean, Long, Double, String ou <code>null</code>.
	 * @return La valeur Json.
	 */
	public abstract Object getValue();



}
