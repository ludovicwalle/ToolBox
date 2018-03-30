package toolbox.oracle;

import java.sql.*;
import java.util.*;

import oracle.jdbc.*;
import oracle.jdbc.pool.*;



/**
 * La classe {@link OracleConnectionCache} implémente un cache de connexions à une base Oracle, utilisant une connexion "thin".<br>
 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
 * Cette classe est utilisable de deux façons:
 * <ul>
 * <li>en utilisant un cache créé par {@link #OracleConnectionCache(String, String, String, int, String)} ou {@link #OracleConnectionCache(OracleConnectionPoolDataSource)}, à partir duquel on obtient
 * des connexions par {@link #newConnection()};
 * <li>en obtenant des connexions par {@link #newConnection(String, String, String, int, String)}, provenant de caches internes de cette classe.
 * </ul>
 * Cette classe peut être utilisée pour des connexions à n'importe quelle base Oracle.
 * @author Ludovic WALLE
 */
public class OracleConnectionCache {



	/**
	 * Crée un cache de connexion correspondant à la source de données indiquée.
	 * @param dataSource Source de données.
	 */
	public OracleConnectionCache(OracleConnectionPoolDataSource dataSource) {
		this.dataSource = dataSource;
	}



	/**
	 * Crée un cache de connexion correspondant aux paramètres indiqués.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Numéro du port;
	 * @param databaseName Nom de la base.
	 */
	public OracleConnectionCache(String user, String password, String hostName, int portNumber, String databaseName) {
		try {
			dataSource = new OracleConnectionPoolDataSource();
			dataSource.setDriverType("thin");
			dataSource.setServerName(hostName);
			dataSource.setNetworkProtocol("tcp");
			dataSource.setDatabaseName(databaseName);
			dataSource.setPortNumber(portNumber);
			dataSource.setUser(user);
			dataSource.setPassword(password);
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue de ce cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED.
	 * @return La nouvelle connexion à la base.
	 */
	public OracleCachedConnection newConnection() {
		OraclePooledConnection pooledConnection;

		try {
			synchronized (reusableConnections) {
				if (reusableConnections.isEmpty()) {
					pooledConnection = (OraclePooledConnection) dataSource.getPooledConnection();
				} else {
					pooledConnection = reusableConnections.remove(0);
				}
			}
			return new OracleCachedConnection(pooledConnection, this);
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Enregistre la connexion partagée indiquée, comme réutilisable pour une nouvelle connexion.
	 * @param pooledConnection Connexion partagée réutilisable.
	 */
	private void registerClosed(OraclePooledConnection pooledConnection) {
		synchronized (reusableConnections) {
			reusableConnections.add(pooledConnection);
		}
	}



	/**
	 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux paramètres indiqués est créé si nécessaire, et sera réutilisé en cas d'appels successifs avec les mêmes valeurs.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Numéro du port.
	 * @param databaseName Nom de la base.
	 * @return La nouvelle connexion à la base.
	 */
	public static OracleConnection newConnection(String user, String password, String hostName, int portNumber, String databaseName) {
		String fullUrl = /* "jdbc:oracle:thin:" + */user + "/" + password + "@" + hostName + ":" + portNumber + ":" + databaseName;
		OracleConnection connection;
		OracleConnectionCache cache;

		synchronized (caches) {
			if ((cache = caches.get(fullUrl)) == null) {
				cache = new OracleConnectionCache(user, password, hostName, portNumber, databaseName);
				caches.put(fullUrl, cache);
			}
		}
		connection = cache.newConnection();
		return connection;
	}



	/**
	 * Source de données pour ce cache de connexions.
	 */
	private final OracleConnectionPoolDataSource dataSource;



	/**
	 * Connexions réutilisables.
	 */
	private final Vector<OraclePooledConnection> reusableConnections = new Vector<>();



	/**
	 * Caches de connexion créés lors des appels à {@link #newConnection(String, String, String, int, String)}.<br>
	 * La clé est l'url, et l'object associé est de la classe {@link OracleConnectionCache}.
	 */
	private static final Map<String, OracleConnectionCache> caches = new HashMap<>();



	/**
	 * La classe {@link OracleCachedConnection} implémente une connexion à une base Oracle en dérivant la classe {@link OracleConnectionWrapper} pour ajouter un rollback lors de la fermeture de la
	 * connexion.<br>
	 * Cette classe est privée car seule les classes parentes {@link Connection} et {@link OracleConnection} ont besoin d'être manipulées à l'extérieur.
	 * @author Ludovic WALLE
	 */
	public static class OracleCachedConnection extends OracleConnectionWrapper {



		/**
		 * Crée une nouvelle connexion à partir d'une connexion partagée, en lui associant un cache où placer la connexion lors de sa fermeture, pour réutilisation.
		 * @param pooledConnection Connexion partagée.
		 * @param cache Cache où placer la connexion lors de sa fermeture, pour réutilisation.
		 * @throws SQLException En cas d'erreur Oracle.
		 */
		private OracleCachedConnection(OraclePooledConnection pooledConnection, OracleConnectionCache cache) throws SQLException {
			super((OracleConnection) pooledConnection.getConnection());
			setAutoCommit(false);
			setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			this.pooledConnection = pooledConnection;
			this.cache = cache;
		}



		/**
		 * Ferme la connexion, après avoir fait un rollback, et la place dans le cache des connexions utilisables.
		 */
		@Override public void close() throws SQLException {
			if (!isClosed()) {
				super.rollback();
				super.close();
				cache.registerClosed(pooledConnection);
			}
		}



		/**
		 * Cache où placer la connexion lors de sa fermeture, pour réutilisation.
		 */
		private final OracleConnectionCache cache;



		/**
		 * Connexion partagée dont est issue cette connexion.
		 */
		private final OraclePooledConnection pooledConnection;



	}



}
