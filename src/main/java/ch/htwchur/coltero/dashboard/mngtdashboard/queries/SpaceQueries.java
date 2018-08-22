package ch.htwchur.coltero.dashboard.mngtdashboard.queries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.ibm.icu.util.Calendar;

import ch.htwchur.coltero.api.common.CollaborationIndicatorDTO;
import ch.htwchur.coltero.api.common.CollaborationMetricDTO;
import ch.htwchur.coltero.api.common.ContentBodyDTO;
import ch.htwchur.coltero.api.dto.space.SharedSpacesDTO;
import ch.htwchur.coltero.api.dto.space.SpaceAllOtherTagsDTO;
import ch.htwchur.coltero.api.dto.space.SpaceDTO;
import ch.htwchur.coltero.api.dto.space.SpaceTagDTO;
import ch.htwchur.coltero.api.dto.space.SpaceTop10DTO;
import ch.htwchur.coltero.api.dto.space.SpaceTypeDTO;
import ch.htwchur.coltero.api.dto.space.network.Links;
import ch.htwchur.coltero.api.dto.space.network.NetworkModel;
import ch.htwchur.coltero.api.dto.space.network.Node;
import ch.htwchur.coltero.api.dto.space.network.SpaceUser;
import ch.htwchur.coltero.dashboard.helper.DateHelper;
import ch.htwchur.coltero.dashboard.logic.extraction.HtmlContentExtractor;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

public class SpaceQueries {

	private static final Logger log = LoggerFactory.getLogger(SpaceQueries.class);

	/**
	 * Querie aggregated dashboard-count values
	 */
	private static final String QRY_SPC_CREATION = "dataqueries/space/querie_spacecreationot.sql";
	private static final String QRY_SPC_TYPES = "dataqueries/space/querie_spacecreationtypeot.sql";
	private static final String QRY_SPC_TAG = "dataqueries/space/querie_spacetagot.sql";
	private static final String QRY_SPC_OTHERTAGS = "dataqueries/space/querie_spaceothertags_ot.sql";
	private static final String QRY_SPC_TOPUSERCOUNT = "dataqueries/top10/querie_spaces_userscount.sql";
	private static final String QRY_SPC_TOPAUTHCOMUPL = "dataqueries/top10/querie_spaces_authors_comm_upload.sql";
	private static final String QRY_SPC_TOPCOLLABORATIVE = "dataqueries/top10/querie_spaces_collaborative.sql";
	private static final String QRY_SPC_TOPCOMMENTET = "dataqueries/top10/querie_spaces_commentcount.sql";
	private static final String QRY_SPC_ISOLATEDCOUNT = "dataqueries/space/querie_isolatedspaces.sql";
	private static final String QRY_SPC_ALL = "dataqueries/space/querie_spaces.sql";
	private static final String QRY_SPC_NETWORK = "dataqueries/space/querie_cospaces_networkr.sql";
	private static final String QRY_SPC_SHAREDLOCATION = "dataqueries/space/querie_locationsharedspaces.sql";
	private static final String QRY_SPC_COLLABVSDOCUMEN = "dataqueries/space/querie_collaborationvsdocumentation.sql";
	private static final String QRY_FIRST_DBENTRY = "date/querie_firstentrydatedb.sql";
	private static final String QRY_ALL_SPACES = "dataqueries/space/querie_allspaces_4spacetag.sql";
	private static final String QRY_ALL_LOCATIONS = "location/qry_alllocations.sql";

	private static IDatabase database;
	private QuerieLoader querieLoader;
	private HtmlContentExtractor contentExtractor;

