package toolbox;

import java.util.*;



/**
 * La classe {@link Cache} implémente un cache de résultat de traitement.<br>
 * Pour que cette classe fonctionne correctement, le résultat du traitement doit être déterministe, c'est à dire que la valeur retournée par cette méthode ne doit dépendre que de la valeur du
 * paramètre.
 * @author Ludovic WALLE
 * @param <K> Type du paramètre du traitement (clé du cache).
 * @param <V> Type du résultat du traitement (valeur du cache).
 */
public abstract class Cache<K, V> {



	/**
	 * Calcule le résultat du traitement appliqué au paramètre indiqué.<br>
	 * Pour que cette classe fonctionne correctement, il faut que la valeur retournée par cette méthode dépende uniquement de la valeur du paramètre, et que ni le paramètre ni le résultat ne soient
	 * modifiés après l'appel de cette méthode.
	 * @param key Paramètre du traitement, qui sera utilisé comme clé de cache (peut être <code>null</code>).
	 * @return Le résultat du traitement appliqué au paramètre indiqué.
	 */
	protected abstract V delegateCompute(K key);



	/**
	 * Retourne le résultat du traitement appliqué au paramètre indiqué.<br>
	 * Le premier appel de cette méthode avec le paramètre indiqué provoque le calcul du résultat et son stockage dans le cache avant de le retourner. Les appels suivants ne font que retourner la
	 * valeur stockée dans le cache.
	 * @param key paramètre du traitement.
	 * @return Le résultat du traitement appliqué au paramètre indiqué.
	 */
	public V get(K key) {
		V value;

		synchronized (CACHED) {
			count++;
			if (CACHED.containsKey(key)) {
				return CACHED.get(key);
			}
		}
		synchronized (CACHING) {
			if (CACHING.contains(key)) {
				while (CACHING.contains(key)) {
					try {
						CACHING.wait();
					} catch (InterruptedException exception) {
						throw new RuntimeException(exception);
					}
				}
				synchronized (CACHED) {
					return CACHED.get(key);
				}
			} else {
				CACHING.add(key);
			}
		}
		value = delegateCompute(key);
		synchronized (CACHED) {
			CACHED.put(key, value);
			synchronized (CACHING) {
				CACHING.remove(key);
				CACHING.notifyAll();
			}
		}
		return value;
	}



	/**
	 * Retourne le nombre d'appels à la méthode {@link #get(Object)} dont les résultats étaient en cache.
	 * @return Le nombre d'appels à la méthode {@link #get(Object)} dont les résultats étaient en cache.
	 */
	public int getHitCount() {
		synchronized (CACHED) {
			return count - CACHED.size();
		}
	}



	/**
	 * Retourne le nombre d'appels à la méthode {@link #get(Object)} dont les résultats n'étaient pas en cache.
	 * @return Le nombre d'appels à la méthode {@link #get(Object)} dont les résultats n'étaient pas en cache.
	 */
	public int getMissCount() {
		synchronized (CACHED) {
			return CACHED.size();
		}
	}



	/**
	 * Retourne le nombre d'appels à la méthode {@link #get(Object)}.
	 * @return Le nombre d'appels à la méthode {@link #get(Object)}.
	 */
	public int getTotalCount() {
		synchronized (CACHED) {
			return count;
		}
	}



	/**
	 * Cache.
	 */
	private final Map<K, V> CACHED = new HashMap<>();



	/**
	 * Clés des traitement en cours.
	 */
	private final Set<K> CACHING = new HashSet<>();



	/**
	 * Nombre d'appels à {@link #get(Object)}.
	 */
	private int count = 0;



}
