package toolbox;



/**
 * La classe {@link NamedString} implemente une association entre une chaine et un nom (une autre chaine).
 * @author Ludovic WALLE
 */
public class NamedString implements Comparable<NamedString> {



	/**
	 * @param name Nom de la chaine.
	 * @param string Chaine.
	 */
	public NamedString(String name, String string) {
		this.name = name;
		this.string = string;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int compareTo(NamedString other) {
		int returnCode;

		if ((returnCode = name.compareTo(other.name)) == 0) {
			returnCode = string.compareTo(other.string);
		}
		return returnCode;
	}



	/**
	 * Retourne une chaine décrivant cette chaine nommée.
	 * @return une chaine décrivant cette chaine nommée.
	 */
	public String dump() {
		return name + " = " + string;
	}



	/**
	 * Teste si l'objet indiqué est égal à cet objet.<br>
	 * Il sont égaux si le nom et la chaine sont égaux.
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
		NamedString other = (NamedString) object;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!string.equals(other.string)) {
			return false;
		}
		return true;
	}



	/**
	 * Retourne le nom de la chaine.
	 * @return Le nom de la chaine.
	 */
	public String getName() {
		return name;
	}



	/**
	 * Retourne la chaine.
	 * @return La chaine.
	 */
	public String getString() {
		return string;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((string == null) ? 0 : string.hashCode());
		return result;
	}



	/**
	 * Nom de la chaine.
	 */
	private final String name;



	/**
	 * Chaine.
	 */
	private final String string;



}
