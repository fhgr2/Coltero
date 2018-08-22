package ch.htwchur.coltero.dashboard.spacedashboard;

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
import java.util.stream.Collectors;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.boxplot.BoxPlotDTO;
import ch.htwchur.coltero.api.dto.space.SumDTO;
import ch.htwchur.coltero.api.dto.space.network.Links;
import ch.htwchur.coltero.api.dto.space.network.NetworkModel;
import ch.htwchur.coltero.api.dto.space.network.Node;
import ch.htwchur.coltero.api.dto.space.network.SpaceUser;
import ch.htwchur.coltero.api.dto.tag.TagCloudDTO;
import ch.htwchur.coltero.api.dto.user.TagNeighboursDTO;
import ch.htwchur.coltero.api.dto.user.UserPerLocationDTO;
import ch.htwchur.coltero.dashboard.helper.DateHelper;
import ch.htwchur.coltero.dashboard.helper.GenericResolver;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * Querie Class for Space Queries from Space-Dashboard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */

public class SpaceQueries {
	public static final Logger log = LoggerFactory.getLogger(SpaceQueries.class);
	public static final String QRY_AUTHORS_PAGES = "dataqueries/spaceboard/querie_authorscountpage.sql";
	public static final String QRY_AUTHORS_COMMENTS = "dataqueries/spaceboard/querie_authorscountcomments.sql";
	public static final String QRY_COUNT_COMMENTS = "dataqueries/spaceboard/querie_commentcount.sql";
	public static final String QRY_COUNT_TAGS = "dataqueries/spaceboard/querie_tagcount.sql";
	public static final String QRY_COUNT_LIKES = "dataqueries/spaceboard/querie_likecount.sql";
	public static final String QRY_COUNT_ATTACHMENTS = "dataqueries/spaceboard/querie_attachmentcount.sql";
	private static final String QRY_COUNT_NOTHING = "dataqueries/spaceboard/querie_nothingcount.sql";
	private static final String QRY_SUM_PGNCOM = "dataqueries/spaceboard/querie_commentandpagecount.sql";
	private static final String QRY_USR_LOCATION = "dataqueries/spaceboard/querie_userlocation.sql";
	private static final String QRY_TAG_CLOUD = "dataqueries/spaceboard/querie_tagcloud.sql";
	private static final String QRY_TAG_ALL = "dataqueries/spaceboard/querie_spacetags_all.sql";
	private static final String QRY_TAG_SPC = "dataqueries/spaceboard/querie_spacetags.sql";
	private static final String QRY_CO_NETWORK = "dataqueries/spaceboard/querie_cospace_network.sql";
	private static final String QRY_CO_NETWORK_ALL = "dataqueries/spaceboard/querie_cospace_network_all.sql";

	public static final int NO_DATA_AVAILABLE = 99;

	private QuerieLoader querieLoader;
	private IDatabase database;
	private GenericResolver genericResolver;

	@Autowired
	public SpaceQueries(@ComponentImport QuerieLoader querieLoader, @ComponentImport IDatabase database,
			@ComponentImport GenericResolver genericResolver) {
		this.querieLoader = querieLoader;
		this.database = database;
		this.genericResolver = genericResolver;
	}

