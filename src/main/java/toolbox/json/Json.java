package toolbox.json;

import java.util.regex.*;

import toolbox.*;



/**
 * La classe {@link Json} est un anc�tre commun abstrait � tous les �l�ments Json. Voir {@link "http://www.json.org/json-fr.html"}.
 * @author Ludovic WALLE
 */
public abstract class Json {



	/**
	 * Retourne une copie en profondeur de cet objet.
	 * @return Une copie en profondeur de cet objet.
	 */
	@Override public abstract Json clone();



	/**
	 * S�rialise cet �l�ment Json sans pr�sentation (indentation, ...).
	 * @return S�rialise l'�l�ment Json.
	 */
	public final String serialize() {
		return serialize(null);
	}



	/**
	 * S�rialise cet �l�ment Json.
	 * @param indentation Indentation globale de pr�sentation de cet �l�ment Json, ou <code>null</code> pour s�rialiser sans pr�sentation.
	 * @return S�rialise l'�l�ment Json.
	 */
	public abstract String serialize(String indentation);



	/**
	 * {@inheritDoc}
	 */
	@Override public String toString() {
		return serialize(null);
	}



	/**
	 * Retourne une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>\\</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 * sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 sauf TAB, CR et LF on �t� remplac�s par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiqu�e est <code>null</code>.<br>
	 * Cela garantit une interpr�tation correcte par tous les navigateurs, configur�s avec n'importe quel encodage qui soit un sur-ensemble de l'US-ASCII (ISO-8859-1, Windows-1252, UTF-8, ...).
	 * @param string Chaine � transformer (peut �tre <code>null</code>).
	 * @return Une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 *         sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 sauf TAB, CR et LF on �t� remplac�s par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiqu�e est
	 *         <code>null</code>.
	 */
	protected static String encode(String string) {
		int stringLength;
		char stringChar;
		StringBuilder escapedBuilder;
		String escaped;
		if (string == null) {
			escaped = null;
		} else {
			escapedBuilder = new StringBuilder();
			stringLength = string.length();
			for (int i = 0; i < stringLength; i++) {
				stringChar = string.charAt(i);
				if (stringChar == '"') {
					escapedBuilder.append("\\\"");
				} else if (stringChar == '\\') {
					escapedBuilder.append("\\\\");
				} else if (stringChar == '/') {
					escapedBuilder.append("\\/");
				} else if (stringChar == '\b') {
					escapedBuilder.append("\\b");
				} else if (stringChar == '\f') {
					escapedBuilder.append("\\f");
				} else if (stringChar == '\n') {
					escapedBuilder.append("\\n");
				} else if (stringChar == '\r') {
					escapedBuilder.append("\\r");
				} else if (stringChar == '\t') {
					escapedBuilder.append("\\t");
				} else if ((stringChar < ((char) 32)) || (stringChar >= ((char) 128))) {
					escapedBuilder.append(String.format("\\u%04X", (int) stringChar));
				} else {
					escapedBuilder.append(stringChar);
				}
			}
			escaped = escapedBuilder.toString();
		}
		return escaped;
	}



	/**
	 * Simule l'appel � {@link Scanner#getMatchingPart(Pattern)} avec <code>\A"(?:\\([\\\/\"bfnrt])|\\u([0-9a-fA-F]{4})|([^\x01-\x1F\\\"]+))*"</code> comme expression r�guli�re.<br>
	 * Permet d'�viter les {@link StackOverflowError} si la chaine � parcourir est trop complexe.
	 * @param scanner Chaine � parcourir.
	 * @return Le fragment correspondant � une chaine json, ou <code>null</code>.
	 */
	private static String getMatchingPartForString(Scanner scanner) {
		int initialIndex = scanner.getIndex();
		StringBuilder string = new StringBuilder();
		String fragment;

		if (scanner.currentCharIsOneOf('"')) {
			scanner.move(1);
			while ((fragment = scanner.getMatchingPart(STRING_FRAGMENT_PATTERN)) != null) {
				string.append(fragment);
			}
			if (scanner.currentCharIsOneOf('"')) {
				scanner.move(1);
				return string.toString();
			}
		}
		scanner.setIndex(initialIndex);
		return null;
	}



