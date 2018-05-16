package toolbox.json;

import java.util.*;



/**
 * La classe {@link JsonArray} sp�cifie un tableau.
 * @author Ludovic WALLE
 */
public class JsonArray extends JsonContainer implements Iterable<Json> {



	/**
	 */
	public JsonArray() {}



	/**
	 * @param booleans Chaines.
	 */
	public JsonArray(Boolean... booleans) {
		if (booleans != null) {
			for (Boolean flag : booleans) {
				this.jsons.add(new JsonBoolean(flag));
			}
		}
	}



	/**
	 * @param bytes Octets.
	 */
	public JsonArray(byte... bytes) {
		if (bytes != null) {
			for (int number : bytes) {
				this.jsons.add(new JsonNumber(number));
			}
		}
	}



	/**
	 * @param numbers Nombres.
	 */
	public JsonArray(int... numbers) {
		if (numbers != null) {
			for (int number : numbers) {
				this.jsons.add(new JsonNumber(number));
			}
		}
	}



	/**
	 * @param jsons Elements Json. Les <code>null</code> sont ignor�s.
	 */
	public JsonArray(Json... jsons) {
		for (Json json : jsons) {
			if (json != null) {
				this.jsons.add(json);
			}
		}
	}



	/**
	 * @param other Autre tableau Json.
	 */
	public JsonArray(JsonArray other) {
		for (Json json : other.jsons) {
			jsons.add(json.clone());
		}
	}



	/**
	 * @param numbers Nombres.
	 */
	public JsonArray(long... numbers) {
		if (numbers != null) {
			for (long number : numbers) {
				this.jsons.add(new JsonNumber(number));
			}
		}
	}



	/**
	 * @param strings Chaines.
	 */
	public JsonArray(String... strings) {
		if (strings != null) {
			for (String string : strings) {
				this.jsons.add(new JsonString(string));
			}
		}
	}



	/**
	 * Ins�re la valeur indiqu�e en derni�re position.
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 */
	public JsonArray append(Boolean value) {
		jsons.add(new JsonBoolean(value));
		return this;
	}



	/**
	 * Ins�re la valeur indiqu�e en derni�re position.
	 * @param json Element � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 */
	public JsonArray append(Json json) {
		jsons.add(json);
		return this;
	}



	/**
	 * Ins�re la valeur indiqu�e en derni�re position.
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 */
	public JsonArray append(Number value) {
		jsons.add(new JsonNumber(value));
		return this;
	}



	/**
	 * Ins�re la valeur indiqu�e en derni�re position.
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 */
	public JsonArray append(String value) {
		jsons.add(new JsonString(value));
		return this;
	}



