package toolbox.json;



/**
 * La classe {@link JsonContainer} est un anc�tre commun abstrait � tous les �l�ments Json non atomiques (objets, tableaux).
 * @author Ludovic WALLE
 */
public abstract class JsonContainer extends Json {



	/**
	 * Teste si cet �l�ment est vide.
	 * @return <code>true</code> si cet �l�ment est vide, <code>false</code> sinon.
	 */
	public abstract boolean isEmpty();



	/**
	 * Teste si cet �l�ment n'est pas vide.
	 * @return <code>true</code> si cet �l�ment Json n'est pas vide, <code>false</code> sinon.
	 */
	public final boolean isNotEmpty() {
		return !isEmpty();
	}



}
