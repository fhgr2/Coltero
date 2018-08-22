package ch.htwchur.coltero.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabase {
	/**
	 * Creates Postgres connection and returns it
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getDatabaseConnection() throws SQLException;

	/**
	 * True if connection is closed
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean connectionClosed();

	/**
	 * Closes connection if open, otherwise do nothing
	 * 
	 * @throws SQLException
	 */
	public void closeConnection();

	/**
	 * queryData from DB with predefined querie-string
	 * 
	 * @param querie
	 * @return ResultSet of queried data
	 */
	public ResultSet queryData(String querie);
}
