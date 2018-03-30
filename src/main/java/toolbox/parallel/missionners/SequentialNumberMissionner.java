package toolbox.parallel.missionners;

import java.security.*;

import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link SequentialNumberMissionner} implémente un générateur qui retourne dans l'ordre croissant les nombres compris entre deux valeurs. Les deux valeurs doivent être strictement
 * positives.
 * @author Ludovic WALLE
 */
public class SequentialNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * Crée un générateur de nombre compris entre les deux valeurs indiquées, dans l'ordre croissant. Les deux valeurs doivent être strictement positives, et la valeur minimale doit être inférieure ou
	 * égale à la valeur maximale.
	 * @param min Valeur minimale.
	 * @param max Valeur maximale.
	 */
	public SequentialNumberMissionner(int min, int max) {
		super();
		if (min <= 0) {
			throw new InvalidParameterException("La valeur minimale du générateur séquentiel (" + min + ") doit être strictement positive.");
		}
		if (min > max) {
			throw new InvalidParameterException("La valeur minimale du générateur séquentiel (" + min + ") doit être strictement inférieure à la valeur maximale(" + max + ").");
		}
		this.min = min;
		this.max = max;
	}



	/**
	 * Retourne le nombre total de numéros à traiter.
	 * @return Le nombre total de numéros à traiter.
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
	 * Valeur maximale générable (inclue).
	 */
	private final int max;



	/**
	 * Valeur minimale générable (inclue).
	 */
	private int min;



}
