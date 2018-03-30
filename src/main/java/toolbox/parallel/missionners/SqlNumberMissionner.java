package toolbox.parallel.missionners;

import java.sql.*;

import toolbox.oracle.*;
import toolbox.parallel.*;
import toolbox.parallel.missions.*;



/**
 * La classe {@link SqlNumberMissionner} implémente un générateur de numéros à partir d'une requète SQL. La requète ne doit prendre aucun paramètre en entrée et doit retourner une liste de numéros.
 * @author Ludovic WALLE
 */
public class SqlNumberMissionner extends Missionner<IntegerMission> {



	/**
	 * @param connection Connection.
	 * @param query Texte de la requète de selection des numéros.
	 * @param closeConnection Indicateur de fermeture de connection en fin de traitement.
	 */
	public SqlNumberMissionner(Connection connection, String query, boolean closeConnection) {
		this.connection = connection;
		this.query = query;
		this.closeConnection = closeConnection;
	}



	/**
	 * Retourne le nombre total de numéros à traiter.
	 * @return Le nombre total de numéros à traiter.
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
	 * Connection à utiliser pour les accès à la base.
	 */
	private final Connection connection;



	/**
	 * Texte de la requète de sélection des numéros de composants à traiter.
	 */
	private final String query;



	/**
	 * ResultSet des numéros de composants à traiter.
	 */
	private ResultSet resultSet;



	/**
	 * Requète de sélection des numéros de composants à traiter.
	 */
	private PreparedStatement statement;



}
