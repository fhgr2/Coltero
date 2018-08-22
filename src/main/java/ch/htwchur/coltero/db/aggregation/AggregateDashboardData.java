package ch.htwchur.coltero.db.aggregation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.dashboard.mngtdashboard.queries.MngtCountQueries;
import ch.htwchur.coltero.dashboard.userdashboard.UserQueries;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

public class AggregateDashboardData {

	private IDatabase database;
	private QuerieLoader querieLoader;
	private UserQueries userQueries;
	
	private static final Logger log = LoggerFactory.getLogger(MngtCountQueries.class);
	
	private static final String QRY_AGG_COLTERO = "agg_coltero_querie.sql";
	private static final String QRY_CALC_COUNTS = "counts_coltero_querie.sql";
	private static final String QRY_CALC_CONTENTCHAIN = "agg_contentchain_coltero.sql";
	private static final String QRY_CALC_SPACERELATIONS = "agg_spacerelations.sql";
	private static final String QRY_CREATE_AGG_TBL = "create_agg_tables.sql";
	private static final String QRY_TRUNCATOR = "querie_truncator.sql";
	private static final String INS_LOCATIONS = "location/insert_coltero_location.sql";
	private static final String QRY_LOCATIONS = "select count(*) from coltero_location";
	private static final String INS_SHAREDLOCATION = "insert_sharedlocation.sql";
	
	@Autowired
	public AggregateDashboardData(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader, @ComponentImport UserQueries userQueries) {
		this.database = database;
		this.querieLoader = querieLoader;
		this.userQueries = userQueries;
	}
	
	/**
	 * Calculate new Aggregation
	 * @throws SQLException
	 */
	public boolean preAggregateData() throws SQLException {
		log.debug("Preaggregating Data from Database...");
		Statement stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_CREATE_AGG_TBL));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_TRUNCATOR));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_AGG_COLTERO));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_CALC_COUNTS));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_CALC_CONTENTCHAIN));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(QRY_CALC_SPACERELATIONS));
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.executeQuery(QRY_LOCATIONS);
		stmnt = database.getDatabaseConnection().createStatement();
		stmnt.execute(querieLoader.loadQuerie(INS_SHAREDLOCATION));
		if(!checkLocationsAlreadyInserted()) {
			stmnt = database.getDatabaseConnection().createStatement();
			stmnt.execute(querieLoader.loadQuerie(INS_LOCATIONS));
		}
		userQueries.aggregateUserStatisticsForAllUsers();
		database.closeConnection();
		return true;
		
	}
	
	/**
	 * Checks if locations already inserted into coltero_locations table
	 * @return true if so
	 * @throws SQLException
	 */
	private boolean checkLocationsAlreadyInserted() throws SQLException {
		Statement stmnt = database.getDatabaseConnection().createStatement();
		ResultSet rs = stmnt.executeQuery(QRY_LOCATIONS);
		while(rs.next()) {
			if(rs.getInt("count") > 5) {
				return true;
			}
		}
		return false;
		
		
	}
	
}
