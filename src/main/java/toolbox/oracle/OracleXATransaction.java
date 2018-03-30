package toolbox.oracle;



import java.sql.*;
import java.util.*;

import javax.transaction.xa.*;

import oracle.jdbc.*;
import oracle.jdbc.xa.*;
import oracle.jdbc.xa.client.OracleXAConnection;
import oracle.jdbc.xa.client.OracleXADataSource;
import oracle.jdbc.xa.client.OracleXAResource;

import toolbox.oracle.OracleXATransaction.OracleXAConnectionCache.*;



/**
 * La classe {@link OracleXATransaction} implémente une transaction distribuée à une base Oracle, utilisant une connexion "thin".<br>
 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
 * Cette classe est utilisable en utilisant une connexion créée par {@link #newConnection(OracleXAConnectionCache)}, {@link #newConnection(OracleXADataSource)} ou
 * {@link #newConnection(String, String, String, int, String)}.
 * @author Ludovic WALLE
 */
public class OracleXATransaction {



	/**
	 * Démarre une nouvelle transaction distribuée.
	 */
	public OracleXATransaction() {}



	/**
	 * Valide une transaction distribuée et ferme toutes les connexions rattachées.<br>
	 * @return <code>true</code> si aucune branche si toutes les branches de la transaction distribuée ont pu être validées, <code>false</code> sinon.
	 * @throws XAException Si il y a une erreur dans la transaction distribuée.
	 */
	public boolean commit() throws XAException {
		boolean commit = true;

		synchronized (connections) {
			for (OracleXACachedConnection connection : connections) {
				/* on ne peut faire qu'un seul end sur une connexion */
				connection.end();
			}
			for (OracleXACachedConnection connection : connections) {
				connection.prepare();
				if ((connection.getLastPrepareReturnCode() != XAResource.XA_OK) && (connection.getLastPrepareReturnCode() != XAResource.XA_RDONLY)) {
					commit = false;
				}
			}
			for (OracleXACachedConnection connection : connections) {
				/* on ne peut pas faire de commit ni de rollback sur une transaction dont le prepare ne retourne pas XA_OK */
				if (connection.getLastPrepareReturnCode() == XAResource.XA_OK) {
					if (commit) {
						connection.commitBranch();
					} else {
						connection.rollbackBranch();
					}
				}
				connection.recycle();
			}
			connections.clear();
		}
		return commit;
	}



	/**
	 * Retourne l'identifiant de cette transaction distribuée.
	 * @return L'identifiant de cette transaction distribuée.
	 */
	private byte[] getTransactionId() {
		return transactionId;
	}



	/**
	 * Retourne un nouvel identifiant de branche pour cette transaction distribuée.
	 * @return Un nouvel identifiant de branche pour cette transaction distribuée.
	 */
	private byte[] newBranchId() {
		return id(connections.size() + 1);
	}



	/**
	 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED.<br>
	 * Cette connexion est rattachée à cette transaction distribuée.
	 * @param cache Cache de connexion.
	 * @return La nouvelle connexion à la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribuée.
	 */
	public OracleXACachedConnection newConnection(OracleXAConnectionCache cache) throws XAException {
		return cache.newConnection(this);
	}



	/**
	 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux paramètres indiqués est créé si nécessaire, et sera réutilisé en cas d'appels successifs avec la même valeur.<br>
	 * Cette connexion est rattachée à cette transaction distribuée.
	 * @param dataSource Source de données.
	 * @return La nouvelle connexion à la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribuée.
	 */
	public OracleXACachedConnection newConnection(OracleXADataSource dataSource) throws XAException {
		return OracleXAConnectionCache.newConnection(this, dataSource);
	}



	/**
	 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux paramètres indiqués est créé si nécessaire, et sera réutilisé en cas d'appels successifs avec les mêmes valeurs.<br>
	 * Cette connexion est rattachée à cette transaction distribuée.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Numéro du port.
	 * @param databaseName Nom de la base.
	 * @return La nouvelle connexion à la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribuée.
	 */
	public OracleXACachedConnection newConnection(String user, String password, String hostName, int portNumber, String databaseName) throws XAException {
		return OracleXAConnectionCache.newConnection(this, user, password, hostName, portNumber, databaseName);
	}



