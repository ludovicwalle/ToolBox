package toolbox;

import java.util.regex.*;



/**
 * La classe {@link #ReTools} contient des m�thodes statiques d'usage g�n�ral relatives aux expressions r�guli�res.
 * @author walle
 */
public abstract class ReTools {



	/**
	 * Retourne l'expression r�guli�re correspondant � l'intervalle de caract�res indiqu�.<br>
	 * Le caract�re inf�rieur doit �tre inf�rieur ou �gal au caract�re sup�rieur.
	 * @param min Caract�re inf�rieur.
	 * @param max Caract�re sup�rieur.
	 * @return L'expression r�guli�re correspondant � l'intervalle de caract�res indiqu�.
	 */
	public static final String charRangeRE(char min, char max) {
		if (min > max) {
			throw new IllegalArgumentException(min + " > " + max);
		}
		return (min == max) ? String.valueOf((Character.isLetterOrDigit(min) ? min : String.format("\\u%04X", min))) : "[" + (Character.isLetterOrDigit(min) ? min : String.format("\\u%04X", min)) + "-" + (Character.isLetterOrDigit(max) ? max : String.format("\\u%04X", max)) + "]";
	}



	/**
	 * Retourne l'expression r�guli�re correspondant � l'intervalle de caract�res indiqu� avec le nombre d'occurrences indiqu�.<br>
	 * Le caract�re inf�rieur doit �tre inf�rieur ou �gal au caract�re sup�rieur.
	 * @param min Caract�re inf�rieur.
	 * @param max Caract�re sup�rieur.
	 * @param count Nombre d'occurrences (doit �tre positif ou nul).
	 * @return L'expression r�guli�re correspondant � l'intervalle de caract�res indiqu� avec le nombre d'occurrences indiqu�.
	 */
	public static final String charRangeRE(char min, char max, int count) {
		if (count < 0) {
			throw new IllegalArgumentException(String.valueOf(count));
		} else if (count == 0) {
			return "";
		} else if (count == 1) {
			return charRangeRE(min, max);
		} else {
			return charRangeRE(min, max) + "{" + count + "}";
		}
	}



	/**
	 * Retourne l'expression r�guli�re correspondant � des chiffres avec le nombre d'occurrences indiqu�.
	 * @param count Nombre d'occurrences (doit �tre positif ou nul).
	 * @return L'expression r�guli�re correspondant � des chiffres avec le nombre d'occurrences indiqu�.
	 */
	public static final String digitsRE(int count) {
		if (count < 0) {
			throw new IllegalArgumentException(String.valueOf(count));
		} else if (count == 0) {
			return "";
		} else if (count == 1) {
			return "[0-9]";
		} else {
			return "[0-9]{" + count + "}";
		}
	}



	/**
	 * Calcule l'expression r�guli�re correspondant aux caract�res indiqu�s encod�s sous la forme java.
	 * @param flags Tableau d'indicateurs de caract�res (l'indice est le num�ro unicode du caract�re) (ne doit pas �tre <code>null</code> ni contenir plus de 0xFFFF cases).
	 * @return L'expression r�guli�re.
	 */
	public static final String javaPatternString(boolean[] flags) {
		StringBuilder java = new StringBuilder();

		if (flags.length > 0x10000) {
			throw new IllegalArgumentException();
		}
		java.append("[");
		for (int unicodeNumber = 0; unicodeNumber < flags.length; unicodeNumber++) {
			if (flags[unicodeNumber]) {
				if ((unicodeNumber == 0) || !flags[unicodeNumber - 1]) {
					java.append(String.format("\\u%04X", unicodeNumber));
				} else if ((unicodeNumber == (flags.length - 1)) || !flags[unicodeNumber + 1]) {
					if ((unicodeNumber > 1) && flags[unicodeNumber - 2]) {
						java.append("-");
					}
					java.append(String.format("\\u%04X", unicodeNumber));
				}
			}
		}
		java.append("]");
		return (java.length() == 2) ? "" : java.toString();
	}



	/**
	 * Calcule l'expression r�guli�re correspondant � l'intervalle indiqu�. Les bornes doivent �tre positives, et �tre dans le bon ordre. Elles peuvent �tre �gales.<br>
	 * La borne inf�rieure sera compl�t�e par des z�ros � gauche pour avoir le m�me nombre de chiffres que la borne sup�rieure.
	 * @param min Borne inf�rieure.
	 * @param max Borne sup�rieure.
	 * @return L'expression r�guli�re correspondant � l'intervalle indiqu�.
	 */
	public static final String numberRangeRE(int min, int max) {
		if (0 > min) {
			throw new IllegalArgumentException(min + " < " + 0);
		}
		if (min > max) {
			throw new IllegalArgumentException(min + " > " + max);
		}
		return numberRangeRE(String.format("%0" + OtherTools.digitCount(max) + "d", min), String.valueOf(max));
	}



