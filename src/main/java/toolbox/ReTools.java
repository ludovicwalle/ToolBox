package toolbox;

import java.util.regex.*;



/**
 * La classe {@link #ReTools} contient des méthodes statiques d'usage général relatives aux expressions régulières.
 * @author walle
 */
public abstract class ReTools {



	/**
	 * Retourne l'expression régulière correspondant à l'intervalle de caractères indiqué.<br>
	 * Le caractère inférieur doit être inférieur ou égal au caractère supérieur.
	 * @param min Caractère inférieur.
	 * @param max Caractère supérieur.
	 * @return L'expression régulière correspondant à l'intervalle de caractères indiqué.
	 */
	public static final String charRangeRE(char min, char max) {
		if (min > max) {
			throw new IllegalArgumentException(min + " > " + max);
		}
		return (min == max) ? String.valueOf((Character.isLetterOrDigit(min) ? min : String.format("\\u%04X", min))) : "[" + (Character.isLetterOrDigit(min) ? min : String.format("\\u%04X", min)) + "-" + (Character.isLetterOrDigit(max) ? max : String.format("\\u%04X", max)) + "]";
	}



	/**
	 * Retourne l'expression régulière correspondant à l'intervalle de caractères indiqué avec le nombre d'occurrences indiqué.<br>
	 * Le caractère inférieur doit être inférieur ou égal au caractère supérieur.
	 * @param min Caractère inférieur.
	 * @param max Caractère supérieur.
	 * @param count Nombre d'occurrences (doit être positif ou nul).
	 * @return L'expression régulière correspondant à l'intervalle de caractères indiqué avec le nombre d'occurrences indiqué.
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
	 * Retourne l'expression régulière correspondant à des chiffres avec le nombre d'occurrences indiqué.
	 * @param count Nombre d'occurrences (doit être positif ou nul).
	 * @return L'expression régulière correspondant à des chiffres avec le nombre d'occurrences indiqué.
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
	 * Calcule l'expression régulière correspondant aux caractères indiqués encodés sous la forme java.
	 * @param flags Tableau d'indicateurs de caractères (l'indice est le numéro unicode du caractère) (ne doit pas être <code>null</code> ni contenir plus de 0xFFFF cases).
	 * @return L'expression régulière.
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
	 * Calcule l'expression régulière correspondant à l'intervalle indiqué. Les bornes doivent être positives, et être dans le bon ordre. Elles peuvent être égales.<br>
	 * La borne inférieure sera complétée par des zéros à gauche pour avoir le même nombre de chiffres que la borne supérieure.
	 * @param min Borne inférieure.
	 * @param max Borne supérieure.
	 * @return L'expression régulière correspondant à l'intervalle indiqué.
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
	 * Calcule l'expression régulière correspondant à l'intervalle indiqué. Les bornes doivent être constituées uniquement de chiffres, avoir la même longueur, et les bornes doivent être dans le bon
	 * ordre. Les bornes peuvent être égales ou vides.
	 * @param min Borne inférieure (ne doit pas être <code>null</code>).
	 * @param max Borne supérieure (ne doit pas être <code>null</code>).
	 * @return L'expression régulière correspondant à l'intervalle indiqué.
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
			throw new IllegalArgumentException("La chaine n'est pas constituée que de chiffres: " + min);
		}
		if (!PATTERN.matcher(max).matches()) {
			throw new IllegalArgumentException("La chaine n'est pas constituée que de chiffres: " + max);
		}
		if (min.length() != max.length()) {
			throw new IllegalArgumentException("Les chaines n'ont pas la même longueur: " + min + " " + max);
		}
		if (min.compareTo(max) > 0) {
			throw new IllegalArgumentException(min + " > " + max);
		}
		/* les chiffres communs du début sont pris tels quels en début de RE */
		while ((firstDifferenceIndex <= lastIndex) && (min.charAt(firstDifferenceIndex) == max.charAt(firstDifferenceIndex))) {
			regexp.append(min.charAt(firstDifferenceIndex++));
		}
		/* les 0 de fin de min et les 9 de fin de max génèreront [0-9]{#} en fin de RE */
		while ((firstDifferenceIndex <= lastDifferenceIndex) && (min.charAt(lastDifferenceIndex) == '0') && (max.charAt(lastDifferenceIndex) == '9')) {
			lastDifferenceIndex--;
		}
		/* reste une éventuelle zone avec des différences */
		if (firstDifferenceIndex > lastDifferenceIndex) {
			/* pas de différence */
		} else if (firstDifferenceIndex == lastDifferenceIndex) {
			/* 1 caractère différent */
			regexp.append(charRangeRE(min.charAt(firstDifferenceIndex), max.charAt(firstDifferenceIndex)));
		} else {
			/* plusieurs caractères diffèrent */
			/* à ce niveau, les caractères à l'index firstDifferenceIndex sont différents, et le caractère à l'index lastDifferenceIndex est différent de 0 dans min et/ou différent de 9 dans max */
			regexp.append("(?:");
			/* chercher la position du dernier caractère autre que 9 dans la partie differente de max */
			lastNonNineIndex = lastDifferenceIndex;
			while ((firstDifferenceIndex <= lastNonNineIndex) && (max.charAt(lastNonNineIndex) == '9')) {
				lastNonNineIndex--;
			}
			/* chercher la position du dernier caractère autre que 0 dans la partie differente de min */
			lastNonZeroIndex = lastDifferenceIndex;
			while ((firstDifferenceIndex < lastNonZeroIndex) && (min.charAt(lastNonZeroIndex) == '0')) {
				lastNonZeroIndex--;
			}
			if (firstDifferenceIndex < lastNonZeroIndex) {
				/* il y a un chiffre autre que 0 dans les chiffres qui suivent le premier de min (borne inférieure complexe) */
				/* mettre la portion différente de min avec [0-9]{#} pour les zéro de fin */
				regexp.append(separator + min.substring(firstDifferenceIndex, lastNonZeroIndex) + charRangeRE(min.charAt(lastNonZeroIndex), '9') + charRangeRE('0', '9', lastDifferenceIndex - lastNonZeroIndex));
				separator = "|";
				/* mettre la portion différente de min progressivement raccourcie, avec le dernier chiffre augmenté de 1 et [0-9]{#} pour la fin */
				for (int index = lastNonZeroIndex - 1; firstDifferenceIndex < index; index--) {
					if (min.charAt(index) != '9') {
						regexp.append(separator + min.substring(firstDifferenceIndex, index) + charRangeRE((char) (min.charAt(index) + 1), '9') + charRangeRE('0', '9', lastDifferenceIndex - index));
						separator = "|";
					}
				}
				if (firstDifferenceIndex < lastNonNineIndex) {
					/* il y a un chiffre autre que 9 dans les chiffres qui suivent le premier de max (borne supérieure complexe) */
					/* mettre les chiffres compris entre les premiers de min et de max (exclus) avec [0-9]{#} pour le reste */
					if ((min.charAt(firstDifferenceIndex) + 1) <= (max.charAt(firstDifferenceIndex) - 1)) {
						regexp.append(separator + charRangeRE((char) (min.charAt(firstDifferenceIndex) + 1), (char) (max.charAt(firstDifferenceIndex) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
						separator = "|";
					}
					/* mettre la portion différente de max progressivement augmentée, avec le dernier chiffre diminué de 1 et [0-9]{#} pour la fin */
					for (int index = firstDifferenceIndex + 1; index < lastNonNineIndex; index++) {
						if (max.charAt(index) != '0') {
							regexp.append(separator + max.substring(firstDifferenceIndex, index) + charRangeRE('0', (char) (max.charAt(index) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - index));
							separator = "|";
						}
					}
					/* mettre la portion différente de max avec [0-9]{#} pour les neuf de fin */
					regexp.append(separator + max.substring(firstDifferenceIndex, lastNonNineIndex) + charRangeRE('0', max.charAt(lastNonNineIndex)) + charRangeRE('0', '9', lastDifferenceIndex - lastNonNineIndex));
					separator = "|";
				} else {
					/* il n'y a que des 9 après le premier chiffre de max (borne supérieure simple) => inclure dans la portion à un seul chiffre et le reste libre */
					if ((min.charAt(firstDifferenceIndex) + 1) <= max.charAt(firstDifferenceIndex)) {
						regexp.append(separator + charRangeRE((char) (min.charAt(firstDifferenceIndex) + 1), max.charAt(firstDifferenceIndex)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
						separator = "|";
					}
				}
			} else {
				/* il n'y a que des 0 après le premier chiffre de min (borne inférieure simple) => inclure dans la portion à un seul chiffre et le reste libre */
				regexp.append(separator + charRangeRE(min.charAt(firstDifferenceIndex), (char) (max.charAt(firstDifferenceIndex) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - firstDifferenceIndex));
				separator = "|";
				for (int index = firstDifferenceIndex + 1; index < lastNonNineIndex; index++) {
					if (max.charAt(index) != '0') {
						regexp.append(separator + max.substring(firstDifferenceIndex, index) + charRangeRE('0', (char) (max.charAt(index) - 1)) + charRangeRE('0', '9', lastDifferenceIndex - index));
						separator = "|";
					}
				}
				/* mettre la portion différente de max avec [0-9]{#} pour les neuf de fin */
				regexp.append(separator + max.substring(firstDifferenceIndex, lastNonNineIndex) + charRangeRE('0', max.charAt(lastNonNineIndex)) + charRangeRE('0', '9', lastDifferenceIndex - lastNonNineIndex));
			}
			regexp.append(")");
		}
		/* générer les [0-9]{#} en fin de RE pour les 0 de fin de min et les 9 de fin de max */
		regexp.append(charRangeRE('0', '9', (lastIndex - lastDifferenceIndex)));
		return regexp.toString();
	}



	/**
	 * Echape les caractères de la chaine indiquée pour qu'elle soit interprétée littéralement dans une chaine d'expression régulière.
	 * @param string Chaine.
	 * @return La chaine échapée.
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
	 * Calcule l'expression régulière correspondant aux caractères indiqués encodés sous la forme XML RefDoc.
	 * @param flags Tableau d'indicateurs de caractères (l'indice est le numéro unicode du caractère) (ne doit pas être <code>null</code> ni contenir plus de 0xFFFF cases).
	 * @return L'expression régulière.
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
	 * Modèle pour les bornes d'intervalle.
	 */
	private static final Pattern PATTERN = Pattern.compile("[0-9]*");



}
