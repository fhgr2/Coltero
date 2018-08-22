package ch.htwchur.coltero.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database management Class for creating, closing and executing queries against
 * the database
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class Database implements IDatabase {

	private static final Logger log = LoggerFactory.getLogger(Database.class);
	private static Connection connection;

	public Database() {
	}

	@Override
	public Connection getDatabaseConnection() throws SQLException {

		if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
			return connection;
		}
		if (connection != null) {
			try {
				connection.isValid(1000);
			} catch (SQLException e) {
				log.info("Connection not valid anymore, reconnecting " + e.getMessage() + " " + e.getSQLState());
				connection.close();
			}
		}
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			log.error("Could not load Database Driver: " + ex.getCause() + " " + ex.getMessage());
		}
		// TODO: fill in jdbc connection string, user and password
		connection = DriverManager.getConnection(/* jdbc://connection url */ "", /* user */ "", /* password */ "");
		return connection;
	}

	@Override
	public boolean connectionClosed() {
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			log.error("Could not check connection " + e.getCause());
		}
		return true;
	}

	@Override
	public void closeConnection() {
		try {
			if (!connectionClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error("Could not close Connection " + e.getCause());
		}
	}

	@Override
	public ResultSet queryData(String querie) {
		if (querie == null) {
			log.error("No query String set. Querie= " + querie);
			return null;
		}
		ResultSet rs = null;
		log.debug("Executing querie: " + querie);
		PreparedStatement stmnt;
		try {
			stmnt = getDatabaseConnection().prepareStatement(querie);
			rs = stmnt.executeQuery();
		} catch (SQLException e) {
			log.error("Error in executing querie: " + querie + " " + e.getSQLState() + " " + e.getMessage());
			closeConnection();
		}
		return rs;
	}
}