	/**
	 * Calcule l'expression r�guli�re correspondant � l'intervalle indiqu�. Les bornes doivent �tre constitu�es uniquement de chiffres, avoir la m�me longueur, et les bornes doivent �tre dans le bon
	 * ordre. Les bornes peuvent �tre �gales ou vides.
	 * @param min Borne inf�rieure (ne doit pas �tre <code>null</code>).
	 * @param max Borne sup�rieure (ne doit pas �tre <code>null</code>).
	 * @return L'expression r�guli�re correspondant � l'intervalle indiqu�.
	 */
	public static final String numberRangeRE(String min, String max) {
		String separator = "";
		StringBuilder regexp = new StringBuilder();
		int lastIndex = min.length() - 1;
		int firstDifferenceIndex = 0;
		int lastDifferenceIndex = lastIndex;
		int lastNonNineIndex;
		int lastNonZeroIndex;

		if (!PATTERN.matcher(min).matches()) {
			throw new IllegalArgumentException("La chaine n'est pas constitu�e que de chiffres: " + min);
		}
		if (!PATTERN.matcher(max).matches()) {
			throw new IllegalArgumentException("La chaine n'est pas constitu�e que de chiffres: " + max);
		}
		if (min.length() != max.length()) {
			throw new IllegalArgumentException("Les chaines n'ont pas la m�me longueur: " + min + " " + max);
		}
		if (min.compareTo(max) > 0) {
			throw new IllegalArgumentException(min + " > " + max);
		}
		/* les chiffres communs du d�but sont pris tels quels en d�but de RE */
		while ((firstDifferenceIndex <= lastIndex) && (min.charAt(firstDifferenceIndex) == max.charAt(firstDifferenceIndex))) {
			regexp.append(min.charAt(firstDifferenceIndex++));
		}
		/* les 0 de fin de min et les 9 de fin de max g�n�reront [0-9]{#} en fin de RE */
		while ((firstDifferenceIndex <= lastDifferenceIndex) && (min.charAt(lastDifferenceIndex) == '0') && (max.charAt(lastDifferenceIndex) == '9')) {
			lastDifferenceIndex--;
		}
		/* reste une �ventuelle zone avec des diff�rences */
		if (firstDifferenceIndex > lastDifferenceIndex) {
			/* pas de diff�rence */
		} else if (firstDifferenceIndex == lastDifferenceIndex) {
			/* 1 caract�re diff�rent */
			regexp.append(charRangeRE(min.charAt(firstDifferenceIndex), max.charAt(firstDifferenceIndex)));
		} else {
			/* plusieurs caract�res diff�rent */
			/* � ce niveau, les caract�res � l'index firstDifferenceIndex sont diff�rents, et le caract�re � l'index lastDifferenceIndex est diff�rent de 0 dans min et/ou diff�rent de 9 dans max */
			regexp.append("(?:");
			/* chercher la position du dernier caract�re autre que 9 dans la partie differente de max */
			lastNonNineIndex = lastDifferenceIndex;
			while ((firstDifferenceIndex <= lastNonNineIndex) && (max.charAt(lastNonNineIndex) == '9')) {
				lastNonNineIndex--;
			}
			/* chercher la position du dernier caract�re autre que 0 dans la partie differente de min */
			lastNonZeroIndex = lastDifferenceIndex;
			while ((firstDifferenceIndex < lastNonZeroIndex) && (min.charAt(lastNonZeroIndex) == '0')) {
				lastNonZeroIndex--;
			}
			if (firstDifferenceIndex < lastNonZeroIndex) {
				/* il y a un chiffre autre que 0 dans les chiffres qui suivent le premier de min (borne inf�rieure complexe) */
				/* mettre la portion diff�rente de min avec [0-9]{#} pour les z�ro de fin */
				regexp.append(separator + min.substring(firstDifferenceIndex, lastNonZeroIndex) + charRangeRE(min.charAt(lastNonZeroIndex), '9') + charRangeRE('0', '9', lastDifferenceIndex - lastNonZeroIndex));
				separator = "|";
				/* mettre la portion diff�rente de min progressivement raccourcie, avec le dernier chiffre augment� de 1 et [0-9]{#} pour la fin */
				for (int index = lastNonZeroIndex - 1; firstDifferenceIndex < index; index--) {
					if (min.charAt(index) != '9') {
						regexp.append(separator + min.substring(firstDifferenceIndex, index) + charRangeRE((char) (min.charAt(index) + 1), '9') + charRangeRE('0', '9', lastDifferenceIndex - index));
						separator = "|";
					}
				}
				if (firstDifferenceIndex < lastNonNineIndex) {
					/* il y a un chiffre autre que 9 dans les chiffres qui suivent le premier de max (borne sup�rieure complexe) */
					/* mettre les chiffres compris entre les premiers de min et de max (exclus) avec [0-9]{#} pour le reste */
					if ((min.charAt(firstDifferenceIndex) + 1) <= (max.charAt(firstDifferenceIndex) - 1)) {
						regexp.append(separator + charRangeRE((char) (min.charAt(firstDifferenceIndex) + 1), (char) (max.charAt(firstDifferenceIndex) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
						separator = "|";
					}
					/* mettre la portion diff�rente de max progressivement augment�e, avec le dernier chiffre diminu� de 1 et [0-9]{#} pour la fin */
					for (int index = firstDifferenceIndex + 1; index < lastNonNineIndex; index++) {
						if (max.charAt(index) != '0') {
							regexp.append(separator + max.substring(firstDifferenceIndex, index) + charRangeRE('0', (char) (max.charAt(index) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - index));
							separator = "|";
						}
					}
					/* mettre la portion diff�rente de max avec [0-9]{#} pour les neuf de fin */
					regexp.append(separator + max.substring(firstDifferenceIndex, lastNonNineIndex) + charRangeRE('0', max.charAt(lastNonNineIndex)) + charRangeRE('0', '9', lastDifferenceIndex - lastNonNineIndex));
					separator = "|";
				} else {
					/* il n'y a que des 9 apr�s le premier chiffre de max (borne sup�rieure simple) => inclure dans la portion � un seul chiffre et le reste libre */
					if ((min.charAt(firstDifferenceIndex) + 1) <= max.charAt(firstDifferenceIndex)) {
						regexp.append(separator + charRangeRE((char) (min.charAt(firstDifferenceIndex) + 1), max.charAt(firstDifferenceIndex)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
						separator = "|";
					}
				}
			} else {
				/* il n'y a que des 0 apr�s le premier chiffre de min (borne inf�rieure simple) => inclure dans la portion � un seul chiffre et le reste libre */
				regexp.append(separator + charRangeRE(min.charAt(firstDifferenceIndex), (char) (max.charAt(firstDifferenceIndex) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
				separator = "|";
				for (int index = firstDifferenceIndex + 1; index < lastNonNineIndex; index++) {
					if (max.charAt(index) != '0') {
						regexp.append(separator + max.substring(firstDifferenceIndex, index) + charRangeRE('0', (char) (max.charAt(index) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - index));
						separator = "|";
					}
				}
				/* mettre la portion diff�rente de max avec [0-9]{#} pour les neuf de fin */
				regexp.append(separator + max.substring(firstDifferenceIndex, lastNonNineIndex) + charRangeRE('0', max.charAt(lastNonNineIndex)) + charRangeRE('0', '9', lastDifferenceIndex - lastNonNineIndex));
			}
			regexp.append(")");
		}
		/* g�n�rer les [0-9]{#} en fin de RE pour les 0 de fin de min et les 9 de fin de max */
		regexp.append(charRangeRE('0', '9', (lastIndex - lastDifferenceIndex)));
		return regexp.toString();
	}



	/**
	 * Echape les caract�res de la chaine indiqu�e pour qu'elle soit interpr�t�e litt�ralement dans une chaine d'expression r�guli�re.
	 * @param string Chaine.
	 * @return La chaine �chap�e.
	 */
	public static final String patternEscape(String string) {
		StringBuilder escaped = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			switch (string.charAt(i)) {
			case '\n':
				escaped.append("\\n");
				break;
			case '\r':
				escaped.append("\\r");
				break;
			case '\t':
				escaped.append("\\t");
				break;
			case '\\':
				escaped.append("\\\\");
				break;
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case '.':
			case '*':
			case ' ':
			case '?':
			case '#':
			case '^':
			case '$':
			case '+':
			case '|':
				escaped.append("\\" + string.charAt(i));
				break;
			default:
				escaped.append(string.charAt(i));
				break;
			}
		}
		return escaped.toString();
	}



	/**
	 * Calcule l'expression r�guli�re correspondant aux caract�res indiqu�s encod�s sous la forme XML RefDoc.
	 * @param flags Tableau d'indicateurs de caract�res (l'indice est le num�ro unicode du caract�re) (ne doit pas �tre <code>null</code> ni contenir plus de 0xFFFF cases).
	 * @return L'expression r�guli�re.
	 */
	public static final String xmlPatternString(boolean[] flags) {
		StringBuilder xml = new StringBuilder();
		String separator = "";

		if (flags.length > 0x10000) {
			throw new IllegalArgumentException();
		}
		xml.append("(");
		for (int unicodeNumber = 0; unicodeNumber < flags.length; unicodeNumber++) {
			if (flags[unicodeNumber]) {
				xml.append(separator + patternEscape(((unicodeNumber == '&') || (unicodeNumber == '<') || (unicodeNumber == '>') || (unicodeNumber == '"') || (unicodeNumber == '\'') || (unicodeNumber >= 128)) ? String.format("&#%d;", unicodeNumber) : String.valueOf((char) unicodeNumber)));
				separator = "|";
			}
		}
		xml.append(")");
		return xml.toString();
	}



	/**
	 * Mod�le pour les bornes d'intervalle.
	 */
	private static final Pattern PATTERN = Pattern.compile("[0-9]*");



}
