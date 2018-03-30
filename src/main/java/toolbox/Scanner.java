package toolbox;

import java.util.regex.*;



/**
 * La classe {@link Scanner} facilite le parcours de chaines. Elle associe une chaine de caract�re et une position courante.<br>
 * <br>
 * Lorsque la position courante est modifi�e, aucun contr�le de validit� n'est effectu�. La non validit� �ventuelle de la position courante n'est d�tect�e que lors de l'acc�s � la chaine.<br>
 * La chaine ne doit pas �tre <code>null</code>, mais elle peut �tre vide.
 * @author Ludovic WALLE
 */
public final class Scanner {



	/**
	 * Construit un nouveau {@link Scanner} avec la chaine passe en param�tre comme chaine � traiter, et 0 comme position courante.
	 * @param string La chaine � traiter (ne doit pas �tre <code>null</code>).
	 */
	public Scanner(String string) {
		if (string == null) {
			throw new NullPointerException();
		}
		this.string = string;
		index = 0;
	}



	/**
	 * Teste si le caract�re � la position indiqu�e n'est aucun de ceux indiqu�s.
	 * @param index Position absolue du caract�re � tester.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position indiqu�e est valide et que le caract�re � cette position n'est aucun de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean absoluteCharIsNoneOf(@SuppressWarnings("hiding") int index, char... expectedChars) {
		return (index >= 0) && (index < string.length()) && charIsNoneOf(string.charAt(index), expectedChars);
	}



	/**
	 * Teste si le caract�re � la position indiqu�e est un de ceux indiqu�s.
	 * @param index Position absolue du caract�re � tester.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position indiqu�e est valide et que le caract�re � cette position est un de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean absoluteCharIsOneOf(@SuppressWarnings("hiding") int index, char... expectedChars) {
		return (index >= 0) && (index < string.length()) && charIsOneOf(string.charAt(index), expectedChars);
	}



	/**
	 * Teste si le caract�re � la position courante n'est aucun de ceux indiqu�s.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position courante est valide et que le caract�re � cette position n'est aucun de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean currentCharIsNoneOf(char... expectedChars) {
		return (index >= 0) && (index < string.length()) && charIsNoneOf(string.charAt(index), expectedChars);
	}



	/**
	 * Teste si le caract�re � la position courante est un de ceux indiqu�s.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position courante est valide et que le caract�re � cette position est un de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean currentCharIsOneOf(char... expectedChars) {
		return (index >= 0) && (index < string.length()) && charIsOneOf(string.charAt(index), expectedChars);
	}



	/**
	 * Retourne le caract�re � la position absolue indiqu�e.
	 * @param index Position absolue du caract�re.
	 * @return Le caract�re � la position indiqu�e.
	 * @throws StringIndexOutOfBoundsException Si la position courante est en dehors de la chaine.
	 */
	public char getAbsoluteChar(@SuppressWarnings("hiding") int index) throws StringIndexOutOfBoundsException {
		return string.charAt(index);
	}



	/**
	 * Retourne le nombre de caract�res entre la position courante (comprise) et la fin de la chaine.<br>
	 * La valeur retourn�e est 0 si la position courante est �gale au nombre de caract�res de la chaine, et n�gative si elle est sup�rieure.
	 * @return Le nombre de caract�res apr�s la position courante.
	 */
	public int getCharsToParseCount() {
		return string.length() - index;
	}



	/**
	 * Retourne le caract�re � la position courante.<br>
	 * La position courante n'est pas modifi�e.
	 * @return Le caract�re � la position courante.
	 * @throws StringIndexOutOfBoundsException Si la position courante est en dehors de la chaine.
	 */
	public char getCurrentChar() throws StringIndexOutOfBoundsException {
		return string.charAt(index);
	}



	/**
	 * Retourne le caract�re � la position courante puis modifie la position courante de la valeur indiqu�e.<br>
	 * La position courante est incr�ment�e de 1. Elle peut se retrouver en dehors de la chaine.
	 * @param charCount Nombre de caract�res.
	 * @return Le caract�re � l'ancienne position courante.
	 * @throws StringIndexOutOfBoundsException Si l'ancienne position courante est en dehors de la chaine.
	 */
	public char getCurrentCharThenMove(int charCount) throws StringIndexOutOfBoundsException {
		char currentChar;

		currentChar = string.charAt(index);
		index += charCount;
		return currentChar;
	}



