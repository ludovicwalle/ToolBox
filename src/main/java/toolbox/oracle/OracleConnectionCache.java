package toolbox.oracle;

import java.sql.*;
import java.util.*;

import oracle.jdbc.*;
import oracle.jdbc.pool.*;



/**
 * La classe {@link OracleConnectionCache} impl�mente un cache de connexions � une base Oracle, utilisant une connexion "thin".<br>
 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
 * Cette classe est utilisable de deux fa�ons:
 * <ul>
 * <li>en utilisant un cache cr�� par {@link #OracleConnectionCache(String, String, String, int, String)} ou {@link #OracleConnectionCache(OracleConnectionPoolDataSource)}, � partir duquel on obtient
 * des connexions par {@link #newConnection()};
 * <li>en obtenant des connexions par {@link #newConnection(String, String, String, int, String)}, provenant de caches internes de cette classe.
 * </ul>
 * Cette classe peut �tre utilis�e pour des connexions � n'importe quelle base Oracle.
 * @author Ludovic WALLE
 */
public class OracleConnectionCache {



	/**
	 * Cr�e un cache de connexion correspondant � la source de donn�es indiqu�e.
	 * @param dataSource Source de donn�es.
	 */
	public OracleConnectionCache(OracleConnectionPoolDataSource dataSource) {
		this.dataSource = dataSource;
	}



	/**
	 * Cr�e un cache de connexion correspondant aux param�tres indiqu�s.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Num�ro du port;
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
	 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue de ce cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED.
	 * @return La nouvelle connexion � la base.
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
	 * Enregistre la connexion partag�e indiqu�e, comme r�utilisable pour une nouvelle connexion.
	 * @param pooledConnection Connexion partag�e r�utilisable.
	 */
	private void registerClosed(OraclePooledConnection pooledConnection) {
		synchronized (reusableConnections) {
			reusableConnections.add(pooledConnection);
		}
	}



	/**
	 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux param�tres indiqu�s est cr�� si n�cessaire, et sera r�utilis� en cas d'appels successifs avec les m�mes valeurs.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Num�ro du port.
	 * @param databaseName Nom de la base.
	 * @return La nouvelle connexion � la base.
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
	 * Source de donn�es pour ce cache de connexions.
	 */
	private final OracleConnectionPoolDataSource dataSource;



	/**
	 * Connexions r�utilisables.
	 */
	private final Vector<OraclePooledConnection> reusableConnections = new Vector<>();



	/**
	 * Caches de connexion cr��s lors des appels � {@link #newConnection(String, String, String, int, String)}.<br>
	 * La cl� est l'url, et l'object associ� est de la classe {@link OracleConnectionCache}.
	 */
	private static final Map<String, OracleConnectionCache> caches = new HashMap<>();



	/**
	 * La classe {@link OracleCachedConnection} impl�mente une connexion � une base Oracle en d�rivant la classe {@link OracleConnectionWrapper} pour ajouter un rollback lors de la fermeture de la
	 * connexion.<br>
	 * Cette classe est priv�e car seule les classes parentes {@link Connection} et {@link OracleConnection} ont besoin d'�tre manipul�es � l'ext�rieur.
	 * @author Ludovic WALLE
	 */
	public static class OracleCachedConnection extends OracleConnectionWrapper {



		/**
		 * Cr�e une nouvelle connexion � partir d'une connexion partag�e, en lui associant un cache o� placer la connexion lors de sa fermeture, pour r�utilisation.
		 * @param pooledConnection Connexion partag�e.
		 * @param cache Cache o� placer la connexion lors de sa fermeture, pour r�utilisation.
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
		 * Ferme la connexion, apr�s avoir fait un rollback, et la place dans le cache des connexions utilisables.
		 */
		@Override public void close() throws SQLException {
			if (!isClosed()) {
				super.rollback();
				super.close();
				cache.registerClosed(pooledConnection);
			}
		}



		/**
		 * Cache o� placer la connexion lors de sa fermeture, pour r�utilisation.
		 */
		private final OracleConnectionCache cache;



		/**
		 * Connexion partag�e dont est issue cette connexion.
		 */
		private final OraclePooledConnection pooledConnection;



	}



}
