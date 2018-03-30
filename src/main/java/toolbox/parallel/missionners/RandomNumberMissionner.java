package toolbox.parallel.missionners;

import java.security.*;
import java.util.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link RandomNumberMissionner} implémente un générateur qui retourne des numéros aléatoires dans un intervalle donné.
 * @author Ludovic WALLE
 */
public class RandomNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * Crée un générateur de nombre aléatoire dont les valeurs sont comprises entre celles indiquées (inclues).
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 */
	public RandomNumberMissionner(int min, int max) {
		this(min, max, -1);
	}



	/**
	 * Crée un générateur de nombre aléatoire dont les valeurs sont comprises entre celles indiquées (inclues).
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 * @param count Nombre de valeurs à générer si positif ou nul, ou sans limite si négatif.
	 */
	public RandomNumberMissionner(int min, int max, int count) {
		super();
		if (min <= 0) {
			throw new InvalidParameterException("La valeur minimale du générateur aléatoire (" + min + ") doit être strictement positive.");
		}
		if (min > max) {
			throw new InvalidParameterException("La valeur minimale du générateur aléatoire (" + min + ") doit être strictement inférieure à la valeur maximale(" + max + ").");
		}
		this.count = count;
		this.min = min;
		this.max = max;
	}



	/**
	 * Retourne le nombre total de numéros, ou {@link Missionner#NOT_COMPUTABLE} lorsqu'il n'est pas calculable.
	 * @return Le nombre total de numéros, ou {@link Missionner#NOT_COMPUTABLE} lorsqu'il n'est pas calculable.
	 */
	@Override protected synchronized int delegateComputeExpectedCount() {
		return (count >= 0) ? count : NOT_COMPUTABLE;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected IntegerMission delegateGetNext() {
		if (count == 0) {
			return null;
		} else if (count > 0) {
			count--;
		}
		return new IntegerMission(random.nextInt((max - min) + 1) + min);
	}



	/**
	 * Nombre de valeurs restant à générer si positif ou nul, sans limite si négatif.
	 */
	private int count;



	/**
	 * Valeur maximale générable (inclue).
	 */
	private final int max;



	/**
	 * Valeur minimale générable (inclue).
	 */
	private final int min;



	/**
	 * Générateur de nombres aléatoires.
	 */
	private final Random random = new Random();



}
