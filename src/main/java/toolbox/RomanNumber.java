package toolbox;



/**
 * La classe {@link RomanNumber} implémente un nombre romain.<br>
 * La valeur doit être dans l'intervalle [1..3999].<br>
 * Il peut être construit depuis un entier ou une chaine représentant le nombre romain.
 * @author Ludovic WALLE
 */
public class RomanNumber {



	/**
	 * @param value Valeur numérique.
	 * @throws NumberFormatException Si le nombre n'est pas dans l'intervalle [1..3999].
	 */
	public RomanNumber(int value) throws NumberFormatException {
		if ((value < 1) || (value > 3999)) {
			throw new NumberFormatException("La valeur n'est pas dans l'intervalle [1..3999]: " + value);
		}
		this.value = value;
	}



	/**
	 * @param romanNumber Nombre romain. Peut être en majuscules ou en minuscules, mais la casse ne doit pas être mélangée.
	 * @throws NumberFormatException Si le nombre romain est invalide.
	 */
	public RomanNumber(String romanNumber) throws NumberFormatException {
		int iRomanNumber = 0;
		char romanDigit;
		int romanDigitValue;
		int nextRomanDigitValue;
		int converted = 0;

		if (romanNumber.length() == 0) {
			throw new NumberFormatException("Le nombre romain est vide.");
		}
		if (!romanNumber.toUpperCase().equals(romanNumber) && !romanNumber.toLowerCase().equals(romanNumber)) {
			throw new NumberFormatException("La casse des chiffres du nombre romain est mélangée: " + romanNumber);
		}

		while (iRomanNumber < romanNumber.length()) {
			romanDigit = romanNumber.charAt(iRomanNumber);
			romanDigitValue = getRomanDigitNumericalValue(romanDigit);
			if (romanDigitValue < 0) {
				throw new NumberFormatException("\"" + romanDigit + "\" n'est pas un chiffre romain: " + romanNumber);
			}
			iRomanNumber++;

			if (iRomanNumber == romanNumber.length()) {
				/* dernier chiffre romain du nombre => ajouter simplement */
				converted += romanDigitValue;
			} else {
				/* d'autres chiffres romains suivent => combiner si la valeur du chiffre qui suit est supérieure à celle de ce chiffre */
				nextRomanDigitValue = getRomanDigitNumericalValue(romanNumber.charAt(iRomanNumber));
				if (nextRomanDigitValue < 0) {
					throw new NumberFormatException("\"" + nextRomanDigitValue + "\" n'est pas un chiffre romain: " + romanNumber);
				}
				if (nextRomanDigitValue > romanDigitValue) {
					converted += nextRomanDigitValue - romanDigitValue;
					iRomanNumber++;
				} else {
					converted += romanDigitValue;
				}
			}
		}

		if (converted > 3999) {
			throw new NumberFormatException("Le nombre romain n'est pas dans l'intervalle [1..3999]: " + romanNumber);
		}

		value = converted;
	}



	/**
	 * Retourne la valeur de ce nombre romain.
	 * @return La valeur de ce nombre romain.
	 */
	public int getValue() {
		return value;
	}



	/**
	 * Retourne ce nombre romain sous forme de chaine.
	 */
	@Override public String toString() {
		StringBuilder romanNumber = new StringBuilder();
		int remaining = value;

		for (int i = 0; i < ROMAN_TOKENS_VALUES.length; i++) {
			while (remaining >= ROMAN_TOKENS_VALUES[i]) {
				romanNumber.append(ROMAN_TOKENS[i]);
				remaining -= ROMAN_TOKENS_VALUES[i];
			}
		}
		return romanNumber.toString();
	}



	/**
	 * Retourne la valeur du chiffre romain indiqué, ou -1 si ce n'est pas un chiffre valide.
	 * @param romanDigit Chiffre romain.
	 * @return La valeur du chiffre romain indiqué, ou -1 si ce n'est pas un chiffre valide.
	 */
	private static int getRomanDigitNumericalValue(char romanDigit) {
		switch (Character.toUpperCase(romanDigit)) {
		case 'I':
			return 1;
		case 'V':
			return 5;
		case 'X':
			return 10;
		case 'L':
			return 50;
		case 'C':
			return 100;
		case 'D':
			return 500;
		case 'M':
			return 1000;
		default:
			return -1;
		}
	}



	/**
	 * Valeur du nombre romain.
	 */
	private final int value;



	/**
	 * Chiffres romains. Fonctionne en parallèle avec {@link #ROMAN_TOKENS_VALUES}.
	 */
	private static final String[] ROMAN_TOKENS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};



	/**
	 * Valeur correspondant aux chiffres romains de {@link #ROMAN_TOKENS}.
	 */
	private static final int[] ROMAN_TOKENS_VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};



}
