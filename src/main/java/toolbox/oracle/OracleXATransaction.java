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
 * La classe {@link OracleXATransaction} impl�mente une transaction distribu�e � une base Oracle, utilisant une connexion "thin".<br>
 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
 * Cette classe est utilisable en utilisant une connexion cr��e par {@link #newConnection(OracleXAConnectionCache)}, {@link #newConnection(OracleXADataSource)} ou
 * {@link #newConnection(String, String, String, int, String)}.
 * @author Ludovic WALLE
 */
public class OracleXATransaction {



	/**
	 * D�marre une nouvelle transaction distribu�e.
	 */
	public OracleXATransaction() {}



	/**
	 * Valide une transaction distribu�e et ferme toutes les connexions rattach�es.<br>
	 * @return <code>true</code> si aucune branche si toutes les branches de la transaction distribu�e ont pu �tre valid�es, <code>false</code> sinon.
	 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
	 * Retourne l'identifiant de cette transaction distribu�e.
	 * @return L'identifiant de cette transaction distribu�e.
	 */
	private byte[] getTransactionId() {
		return transactionId;
	}



	/**
	 * Retourne un nouvel identifiant de branche pour cette transaction distribu�e.
	 * @return Un nouvel identifiant de branche pour cette transaction distribu�e.
	 */
	private byte[] newBranchId() {
		return id(connections.size() + 1);
	}



	/**
	 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED.<br>
	 * Cette connexion est rattach�e � cette transaction distribu�e.
	 * @param cache Cache de connexion.
	 * @return La nouvelle connexion � la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
	 */
	public OracleXACachedConnection newConnection(OracleXAConnectionCache cache) throws XAException {
		return cache.newConnection(this);
	}



	/**
	 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux param�tres indiqu�s est cr�� si n�cessaire, et sera r�utilis� en cas d'appels successifs avec la m�me valeur.<br>
	 * Cette connexion est rattach�e � cette transaction distribu�e.
	 * @param dataSource Source de donn�es.
	 * @return La nouvelle connexion � la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
	 */
	public OracleXACachedConnection newConnection(OracleXADataSource dataSource) throws XAException {
		return OracleXAConnectionCache.newConnection(this, dataSource);
	}



	/**
	 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache correspondant
	 * aux param�tres indiqu�s est cr�� si n�cessaire, et sera r�utilis� en cas d'appels successifs avec les m�mes valeurs.<br>
	 * Cette connexion est rattach�e � cette transaction distribu�e.
	 * @param user Utilisateur Oracle.
	 * @param password Mot de passe.
	 * @param hostName Nom de la machine.
	 * @param portNumber Num�ro du port.
	 * @param databaseName Nom de la base.
	 * @return La nouvelle connexion � la base.
	 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
	 */
	public OracleXACachedConnection newConnection(String user, String password, String hostName, int portNumber, String databaseName) throws XAException {
		return OracleXAConnectionCache.newConnection(this, user, password, hostName, portNumber, databaseName);
	}



	/**
	 * Enregistre la connexion comme rattach�e � la transaction distribu�e.
	 * @param connection Connexion rattach�e � la transaction distribu�e.
	 */
	private void registerConnection(OracleXACachedConnection connection) {
		synchronized (connections) {
			connections.add(connection);
		}
	}



	/**
	 * Annule une transaction distribu�e et ferme toutes les connexions rattach�es.
	 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
	 * Calcule l'identifiant correspondant au num�ro indiqu�.
	 * @param number Num�ro pour lequel il faut calculer l'identifiant.
	 * @return L'identifiant correspondant au num�ro indiqu�.
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
	 * Connexions rattach�es � la transaction distribu�e. Les �l�ments sont des {@link OracleXACachedConnection}.
	 */
	private final Vector<OracleXACachedConnection> connections = new Vector<>();



	/**
	 * Partie commune aux diff�rentes branches de l'identifiant de transaction distribu�e.
	 */
	private final byte[] transactionId = id(transaction++);



	/**
	 * Prochain num�ro de transaction.
	 */
	private static int transaction = (int) System.currentTimeMillis() % 1000000000;



	/**
	 * La classe {@link OracleXAConnectionCache} impl�mente un cache de connexions � une base Oracle, utilisant une connexion "thin".<br>
	 * Les connexions ne sont pas en mode autocommit, et sont en mode READ COMMITED.<br>
	 * Cette classe est utilisable de deux fa�ons:
	 * <ul>
	 * <li>en utilisant un cache cr�� par {@link OracleXAConnectionCache#OracleXAConnectionCache(String, String, String, int, String)} ou {@link #OracleXATransaction(OracleXADataSource)}, � partir
	 * duquel on obtient des connexions par {@link OracleXAConnectionCache#newConnection(OracleXATransaction)};
	 * <li>en obtenant des connexions par {@link OracleXAConnectionCache#newConnection(OracleXATransaction, String, String, String, int, String)} , provenant de caches priv�s de cette classe.<br>
	 * Cette classe n'est pas li�e � RefDoc. Elle peut �tre utilis�e pour des connexions � n'importe quelle base. Par contre, elle est li�e � Oracle.
	 * @author Ludovic WALLE
	 */
	public static class OracleXAConnectionCache {



		/**
		 * Cr�e un cache de connexion correspondant � la source de donn�es indiqu�e.
		 * @param dataSource Source de donn�es.
		 */
		public OracleXAConnectionCache(OracleXADataSource dataSource) {
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
		 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue de ce cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED.
		 * @param xaTransaction Transaction � laquelle est rattach�e cette connexion.
		 * @return La nouvelle connexion � la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
		 * Enregistre la connexion partag�e indiqu�e, comme r�utilisable pour une nouvelle connexion.
		 * @param pooledConnection Connexion partag�e r�utilisable.
		 */
		private void recycle(OracleXAConnection pooledConnection) {
			synchronized (reusableConnections) {
				reusableConnections.add(pooledConnection);
			}
		}



		/**
		 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache
		 * correspondant aux param�tres indiqu�s est cr�� si n�cessaire, et sera r�utilis� en cas d'appels successifs avec les m�mes valeurs.
		 * @param xaTransaction Transaction � laquelle est rattach�e cette connexion.
		 * @param dataSource Source de donn�es.
		 * @return La nouvelle connexion � la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
		 * Retourne une connexion JDBC � une base oracle avec le driver thin, issue d'un cache de connexion. Cette connexion n'est pas en autocommit, et est en mode READ COMMITED. Un cache
		 * correspondant aux param�tres indiqu�s est cr�� si n�cessaire, et sera r�utilis� en cas d'appels successifs avec les m�mes valeurs.
		 * @param xaTransaction Transaction � laquelle est rattach�e cette connexion.
		 * @param user Utilisateur Oracle.
		 * @param password Mot de passe.
		 * @param hostName Nom de la machine.
		 * @param portNumber Num�ro du port.
		 * @param databaseName Nom de la base.
		 * @return La nouvelle connexion � la base.
		 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
		 * Source de donn�es pour ce cache de connexions.
		 */
		private final OracleXADataSource dataSource;



		/**
		 * Connexions r�utilisables.
		 */
		private final Vector<OracleXAConnection> reusableConnections = new Vector<>();



		/**
		 * Caches de connexion cr��s lors des appels � {@link #newConnection(OracleXATransaction)} sur un cache cr�� par {@link #OracleXAConnectionCache(OracleXADataSource)}, ou �
		 * {@link #newConnection(OracleXATransaction, OracleXADataSource)}.<br>
		 * La cl� est la source de donn�es, et la valeur est le cache correspondant.
		 */
		private static final Map<OracleXADataSource, OracleXAConnectionCache> cacheByDataSource = new HashMap<>();



		/**
		 * Caches de connexion cr��s lors des appels � {@link #newConnection(OracleXATransaction)} sur un cache cr�� par {@link #OracleXAConnectionCache(String, String, String, int, String)}, ou �
		 * {@link OracleXAConnectionCache#newConnection(OracleXATransaction, String, String, String, int, String)} .<br>
		 * La cl� est l'url, et la valeur est le cache correspondant.
		 */
		private static final Map<String, OracleXAConnectionCache> cacheByUrl = new HashMap<>();



		/**
		 * La classe {@link OracleXACachedConnection} impl�mente une connexion � une base Oracle rattach�e � une transaction distribu�e en d�rivant la classe {@link OracleConnectionWrapper} pour
		 * interdire la fermeture, la validation ou l'annulation de la connexion. La validation ou l'annulation de la transaction doit se faire via la validation ou l'annulation de la transaction
		 * distribu�e � laquelle elle est rattach�e, qui ferme aussi la connexion (elle ne peut pas �tre ferm�e individuellement).
		 * @author Ludovic WALLE
		 */
		public class OracleXACachedConnection extends OracleConnectionWrapper {



			/**
			 * Cr�e une nouvelle connexion � partir d'une connexion partag�e, en lui associant un cache o� placer la connexion lors de sa fermeture, pour r�utilisation.
			 * @param xaConnection Connexion partag�e.
			 * @param xaTransaction Transaction � laquelle cette connexion est rattach�e.
			 * @throws SQLException Si il y a une erreur Oracle.
			 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
			 * Interdit la fermeture de connexion, qui ne peut �tre r�alis�e que par la validation ou l'annulation de la transaction distribu�e � laquelle cette connexion est rattach�e.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void close() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Interdit la validation de la transaction car elle est rattach�e � une transaction distribu�e.<br>
			 * Une transaction rattach�e � une transaction distribu�e ne peut �tre valid�e que lors de la validation de la transaction distribu�e, ce qui fermera aussi la connexion.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void commit() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Valide la branche de transaction distribu�e correspondant � cette connexion.
			 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
			 */
			private void commitBranch() throws XAException {
				xaResource.commit(xid, false);
			}



			/**
			 * D�tache la connexion de la transaction distribu�e.<br>
			 * Cette m�thode ne devrait jamais �tre appel�e directement.
			 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
			 */
			private void end() throws XAException {
				xaResource.end(xid, XAResource.TMSUCCESS);
			}



			/**
			 * Retourne la valeur de la derni�re op�ration de pr�paration de la connexion pour une annulation ou validation.
			 * @return La valeur de la derni�re op�ration de pr�paration de la connexion pour une annulation ou validation.
			 */
			private int getLastPrepareReturnCode() {
				return prepare;
			}



			/**
			 * Pr�pare la connexion pour une annulation ou validation.
			 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
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
			 * Interdit l'annulation de la transaction car elle est rattach�e � une transaction distribu�e.<br>
			 * Une transaction rattach�e � une transaction distribu�e ne peut �tre annul�e que lors de l'annulation de la transaction distribu�e, ce qui fermera aussi la connexion.
			 * @throws UnsupportedOperationException Toujours.
			 */
			@Override public void rollback() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}



			/**
			 * Annule la branche de transaction distribu�e correspondant � cette connexion et recycle cette connexion.
			 * @throws XAException Si il y a une erreur dans la transaction distribu�e.
			 */
			private void rollbackBranch() throws XAException {
				xaResource.rollback(xid);
			}



			/**
			 * R�sultat de la pr�paration de la validation ou de l'annulation.
			 */
			private int prepare;



			/**
			 * Connexion partag�e dont est issue cette connexion.
			 */
			private final OracleXAConnection xaConnection;



			/**
			 * Ressource sur laquelle s'appuie la connexion.
			 */
			private final OracleXAResource xaResource;



			/**
			 * Identifiant de transaction distribu�e.
			 */
			private final OracleXid xid;



		}



	}



}