	/**
	 * Enregistre la connexion comme rattachée à la transaction distribuée.
	 * @param connection Connexion rattachée à la transaction distribuée.
	 */
	private void registerConnection(OracleXACachedConnection connection) {
		synchronized (connections) {
			connections.add(connection);
		}
	}



	/**
	 * Annule une transaction distribuée et ferme toutes les connexions rattachées.
	 * @throws XAException Si il y a une erreur dans la transaction distribuée.
	 */
	public void rollback() throws XAException {
		synchronized (connections) {
			for (OracleXACachedConnection connection : connections) {
				connection.end();
			}
			for (OracleXACachedConnection connection : connections) {
				connection.prepare();
			}
			for (OracleXACachedConnection connection : connections) {
				if (connection.getLastPrepareReturnCode() == XAResource.XA_OK) {
					connection.rollbackBranch();
				}
				connection.recycle();
			}
			connections.clear();
		}
	}



	/**
	 * Calcule l'identifiant correspondant au numéro indiqué.
	 * @param number Numéro pour lequel il faut calculer l'identifiant.
	 * @return L'identifiant correspondant au numéro indiqué.
	 */
	private static byte[] id(int number) {
		byte[] id = new byte[64];

		id[0] = (byte) ((number >> 0) & 0x7F);
		id[1] = (byte) ((number >> 7) & 0x7F);
		id[2] = (byte) ((number >> 14) & 0x7F);
		id[3] = (byte) ((number >> 21) & 0x7F);
		id[4] = (byte) ((number >> 28) & 0x7F);
		return id;
	}



	/**
	 * Connexions rattachées à la transaction distribuée. Les éléments sont des {@link OracleXACachedConnection}.
	 */
	private final Vector<OracleXACachedConnection> connections = new Vector<>();



	/**
	 * Partie commune aux différentes branches de l'identifiant de transaction distribuée.
	 */
	private final byte[] transactionId = id(transaction++);



	/**
	 * Prochain numéro de transaction.
	 */
	private static int transaction = (int) System.currentTimeMillis() % 1000000000;



	/**
	 * La classe {@link OracleXAConnectionCache} implémente un cache de connexions à une base Oracle, utilisant une connexion "thin".<br>
	 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
	 * Cette classe est utilisable de deux façons:
	 * <ul>
	 * <li>en utilisant un cache créé par {@link OracleXAConnectionCache#OracleXAConnectionCache(String, String, String, int, String)} ou {@link #OracleXATransaction(OracleXADataSource)}, à partir
	 * duquel on obtient des connexions par {@link OracleXAConnectionCache#newConnection(OracleXATransaction)};
	 * <li>en obtenant des connexions par {@link OracleXAConnectionCache#newConnection(OracleXATransaction, String, String, String, int, String)} , provenant de caches privés de cette classe.<br>
	 * Cette classe n'est pas liée à RefDoc. Elle peut être utilisée pour des connexions à n'importe quelle base. Par contre, elle est liée à Oracle.
	 * @author Ludovic WALLE
	 */
	public static class OracleXAConnectionCache {



