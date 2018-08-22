package ch.htwchur.coltero.dashboard.logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.user.UserInteractionsDTO;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

public class UserInteractionLogic {
	private static final Logger log = LoggerFactory.getLogger(UserInteractionLogic.class);
	private static final String QRY_USR_INTERACTIONS = "querie_userinteractions.sql";
	private static final String INS_USR_INTERACTIONS = "agg_userinteractions.sql";

	private QuerieLoader querieLoader;
	private IDatabase database;
	
	@Autowired
	public UserInteractionLogic(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		this.querieLoader = querieLoader;
		this.database = database;
	}
	
	/**
	 * Queries overall interactions splittet by space and date for Boxplot viz
	 * @return
	 * @throws SQLException
	 */
	public List<UserInteractionsDTO> calcUserInteractions() throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_INTERACTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_INTERACTIONS));
		ResultSet rs = stmnt.executeQuery();
		List<UserInteractionsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserInteractionsDTO(rs.getString("display_name"), rs.getString("date"),
					rs.getString("spacename"), rs.getInt("count")));
		}
		return resultList;
	}
	
	/**
	 * Aggregates UserInteractionList to Database for faster querie
	 * @param interactionList
	 * @throws SQLException
	 */
	public void aggregateUserInteractions(List<UserInteractionsDTO> interactionList) throws SQLException {
		log.debug("Pre-Aggregating UserInteractions: " + QRY_USR_INTERACTIONS);
		String statement = querieLoader.loadQuerie(INS_USR_INTERACTIONS);
		for(UserInteractionsDTO item : interactionList) {
			PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(statement);
			stmnt.setString(1, item.getDisplayName());
			stmnt.setString(2, item.getSpaceName());
			stmnt.setString(3, item.getDate());
			stmnt.setInt(4, item.getInteractions());
			stmnt.execute();
		}
		
	}
	
}