	/**
	 * Construit l'�l�ment Json correspondant � la chaine indiqu�e.
	 * @param scanner Element Json s�rialis� en cours de parsage.
	 * @return L'�l�ment Json.
	 * @throws JsonException
	 * @throws StringIndexOutOfBoundsException
	 */
	private static Json parse(Scanner scanner) throws JsonException {
		JsonArray jsonArray;
		JsonObject jsonObject;
		Json jsonName;
		int index = 0;
		String string;
		Matcher matcher;
		StringBuilder builder;

		scanner.skipWhitespaces();

		if (scanner.currentCharIsOneOf('{')) {
			jsonObject = new JsonObject();
			scanner.moveThenSkipWhitespaces(1);
			if (scanner.currentCharIsOneOf('}')) {
				scanner.move(1);
				return jsonObject;
			} else {
				for (;;) {
					index = scanner.getIndex();
					if (!((jsonName = parse(scanner)) instanceof JsonString)) {
						throw new JsonException("Nom de propri�t� Json attendu: " + scanner.getFragment(index, 5, 5));
					}
					if (jsonObject.has(((JsonString) jsonName).getValue())) {
						throw new JsonException("Il a a plusieurs propri�t�s \"" + ((JsonString) jsonName).getValue() + "\" dans le m�me objet Json: " + scanner.getFragment(index, 5, 50));
					}
					scanner.skipWhitespaces();
					if (scanner.currentCharIsNoneOf(':')) {
						throw new JsonException("\":\" attendu: " + scanner.getFragment(5, 5));
					}
					scanner.moveThenSkipWhitespaces(1);
					jsonObject.set(((JsonString) jsonName).getValue(), parse(scanner));
					scanner.skipWhitespaces();
					if (scanner.currentCharIsOneOf(',')) {
						scanner.moveThenSkipWhitespaces(1);
					} else if (scanner.currentCharIsOneOf('}')) {
						scanner.move(1);
						return jsonObject;
					} else if (scanner.hasCharToParse()) {
						throw new JsonException("\"" + scanner.getCurrentChar() + "\" non attendu: " + scanner.getFragment(5, 5));
					} else {
						throw new JsonException("\",\" ou \"}\" attendu: " + scanner.getFragment(5, 5));
					}
				}
			}

		} else if (scanner.currentCharIsOneOf('[')) {
			jsonArray = new JsonArray();
			scanner.moveThenSkipWhitespaces(1);
			if (scanner.currentCharIsOneOf(']')) {
				scanner.move(1);
				return jsonArray;
			} else {
				for (;;) {
					jsonArray.append(parse(scanner));
					scanner.skipWhitespaces();
					if (scanner.currentCharIsOneOf(',')) {
						scanner.moveThenSkipWhitespaces(1);
					} else if (scanner.currentCharIsOneOf(']')) {
						scanner.move(1);
						return jsonArray;
					} else if (scanner.hasCharToParse()) {
						throw new JsonException("\"" + scanner.getCurrentChar() + "\" non attendu: " + scanner.getFragment(5, 5));
					} else {
						throw new JsonException("\",\" ou \"]\" attendu: " + scanner.getFragment(5, 5));
					}
				}
			}

		} else if ((string = scanner.getMatchingPart(BOOLEAN_PATTERN)) != null) {
			return new JsonBoolean(Boolean.parseBoolean(string));

		} else if (scanner.getMatchingPart(NULL_PATTERN) != null) {
			return null;

		} else if ((string = getMatchingPartForString(scanner)) != null) {
			builder = new StringBuilder();
			matcher = ENCODING_PATTERN.matcher(string);
			while (matcher.find()) {
				if (matcher.group(1) != null) {
					switch (matcher.group(1).charAt(0)) {
					case '"':
						builder.append("\"");
						break;
					case '/':
						builder.append("/");
						break;
					case 'b':
						builder.append("\b");
						break;
					case 'f':
						builder.append("\f");
						break;
					case 'n':
						builder.append("\n");
						break;
					case 'r':
						builder.append("\r");
						break;
					case 't':
						builder.append("\t");
						break;
					case '\\':
						builder.append("\\");
						break;
					}
				} else if (matcher.group(2) != null) {
					builder.append((char) Long.parseLong(matcher.group(2), 16));
				} else if (matcher.group(3) != null) {
					builder.append(matcher.group(3));
				} else {
					throw new JsonException("Erreur dans le parsage de la chaine Json: " + scanner.getFragment(5, 5));
				}
			}
			return new JsonString(builder.toString());

		} else if ((string = scanner.getMatchingPart(NUMBER_PATTERN)) != null) {
			if (INTEGER_PATTERN.matcher(string).matches()) {
				return new JsonNumber(Long.valueOf(string));
			} else {
				return new JsonNumber(Double.valueOf(string));
			}

		} else if (scanner.hasCharToParse()) {
			throw new JsonException("\"" + scanner.getCurrentChar() + "\" non attendu: " + scanner.getFragment(5, 5));
		} else {
			throw new JsonException("\",\" ou \"}\" attendu: " + scanner.getFragment(5, 5));
		}
	}



