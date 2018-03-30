package toolbox.parallel.missionners;

import java.sql.*;

import toolbox.oracle.*;
import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link SqlNumberMissionner} impl�mente un g�n�rateur de num�ros � partir d'une requ�te SQL. La requ�te ne doit prendre aucun param�tre en entr�e et doit retourner une liste de num�ros.
 * @author Ludovic WALLE
 */
public class SqlNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * @param connection Connection.
	 * @param query Texte de la requ�te de selection des num�ros.
	 * @param closeConnection Indicateur de fermeture de connection en fin de traitement.
	 */
	public SqlNumberMissionner(Connection connection, String query, boolean closeConnection) {
		this.connection = connection;
		this.query = query;
		this.closeConnection = closeConnection;
	}



	/**
	 * Retourne le nombre total de num�ros � traiter.
	 * @return Le nombre total de num�ros � traiter.
	 */
	@Override protected synchronized int delegateComputeExpectedCount() {
		String countQuery = "SELECT COUNT(*) FROM (" + query + ")";

		try {
			try (@SuppressWarnings("hiding") PreparedStatement statement = connection.prepareStatement(countQuery)) {
				try (@SuppressWarnings("hiding") ResultSet resultSet = statement.executeQuery()) {
					resultSet.next();
					return resultSet.getInt(1);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException(countQuery, exception);
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected void delegateFinalize() {
		OracleTools.close(resultSet);
		OracleTools.close(statement);
		if (closeConnection) {
			OracleTools.close(connection);
		}
	}



	/**
	 * {@inheritDoc}
	 * @throws SQLException
	 */
	@Override protected IntegerMission delegateGetNext() throws SQLException {
		int integer;

		if (resultSet.next()) {
			integer = resultSet.getInt(1);
			if (resultSet.wasNull()) {
				return null;
			} else {
				return new IntegerMission(integer);
			}
		} else {
			return null;
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected void delegateInitialize() {
		try {
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
		} catch (SQLException exception) {
			throw new RuntimeException(query, exception);
		}
	}



	/**
	 * Indicateur de fermeture de connection en fin de traitement.
	 */
	private final boolean closeConnection;



	/**
	 * Connection � utiliser pour les acc�s � la base.
	 */
	private final Connection connection;



	/**
	 * Texte de la requ�te de s�lection des num�ros de composants � traiter.
	 */
	private final String query;



	/**
	 * ResultSet des num�ros de composants � traiter.
	 */
	private ResultSet resultSet;



	/**
	 * Requ�te de s�lection des num�ros de composants � traiter.
	 */
	private PreparedStatement statement;



}
