package ch.htwchur.coltero.dashboard.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * Helper Class for Resolving names to ids and back
 * {Space, User, Page}
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class GenericResolver {

	public static final Logger log = LoggerFactory.getLogger(GenericResolver.class);

	private final static String QRY_USR_ID = "dataqueries/user/querie_usernameuserkey.sql";
	private final static String QRY_USR_NAME = "dataqueries/user/querie_userkeyusername.sql";
	private final static String QRY_PAGEID_TITLE = "dataqueries/pageboard/querie_pageid_pagetitle.sql";
	private final static String QRY_TITLE_PAGEID = "dataqueries/pageboard/querie_pagetitle_pageid.sql";
	private final static String QRY_SPACENAME_ID = "dataqueries/spaceboard/querie_spacenamespaceid.sql";
	private final static String QRY_SPACEID_NAME = "dataqueries/spaceboard/querie_spaceidspacename.sql";

	private IDatabase database;
	private QuerieLoader querieLoader;

	@Autowired
	public GenericResolver(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		this.database = database;
		this.querieLoader = querieLoader;
	}

	/**
	 * Resolves SpaceName to Spaceid
	 * 
	 * @param spaceName
	 * @return spaceid
	 */
	public int resolveSpaceNameToSpaceId(String spaceName) {
		log.debug("Executing Querie: " + QRY_SPACENAME_ID);
		PreparedStatement stmnt = null;
		int spaceId = 0;
		try {
			stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_SPACENAME_ID));
			stmnt.setString(1, spaceName);
			ResultSet rs;
			rs = stmnt.executeQuery();

			while (rs.next()) {
				spaceId = rs.getInt("spaceid");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return spaceId;
	}

	/**
	 * Resolves spaceId to SpaceName
	 * 
	 * @param spaceId
	 * @return spacename
	 */
	public String resolveSpaceIdToSpaceName(int spaceId) {
		log.debug("Executing Querie: " + QRY_SPACEID_NAME);
		PreparedStatement stmnt = null;
		String spaceName = null;
		try {
			stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_SPACEID_NAME));
			stmnt.setInt(1, spaceId);
			ResultSet rs;
			rs = stmnt.executeQuery();

			while (rs.next()) {
				spaceName = rs.getString("spacename");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return spaceName;
	}

	/**
	 * Resolves Page title to pageid
	 * 
	 * @param pageTitle
	 * @return
	 * @throws SQLException
	 */
	public int resolveTitleToPageId(String pageTitle) {
		log.debug("Executing Querie: " + QRY_TITLE_PAGEID);
		PreparedStatement stmnt = null;
		int pageid = 0;
		try {
			stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_TITLE_PAGEID));
			stmnt.setString(1, pageTitle);
			ResultSet rs;
			rs = stmnt.executeQuery();

			while (rs.next()) {
				pageid = rs.getInt("pageid");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return pageid;
	}

	/**
	 * Resolves pageid to pagetitle
	 * 
	 * @param pageid
	 * @return
	 * @throws SQLException
	 */
	public String resolvePageidToPageTitle(int pageid) {
		log.debug("Executing Querie: " + QRY_PAGEID_TITLE);
		String pageTitle = null;
		try {
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_PAGEID_TITLE));
			stmnt.setInt(1, pageid);
			ResultSet rs = stmnt.executeQuery();
			while (rs.next()) {
				pageTitle = rs.getString("title");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return pageTitle;
	}

	/**
	 * Resolve userkey to Username
	 * 
	 * @param userid
	 *            like lastmodifier
	 * @return
	 * @throws SQLException
	 */
	public String resolveUserkeyToUsername(String userid) throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_ID);
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_ID));
		stmnt.setString(1, userid);
		ResultSet rs = stmnt.executeQuery();
		String username = null;
		while (rs.next()) {
			username = rs.getString("display_name");
		}
		return username;
	}

	/**
	 * Resolves user display-name to user-key
	 * 
	 * @param username
	 *            like display name
	 * @return
	 * @throws SQLException
	 */
	public String resolveUsernameToUserKey(String username) throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_NAME);
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_NAME));
		stmnt.setString(1, username);
		ResultSet rs = stmnt.executeQuery();
		String userid = null;
		while (rs.next()) {
			userid = rs.getString("userid");
		}
		return userid;
	}
}