	/**
	 * Supprime tous les �l�ments du tableau.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 */
	public JsonArray clear() {
		jsons.clear();
		return this;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public JsonArray clone() {
		return new JsonArray(this);
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public Json cut(int index) {
		return jsons.remove(index);
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un bool�en.
	 */
	public Boolean cutBoolean(int index) {
		JsonBoolean element;

		element = (JsonBoolean) jsons.remove(index);
		return (element != null) ? (Boolean) element.getValue() : null;
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Double cutDouble(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.remove(index);
		return (element != null) ? element.getValue().doubleValue() : null;
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Long cutInteger(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.remove(index);
		return (element != null) ? element.getValue().longValue() : null;
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas tableau Json.
	 */
	public JsonArray cutJsonArray(int index) {
		return (JsonArray) jsons.remove(index);
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un objet Json.
	 */
	public JsonObject cutJsonObject(int index) {
		return (JsonObject) jsons.remove(index);
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Number cutNumber(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.remove(index);
		return (element != null) ? element.getValue() : null;
	}



	/**
	 * Coupe l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return La valeur coup�e (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas une cha�ne.
	 */
	public String cutString(int index) {
		JsonString element;

		element = (JsonString) jsons.remove(index);
		return (element != null) ? element.getValue() : null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JsonArray other = (JsonArray) obj;
		if (jsons == null) {
			if (other.jsons != null) {
				return false;
			}
		} else if (!jsons.equals(other.jsons)) {
			return false;
		}
		return true;
	}



	/**
	 * Retourne l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public Json get(int index) {
		return jsons.get(index);
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un bool�en.
	 */
	public Boolean getBoolean(int index) {
		JsonBoolean element;

		element = (JsonBoolean) jsons.get(index);
		return (element != null) ? (Boolean) element.getValue() : null;
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Double getDouble(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.get(index);
		return (element != null) ? element.getValue().doubleValue() : null;
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Long getInteger(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.get(index);
		return (element != null) ? element.getValue().longValue() : null;
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas tableau Json.
	 */
	public JsonArray getJsonArray(int index) {
		return (JsonArray) jsons.get(index);
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un objet Json.
	 */
	public JsonObject getJsonObject(int index) {
		return (JsonObject) jsons.get(index);
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas un nombre.
	 */
	public Number getNumber(int index) {
		JsonNumber element;

		element = (JsonNumber) jsons.get(index);
		return (element != null) ? element.getValue() : null;
	}



	/**
	 * Retourne la valeur � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return L'�l�ment � l'index indiqu� (peut �tre <code>null</code>).
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 * @throws ClassCastException Si la valeur n'est pas une cha�ne.
	 */
	public String getString(int index) {
		JsonString element;

		element = (JsonString) jsons.get(index);
		return (element != null) ? element.getValue() : null;
	}



	/**
	 * Retourne un tableau contenant les �l�ments Json.<br>
	 * Le tableau retourn� est une copie.
	 * @return Un tableau contenant les �l�ments Json, jamais <code>null</code>.
	 */
	public Json[] getValues() {
		return jsons.toArray(new Json[jsons.size()]);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((jsons == null) ? 0 : jsons.hashCode());
		return result;
	}



	/**
	 * Ins�re la valeur indiqu�e � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau ou juste apr�s la fin du tableau).
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau et pas non plus juste apr�s la fin du tableau.
	 */
	public JsonArray insert(Boolean value, int index) {
		jsons.add(index, new JsonBoolean(value));
		return this;
	}



	/**
	 * Ins�re la valeur indiqu� � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau ou juste apr�s la fin du tableau).
	 * @param json Element � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau et pas non plus juste apr�s la fin du tableau.
	 */
	public JsonArray insert(Json json, int index) {
		jsons.add(index, json);
		return this;
	}



	/**
	 * Ins�re la valeur indiqu�e � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau ou juste apr�s la fin du tableau).
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau et pas non plus juste apr�s la fin du tableau.
	 */
	public JsonArray insert(Number value, int index) {
		jsons.add(index, new JsonNumber(value));
		return this;
	}



	/**
	 * Ins�re la valeur indiqu�e � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau ou juste apr�s la fin du tableau).
	 * @param value Valeur � ajouter.
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau et pas non plus juste apr�s la fin du tableau.
	 */
	public JsonArray insert(String value, int index) {
		jsons.add(index, new JsonString(value));
		return this;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean isEmpty() {
		return jsons.isEmpty();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Iterator<Json> iterator() {
		return jsons.iterator();
	}



	/**
	 * Supprime l'�l�ment � l'index indiqu�.
	 * @param index Index (doit �tre dans le tableau).
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public JsonArray remove(int index) {
		jsons.remove(index);
		return this;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String serialize(String indentation) {
		StringBuilder builder = new StringBuilder();
		String separator;
		Json previous = null;
		boolean first = true;
		boolean hasNonAtomic = false;

		builder.append("[");
		for (Json json : jsons) {
			if ((json != null) && (!(json instanceof JsonAtomic))) {
				hasNonAtomic = true;
				break;
			}
		}
		separator = (hasNonAtomic && (indentation != null)) ? "\n" + indentation + INDENTATION : "";
		for (Json json : jsons) {
			builder.append(separator);
			separator = ",";
			if (hasNonAtomic && (indentation != null)) {
				if (first) {
					first = false;
				} else if ((previous instanceof JsonObject) && (json instanceof JsonObject)) {
					builder.append(" ");
				} else {
					builder.append("\n" + indentation + INDENTATION);
				}
				previous = json;
			}
			builder.append((json == null) ? "null" : json.serialize((indentation != null) ? indentation + INDENTATION : null));
		}
		if ((indentation != null) && !first) {
			builder.append("\n" + indentation);
		}
		builder.append("]");
		return builder.toString();
	}



	/**
	 * Sp�cifie la valeur � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau).
	 * @param value Valeur de l'�l�ment (peut �tre <code>null</code>).
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public JsonArray set(Boolean value, int index) {
		jsons.set(index, new JsonBoolean(value));
		return this;
	}



	/**
	 * Sp�cifie la valeur � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau).
	 * @param json Valeur de l'�l�ment (peut �tre <code>null</code>).
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public JsonArray set(Json json, int index) {
		jsons.set(index, json);
		return this;
	}



	/**
	 * Sp�cifie la valeur � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau).
	 * @param value Valeur de l'�l�ment (peut �tre <code>null</code>).
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public JsonArray set(Number value, int index) {
		jsons.set(index, new JsonNumber(value));
		return this;
	}



	/**
	 * Sp�cifie la valeur � la position indiqu�e.
	 * @param index Index (doit �tre dans le tableau).
	 * @param value Valeur de l'�l�ment (peut �tre <code>null</code>).
	 * @return Ce tableau, pour pouvoir chainer les appels.
	 * @throws ArrayIndexOutOfBoundsException Si l'index est en dehors du tableau.
	 */
	public JsonArray set(String value, int index) {
		jsons.set(index, new JsonString(value));
		return this;
	}



	/**
	 * Retourne le nombre d'�l�ments dans le tableau.
	 * @return Le nombre d'�l�ments dans le tableau.
	 */
	public int size() {
		return jsons.size();
	}



	/**
	 * Construit l'objet Json correspondant � la chaine indiqu�e.
	 * @param string Objet Json s�rialis�.
	 * @return L'�l�ment Json.
	 * @throws JsonException
	 */
	public static JsonArray parse(String string) throws JsonException {
		return (JsonArray) Json.parse(string);
	}



	/**
	 * Contenu du tableau.
	 */
	private final Vector<Json> jsons = new Vector<>();



}
