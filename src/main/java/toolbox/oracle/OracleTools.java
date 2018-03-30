package toolbox.oracle;



import java.sql.*;

import javax.transaction.xa.*;



/**
 * La classe DTDTools contient des m�thodes statiques d'usage g�n�ral.
 * @author Ludovic WALLE
 */
public final class OracleTools {



	/**
	 * Constructeur priv� pour interdire l'instanciation.
	 */
	private OracleTools() {}



	/**
	 * Ferme la connexion indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param connection Connexion � fermer.
	 * @return <code>null</code> (permet de faire <code>x = close(x)</code>).
	 */
	public static final Connection close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
			return null;
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Ferme l'instruction indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param statement instruction a fermer.
	 * @return <code>null</code> (permet de faire <code>x = close(x)</code>).
	 */
	public static final PreparedStatement close(PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
			return null;
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Ferme le {@link ResultSet} indiqu�, si il n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param resultSet {@link ResultSet} � fermer.
	 * @return <code>null</code> (permet de faire <code>x = close(x)</code>).
	 */
	public static final ResultSet close(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			return null;
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Ferme l'instruction indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param statement Instruction a fermer.
	 * @return <code>null</code> (permet de faire <code>x = close(x)</code>).
	 */
	public static final Statement close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
			return null;
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Ferme la connexion indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param connection Connexion � fermer.
	 */
	public static final void commit(Connection connection) {
		try {
			if (connection != null) {
				connection.commit();
			}
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Valide la transaction indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param transaction Transaction � valider.
	 */
	public static final void commit(OracleXATransaction transaction) {
		try {
			if (transaction != null) {
				transaction.commit();
			}
		} catch (XAException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Ferme la connexion indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param connection Connexion pour laquelle il faut annuler la transaction.
	 */
	public static final void rollback(Connection connection) {
		try {
			if (connection != null) {
				connection.rollback();
			}
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Annule la transaction indiqu�e, si elle n'est pas <code>null</code>, et g�n�re une {@link RuntimeException} en cas d'erreur.
	 * @param transaction Transaction � annuler.
	 */
	public static final void rollback(OracleXATransaction transaction) {
		try {
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (XAException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Code oracle retourn� en cas de double dans un index unique.
	 */
	public static final int DUPLICATE = 1;



}