	/**
	 * Retourne le caract�re � la position courante puis modifie la position courante de la valeur indiqu�e si ce caract�re est un de ceux indiqu�s en param�tre.<br>
	 * La position courante est incr�ment�e de la valeur indiqu�e ou inchang�e. Elle peut se retrouver en dehors de la chaine.
	 * @param condition Condition.
	 * @param charCount Nombre de caract�res.
	 * @return Le caract�re � la position courante.
	 * @throws StringIndexOutOfBoundsException Si la position courante est en dehors de la chaine.
	 */
	public char getCurrentCharThenMoveIf(boolean condition, int charCount) throws StringIndexOutOfBoundsException {
		char currentChar;

		currentChar = string.charAt(index);
		if (condition) {
			index += charCount;
		}
		return currentChar;
	}



	/**
	 * Retourne un extrait de la requ�te.<br>
	 * L'extrait commence au caract�re en position courrante - <code>before</code> et se termine au caract�re en position courante + <code>after</code>. Il est encadr� par des <code>...</code> pour
	 * indiquer que l'extrait est tronqu�, au d�but (sauf dans le cas o� l'extrait atteint le d�but de la chaine), et � la fin (sauf dans le cas o� l'extrait atteint la fin de la chaine).
	 * @param before Le nombre de caract�res � extraire avant la position dans la requ�te.
	 * @param after Le nombre de caract�res � extraire apr�s la position dans la requ�te.
	 * @return Un extrait de la requ�te.
	 */
	public String getFragment(int before, int after) {
		return getFragment(index, before, after);
	}



	/**
	 * Retourne un extrait de la requ�te.<br>
	 * L'extrait commence au caract�re en position <code>index - before</code> et se termine au caract�re en position <code>index + after</code>. Il est encadr� par des <code>...</code> pour indiquer
	 * que l'extrait est tronqu�, au d�but (sauf dans le cas o� l'extrait atteint le d�but de la chaine), et � la fin (sauf dans le cas o� l'extrait atteint la fin de la chaine).
	 * @param index La position dans la chaine.
	 * @param before Le nombre de caract�res � extraire avant la position dans la requ�te.
	 * @param after Le nombre de caract�res � extraire apr�s la position dans la requ�te.
	 * @return Un extrait de la requ�te.
	 */
	public String getFragment(@SuppressWarnings("hiding") int index, int before, int after) {
		int first;
		String textBefore;
		int last;
		String textAfter;

		if ((index - before) > 0) {
			first = index - before;
			textBefore = "...";
		} else {
			first = 0;
			textBefore = "";
		}
		if ((index + after) < string.length()) {
			last = index + after;
			textAfter = "...";
		} else {
			last = string.length();
			textAfter = "";
		}
		return textBefore + string.substring(first, last) + textAfter;
	}



	/**
	 * Retourne la position courante.
	 * @return La position courante.
	 */
	public int getIndex() {
		return index;
	}



	/**
	 * Retourne la partie de la chaine correspondant au premier groupe capturant du motif indiqu� � partir de la position courante, ou <code>null</code> si la chaine ne correspond pas au motif.<br>
	 * La position courante est incr�ment�e de la longueur totale correspondant au motif.
	 * @param pattern Motif (doit �tre valide, et comporter au moins un groupe capturant).
	 * @return La partie de la chaine correspondant au premier groupe capturant du motif indiqu� � partir de la position courante, ou <code>null</code> si la chaine ne correspond pas au motif.
	 */
	public String getMatchingPart(Pattern pattern) {
		Matcher matcher;

		if ((index >= 0) && (index < string.length()) && (matcher = pattern.matcher(string).region(index, string.length())).find() && (matcher.start() == index)) {
			index = matcher.end();
			return matcher.group(1);
		} else {
			return null;
		}
	}



	/**
	 * Retourne le caract�re � la position relative indiqu�e.
	 * @param charCount Position relative du caract�re.
	 * @return Le caract�re � la position indique.
	 * @throws StringIndexOutOfBoundsException Si la position courante est en dehors de la chaine.
	 */
	public char getRelativeChar(int charCount) throws StringIndexOutOfBoundsException {
		return string.charAt(index + charCount);
	}



	/**
	 * Retourne la chaine.
	 * @return La chaine.
	 */
	public String getString() {
		return string;
	}



	/**
	 * Teste si la chaine est coml�tement trait�e.
	 * @return <code>true</code> si la chaine est compl�tement trait�e, <code>false</code> sinon.
	 */
	public boolean hasCharToParse() {
		return index < string.length();
	}



	/**
	 * Change la position courante du nombre de caract�res indiqu�.<br>
	 * Il n'y a aucune v�rification de la position courante, qui peut donc se retrouver en dehors de la chaine.
	 * @param charCount Nombre de caract�res.
	 */
	public void move(int charCount) {
		index += charCount;
	}



