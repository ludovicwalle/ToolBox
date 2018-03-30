package toolbox.parallel.missionners;

import java.security.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link SequentialNumberMissionner} impl�mente un g�n�rateur qui retourne dans l'ordre croissant les nombres compris entre deux valeurs. Les deux valeurs doivent �tre strictement
 * positives.
 * @author Ludovic WALLE
 */
public class SequentialNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * Cr�e un g�n�rateur de nombre compris entre les deux valeurs indiqu�es, dans l'ordre croissant. Les deux valeurs doivent �tre strictement positives, et la valeur minimale doit �tre inf�rieure ou
	 * �gale � la valeur maximale.
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 */
	public SequentialNumberMissionner(int min, int max) {
		super();
		if (min <= 0) {
			throw new InvalidParameterException("La valeur minimale du g�n�rateur s�quentiel (" + min + ") doit �tre strictement positive.");
		}
		if (min > max) {
			throw new InvalidParameterException("La valeur minimale du g�n�rateur s�quentiel (" + min + ") doit �tre strictement inf�rieure � la valeur maximale(" + max + ").");
		}
		this.min = min;
		this.max = max;
	}



	/**
	 * Retourne le nombre total de num�ros � traiter.
	 * @return Le nombre total de num�ros � traiter.
	 */
	@Override protected synchronized int delegateComputeExpectedCount() {
		return (max - min) + 1;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected IntegerMission delegateGetNext() {
		if ((min > max) || (min <= 0)) {
			return null;
		}
		return new IntegerMission(min++);
	}



	/**
	 * Valeur maximale g�n�rable (inclue).
	 */
	private final int max;



	/**
	 * Valeur minimale g�n�rable (inclue).
	 */
	private int min;



}
