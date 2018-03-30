package toolbox;

import java.util.*;



/**
 * La classe {@link Cache} impl�mente un cache de r�sultat de traitement.<br>
 * Pour que cette classe fonctionne correctement, le r�sultat du traitement doit �tre d�terministe, c'est � dire que la valeur retourn�e par cette m�thode ne doit d�pendre que de la valeur du
 * param�tre.
 * @author Ludovic WALLE
 * @param <K> Type du param�tre du traitement (cl� du cache).
 * @param <V> Type du r�sultat du traitement (valeur du cache).
 */
public abstract class Cache<K, V> {



	/**
	 * Calcule le r�sultat du traitement appliqu� au param�tre indiqu�.<br>
	 * Pour que cette classe fonctionne correctement, il faut que la valeur retourn�e par cette m�thode d�pende uniquement de la valeur du param�tre, et que ni le param�tre ni le r�sultat ne soient
	 * modifi�s apr�s l'appel de cette m�thode.
	 * @param key Param�tre du traitement, qui sera utilis� comme cl� de cache (peut �tre <code>null</code>).
	 * @return Le r�sultat du traitement appliqu� au param�tre indiqu�.
	 */
	protected abstract V delegateCompute(K key);



	/**
	 * Retourne le r�sultat du traitement appliqu� au param�tre indiqu�.<br>
	 * Le premier appel de cette m�thode avec le param�tre indiqu� provoque le calcul du r�sultat et son stockage dans le cache avant de le retourner. Les appels suivants ne font que retourner la
	 * valeur stock�e dans le cache.
	 * @param key param�tre du traitement.
	 * @return Le r�sultat du traitement appliqu� au param�tre indiqu�.
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
	 * Retourne le nombre d'appels � la m�thode {@link #get(Object)} dont les r�sultats �taient en cache.
	 * @return Le nombre d'appels � la m�thode {@link #get(Object)} dont les r�sultats �taient en cache.
	 */
	public int getHitCount() {
		synchronized (CACHED) {
			return count - CACHED.size();
		}
	}



	/**
	 * Retourne le nombre d'appels � la m�thode {@link #get(Object)} dont les r�sultats n'�taient pas en cache.
	 * @return Le nombre d'appels � la m�thode {@link #get(Object)} dont les r�sultats n'�taient pas en cache.
	 */
	public int getMissCount() {
		synchronized (CACHED) {
			return CACHED.size();
		}
	}



	/**
	 * Retourne le nombre d'appels � la m�thode {@link #get(Object)}.
	 * @return Le nombre d'appels � la m�thode {@link #get(Object)}.
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
	 * Cl�s des traitement en cours.
	 */
	private final Set<K> CACHING = new HashSet<>();



	/**
	 * Nombre d'appels � {@link #get(Object)}.
	 */
	private int count = 0;



}
