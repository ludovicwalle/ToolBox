package toolbox.parallel.missionners;

import java.security.*;
import java.util.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link RandomNumberMissionner} impl�mente un g�n�rateur qui retourne des num�ros al�atoires dans un intervalle donn�.
 * @author Ludovic WALLE
 */
public class RandomNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * Cr�e un g�n�rateur de nombre al�atoire dont les valeurs sont comprises entre celles indiqu�es (inclues).
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 */
	public RandomNumberMissionner(int min, int max) {
		this(min, max, -1);
	}



	/**
	 * Cr�e un g�n�rateur de nombre al�atoire dont les valeurs sont comprises entre celles indiqu�es (inclues).
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 * @param count Nombre de valeurs � g�n�rer si positif ou nul, ou sans limite si n�gatif.
	 */
	public RandomNumberMissionner(int min, int max, int count) {
		super();
		if (min <= 0) {
			throw new InvalidParameterException("La valeur minimale du g�n�rateur al�atoire (" + min + ") doit �tre strictement positive.");
		}
		if (min > max) {
			throw new InvalidParameterException("La valeur minimale du g�n�rateur al�atoire (" + min + ") doit �tre strictement inf�rieure � la valeur maximale(" + max + ").");
		}
		this.count = count;
		this.min = min;
		this.max = max;
	}



	/**
	 * Retourne le nombre total de num�ros, ou {@link Missionner#NOT_COMPUTABLE} lorsqu'il n'est pas calculable.
	 * @return Le nombre total de num�ros, ou {@link Missionner#NOT_COMPUTABLE} lorsqu'il n'est pas calculable.
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
	 * Nombre de valeurs restant � g�n�rer si positif ou nul, sans limite si n�gatif.
	 */
	private int count;



	/**
	 * Valeur maximale g�n�rable (inclue).
	 */
	private final int max;



	/**
	 * Valeur minimale g�n�rable (inclue).
	 */
	private final int min;



	/**
	 * G�n�rateur de nombres al�atoires.
	 */
	private final Random random = new Random();



}