		/**
		 * Crée un cache de connexion correspondant à la source de données indiquée.
		 * @param dataSource Source de données.
		 */
		public OracleXAConnectionCache(OracleXADataSource dataSource) {
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
		public OracleXAConnectionCache(String user, String password, String hostName, int portNumber, String databaseName) {
			try {
				dataSource = new OracleXADataSource();
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
		 * @param xaTransaction Transaction à laquelle est rattachée cette connexion.
		 * @return La nouvelle connexion à la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribuée.
		 */
		public OracleXACachedConnection newConnection(OracleXATransaction xaTransaction) throws XAException {
			OracleXAConnection xaConnection;
			OracleXACachedConnection connection;

			try {
				synchronized (reusableConnections) {
					if (reusableConnections.isEmpty()) {
						xaConnection = (OracleXAConnection) dataSource.getXAConnection();
					} else {
						xaConnection = reusableConnections.remove(0);
					}
				}
				connection = new OracleXACachedConnection(xaConnection, xaTransaction);
				return connection;
			} catch (SQLException exception) {
				throw new RuntimeException(exception);
			}
		}



		/**
		 * Enregistre la connexion partagée indiquée, comme réutilisable pour une nouvelle connexion.
		 * @param pooledConnection Connexion partagée réutilisable.
		 */
		private void recycle(OracleXAConnection pooledConnection) {
			synchronized (reusableConnections) {
				reusableConnections.add(pooledConnection);
			}
		}



		/**
		 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache
		 * correspondant aux paramètres indiqués est créé si nécessaire, et sera réutilisé en cas d'appels successifs avec les mêmes valeurs.
		 * @param xaTransaction Transaction à laquelle est rattachée cette connexion.
		 * @param dataSource Source de données.
		 * @return La nouvelle connexion à la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribuée.
		 */
		public static OracleXACachedConnection newConnection(OracleXATransaction xaTransaction, OracleXADataSource dataSource) throws XAException {
			OracleXACachedConnection connection;
			OracleXAConnectionCache cache;

			synchronized (cacheByDataSource) {
				if ((cache = cacheByDataSource.get(dataSource)) == null) {
					cache = new OracleXAConnectionCache(dataSource);
					cacheByDataSource.put(dataSource, cache);
				}
			}
			connection = cache.newConnection(xaTransaction);
			return connection;
		}



		/**
		 * Retourne une connexion JDBC à une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache
		 * correspondant aux paramètres indiqués est créé si nécessaire, et sera réutilisé en cas d'appels successifs avec les mêmes valeurs.
		 * @param xaTransaction Transaction à laquelle est rattachée cette connexion.
		 * @param user Utilisateur Oracle.
		 * @param password Mot de passe.
		 * @param hostName Nom de la machine.
		 * @param portNumber Numéro du port.
		 * @param databaseName Nom de la base.
		 * @return La nouvelle connexion à la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribuée.
		 */
		public static OracleXACachedConnection newConnection(OracleXATransaction xaTransaction, String user, String password, String hostName, int portNumber, String databaseName) throws XAException {
			String fullUrl = /* "jdbc:oracle:thin:" + */user + "/" + password + "@" + hostName + ":" + portNumber + ":" + databaseName;
			OracleXACachedConnection connection;
			OracleXAConnectionCache cache;

			synchronized (cacheByUrl) {
				if ((cache = cacheByUrl.get(fullUrl)) == null) {
					cache = new OracleXAConnectionCache(user, password, hostName, portNumber, databaseName);
					cacheByUrl.put(fullUrl, cache);
				}
			}
			connection = cache.newConnection(xaTransaction);
			return connection;
		}



		/**
		 * Source de données pour ce cache de connexions.
		 */
		private final OracleXADataSource dataSource;



		/**
		 * Connexions réutilisables.
		 */
		private final Vector<OracleXAConnection> reusableConnections = new Vector<>();



		/**
		 * Caches de connexion créés lors des appels à {@link #newConnection(OracleXATransaction)} sur un cache créé par {@link #OracleXAConnectionCache(OracleXADataSource)}, ou à
		 * {@link #newConnection(OracleXATransaction, OracleXADataSource)}.<br>
		 * La clé est la source de données, et la valeur est le cache correspondant.
		 */
		private static final Map<OracleXADataSource, OracleXAConnectionCache> cacheByDataSource = new HashMap<>();



		/**
		 * Caches de connexion créés lors des appels à {@link #newConnection(OracleXATransaction)} sur un cache créé par {@link #OracleXAConnectionCache(String, String, String, int, String)}, ou à
		 * {@link OracleXAConnectionCache#newConnection(OracleXATransaction, String, String, String, int, String)} .<br>
		 * La clé est l'url, et la valeur est le cache correspondant.
		 */
		private static final Map<String, OracleXAConnectionCache> cacheByUrl = new HashMap<>();



		/**
		 * La classe {@link OracleXACachedConnection} implémente une connexion à une base Oracle rattachée à une transaction distribuée en dérivant la classe {@link OracleConnectionWrapper} pour
		 * interdire la fermeture, la validation ou l'annulation de la connexion. La validation ou l'annulation de la transaction doit se faire via la validation ou l'annulation de la transaction
		 * distribuée à laquelle elle est rattachée, qui ferme aussi la connexion (elle ne peut pas être fermée individuellement).
		 * @author Ludovic WALLE
		 */
		public class OracleXACachedConnection extends OracleConnectionWrapper {



			/**
			 * Crée une nouvelle connexion à partir d'une connexion partagée, en lui associant un cache où placer la connexion lors de sa fermeture, pour réutilisation.
			 * @param xaConnection Connexion partagée.
			 * @param xaTransaction Transaction à laquelle cette connexion est rattachée.
			 * @throws SQLException Si il y a une erreur Oracle.
			 * @throws XAException Si il y a une erreur dans la transaction distribuée.
			 */
			private OracleXACachedConnection(OracleXAConnection xaConnection, OracleXATransaction xaTransaction) throws SQLException, XAException {
				super((OracleConnection) xaConnection.getConnection());
				setAutoCommit(false);
				setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.xaConnection = xaConnection;
				this.xaResource = (OracleXAResource) xaConnection.getXAResource();
				this.xid = new OracleXid(0x1234, xaTransaction.getTransactionId(), xaTransaction.newBranchId());
				this.xaResource.start(xid, XAResource.TMNOFLAGS);
				xaTransaction.registerConnection(this);
			}



			/**
			 * Interdit la fermeture de connexion, qui ne peut être réalisée que par la validation ou l'annulation de la transaction distribuée à laquelle cette connexion est rattachée.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void close() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Interdit la validation de la transaction car elle est rattachée à une transaction distribuée.<br>
			 * Une transaction rattachée à une transaction distribuée ne peut être validée que lors de la validation de la transaction distribuée, ce qui fermera aussi la connexion.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void commit() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Valide la branche de transaction distribuée correspondant à cette connexion.
			 * @throws XAException Si il y a une erreur dans la transaction distribuée.
			 */
			private void commitBranch() throws XAException {
				xaResource.commit(xid, false);
			}



			/**
			 * Détache la connexion de la transaction distribuée.<br>
			 * Cette méthode ne devrait jamais être appelée directement.
			 * @throws XAException Si il y a une erreur dans la transaction distribuée.
			 */
			private void end() throws XAException {
				xaResource.end(xid, XAResource.TMSUCCESS);
			}



			/**
			 * Retourne la valeur de la dernière opération de préparation de la connexion pour une annulation ou validation.
			 * @return La valeur de la dernière opération de préparation de la connexion pour une annulation ou validation.
			 */
			private int getLastPrepareReturnCode() {
				return prepare;
			}



			/**
			 * Prépare la connexion pour une annulation ou validation.
			 * @throws XAException Si il y a une erreur dans la transaction distribuée.
			 */
			private void prepare() throws XAException {
				prepare = xaResource.prepare(xid);
			}



			/**
			 * Recycle cette connexion.
			 */
			private void recycle() {
				OracleXAConnectionCache.this.recycle(xaConnection);
			}



			/**
			 * Interdit l'annulation de la transaction car elle est rattachée à une transaction distribuée.<br>
			 * Une transaction rattachée à une transaction distribuée ne peut être annulée que lors de l'annulation de la transaction distribuée, ce qui fermera aussi la connexion.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void rollback() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Annule la branche de transaction distribuée correspondant à cette connexion et recycle cette connexion.
			 * @throws XAException Si il y a une erreur dans la transaction distribuée.
			 */
			private void rollbackBranch() throws XAException {
				xaResource.rollback(xid);
			}



			/**
			 * Résultat de la préparation de la validation ou de l'annulation.
			 */
			private int prepare;



			/**
			 * Connexion partagée dont est issue cette connexion.
			 */
			private final OracleXAConnection xaConnection;



			/**
			 * Ressource sur laquelle s'appuie la connexion.
			 */
			private final OracleXAResource xaResource;



			/**
			 * Identifiant de transaction distribuée.
			 */
			private final OracleXid xid;



		}



	}



}