	/**
	 * Construit l'�l�ment Json correspondant � la chaine indiqu�e.
	 * @param string �l�ment Json s�rialis�.
	 * @return L'�l�ment Json.
	 * @throws JsonException
	 */
	public static Json parse(String string) throws JsonException {
		Json json;
		Scanner scanner;

		if (string == null) {
			return null;
		} else {
			scanner = new Scanner(string);
			scanner.skipWhitespaces();
			if (!scanner.relativeCharIsOneOf(0, '{', '[')) {
				throw new JsonException("\"{\" ou \"[\" attendu: " + scanner.getFragment(0, 10));
			} else {
				json = parse(scanner);
				if (scanner.getCharsToParseCount() > 0) {
					throw new JsonException("Caract�res non attendus: " + scanner.getFragment(0, 10));
				}
				return json;
			}
		}
	}



	/**
	 * Mod�le de syntaxe des bool�ens.
	 */
	private static final Pattern BOOLEAN_PATTERN = Pattern.compile("\\A(true|false)");



	/**
	 * Mod�le de syntaxe des fragments de chaines.
	 */
	public static final Pattern ENCODING_PATTERN = Pattern.compile("\\A(?:\\\\([\\\\\\/\\\"bfnrt])|\\\\u([0-9a-fA-F]{4})|([^\\x01-\\x1F\\\\\\\"]+))");



	/**
	 * Chaine utilis�e pour chaque niveau d'indentation.
	 */
	public static final String INDENTATION = "\t";



	/**
	 * Modele de syntaxe des nombres entiers.
	 */
	private static final Pattern INTEGER_PATTERN = Pattern.compile("\\A(-?(?:0|[1-9][0-9]*))\\z");



	/**
	 * Mod�le de syntaxe des bool�ens.
	 */
	private static final Pattern NULL_PATTERN = Pattern.compile("\\A(null)");



	/**
	 * Mod�le de syntaxe des nombres.
	 */
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\A(-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?)");



	/**
	 * Mod�le de syntaxe des fragments de chaines.
	 */
	public static final Pattern STRING_FRAGMENT_PATTERN = Pattern.compile("\\A(\\\\[\\\\\\/\\\"bfnrt]|\\\\u[0-9a-fA-F]{4}|[^\\x01-\\x1F\\\\\\\"]+)");



}