	/**
	 * Change la position courante du nombre de caract�res indiqu�, si la condition est vraie.<br>
	 * Il n'y a aucune v�rification de la position courante, qui peut donc se retrouver en dehors de la chaine.
	 * @param condition Condition.
	 * @param charCount Nombre de caract�res.
	 * @return La valeur de la condition.
	 */
	public boolean moveIf(boolean condition, int charCount) {
		if (condition) {
			index += charCount;
		}
		return condition;
	}



	/**
	 * Change la position courante du nombre de caract�res indiqu�, puis retourne le caract�re � cette position.<br>
	 * Il n'y a aucune v�rification de la position courante, qui peut donc se retrouver en dehors de la chaine.
	 * @param charCount Nombre de caract�res.
	 * @return Le caract�re � la nouvelle position courante.
	 * @throws StringIndexOutOfBoundsException Si la position courante est en dehors de la chaine.
	 */
	public char moveThenGetCurrentChar(int charCount) throws StringIndexOutOfBoundsException {
		return string.charAt(index += charCount);
	}



	/**
	 * Change la position courante du nombre de caract�res indiqu�, puis passe tous les caract�res class�s comme caract�res d'espacement � partir de cette position.<br>
	 * Il n'y a aucune v�rification de la position courante, qui peut donc se retrouver en dehors de la chaine.
	 * @param charCount Nombre de caract�res.
	 */
	public void moveThenSkipWhitespaces(int charCount) {
		index += charCount;
		while ((index >= 0) && (index < string.length()) && Character.isWhitespace(string.charAt(index))) {
			index++;
		}
	}



	/**
	 * Teste si le caract�re � la position relative indiqu�e n'est aucun de ceux indiqu�s.
	 * @param charCount Position du caract�re � tester, relativement � la position courante.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position indiqu�e est valide et que le caract�re � cette position n'est aucun de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean relativeCharIsNoneOf(int charCount, char... expectedChars) {
		return absoluteCharIsNoneOf(index + charCount, expectedChars);
	}



	/**
	 * Teste si le caract�re � la position relative indiqu�e est un de ceux indiqu�s.
	 * @param charCount Position du caract�re � tester, relativement � la position courante.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position indiqu�e est valide et que le caract�re � cette position est un de ceux indiqu�s, <code>false</code> sinon.
	 */
	public boolean relativeCharIsOneOf(int charCount, char... expectedChars) {
		return absoluteCharIsOneOf(index + charCount, expectedChars);
	}



	/**
	 * Sp�cifie la position indiqu�e comme position courante.<br>
	 * Il n'y a aucune v�rification de la position, qui peut donc se retrouver en dehors de la chaine.
	 * @param index Position.
	 */
	public void setIndex(int index) {
		this.index = index;
	}



	/**
	 * Passe tous les caract�res class�s comme caract�res d'espacement � partir de la position courante.<br>
	 * La position courante doit �tre valide.<br>
	 * La position courante est incr�ment�e du nombre de caract�res class�s comme caract�res d'espacement cons�cutifs � partir de la position courante. Elle peut se retrouver en dehors de la chaine,
	 * si il n'y avait pas d'autres caract�res avant la fin de la chaine.
	 */
	public void skipWhitespaces() {
		while ((index >= 0) && (index < string.length()) && Character.isWhitespace(string.charAt(index))) {
			index++;
		}
	}



	/**
	 * Teste si le caract�re indiqu� n'est aucun de ceux indiqu�s.
	 * @param thatChar Caract�re � tester.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position courante est valide et que le caract�re � cette position n'est aucun de ceux indiqu�s, <code>false</code> sinon.
	 */
	public static boolean charIsNoneOf(char thatChar, char... expectedChars) {
		for (char expectedChar : expectedChars) {
			if (thatChar == expectedChar) {
				return false;
			}
		}
		return true;
	}



	/**
	 * Teste si le caract�re indiqu� est un de ceux indiqu�s.
	 * @param thatChar Caract�re � tester.
	 * @param expectedChars Caract�res attendus.
	 * @return <code>true</code> si la position courante est valide et que le caract�re � cette position est un de ceux indiqu�s, <code>false</code> sinon.
	 */
	public static boolean charIsOneOf(char thatChar, char... expectedChars) {
		for (char expectedChar : expectedChars) {
			if (thatChar == expectedChar) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Position courante.<br>
	 * Cette position peut �tre en dehors de la chaine.
	 */
	private int index;



	/**
	 * Chaine � parcourir.
	 */
	private final String string;



}
