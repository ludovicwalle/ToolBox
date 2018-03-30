package toolbox;

import java.util.*;



/**
 * La classe {@link EditDistance} Calcule la distance d'�dition entre deux chaines.<br>
 * Un changement de caract�re compte comme un ajout + une suppression.
 * @author Ludovic WALLE
 */
public abstract class EditDistance {



	/**
	 * Calcule r�cursivement la distance d'�dition entre les deux chaines indiqu�es.
	 * @param one Premi�re chaine.
	 * @param two Deuxi�me chaine.
	 * @return La distance.
	 */
	public static int compute(String one, String two) {
		int temporaryDistance;
		int distance;
		int bestDistance;
		int start;
		int oneStart;
		int twoStart;
		int oneStop = one.length();
		int twoStop = two.length();
		int index;

		/* passer les parties identiques en d�but et fin */
		if (oneStop > twoStop) {
			for (start = 0; (start < twoStop) && (one.charAt(start) == two.charAt(start)); start++) {
				;
			}
			while ((start < twoStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				oneStop--;
				twoStop--;
			}
		} else {
			for (start = 0; (start < oneStop) && (one.charAt(start) == two.charAt(start)); start++) {
				;
			}
			while ((start < oneStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				oneStop--;
				twoStop--;
			}
		}
		if (oneStop == start) {
			/* le deuxi�me est �gal au premier plus une insertion d'une chaine */
			return twoStop - start;
		} else if (twoStop == start) {
			/* le premier est �gal au deuxi�me plus une insertion d'une chaine */
			return oneStop - start;
		} else if (((oneStop - start) <= 2) && ((twoStop - start) <= 2)) {
			/* un changement, ou deux changements, ou un changement et une insertion, ou un changement et une suppression */
			return ((oneStop - start) + twoStop) - start;
		} else {
			/* une chaine comporte au moins 3 caract�res, l'autre au moins un, le premier et le dernier caract�res des deux chaines sont diff�rents */
			/* passer les parties qui ne peuvent pas �tre identiques en d�but et fin */
			twoStart = oneStart = start;
			distance = 0;
			do {
				temporaryDistance = distance;
				while (((index = one.indexOf(two.charAt(twoStart), oneStart)) == -1) || (index >= oneStop)) {
					/* le premier caract�re de la deuxi�me chaine est absent de la premi�re */
					distance++;
					if (++twoStart >= twoStop) {
						return (distance + oneStop) - oneStart;
					}
				}
				while (((index = two.indexOf(one.charAt(oneStart), twoStart)) == -1) || (index >= twoStop)) {
					/* le premier caract�re de la premi�re chaine est absent de la deuxi�me */
					distance++;
					if (++oneStart >= oneStop) {
						return (distance + twoStop) - twoStart;
					}
				}
				while (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) == -1) || (index < oneStart)) {
					/* le dernier caract�re de la premi�re chaine est absent de la deuxi�me */
					distance++;
					if (--twoStop <= twoStart) {
						return (distance + oneStop) - oneStart;
					}
				}
				while (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) == -1) || (index < twoStart)) {
					/* le dernier caract�re de la deuxi�me chaine est absent de la premi�re */
					distance++;
					if (--oneStop <= oneStart) {
						return (distance + twoStop) - twoStart;
					}
				}
			} while (distance > temporaryDistance);
			if ((one.charAt(oneStart) == two.charAt(twoStart)) || (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				/* on retombe sur des parties communes en d�but ou en fin => appeler r�cursivement */
				return distance + compute(one.substring(oneStart, oneStop), two.substring(twoStart, twoStop));
			} else {
				/* passer une partie incompatible, en d�but ou en fin de chaque chaine */
				bestDistance = Integer.MAX_VALUE;
				if (((index = one.indexOf(two.charAt(twoStart), oneStart)) != -1) && (index < oneStop)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(index + 1, oneStop), two.substring(twoStart + 1, twoStop))) <= bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.indexOf(one.charAt(oneStart), twoStart)) != -1) && (index < twoStop)) {
					/* le premier caract�re de la premi�re chaine peut correspondre dans la deuxi�me */
					if ((temporaryDistance = compute(one.substring(oneStart + 1, oneStop), two.substring(index + 1, twoStop))) <= bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) != -1) && (index >= oneStart)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(oneStart, index), two.substring(twoStart, twoStop - 1))) <= bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) != -1) && (index >= twoStart)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(oneStart, oneStop - 1), two.substring(twoStart, index))) <= bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				return distance + bestDistance;
			}
		}
	}



	/**
	 * Calcule r�cursivement la distance d'�dition entre les deux chaines indiqu�es, avec une limite d'�valuation.
	 * @param one Premi�re chaine.
	 * @param two Deuxi�me chaine.
	 * @param tooFar Limite d'�valuation de la distance (arr�te l'�valuation si la distance est sup�rieure ou �gale � cette valeur).
	 * @return La distance.
	 */
	public static int compute(String one, String two, int tooFar) {
		int distance;

		if (tooFar >= TOO_FAR) {
			throw new IllegalArgumentException("La limite doit �tre strictement inf�rieure � " + TOO_FAR + ".");
		}
		if (((one.length() > two.length()) && ((one.length() - two.length()) >= tooFar)) || ((two.length() > one.length()) && ((two.length() - one.length()) >= tooFar)) || ((distance = compute(one, two, tooFar, 0)) >= tooFar)) {
			distance = TOO_FAR;
		}
		return distance;
	}



	/**
	 * Calcule r�cursivement la distance d'�dition entre les deux chaines indiqu�es, avec une limite d'�valuation.
	 * @param one Premi�re chaine.
	 * @param two Deuxi�me chaine.
	 * @param tooFar Limite d'�valuation de la distance (arr�te l'�valuation si la distance est sup�rieure ou �gale � cette valeur).
	 * @param distance Distance pour la partie d�j� trait�e.
	 * @return La distance.
	 */
	private static int compute(String one, String two, int tooFar, int distance) {
		int temporaryDistance;
		int bestDistance;
		int start;
		int oneStart;
		int twoStart;
		int oneStop = one.length();
		int twoStop = two.length();
		int index;

		/* passer les parties identiques en d�but et fin */
		if (oneStop > twoStop) {
			for (start = 0; (start < twoStop) && (one.charAt(start) == two.charAt(start)); start++) {
				;
			}
			while ((start < twoStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				oneStop--;
				twoStop--;
			}
		} else {
			for (start = 0; (start < oneStop) && (one.charAt(start) == two.charAt(start)); start++) {
				;
			}
			while ((start < oneStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				oneStop--;
				twoStop--;
			}
		}
		if (oneStop == start) {
			/* le deuxi�me est �gal au premier plus une insertion d'une chaine */
			return (distance + twoStop) - start;
		} else if (twoStop == start) {
			/* le premier est �gal au deuxi�me plus une insertion d'une chaine */
			return (distance + oneStop) - start;
		} else if (((oneStop - start) <= 2) && ((twoStop - start) <= 2)) {
			/* un changement, ou deux changements, ou un changement et une insertion, ou un changement et une suppression */
			return (((distance + oneStop) - start) + twoStop) - start;
		} else {
			/* une chaine comporte au moins 3 caract�res, l'autre au moins un, le premier et le dernier caract�res des deux chaines sont diff�rents */
			/* passer les parties qui ne peuvent pas �tre identiques en d�but et fin */
			twoStart = oneStart = start;
			do {
				temporaryDistance = distance;
				while (((index = one.indexOf(two.charAt(twoStart), oneStart)) == -1) || (index >= oneStop) || ((((distance + index) - oneStart) >= tooFar) && (((distance + oneStop) - index) >= tooFar))) {
					/* le premier caract�re de la deuxi�me chaine est absent de la premi�re ou trop loin */
					if (++twoStart >= twoStop) {
						return (distance + 1 + oneStop) - oneStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = two.indexOf(one.charAt(oneStart), twoStart)) == -1) || (index >= twoStop) || ((((distance + index) - twoStart) >= tooFar) && (((distance + twoStop) - index) >= tooFar))) {
					/* le premier caract�re de la premi�re chaine est absent de la deuxi�me ou trop loin */
					if (++oneStart >= oneStop) {
						return (distance + 1 + twoStop) - twoStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) == -1) || (index < oneStart) || ((((distance + oneStop) - index) >= tooFar) && (((distance + index) - oneStart) >= tooFar))) {
					/* le dernier caract�re de la premi�re chaine est absent de la deuxi�me ou trop loin */
					if (--twoStop <= twoStart) {
						return (distance + 1 + oneStop) - oneStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) == -1) || (index < twoStart) || ((((distance + twoStop) - index) >= tooFar) && (((distance + index) - twoStart) >= tooFar))) {
					/* le dernier caract�re de la deuxi�me chaine est absent de la premi�re ou trop loin */
					if (--oneStop <= oneStart) {
						return (distance + 1 + twoStop) - twoStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
			} while (distance > temporaryDistance);
			if ((one.charAt(oneStart) == two.charAt(twoStart)) || (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				/* on retombe sur des parties communes en d�but ou en fin => appeler r�cursivement */
				return compute(one.substring(oneStart, oneStop), two.substring(twoStart, twoStop), tooFar, distance);
			} else {
				/* passer une partie incompatible, en d�but ou en fin de chaque chaine */
				bestDistance = TOO_FAR;
				if (((index = one.indexOf(two.charAt(twoStart), oneStart)) != -1) && (index < oneStop) && (((distance + index) - oneStart) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(index + 1, oneStop), two.substring(twoStart + 1, twoStop), tooFar, (distance + index) - oneStart)) < bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.indexOf(one.charAt(oneStart), twoStart)) != -1) && (index < twoStop) && (((distance + index) - twoStart) < tooFar)) {
					/* le premier caract�re de la premi�re chaine peut correspondre dans la deuxi�me */
					if ((temporaryDistance = compute(one.substring(oneStart + 1, oneStop), two.substring(index + 1, twoStop), tooFar, (distance + index) - twoStart)) < bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) != -1) && (index >= oneStart) && (((distance + oneStop) - index) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(oneStart, index), two.substring(twoStart, twoStop - 1), tooFar, (distance + oneStop) - index)) < bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) != -1) && (index >= twoStart) && (((distance + twoStop) - index) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = compute(one.substring(oneStart, oneStop - 1), two.substring(twoStart, index), tooFar, (distance + twoStop) - index)) < bestDistance) {
						bestDistance = temporaryDistance;
					}
				}
				return bestDistance;
			}
		}
	}



	/**
	 * Calcule r�cursivement la distance d'�dition entre les deux chaines indiqu�es, avec une limite d'�valuation, en expliquant les diff�rences.
	 * @param one Premi�re chaine.
	 * @param two Deuxi�me chaine.
	 * @param tooFar Limite d'�valuation de la distance (arr�te l'�valuation si la distance est sup�rieure ou �gale � cette valeur).
	 * @param flagsOne Premi�re chaine, marqu�e.
	 * @param flagsTwo Premi�re chaine, marqu�e.
	 * @return La distance.
	 */
	public static int explain(String one, String two, int tooFar, boolean[] flagsOne, boolean[] flagsTwo) {
		int distance;

		if (tooFar >= TOO_FAR) {
			throw new IllegalArgumentException("La limite doit �tre strictement inf�rieure � " + TOO_FAR + ".");
		}
		if (one.length() != flagsOne.length) {
			throw new IllegalArgumentException("La longueur du premier tableau de bool�ens est diff�rente de la longueur de la premi�re chaine.");
		}
		if (two.length() != flagsTwo.length) {
			throw new IllegalArgumentException("La longueur du deuxi�me tableau de bool�ens est diff�rente de la longueur de la deuxi�me chaine.");
		}
		Arrays.fill(flagsOne, false);
		Arrays.fill(flagsTwo, false);
		if (((one.length() > two.length()) && ((one.length() - two.length()) >= tooFar)) || ((two.length() > one.length()) && ((two.length() - one.length()) >= tooFar))) {
			distance = TOO_FAR;
		} else if ((distance = explain(one, two, tooFar, flagsOne, flagsTwo, 0)) >= tooFar) {
			Arrays.fill(flagsOne, false);
			Arrays.fill(flagsTwo, false);
			distance = TOO_FAR;
		}
		return distance;
	}



	/**
	 * Calcule r�cursivement la distance d'�dition entre les deux chaines indiqu�es, avec une limite d'�valuation, en expliquant les diff�rences.
	 * @param one Premi�re chaine.
	 * @param two Deuxi�me chaine.
	 * @param tooFar Limite d'�valuation de la distance (arr�te l'�valuation si la distance est sup�rieure ou �gale � cette valeur).
	 * @param flagsOne Premi�re chaine, marqu�e.
	 * @param flagsTwo Premi�re chaine, marqu�e.
	 * @param distance Distance pour la partie d�j� trait�e.
	 * @return La distance.
	 */
	private static int explain(String one, String two, int tooFar, boolean[] flagsOne, boolean[] flagsTwo, int distance) {
		int temporaryDistance;
		int bestDistance;
		int index;
		int start = 0;
		int oneStart = 0;
		int twoStart = 0;
		int oneStop = one.length();
		int twoStop = two.length();
		boolean[] flagsSubOne;
		boolean[] flagsSubTwo;
		boolean[] bestFlagsOne = null;
		boolean[] bestFlagsTwo = null;

		/* passer les parties identiques en d�but et fin */
		if (oneStop > twoStop) {
			for (start = 0; (start < twoStop) && (one.charAt(start) == two.charAt(start)); start++) {
				flagsOne[start] = true;
				flagsTwo[start] = true;
			}
			while ((start < twoStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				flagsOne[--oneStop] = true;
				flagsTwo[--twoStop] = true;
			}
		} else {
			for (start = 0; (start < oneStop) && (one.charAt(start) == two.charAt(start)); start++) {
				flagsOne[start] = true;
				flagsTwo[start] = true;
			}
			while ((start < oneStop) && (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				flagsOne[--oneStop] = true;
				flagsTwo[--twoStop] = true;
			}
		}
		if (oneStop == start) {
			/* le deuxi�me est �gal au premier plus une insertion d'une chaine */
			distance += twoStop - start;
			return distance;
		} else if (twoStop == start) {
			/* le premier est �gal au deuxi�me plus une insertion d'une chaine */
			distance += oneStop - start;
			return distance;
		} else if (((oneStop - start) <= 2) && ((twoStop - start) <= 2)) {
			/* un changement, ou deux changements, ou un changement et une insertion, ou un changement et une suppression */
			distance += ((oneStop - start) + twoStop) - start;
			return distance;
		} else {
			/* une chaine comporte au moins 3 caract�res, l'autre au moins un, le premier et le dernier caract�res des deux chaines sont diff�rents */
			/* passer les parties qui ne peuvent pas �tre identiques en d�but et fin */
			twoStart = oneStart = start;
			do {
				temporaryDistance = distance;
				while (((index = one.indexOf(two.charAt(twoStart), oneStart)) == -1) || (index >= oneStop) || ((((distance + index) - oneStart) >= tooFar) && (((distance + oneStop) - index) >= tooFar))) {
					/* le premier caract�re de la deuxi�me chaine est absent de la premi�re ou trop loin */
					if (++twoStart >= twoStop) {
						return (distance + 1 + oneStop) - oneStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = two.indexOf(one.charAt(oneStart), twoStart)) == -1) || (index >= twoStop) || ((((distance + index) - twoStart) >= tooFar) && (((distance + twoStop) - index) >= tooFar))) {
					/* le premier caract�re de la premi�re chaine est absent de la deuxi�me ou trop loin */
					if (++oneStart >= oneStop) {
						return (distance + 1 + twoStop) - twoStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) == -1) || (index < oneStart) || ((((distance + oneStop) - index) >= tooFar) && (((distance + index) - oneStart) >= tooFar))) {
					/* le dernier caract�re de la premi�re chaine est absent de la deuxi�me ou trop loin */
					if (--twoStop <= twoStart) {
						return (distance + 1 + oneStop) - oneStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
				while (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) == -1) || (index < twoStart) || ((((distance + twoStop) - index) >= tooFar) && (((distance + index) - twoStart) >= tooFar))) {
					/* le dernier caract�re de la deuxi�me chaine est absent de la premi�re ou trop loin */
					if (--oneStop <= oneStart) {
						return (distance + 1 + twoStop) - twoStart;
					}
					if (++distance >= tooFar) {
						return TOO_FAR;
					}
				}
			} while (distance > temporaryDistance);
			if ((one.charAt(oneStart) == two.charAt(twoStart)) || (one.charAt(oneStop - 1) == two.charAt(twoStop - 1))) {
				/* on retombe sur des parties communes en d�but ou en fin => appeler r�cursivement */
				distance = explain(one.substring(oneStart, oneStop), two.substring(twoStart, twoStop), tooFar, flagsSubOne = new boolean[oneStop - oneStart], flagsSubTwo = new boolean[twoStop - twoStart], distance);
				System.arraycopy(flagsSubOne, 0, flagsOne, oneStart, flagsSubOne.length);
				System.arraycopy(flagsSubTwo, 0, flagsTwo, twoStart, flagsSubTwo.length);
				return distance;
			} else {
				/* passer une partie incompatible, en d�but ou en fin de chaque chaine */
				bestDistance = TOO_FAR;
				if (((index = one.indexOf(two.charAt(twoStart), oneStart)) != -1) && (index < oneStop) && (((distance + index) - oneStart) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = explain(one.substring(index + 1, oneStop), two.substring(twoStart + 1, twoStop), tooFar, flagsSubOne = new boolean[oneStop - index - 1], flagsSubTwo = new boolean[twoStop - twoStart - 1], (distance + index) - oneStart)) < bestDistance) {
						bestFlagsOne = mix(flagsOne, flagsSubOne, index + 1, index);
						bestFlagsTwo = mix(flagsTwo, flagsSubTwo, twoStart + 1, twoStart);
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.indexOf(one.charAt(oneStart), twoStart)) != -1) && (index < twoStop) && (((distance + index) - twoStart) < tooFar)) {
					/* le premier caract�re de la premi�re chaine peut correspondre dans la deuxi�me */
					if ((temporaryDistance = explain(one.substring(oneStart + 1, oneStop), two.substring(index + 1, twoStop), tooFar, flagsSubOne = new boolean[oneStop - oneStart - 1], flagsSubTwo = new boolean[twoStop - index - 1], (distance + index) - twoStart)) < bestDistance) {
						bestFlagsOne = mix(flagsOne, flagsSubOne, oneStart + 1, oneStart);
						bestFlagsTwo = mix(flagsTwo, flagsSubTwo, index + 1, index);
						bestDistance = temporaryDistance;
					}
				}
				if (((index = one.lastIndexOf(two.charAt(twoStop - 1), oneStop - 1)) != -1) && (index >= oneStart) && (((distance + oneStop) - index) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = explain(one.substring(oneStart, index), two.substring(twoStart, twoStop - 1), tooFar, flagsSubOne = new boolean[index - oneStart], flagsSubTwo = new boolean[twoStop - twoStart - 1], (distance + oneStop) - index)) < bestDistance) {
						bestFlagsOne = mix(flagsOne, flagsSubOne, oneStart, index);
						bestFlagsTwo = mix(flagsTwo, flagsSubTwo, twoStart, twoStop - 1);
						bestDistance = temporaryDistance;
					}
				}
				if (((index = two.lastIndexOf(one.charAt(oneStop - 1), twoStop - 1)) != -1) && (index >= twoStart) && (((distance + twoStop) - index) < tooFar)) {
					/* le premier caract�re de la deuxi�me chaine peut correspondre dans la premi�re */
					if ((temporaryDistance = explain(one.substring(oneStart, oneStop - 1), two.substring(twoStart, index), tooFar, flagsSubOne = new boolean[oneStop - oneStart - 1], flagsSubTwo = new boolean[index - twoStart], (distance + twoStop) - index)) < bestDistance) {
						bestFlagsOne = mix(flagsOne, flagsSubOne, oneStart, oneStop - 1);
						bestFlagsTwo = mix(flagsTwo, flagsSubTwo, twoStart, index);
						bestDistance = temporaryDistance;
					}
				}
				if (bestDistance < TOO_FAR) {
					System.arraycopy(bestFlagsOne, 0, flagsOne, 0, flagsOne.length);
					System.arraycopy(bestFlagsTwo, 0, flagsTwo, 0, flagsTwo.length);
				}
				return bestDistance;
			}
		}
	}



	/**
	 * Marque les changement dans la chaine indiqu�e.
	 * @param string Chaine � marquer.
	 * @param flags Tableau des indications de lettres de la chaine � marquer correspondant � l'autre.
	 * @param otherFlags Tableau des indications de lettres de l'autre chaine correspondant � la chaine � marquer.
	 * @return Une chaine avec les changements marqu�s.
	 */
	public static String mark(String string, boolean[] flags, boolean[] otherFlags) {
		StringBuilder marked = new StringBuilder();
		int iFlags = 0;
		int iOtherFlags = 0;
		boolean match = true;

		while ((iFlags < flags.length) && (iOtherFlags < otherFlags.length)) {
			if ((!flags[iFlags] || !otherFlags[iOtherFlags]) && match) {
				marked.append('[');
				match = false;
			}
			if (flags[iFlags] && otherFlags[iOtherFlags] && !match) {
				marked.append(']');
				match = true;
			}
			if (flags[iFlags] && !otherFlags[iOtherFlags]) {
				marked.append(' ');
				iOtherFlags++;
			} else if (!flags[iFlags] && otherFlags[iOtherFlags]) {
				marked.append(string.charAt(iFlags));
				iFlags++;
			} else {
				marked.append(string.charAt(iFlags));
				iFlags++;
				iOtherFlags++;
			}
		}
		if (match && ((iFlags < flags.length) || (iOtherFlags < otherFlags.length))) {
			marked.append('[');
			match = false;
		}
		while (iFlags < flags.length) {
			marked.append(string.charAt(iFlags));
			iFlags++;
		}
		while (iOtherFlags < otherFlags.length) {
			marked.append(' ');
			iOtherFlags++;
		}
		if (!match) {
			marked.append(']');
		}
		return marked.toString();
	}



	/**
	 * Retourne un nouveau tableau r�sultant de l'int�gration de tous les �l�ments indiqu�s.
	 * @param array Tableau.
	 * @param subArray Sous tableau.
	 * @param offset Position de la premi�re case du sous tableau dans le tableau.
	 * @param index Index suppl�mentaire � mettre � vrai dans le tableau.
	 * @return Un nouveau tableau r�sultant de l'int�gration de tous ces �l�ments.
	 */
	private static boolean[] mix(boolean[] array, boolean[] subArray, int offset, int index) {
		boolean[] result = array.clone();

		System.arraycopy(subArray, 0, result, offset, subArray.length);
		result[index] = true;
		return result;
	}



	/**
	 * Distance limite (consid�r�e comme infinie).
	 */
	public static final int TOO_FAR = 1000000;



}