	/**
	 * Executes Boxplot queries
	 * 
	 * @param querie
	 * @param spaceId
	 * @return
	 * @throws SQLException
	 */
	public List<BoxPlotDTO> executeBoxPlotQuerie(String querie, int spaceId) throws SQLException {
		log.debug("Executing Querie: " + querie);
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(querie));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, spaceId);
		}
		ResultSet rs = stmnt.executeQuery();
		List<BoxPlotDTO> resultList = new ArrayList<>();
		int sum = 0;
		while (rs.next()) {
			sum += rs.getInt("value");
			resultList.add(new BoxPlotDTO(rs.getString("title"), sum, DateHelper.df.format(rs.getDate("date"))));
		}
		return resultList;
	}

	/**
	 * Queries different pages in space where no comments, likes, tags, attachments
	 * are attached
	 * 
	 * @param spaceId
	 *            Space for lookup
	 * @return
	 * @throws SQLException
	 */
	public List<BoxPlotDTO> querieNothingCountPerPageInSpace(int spaceId) throws SQLException {
		log.debug("Excuting Querie: " + QRY_COUNT_NOTHING);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_COUNT_NOTHING));
		stmnt.setInt(1, spaceId);
		stmnt.setInt(2, spaceId);
		stmnt.setInt(3, spaceId);
		stmnt.setInt(4, spaceId);
		stmnt.setInt(5, spaceId);
		ResultSet rs = stmnt.executeQuery();
		List<BoxPlotDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new BoxPlotDTO(rs.getString("title"), 1, DateHelper.df.format(rs.getDate("date")) + "-01"));
		}
		return resultList;
	}

	/**
	 * Queries Page and Comment Sum in a predefined space
	 * 
	 * @param spaceId
	 *            Space for lookup
	 * @return
	 * @throws SQLException
	 */
	public SumDTO querieSumPagesAndCommentsInSpace(int spaceId) throws SQLException {
		log.debug("Excuting Querie: " + QRY_SUM_PGNCOM);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SUM_PGNCOM));
		stmnt.setInt(1, spaceId);
		stmnt.setInt(2, spaceId);
		ResultSet rs = stmnt.executeQuery();
		rs.next();
		return new SumDTO(rs.getInt("pages"), rs.getInt("comments"));
	}

	/**
	 * Queries Users per Location in predefined space
	 * 
	 * @param spaceId
	 *            Space for lookup
	 * @return
	 * @throws SQLException
	 */
	public List<UserPerLocationDTO> querieUserPerLocationInSpace(int spaceId) throws SQLException {
		log.debug("Excuting Querie: " + QRY_USR_LOCATION);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_LOCATION));
		stmnt.setInt(1, spaceId);
		ResultSet rs = stmnt.executeQuery();
		List<UserPerLocationDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserPerLocationDTO(rs.getString("location"), rs.getInt("users")));
		}
		return resultList;
	}

	/**
	 * Queries Tag-Cloud in predefined space
	 * 
	 * @param spaceId
	 *            Space for lookup
	 * @return
	 * @throws SQLException
	 */
	public List<TagCloudDTO> querieTagCloudInSpace(int spaceId) throws SQLException {
		log.debug("Excuting Querie: " + QRY_TAG_CLOUD);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_CLOUD));
		stmnt.setInt(1, spaceId);
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
	public NetworkModel querieTagNeighboursFromSpace(int spaceid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_TAG_SPC);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_SPC));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, spaceid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<String> relevantPageTags = new ArrayList<>();
		while (rs.next()) {
			relevantPageTags.add(rs.getString("tag"));
		}
		if (relevantPageTags.isEmpty()) {
			List<Node> noNodesToDisplay = new ArrayList<>();
			Node noData = new Node("No data available", 0);
			noData.setUserCount(NO_DATA_AVAILABLE);
			noData.setGroup(NO_DATA_AVAILABLE); //
			noNodesToDisplay.add(noData);
			return new NetworkModel(noNodesToDisplay, new ArrayList<Links>());
		}
		String spaceName = genericResolver.resolveSpaceIdToSpaceName(spaceid);
		;

		log.debug("Exectuing Querie: " + QRY_TAG_ALL);
		PreparedStatement allTagStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_ALL));
		for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
			allTagStmnt.setInt(i, spaceid);
		}
		rs = allTagStmnt.executeQuery();

		List<TagNeighboursDTO> allPageTags = new ArrayList<>();
		while (rs.next()) {
			allPageTags.add(new TagNeighboursDTO(null, rs.getInt("spaceid"), rs.getString("tag")));
		}
		Set<Integer> uniquePages = new HashSet<>();
		allPageTags.forEach(item -> uniquePages.add(item.getPageid()));

		Iterator<Integer> itr = uniquePages.iterator();

		Map<Integer, List<String>> tagMap = new HashMap<>();
		while (itr.hasNext()) {
			List<String> tagCollection = new ArrayList<>();
			int currentSpace = itr.next();
			for (TagNeighboursDTO item : allPageTags) {
				if (item.getPageid() == currentSpace) {
					tagCollection.add(item.getTag());
				}
			}
			tagMap.put(currentSpace, tagCollection);
		}

		// remove users who havent go the same tags -- change here if other logic should
		// be applied

		tagMap.entrySet().stream()
				.filter(entry -> entry.getValue().stream().anyMatch(usertag -> relevantPageTags.contains(usertag)))
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		// be applied
		Iterator<Entry<Integer, List<String>>> tagMapItr = tagMap.entrySet().iterator();
		int entryCounter = 0;
		while (tagMapItr.hasNext()) {
			tagMapItr.next();
			if (entryCounter > 30) {
				tagMapItr.remove();
			}
			entryCounter++;
		}

		List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(spaceName, 0, relevantPageTags));
		List<Links> links = new ArrayList<>();
		tagMap.forEach((id, tags) -> nodes.add(new Node(genericResolver.resolveSpaceIdToSpaceName(id), 1, tags)));

		tagMap.entrySet().forEach(item -> links
				.add(new Links(spaceName, genericResolver.resolveSpaceIdToSpaceName(item.getKey()), null, 0)));

		// do not add this user to nextLayer userlist -> its already calculated
		List<String> pageList = new ArrayList<>();
		for (Node node : nodes) {
			if (!node.getName().equals(spaceName)) {
				pageList.add(node.getName());
			}
		}
		NetworkModel secondLayer = getNextLayer(pageList, new NetworkModel(nodes, links), 1);
		for (Node node : secondLayer.getNodes()) {
			if (node.getTags().size() >= 10) {
				node.setTags(node.getTags().subList(0, 10));
			}
		}
		return secondLayer;

	}

	/**
	 * Queries next layer of networkModel
	 * 
	 * @param userList
	 *            list of spaces in next layer
	 * @param prevLayer
	 *            nodes and links from previous layer
	 * @param layer
	 *            number 0..n
	 * @return NetworkModel containing all links and nodes
	 * @throws SQLException
	 */
	private NetworkModel getNextLayer(List<String> contentList, NetworkModel prevLayer, int layer) throws SQLException {
		List<String> layerPageList = new ArrayList<>();
		for (String spaceName : contentList) {
			log.debug("Exectuing Querie: " + QRY_TAG_SPC);
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_TAG_SPC));
			for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
				stmnt.setInt(i, genericResolver.resolveSpaceNameToSpaceId(spaceName));
			}
			ResultSet rs = stmnt.executeQuery();
			List<String> relevantPageTags = new ArrayList<>();
			while (rs.next()) {
				relevantPageTags.add(rs.getString("tag"));
			}
			if (relevantPageTags.isEmpty()) {
				continue;
			}

			log.debug("Exectuing Querie: " + QRY_TAG_ALL);
			PreparedStatement allTagStmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_TAG_ALL));
			for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
				allTagStmnt.setInt(i, genericResolver.resolveSpaceNameToSpaceId(spaceName));
			}
			rs = allTagStmnt.executeQuery();

			List<TagNeighboursDTO> allUserTags = new ArrayList<>();
			while (rs.next()) {
				allUserTags.add(new TagNeighboursDTO(null, rs.getInt("spaceid"), rs.getString("tag")));
			}
			Set<Integer> uniquePages = new HashSet<>();
			allUserTags.forEach(item -> uniquePages.add(item.getPageid()));

			Iterator<Integer> itr = uniquePages.iterator();

			Map<Integer, List<String>> tagMap = new HashMap<>();
			while (itr.hasNext()) {
				List<String> tagCollection = new ArrayList<>();
				int currentSpace = itr.next();
				for (TagNeighboursDTO item : allUserTags) {
					if (item.getPageid() == currentSpace) {
						tagCollection.add(item.getTag());
					}
				}
				tagMap.put(currentSpace, tagCollection);
			}

			// remove users who havent go the same tags
			tagMap.entrySet().stream()
					.filter(entry -> entry.getValue().stream().anyMatch(usertag -> relevantPageTags.contains(usertag)))
					.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

			// be applied
			Iterator<Entry<Integer, List<String>>> tagMapItr = tagMap.entrySet().iterator();
			int entryCounter = 0;
			while (tagMapItr.hasNext()) {
				tagMapItr.next();
				if (entryCounter > 60) {
					tagMapItr.remove();
				}
				entryCounter++;
			}

			tagMap.keySet().forEach(item -> {
				if (!listContainsNode(prevLayer.getNodes(), genericResolver.resolveSpaceIdToSpaceName(item))) {
					prevLayer.getNodes().add(
							new Node(genericResolver.resolveSpaceIdToSpaceName(item), layer + 1, relevantPageTags));
				}
			});

			tagMap.entrySet().forEach(item -> {
				String title = genericResolver.resolveSpaceIdToSpaceName(item.getKey());
				if (!listContainsLink(prevLayer.getLinks(), new Links(spaceName, title, null, layer))) {
					prevLayer.getLinks().add(new Links(spaceName, title, null, layer));
				}
			});
			if (!listContainsNode(prevLayer.getNodes(), spaceName)) {
				prevLayer.getNodes().add(new Node(spaceName, layer + 1));
				layerPageList.add(spaceName);
			}
		}
		prevLayer.setContentList(layerPageList);
		return prevLayer;
	}

	/**
	 * Queries Co-Space Network based on user who are contributing in a space ->
	 * link
	 * 
	 * @param spaceid
	 * @return
	 * @throws SQLException
	 */
	public NetworkModel querieCoSpaceNetworkNew(int spaceid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_CO_NETWORK);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_CO_NETWORK));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, spaceid);
		}
		ResultSet rs = stmnt.executeQuery();
		List<String> relevantUsers = new ArrayList<>();
		while (rs.next()) {
			relevantUsers.add(rs.getString("display_name"));
		}
		if (relevantUsers.isEmpty()) {
			List<Node> noNodesToDisplay = new ArrayList<>();
			Node noData = new Node("No data available", 0);
			noData.setUserCount(NO_DATA_AVAILABLE);
			noData.setGroup(NO_DATA_AVAILABLE); //
			noNodesToDisplay.add(noData);
			return new NetworkModel(noNodesToDisplay, new ArrayList<Links>());
		}
		String spaceName = genericResolver.resolveSpaceIdToSpaceName(spaceid);

		log.debug("Exectuing Querie: " + QRY_CO_NETWORK_ALL);
		PreparedStatement allTagStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_CO_NETWORK_ALL));
		for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
			allTagStmnt.setInt(i, spaceid);
		}
		rs = allTagStmnt.executeQuery();

		List<SpaceUser> allSpaceUsers = new ArrayList<>();
		while (rs.next()) {
			allSpaceUsers.add(new SpaceUser(rs.getString("spacename"), rs.getString("display_name"), rs.getInt("occ")));
		}
		Set<String> uniquePages = new HashSet<>();
		allSpaceUsers.forEach(item -> uniquePages.add(item.getSpacename()));

		Iterator<String> itr = uniquePages.iterator();

		Map<String, List<String>> userMap = new HashMap<>();
		while (itr.hasNext()) {
			List<String> userCollection = new ArrayList<>();
			String currentSpace = itr.next();
			for (SpaceUser item : allSpaceUsers) {
				if (item.getSpacename().equals(currentSpace)) {
					userCollection.add(item.getUserName());
				}
			}
			userMap.put(currentSpace, userCollection);
		}

		// remove users who havent go the same tags -- change here if other logic should
		// be applied
		Iterator<Entry<String, List<String>>> userMapItr = userMap.entrySet().iterator();
		while (userMapItr.hasNext()) {
			// change here to > 10
			List<String> thisUsers = userMapItr.next().getValue();
			int userCount = 0;
			for (String thisUser : thisUsers) {
				if (relevantUsers.contains(thisUser)) {
					userCount++;
				}
			}
			if (userCount < 3) {
				userMapItr.remove();
			}
		}

		List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(spaceName, 0, relevantUsers));
		List<Links> links = new ArrayList<>();
		userMap.forEach((id, users) -> nodes.add(new Node(id, 1, users)));

		userMap.entrySet().forEach(item -> links.add(new Links(spaceName, item.getKey(), null, 0)));

		// do not add this user to nextLayer userlist -> its already calculated
		List<String> nodeList = new ArrayList<>();
		for (Node node : nodes) {
			if (!node.getName().equals(spaceName)) {
				nodeList.add(node.getName());
			}
		}
		NetworkModel secondLayer = getNextCoSpaceLayer(nodeList, new NetworkModel(nodes, links), 1);
		List<String> linkSources = new ArrayList<>();
		for (Links link : secondLayer.getLinks()) {
			linkSources.add(link.getSourceName());
		}
		for (Node node : secondLayer.getNodes()) {
			int occurences = Collections.frequency(linkSources, node.getName());
			node.setUserCount(occurences);
			node.setTags(null);
		}
		return secondLayer;

	}

	/**
	 * Queries next layer for Co-Space
	 * 
	 * @param contentList
	 * @param prevLayer
	 * @param layer
	 * @return
	 * @throws SQLException
	 */
	private NetworkModel getNextCoSpaceLayer(List<String> contentList, NetworkModel prevLayer, int layer)
			throws SQLException {
		List<String> coSpaceLayer = new ArrayList<>();
		for (String spaceName : contentList) {
			log.debug("Exectuing Querie: " + QRY_CO_NETWORK);
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_CO_NETWORK));
			for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
				stmnt.setInt(i, genericResolver.resolveSpaceNameToSpaceId(spaceName));
			}
			ResultSet rs = stmnt.executeQuery();
			List<String> relevantSpaceUsers = new ArrayList<>();
			while (rs.next()) {
				relevantSpaceUsers.add(rs.getString("display_name"));
			}
			if (relevantSpaceUsers.isEmpty()) {
				continue;
			}

			log.debug("Exectuing Querie: " + QRY_CO_NETWORK_ALL);
			PreparedStatement allTagStmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_CO_NETWORK_ALL));
			for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
				allTagStmnt.setInt(i, genericResolver.resolveSpaceNameToSpaceId(spaceName));
			}
			rs = allTagStmnt.executeQuery();

			List<SpaceUser> allSpaceUsers = new ArrayList<>();
			while (rs.next()) {
				allSpaceUsers
						.add(new SpaceUser(rs.getString("spacename"), rs.getString("display_name"), rs.getInt("occ")));
			}

			Set<String> uniquePages = new HashSet<>();
			allSpaceUsers.forEach(item -> uniquePages.add(item.getSpacename()));

			Iterator<String> itr = uniquePages.iterator();

			Map<String, List<String>> userMap = new HashMap<>();
			while (itr.hasNext()) {
				List<String> userCollection = new ArrayList<>();
				String currentSpace = itr.next();
				for (SpaceUser item : allSpaceUsers) {
					if (item.getSpacename().equals(currentSpace)) {
						userCollection.add(item.getUserName());
					}
				}
				userMap.put(currentSpace, userCollection);
			}

			// remove users who havent got the same tags
			Iterator<Entry<String, List<String>>> userMapItr = userMap.entrySet().iterator();
			while (userMapItr.hasNext()) {
				List<String> thisUsers = userMapItr.next().getValue();
				boolean remove = true;
				for (String thisUser : thisUsers) {
					if (relevantSpaceUsers.contains(thisUser)) {
						remove = false;
					}
				}
				if (remove) {
					userMapItr.remove();
				}
			}

			userMap.keySet().forEach(item -> {
				if (!listContainsNode(prevLayer.getNodes(), item)) {
					prevLayer.getNodes().add(new Node(item, layer + 1, relevantSpaceUsers));
				}
			});

			userMap.entrySet().forEach(item -> {
				String linkedSpace = item.getKey();
				if (!listContainsLink(prevLayer.getLinks(), new Links(spaceName, linkedSpace, null, layer))) {
					prevLayer.getLinks().add(new Links(spaceName, linkedSpace, null, layer));
				}
			});
			if (!listContainsNode(prevLayer.getNodes(), spaceName)) {
				prevLayer.getNodes().add(new Node(spaceName, layer + 1));
				coSpaceLayer.add(spaceName);
			}
		}
		prevLayer.setContentList(coSpaceLayer);
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
