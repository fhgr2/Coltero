package ch.htwchur.coltero.dashboard.mngtdashboard.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.user.ActiveInactiveUsersDTO;
import ch.htwchur.coltero.api.dto.user.ActiveUsersPerLocationDTO;
import ch.htwchur.coltero.api.dto.user.UserInteractionsDTO;
import ch.htwchur.coltero.api.dto.user.UserPerLocationDTO;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

public class UserQueries {
	private static final Logger log = LoggerFactory.getLogger(MngtCountQueries.class);

	private IDatabase database;
	private QuerieLoader querieLoader;
	static final DateFormat df = new SimpleDateFormat("yyyy-MM");

	private static final String QRY_USR_LOCATION = "dataqueries/user/querie_userperlocation.sql";
	private static final String QRY_USR_AVSP = "dataqueries/user/querie_activevspassiveusers_ot.sql";
	private static final String QRY_USR_ACTIVEPERLOCATION = "dataqueries/user/querie_activeuserslocation_ot.sql";
	private static final String QRY_USR_INTERACTIONS = "dataqueries/user/querie_userinteractions.sql";

	@Autowired
	public UserQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		this.database = database;
		this.querieLoader = querieLoader;
	}

	/**
	 * Queries Users per Location
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<UserPerLocationDTO> querieUserPerLocation() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_LOCATION));
		log.debug("Executing querie: " + QRY_USR_LOCATION);
		ResultSet rs = stmnt.executeQuery();
		List<UserPerLocationDTO> resultList = new ArrayList<UserPerLocationDTO>();
		while (rs.next()) {
			resultList.add(new UserPerLocationDTO(SpaceQueries.temporaryLocationMapper(rs.getString(1)), rs.getInt(2)));
		}
		return resultList;
	}

	/**
	 * Queries Active and Inactive users resolved by date and calculates precentage
	 * and summarization of created users over time
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ActiveInactiveUsersDTO> querieActiveAndInactiveUsers() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_AVSP));
		log.debug("Executing querie: " + QRY_USR_AVSP);
		ResultSet rs = stmnt.executeQuery();
		List<ActiveInactiveUsersDTO> resultList = new ArrayList<ActiveInactiveUsersDTO>();
		float precentage = 0f;
		int summarizedUserCount = 0;
		int inactiveUsers = 0;
		while (rs.next()) {
			summarizedUserCount += rs.getInt(3);
			inactiveUsers = summarizedUserCount - rs.getInt(1);
			precentage = ((float) rs.getInt(1) / summarizedUserCount) * 100;
			resultList
					.add(new ActiveInactiveUsersDTO(rs.getInt(1), inactiveUsers, precentage, df.format(rs.getDate(2))));
		}
		return resultList;
	}

	/**
	 * Queries all Active Users on a location on date resolved by month in addition
	 * with rolled sum of all active users on all locations
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ActiveUsersPerLocationDTO> querieActiveUsersPerLocation() throws SQLException {
		// querie calculated data -> users per location, date and count and
		// allactiveUsers rolling sum
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_ACTIVEPERLOCATION));
		log.debug("Executing querie: " + QRY_USR_ACTIVEPERLOCATION);
		ResultSet rs = stmnt.executeQuery();
		List<ActiveUsersPerLocationDTO> resultList = new ArrayList<ActiveUsersPerLocationDTO>();
		while (rs.next()) {
			resultList.add(new ActiveUsersPerLocationDTO(rs.getInt(2), rs.getInt(3), SpaceQueries.temporaryLocationMapper(rs.getString(1)),
					df.format(rs.getDate(4))));
		}
		return resultList;
	}

	/**
	 * Queries overall interactions splittet by space and date for Boxplot viz
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<UserInteractionsDTO> querieUserInteractions() throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_INTERACTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_INTERACTIONS));
		ResultSet rs = stmnt.executeQuery();
		List<UserInteractionsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserInteractionsDTO(rs.getString("displayname"), df.format(rs.getDate("date")),
					rs.getString("spacename"), rs.getInt("interactions")));
		}
		Set<String> quarterSet = new HashSet<>();
		resultList.forEach(item -> quarterSet.add(item.getDate()));
		List<String> quarterList = new ArrayList<>();
		quarterSet.forEach(item -> quarterList.add(item));
		Collections.sort(quarterList);
		Map<String, Integer> numberOfContent = new HashMap<>();
		for (String quarter : quarterList) {
			for (UserInteractionsDTO current : resultList) {
				if (current.getDate().equals(quarter)) {
					if (numberOfContent.containsKey(current.getDisplayName())) {
						int count = numberOfContent.get(current.getDisplayName());
						numberOfContent.put(current.getDisplayName(), current.getInteractions() + count);
						current.setInteractions(current.getInteractions() + count);
					} else {
						numberOfContent.put(current.getDisplayName(), current.getInteractions());

					}
				}

			}
		}
		return resultList;
	}

}
