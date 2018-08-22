package ch.htwchur.coltero.dashboard.mngtdashboard.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.interaction.InteractionsDTO;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

public class InteractionQueries {

	private static final Logger log = LoggerFactory.getLogger(MngtCountQueries.class);

	/**
	 * Querie aggregated dashboard-count values
	 */
	private static final String QRY_INT_HOURS = "dataqueries/querie_workinteractionhours.sql";
	private static final String QRY_INT_DOW = "dataqueries/querie_workinteractionweek.sql";
	
	private static final String COUNTRY_DATA = "data/world-countries.json";

	private static IDatabase database;
	private QuerieLoader querieLoader;

	@Autowired
	public InteractionQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		InteractionQueries.database = database;
		this.querieLoader = querieLoader;
	}

	/**
	 * Queries Userinteractions per Day of week summarized
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<InteractionsDTO> querieInteractionsPerDOW() throws SQLException {
		PreparedStatement pstmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_INT_DOW));
		log.debug("Executing querie: " + QRY_INT_DOW);
		ResultSet rs = pstmnt.executeQuery();
		List<InteractionsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new InteractionsDTO(rs.getString(1), rs.getInt(3)));
		}
		return resultList;

	}

	/**
	 * Queries Userinteractions per Hour of day summarized
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<InteractionsDTO> querieInteractionsPerHour() throws SQLException {
		PreparedStatement pstmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_INT_HOURS));
		log.debug("Executing querie: " + QRY_INT_HOURS);
		ResultSet rs = pstmnt.executeQuery();
		List<InteractionsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new InteractionsDTO(rs.getInt(1), rs.getInt(2)));
		}
		return resultList;
	}
	
	/**
	 * Reads and return World-map data
	 * @return json String
	 */
	public String querieLocationDataForWoldmap() {
		return querieLoader.loadFile(COUNTRY_DATA);
	}
}
