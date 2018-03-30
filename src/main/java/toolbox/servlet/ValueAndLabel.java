package toolbox.servlet;

/**
 * La classe {@link ValueAndLabel} associe une valeur et sa verbalisation.
 * @author Ludovic WALLE
 */
public class ValueAndLabel implements Comparable<ValueAndLabel> {



	/**
	 * @param value Valeur. La verbalisation sera la m�me que la valeur (ne doit pas �tre <code>null</code>).
	 */
	public ValueAndLabel(String value) {
		this(value, value);
	}



	/**
	 * @param value Valeur (ne doit pas �tre <code>null</code>).
	 * @param label Verbalisation correspondant � la valeur (ne doit pas �tre <code>null</code>).
	 */
	public ValueAndLabel(String value, String label) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (label == null) {
			throw new NullPointerException();
		}
		this.value = value;
		this.label = label;
	}



	/**
	 * {@inheritDoc}
	 */

	@Override public int compareTo(ValueAndLabel other) {
		return value.compareTo(other.value);
	}



	/**
	 * Teste si l'objet indiqu� est �gal � cet objet.<br>
	 * Il sont �gaux si ils ont la m�me valeur.
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
		ValueAndLabel other = (ValueAndLabel) object;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}



	/**
	 * @param other Teste si les objets sont �gaux. Les objets sont �gaux si ils ont la m�me valeur.
	 * @return <code>true</code> si les objets sont �gaux, <code>false</code> sinon.
	 */
	public boolean equals(ValueAndLabel other) {
		return value.equals(other.value);
	}



	/**
	 * Retourne la verbalisation correspondant � la valeur, jamais <code>null</code>.
	 * @return La verbalisation correspondant � la valeur, jamais <code>null</code>.
	 */
	public String getLabel() {
		return label;
	}



	/**
	 * Retourne la valeur, jamais <code>null</code>.
	 * @return La valeur, jamais <code>null</code>.
	 */
	public String getValue() {
		return value;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		return value.hashCode();
	}



	/**
	 * Verbalisation correspondant � la valeur.
	 */
	private final String label;



	/**
	 * Valeur.
	 */
	private final String value;



}