	@Autowired
	public SpaceQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader,
			@ComponentImport HtmlContentExtractor contentExtractor) {
		SpaceQueries.database = database;
		this.querieLoader = querieLoader;
		this.contentExtractor = contentExtractor;
	}

	/**
	 * Queries Space-Creation statistics over time-axis
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceDTO> querieSpaceCreationsOverTime() throws SQLException {
		PreparedStatement pstmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_CREATION));
		log.debug("Executing querie: " + QRY_SPC_CREATION);
		ResultSet rs = pstmnt.executeQuery();
		int summarized = 0;
		List<SpaceDTO> resultList = new ArrayList<SpaceDTO>();
		while (rs.next()) {
			int count = rs.getInt(1);
			summarized = summarized + count;
			Date date = rs.getDate(2);
			String dateString = DateHelper.df.format(date);
			resultList.add(new SpaceDTO(summarized, dateString));
		}
		return resultList;

	}

	/**
	 * Queries Space type creation over time with total count for stacked bar chart
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceTypeDTO> querieSpaceTypesOverTime() throws SQLException {
		PreparedStatement pstmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TYPES));
		log.debug("Executing querie: " + QRY_SPC_TYPES);
		ResultSet rs = pstmnt.executeQuery();
		List<SpaceTypeDTO> resultList = new ArrayList<SpaceTypeDTO>();
		int sumGlobal = 0;
		int sumPersonal = 0;
		while (rs.next()) {
			if (rs.getString(1).equals("personal")) {
				sumPersonal += rs.getInt(2);
			} else {
				sumGlobal += rs.getInt(2);
			}
			resultList.add(new SpaceTypeDTO(DateHelper.df.format(rs.getDate(3)),
					(rs.getString(1).equals("personal") ? sumPersonal : sumGlobal), rs.getString(1)));
		}
		return resultList;
	}

	/**
	 * Queries Space Tag project over time wiht total count and precentage for
	 * stacked bar chart
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public List<SpaceTagDTO> querieSpaceTagsOverTime(String spaceLabel) throws SQLException, ParseException {
		// TODO: The querie attribute is replaced by String.replace -> this because its
		// not possible to setParam in preparedstatement
		// if views are created

		log.debug("Executing querie: " + QRY_FIRST_DBENTRY);
		PreparedStatement firstEntryDate = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_FIRST_DBENTRY));
		ResultSet firstEntryRs = firstEntryDate.executeQuery();
		List<SpaceTagDTO> allSpacesList = new ArrayList<SpaceTagDTO>();
		Date firstDate = null;
		while (firstEntryRs.next()) {
			firstDate = firstEntryRs.getDate(1);
		}
		List<java.util.Date> allMonthsToNow = DateHelper.getAllMonthAsDate(firstDate);
		for (java.util.Date date : allMonthsToNow) {
			allSpacesList.add(new SpaceTagDTO(DateHelper.df.format(date)));
		}

		// allSpacesList.remove(allSpacesList.size() - 1);

		log.debug("Executing querie: " + QRY_ALL_SPACES);
		PreparedStatement allSpaces = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_ALL_SPACES));
		ResultSet allSpacesRs = allSpaces.executeQuery();
		int totalSpaces = 0;
		while (allSpacesRs.next()) {
			int index = allSpacesList.indexOf(new SpaceTagDTO(DateHelper.df.format(allSpacesRs.getDate("date"))));
			SpaceTagDTO thisElement = allSpacesList.get(index);
			totalSpaces += allSpacesRs.getInt("spacecount");
			thisElement.setTotalSpaces(totalSpaces);
		}

		PreparedStatement pstmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TAG));
		pstmnt.setString(1, spaceLabel);
		log.debug("Executing querie: " + QRY_SPC_TAG);
		ResultSet rs = pstmnt.executeQuery();
		Collections.sort(allSpacesList);

		int totalTagged = 0;
		while (rs.next()) {
			int index = allSpacesList.indexOf(new SpaceTagDTO(DateHelper.df.format(rs.getDate("date"))));
			SpaceTagDTO thisElement = allSpacesList.get(index);
			totalTagged += rs.getInt("tagged");
			thisElement.setTaggedSpaces(totalTagged);
			thisElement.setProcessed(true);
		}

		for (int i = 0; i < allSpacesList.size(); i++) {
			if (!allSpacesList.get(i).isProcessed()) {
				if (i > 0) {
					allSpacesList.get(i).setTaggedSpaces(allSpacesList.get(i - 1).getTaggedSpaces());
				}
			}
		}
		// remove anything before actual quarter
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = new Date(cal.getTimeInMillis());
		Iterator<SpaceTagDTO> dateIterator = allSpacesList.iterator();
		while (dateIterator.hasNext()) {
			if (lastDate.before(DateHelper.df.parse(dateIterator.next().getDate()))
					|| lastDate.equals(DateHelper.df.parse(dateIterator.next().getDate()))) {
				dateIterator.remove();
			}
		}
		// remove latest date cause quarter its not finished yet
		allSpacesList.remove(allSpacesList.size()-1);
		return allSpacesList;
	}

	/**
	 * Queries all not defined SpaceTags over time with total count and precentage
	 * for stacked bar chart
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public List<SpaceAllOtherTagsDTO> querieAllOtherSpaceTagsOverTime() throws SQLException, ParseException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_OTHERTAGS));
		log.debug("Executing querie: " + QRY_SPC_OTHERTAGS);
		ResultSet rs = stmnt.executeQuery();
		List<SpaceAllOtherTagsDTO> resultList = new ArrayList<SpaceAllOtherTagsDTO>();
		while (rs.next()) {
			resultList.add(new SpaceAllOtherTagsDTO(rs.getString(1), rs.getInt(2), rs.getInt(3), 0.0f,
					DateHelper.df.format(rs.getDate(4))));
		}
		// get total spaces available on last entry date (ordered on db by date)
		Set<SpaceAllOtherTagsDTO> uniqueTagSet = new HashSet<SpaceAllOtherTagsDTO>(resultList);
		Iterator<SpaceAllOtherTagsDTO> itr = uniqueTagSet.iterator();
		while (itr.hasNext()) {
			SpaceAllOtherTagsDTO compare = itr.next();
			boolean removeTag = false;
			for (SpaceAllOtherTagsDTO element : resultList) {
				if (element.getSpaceTagName().equals(compare.getSpaceTagName())) {
					if (element.getSpaceCount() > 4) {
						removeTag = true;
					}
				}
			}
			if (removeTag) {
				itr.remove();
			}
		}
		resultList.removeAll(uniqueTagSet);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = new Date(cal.getTimeInMillis());
		Iterator<SpaceAllOtherTagsDTO> tagItr = resultList.iterator();
		while (tagItr.hasNext()) {
			if (lastDate.before(DateHelper.df.parse(tagItr.next().getDate()))
					|| lastDate.equals(DateHelper.df.parse(tagItr.next().getDate()))) {
				tagItr.remove();
			}
		}
		resultList.remove(resultList.size()-1);
		// remove any tag with less then 5 tagged spaces
		return resultList;
	}

	/**
	 * Querie Top10 Spaces based on contruibuting users
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceTop10DTO> querieTop10SpacesUserBased() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TOPUSERCOUNT));
		log.debug("Executing querie: " + QRY_SPC_TOPUSERCOUNT);
		ResultSet rs = stmnt.executeQuery();
		List<SpaceTop10DTO> resultList = new ArrayList<SpaceTop10DTO>();
		while (rs.next()) {
			resultList.add(new SpaceTop10DTO(rs.getString(2), rs.getInt(1)));
		}
		return resultList;
	}

	/**
	 * Queries Top 10 Spaces based on commentators, authors and uploaders
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceTop10DTO> querieTop10SpacesAuthCommAttBased() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TOPAUTHCOMUPL));
		log.debug("Executing querie: " + QRY_SPC_TOPAUTHCOMUPL);
		ResultSet rs = stmnt.executeQuery();
		List<SpaceTop10DTO> resultList = new ArrayList<SpaceTop10DTO>();
		while (rs.next()) {
			resultList.add(new SpaceTop10DTO(rs.getString(2), rs.getInt(1)));
		}
		return resultList;
	}

	/**
	 * Querie Top 10 Spaces based on different authors
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceTop10DTO> querieTop10SpacesCollaborative() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TOPCOLLABORATIVE));
		log.debug("Executing querie: " + QRY_SPC_TOPCOLLABORATIVE);
		ResultSet rs = stmnt.executeQuery();
		List<SpaceTop10DTO> resultList = new ArrayList<SpaceTop10DTO>();
		while (rs.next()) {
			resultList.add(new SpaceTop10DTO(rs.getString(2), rs.getInt(1)));
		}
		return resultList;
	}

	/**
	 * Qeurie Top 10 Spaces based on discussed spaces (most comments)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceTop10DTO> querieTop10SpacesCommented() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_TOPCOMMENTET));
		log.debug("Executing querie: " + QRY_SPC_TOPCOMMENTET);
		ResultSet rs = stmnt.executeQuery();
		List<SpaceTop10DTO> resultList = new ArrayList<SpaceTop10DTO>();
		while (rs.next()) {
			resultList.add(new SpaceTop10DTO(rs.getString(2), rs.getInt(1)));
		}
		return resultList;
	}

	/**
	 * Querie all Page content bodys with depending spacename
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ContentBodyDTO> querieSpacePageBodyContent() throws SQLException {
		return contentExtractor.getAggSpaceWordCount();
	}

	/**
	 * Querie all Comment content bodys with depending spacename
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ContentBodyDTO> querieSpaceCommentBodyContent() throws SQLException {
		return contentExtractor.getAggSpaceCommentWordCount();
	}

	/**
	 * Queries count of isolated spaces over time
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SpaceDTO> querieIsolatedSpaceCount() throws SQLException {
		log.debug("Executing querie: " + QRY_SPC_ISOLATEDCOUNT);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_ISOLATEDCOUNT));
		List<SpaceDTO> resultList = new ArrayList<>();
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			resultList.add(new SpaceDTO(rs.getInt("isolated"), DateHelper.df.format(rs.getDate("quarter"))));
		}
		return resultList;
	}

	/**
	 * Calculating network diagram based on contributing users in all spaces
	 * 
	 * @return
	 * @throws SQLException
	 */
	public NetworkModel querieAndCalculateSpaceNetwork() throws SQLException {
		// Read all available spaces in Database and threat them as nodes
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_ALL));
		ResultSet rs = stmnt.executeQuery();
		List<Node> nodeList = new ArrayList<>();
		// add Spaces as Nodes, group 0 (will be calculated later)
		while (rs.next()) {
			nodeList.add(new Node(rs.getString("spacename"), 0));
		}
		stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_SPC_NETWORK));
		ResultSet spaceUserPair = stmnt.executeQuery();
		List<SpaceUser> spaceUsersList = new ArrayList<>();

		// put all values from Resultset into collection
		while (spaceUserPair.next()) {
			spaceUsersList.add(new SpaceUser(spaceUserPair.getString(1), spaceUserPair.getString(2), 1));
		}

		Iterator<Node> itr = nodeList.iterator();

		while (itr.hasNext()) {
			Node node = itr.next();
			for (SpaceUser currentPair : spaceUsersList) {
				// if user does not already exist in space, add it, for every time a user
				// occurs,
				// increase weight

				// Check for spacename, if equal, add users in space
				if (node.getName().equalsIgnoreCase(currentPair.getSpacename())) {
					if (node.getUsers().containsKey(currentPair.getUserName())) {
						int weight = node.getUsers().get(currentPair.getUserName());
						node.getUsers().replace(currentPair.getUserName(), weight, ++weight);
						// if user does not exists, add it with weight 1
					} else {
						node.getUsers().put(currentPair.getUserName(), 1);
					}
				}
			}
		}
		// links
		List<Links> links = new ArrayList<>();
		for (Node currentNode : nodeList) {
			// set contains all usernames for current Node
			Set<String> userSet = currentNode.getUsers().keySet();
			for (Node comparingNode : nodeList) {
				if (!currentNode.equals(comparingNode)) {
					for (Map.Entry<String, Integer> entry : comparingNode.getUsers().entrySet()) {
						if (userSet.contains(entry.getKey())) {
							links.add(new Links(comparingNode.getName(), currentNode.getName(), entry.getKey(),
									entry.getValue()));
							// roll up sum of usermodifications
						}
					}
				}
			}
		}

		// Apply visualization group depending on space-size (defined as distinct
		// UserCount in space)
		for (Node node : nodeList) {
			int spaceSize = node.getUsers().size();
			node.setGroup(101);
			node.setUserCount(spaceSize);
			node.setUsers(null);
		}

		Collections.sort(links, Collections.reverseOrder());
		links = links.subList(0, links.size() >= 1500 ? 1500 : links.size());

		Iterator<Node> nodeItr = nodeList.iterator();
		while (nodeItr.hasNext()) {
			Node node = nodeItr.next();
			boolean removeNode = true;
			for (Links link : links) {
				if (link.getSourceName().equals(node.getName()) || link.getTargetName().equals(node.getName())) {
					removeNode = false;
				}

			}
			if (removeNode) {
				nodeItr.remove();
			}
		}

		return new NetworkModel(nodeList, links);
	}

	public NetworkModel querieSharedLocatedSpaces() throws SQLException {
		log.debug("Executing Querie: " + QRY_SPC_SHAREDLOCATION);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_SHAREDLOCATION));
		ResultSet rs = stmnt.executeQuery();
		List<SharedSpacesDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new SharedSpacesDTO(temporaryLocationMapper(rs.getString("location")),
					rs.getString("spacename"), rs.getInt("occ")));
		}
		// Count occurences of locations to filter them

		Set<String> uniqueLocationSet = new HashSet<>();
		resultList.forEach(item -> uniqueLocationSet.add(item.getLocation()));
		List<Node> nodes = new ArrayList<>();
		int nodeGroup = 0;
		for (String location : uniqueLocationSet) {
			nodes.add(new Node(location, nodeGroup));
			nodeGroup++;
		}
		List<Links> links = new ArrayList<>();
		for (SharedSpacesDTO currentSpace : resultList) {
			for (SharedSpacesDTO comparedSpace : resultList) {
				if (!comparedSpace.getLocation().equals(currentSpace.getLocation())) {
					if (comparedSpace.getSpacename().equals(currentSpace.getSpacename())) {
						links.add(new Links(comparedSpace.getLocation(), currentSpace.getLocation(),
								currentSpace.getSpacename(), 1));
					}
				}
			}

		}
		Iterator<Links> linkItr = links.iterator();
		for (Links link : links) {
			while (linkItr.hasNext()) {
				Links thisLink = linkItr.next();
				if (thisLink.getSourceName().equals(link.getSourceName())
						&& thisLink.getTargetName().equals(link.getTargetName())) {
					thisLink.setWeight(thisLink.getWeight() + 1);
				}
			}
		}
		Iterator<Node> nodeItr = nodes.iterator();
		while (nodeItr.hasNext()) {
			Node node = nodeItr.next();
			for (Links link : links) {
				if (node.getName().equalsIgnoreCase(link.getSourceName())) {
					node.setUserCount(node.getUserCount() + 1);
				}
			}
		}
		addMapDataToLocations(nodes);
		return new NetworkModel(nodes, links);
	}

	/**
	 * Adds long- and latitute to location
	 * 
	 * @param nodes
	 * @throws SQLException
	 */
	private void addMapDataToLocations(List<Node> nodes) throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_ALL_LOCATIONS));
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			for (Node node : nodes) {
				if (node.getName().equalsIgnoreCase(rs.getString("location"))) {
					node.setLatitude(rs.getString("latitude"));
					node.setLongitude(rs.getString("langitude"));
				}
			}
		}
	}

	/**
	 * Locations are already filtered by occurence > 5. This mapper maps the other
	 * most occuring locations which are wrongly written
	 * 
	 * @param location
	 * @return
	 */
	// TODO: implement fixed
	public static String temporaryLocationMapper(String location) {
		if (location.toLowerCase().contains("cologne")) {
			return "Köln";
		} else if (location.toLowerCase().contains("syracuse")) {
			return "Syracuse";
		} else if (location.toLowerCase().contains("balzers")) {
			return "Balzers";
		}
		return location;
	}

	/**
	 * Queries collaboration metrics as collaboration pages vs documentation pages
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<CollaborationMetricDTO> querieCollaborationIndication() throws SQLException {
		log.debug("Executing querie: " + QRY_SPC_COLLABVSDOCUMEN);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_COLLABVSDOCUMEN));
		ResultSet rs = stmnt.executeQuery();
		List<CollaborationIndicatorDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new CollaborationIndicatorDTO(rs.getString("title"), rs.getInt("authors"),
					rs.getInt("mainid"), rs.getString("date")));
		}
		int numberOfCollaboratedPages = 0;
		int numberOfDocumentation = 0;
		for (CollaborationIndicatorDTO current : resultList) {
			if (current.getAuthors() > 2) {
				numberOfCollaboratedPages++;
			} else {
				numberOfDocumentation++;
			}
		}
		List<CollaborationMetricDTO> metric = new ArrayList<>();
		metric.add(new CollaborationMetricDTO("collaboration", numberOfCollaboratedPages));
		metric.add(new CollaborationMetricDTO("documentation", numberOfDocumentation));
		return metric;
	}

}
