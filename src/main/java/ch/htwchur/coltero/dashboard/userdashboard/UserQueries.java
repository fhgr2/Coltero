package ch.htwchur.coltero.dashboard.userdashboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.space.network.Links;
import ch.htwchur.coltero.api.dto.space.network.NetworkModel;
import ch.htwchur.coltero.api.dto.space.network.Node;
import ch.htwchur.coltero.api.dto.tag.TagCloudDTO;
import ch.htwchur.coltero.api.dto.user.TagNeighboursDTO;
import ch.htwchur.coltero.api.dto.user.UserConnectionsDTO;
import ch.htwchur.coltero.api.dto.user.UserPerLocationDTO;
import ch.htwchur.coltero.api.dto.user.UserStatisticsDTO;
import ch.htwchur.coltero.dashboard.helper.GenericResolver;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * Class for User-Dashboard queries
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UserQueries {
	public static final Logger log = LoggerFactory.getLogger(UserQueries.class);
	private final static String QRY_USR_CONNECTIONS = "dataqueries/userboard/querie_userconnections.sql";
	private final static String QRY_COM_LOCATIONS = "dataqueries/userboard/querie_commtoreslocation.sql";
	private final static String QRY_CONTENT_RANGE = "dataqueries/userboard/querie_contentrange.sql";
	private final static String QRY_REACTIONS = "dataqueries/userboard/querie_reaction.sql";
	private final static String QRY_TAG_CLOUD = "dataqueries/userboard/querie_tagcloud.sql";
	private final static String QRY_TAG_NETWORK = "dataqueries/userboard/querie_tagnetwork.sql";
	private final static String QRY_TAG_ALL = "dataqueries/userboard/querie_tagnetwork_all.sql";
	private final static String QRY_USR_ALL = "dataqueries/userboard/querie_allusers.sql";
	private final static String INS_USR_CON = "dataqueries/userboard/insert_userstatistics.sql";
	private final static String QRY_USR_STAT = "dataqueries/userboard/querie_userstatistics.sql";
	private final static String QRY_USR_STAT_ALL = "dataqueries/userboard/querie_userstatistics_all.sql";

	private IDatabase database;
	private QuerieLoader querieLoader;
	private GenericResolver genericResolver;

	@Autowired
	public UserQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader,
			@ComponentImport GenericResolver genericResolver) {
		this.database = database;
		this.querieLoader = querieLoader;
		this.genericResolver = genericResolver;
	}

	/**
	 * Returns all available user-keys
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> querieAllAvailableUsers() throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_ALL);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_ALL));
		ResultSet rs = stmnt.executeQuery();
		List<String> allUserKeys = new ArrayList<>();
		while (rs.next()) {
			allUserKeys.add(rs.getString("user_key"));
		}
		return allUserKeys;
	}

	/**
	 * Aggregate userStatistics over all existing users
	 * 
	 * @throws SQLException
	 */
	public void aggregateUserStatisticsForAllUsers() throws SQLException {
		List<String> userKeys = querieAllAvailableUsers();
		for (String userKey : userKeys) {
			int userConnections = querieUserConnections(userKey).size();
			int contentRange = querieContentRange(userKey);
			int userReactions = querieUserReactions(userKey).size();
			log.debug("Executing Querie: " + INS_USR_CON);
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(INS_USR_CON));
			stmnt.setString(1, userKey);
			stmnt.setInt(2, userConnections);
			stmnt.setInt(3, contentRange);
			stmnt.setInt(4, userReactions);
			stmnt.execute();
		}
	}

	/**
	 * Querie preaggregated user statistics (averages) and actual values
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public UserStatisticsDTO querieUserStatistics(String userid) throws SQLException {
		log.debug("Executing Querie: " + "");
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_STAT_ALL));
		ResultSet rs = stmnt.executeQuery();
		List<UserStatisticsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			int writtenWords = rs.getInt("writtenwords");
			if (rs.wasNull()) {
				writtenWords = 0;
			}
			resultList.add(new UserStatisticsDTO(rs.getInt("userconnections"), rs.getInt("contentrange"),
					rs.getInt("reactions"), writtenWords)); // change 0 to writtenWords
		}
		List<Integer> userConnections = new ArrayList<>();
		List<Integer> contentRange = new ArrayList<>();
		List<Integer> reactions = new ArrayList<>();
		List<Integer> writtenWords = new ArrayList<>();
		for (UserStatisticsDTO userStat : resultList) {
			userConnections.add(userStat.getUserConnections());
			contentRange.add(userStat.getUserRange());
			reactions.add(userStat.getReactions());
			writtenWords.add(userStat.getWrittenWords());
		}

		stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_STAT));
		stmnt.setString(1, userid);
		rs = stmnt.executeQuery();
		int userCount = resultList.size();
		if (userCount == 0) {
			userCount = 1;
		}
		UserStatisticsDTO uStats = new UserStatisticsDTO();
		uStats.setReactionsAvg(calcMedian(reactions));
		uStats.setUserConnectionsAvg(calcMedian(userConnections));
		uStats.setUserRangeAvg(calcMedian(contentRange));
		uStats.setWrittenWordsAvg(calcMedian(writtenWords));
		uStats.setUserKey(userid);

		while (rs.next()) {
			uStats.setUserConnections(rs.getInt("userconnections"));
			uStats.setUserRange(rs.getInt("contentrange"));
			uStats.setReactions(rs.getInt("reactions"));
			int writtenWordsNullSafe = rs.getInt("writtenwords");
			if (rs.wasNull()) {
				writtenWordsNullSafe = 0;
			}
			uStats.setWrittenWords(writtenWordsNullSafe);

		}
		return uStats;
	}

	/**
	 * Calculates median
	 * 
	 * @param values
	 * @return
	 */
	private int calcMedian(List<Integer> values) {
		Collections.sort(values);
		if (values.size() % 2 != 0) {
			return values.get(values.size() / 2);
		}
		return values.get(values.size() / 2 - 1) + values.get(values.size() / 2) / 2;
	}

	/**
	 * Queries all users who are contributing on the same pages as the predefined
	 * user
	 * 
	 * @param userid
	 *            like lastmodifier type
	 * @return
	 */
	public List<String> querieUserConnections(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_USR_CONNECTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_CONNECTIONS));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<String> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(rs.getString("display_name"));
		}
		return resultList;
	}

	/**
	 * Queries Network-Diagram of user-connections
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public NetworkModel querieUserConnectionsNetwork(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_USR_CONNECTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_CONNECTIONS));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<UserConnectionsDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserConnectionsDTO(rs.getString("display_name"), rs.getString("lastmodifier"),
					rs.getInt("occ")));
		}
		Set<UserConnectionsDTO> uniqueUserSet = new HashSet<>();
		resultList.forEach(item -> uniqueUserSet.add(item));
		Set<Node> nodes = new HashSet<>();
		String currentUserName = genericResolver.resolveUserkeyToUsername(userid);
		// uniqueUserSet.forEach(item -> nodes.add(new Node(item.getDisplayName(), 0)));
		List<Links> links = new ArrayList<>();
		uniqueUserSet.forEach(
				item -> links.add(new Links(currentUserName, item.getDisplayName(), null, item.getOccurence())));
		nodes.add(new Node(currentUserName, 0));
		// apply layer 1
		uniqueUserSet.forEach(item -> nodes.add(new Node(item.getDisplayName(), 1)));

		// iterate throgh all unique userIds and connect them togehter
		Iterator<UserConnectionsDTO> itr = uniqueUserSet.iterator();
		while (itr.hasNext()) {
			List<UserConnectionsDTO> connectionList = new ArrayList<>();
			UserConnectionsDTO thisConnection = itr.next();
			log.debug("Exectuing Querie: " + QRY_USR_CONNECTIONS);
			stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_CONNECTIONS));
			for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
				stmnt.setString(i, thisConnection.getUserKey());
			}
			rs = stmnt.executeQuery();
			while (rs.next()) {
				connectionList.add(new UserConnectionsDTO(rs.getString("display_name"), rs.getString("lastmodifier"),
						rs.getInt("occ")));
			}
			connectionList.forEach(item -> links
					.add(new Links(thisConnection.getDisplayName(), item.getDisplayName(), null, item.getOccurence())));
			connectionList.forEach(item -> {
				item.setGroup(2);
				nodes.add(new Node(item.getDisplayName(), item.getGroup(), 0));
			});
		}
		List<Node> nodeList = new ArrayList<Node>(nodes);
		// Iterate throgh all links to determine node-graduant for applying size
		List<String> linkSources = new ArrayList<>();
		for (Links link : links) {
			linkSources.add(link.getSourceName());
		}
		for (Node node : nodeList) {
			int occurences = Collections.frequency(linkSources, node.getName());
			node.setUserCount(occurences);
		}
		return new NetworkModel(nodeList, links);

	}

	/**
	 * Queries Co-Commtores per location who are collaborating with predefined user
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public List<UserPerLocationDTO> querieCommtoresPerLocation(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_COM_LOCATIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_COM_LOCATIONS));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<UserPerLocationDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserPerLocationDTO(rs.getString("location"), rs.getInt("count")));
		}
		return resultList;
	}

	/**
	 * Querie contentrange of a predefined user
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public int querieContentRange(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_CONTENT_RANGE);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_CONTENT_RANGE));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		int coverage = 0;
		while (rs.next()) {
			coverage = rs.getInt("coverage");
		}
		return coverage;
	}

	/**
	 * Queries reactions from other users to content of predefined user
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public List<String> querieUserReactions(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_REACTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_REACTIONS));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<String> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(rs.getString("display_name"));
		}
		return resultList;
	}

	/**
	 * Queries tag cloud for predefined user
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public List<TagCloudDTO> querieTagCloud(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_TAG_CLOUD);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_CLOUD));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<TagCloudDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new TagCloudDTO(rs.getString("tag"), rs.getInt("weight")));
		}
		return resultList;
	}

	/**
	 * Queries Tag-Network
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public NetworkModel querieCoTagNetwork(String userid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_TAG_NETWORK);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_NETWORK));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setString(i, userid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<String> relevantUserTags = new ArrayList<>();
		while (rs.next()) {
			relevantUserTags.add(rs.getString("tag"));
		}
		if (relevantUserTags.isEmpty()) {
			return new NetworkModel();
		}
		String currentUserId = genericResolver.resolveUserkeyToUsername(userid);

		log.debug("Exectuing Querie: " + QRY_TAG_ALL);
		PreparedStatement allTagStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_ALL));
		for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
			allTagStmnt.setString(i, userid);
		}
		rs = allTagStmnt.executeQuery();

		List<TagNeighboursDTO> allUserTags = new ArrayList<>();
		while (rs.next()) {
			allUserTags.add(new TagNeighboursDTO(rs.getString("display_name"), rs.getString("tag")));
		}
		Set<String> uniqueUsers = new HashSet<>();
		allUserTags.forEach(item -> uniqueUsers.add(item.getUsername()));

		Iterator<String> itr = uniqueUsers.iterator();

		Map<String, List<String>> tagMap = new HashMap<>();
		while (itr.hasNext()) {
			List<String> tagCollection = new ArrayList<>();
			String currentUsername = itr.next();
			for (TagNeighboursDTO item : allUserTags) {
				if (item.getUsername().equals(currentUsername)) {
					tagCollection.add(item.getTag());
				}
			}
			tagMap.put(currentUsername, tagCollection);
		}

		tagMap.entrySet().stream()
				.filter(entry -> entry.getValue().stream().anyMatch(usertag -> relevantUserTags.contains(usertag)))
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		// be applied
		Iterator<Entry<String, List<String>>> tagMapItr = tagMap.entrySet().iterator();
		int entryCounter = 0;
		while (tagMapItr.hasNext()) {
			tagMapItr.next();
			if (entryCounter > 20) {
				tagMapItr.remove();
			}
			entryCounter++;
		}

		List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(currentUserId, 0, relevantUserTags));
		List<Links> links = new ArrayList<>();
		tagMap.forEach((usr, tags) -> nodes.add(new Node(usr, 1, tags)));

		tagMap.entrySet().forEach(item -> links.add(new Links(currentUserId, item.getKey(), null, 0)));

		// do not add this user to nextLayer userlist -> its already calculated
		List<String> userList = new ArrayList<>();
		for (Node node : nodes) {
			if (!node.getName().equals(currentUserId)) {
				userList.add(node.getName());
			}
		}
		NetworkModel secondLayer = getNextLayer(userList, new NetworkModel(nodes, links), 1);
		for (Node node : secondLayer.getNodes()) {
			if (node.getTags().size() >= 10) {
				node.setTags(node.getTags().subList(0, 10));
			}
		}
		return secondLayer;// getNextLayer(secondLayer.getContentList(), secondLayer, 2);

	}

	/**
	 * Queries next layer of networkModel
	 * 
	 * @param userList
	 *            list of users in next layer
	 * @param prevLayer
	 *            nodes and links from previous layer
	 * @param layer
	 *            number 0..n
	 * @return NetworkModel containing all links and nodes
	 * @throws SQLException
	 */
	private NetworkModel getNextLayer(List<String> userList, NetworkModel prevLayer, int layer) throws SQLException {
		List<String> layerUserList = new ArrayList<>();
		for (String user : userList) {
			log.debug("Exectuing Querie: " + QRY_TAG_NETWORK);
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_TAG_NETWORK));
			for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
				stmnt.setString(i, genericResolver.resolveUsernameToUserKey(user));
			}
			ResultSet rs = stmnt.executeQuery();
			List<String> relevantUserTags = new ArrayList<>();
			while (rs.next()) {
				relevantUserTags.add(rs.getString("tag"));
			}
			if (relevantUserTags.isEmpty()) {
				continue;
			}

			log.debug("Exectuing Querie: " + QRY_TAG_ALL);
			PreparedStatement allTagStmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_TAG_ALL));
			for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
				allTagStmnt.setString(i, genericResolver.resolveUsernameToUserKey(user));
			}
			rs = allTagStmnt.executeQuery();

			List<TagNeighboursDTO> allUserTags = new ArrayList<>();
			while (rs.next()) {
				allUserTags.add(new TagNeighboursDTO(rs.getString("display_name"), rs.getString("tag")));
			}
			Set<String> uniqueUsers = new HashSet<>();
			allUserTags.forEach(item -> uniqueUsers.add(item.getUsername()));

			Iterator<String> itr = uniqueUsers.iterator();

			Map<String, List<String>> tagMap = new HashMap<>();
			while (itr.hasNext()) {
				List<String> tagCollection = new ArrayList<>();
				String currentUsername = itr.next();
				for (TagNeighboursDTO item : allUserTags) {
					if (item.getUsername().equals(currentUsername)) {
						tagCollection.add(item.getTag());
					}
				}
				tagMap.put(currentUsername, tagCollection);
			}

			tagMap.entrySet().stream()
					.filter(entry -> entry.getValue().stream().anyMatch(usertag -> relevantUserTags.contains(usertag)))
					.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

			Iterator<Entry<String, List<String>>> tagMapItr = tagMap.entrySet().iterator();
			int entryCounter = 0;
			while (tagMapItr.hasNext()) {
				tagMapItr.next();
				if (entryCounter > 60) {
					tagMapItr.remove();
				}
				entryCounter++;
			}

			tagMap.keySet().forEach(item -> {
				if (!listContainsNode(prevLayer.getNodes(), item)) {
					prevLayer.getNodes().add(new Node(item, layer + 1, relevantUserTags));
				}
			});

			tagMap.entrySet().forEach(item -> {
				if (!listContainsLink(prevLayer.getLinks(), new Links(user, item.getKey(), null, layer))) {
					prevLayer.getLinks().add(new Links(user, item.getKey(), null, layer));
				}
			});
			if (!listContainsNode(prevLayer.getNodes(), user)) {
				prevLayer.getNodes().add(new Node(user, layer + 1));
				layerUserList.add(user);
			}
		}
		prevLayer.setContentList(layerUserList);
		return prevLayer;
	}

	/**
	 * returns true if list contains defined node (same name)
	 * 
	 * @param nodes
	 *            List of nodes
	 * @param element
	 *            node name
	 * @return
	 */
	private boolean listContainsNode(List<Node> nodes, String element) {
		return nodes.stream().anyMatch(o -> o.getName().equals(element));
	}

	/**
	 * returns true if if list contains link
	 * 
	 * @param links
	 *            list of links
	 * @param link
	 *            link object
	 * @return
	 */
	private boolean listContainsLink(List<Links> links, Links link) {
		return links.stream().anyMatch(o -> (o.getSourceName().equals(link.getSourceName())
				&& o.getTargetName().equals(link.getTargetName())));
	}
}
