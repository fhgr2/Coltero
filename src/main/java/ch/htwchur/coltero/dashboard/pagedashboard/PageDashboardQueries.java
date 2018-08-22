package ch.htwchur.coltero.dashboard.pagedashboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.common.ContentBodyDTO;
import ch.htwchur.coltero.api.dto.page.AuthorsCommentatorsCountDTO;
import ch.htwchur.coltero.api.dto.page.CommentCountDTO;
import ch.htwchur.coltero.api.dto.page.ReactionsDTO;
import ch.htwchur.coltero.api.dto.space.network.Links;
import ch.htwchur.coltero.api.dto.space.network.NetworkModel;
import ch.htwchur.coltero.api.dto.space.network.Node;
import ch.htwchur.coltero.api.dto.user.TagNeighboursDTO;
import ch.htwchur.coltero.api.dto.user.UserPerLocationDTO;
import ch.htwchur.coltero.dashboard.helper.GenericResolver;
import ch.htwchur.coltero.dashboard.logic.extraction.HtmlContentExtractor;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * Querie Class for Page Dashboard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class PageDashboardQueries {
	private static final Logger log = LoggerFactory.getLogger(PageDashboardQueries.class);

	private static final String QRY_COMMENT_COUNT = "dataqueries/pageboard/querie_commentcount.sql";
	private static final String QRY_COMAUTHORS_COUNT = "dataqueries/pageboard/querie_authorscommentators.sql";
	private static final String QRY_REACTIONS = "dataqueries/pageboard/querie_reactions.sql";
	private static final String QRY_PAGE_BODY = "dataqueries/pageboard/querie_pagebody.sql";
	private static final String QRY_USR_LOCATION = "dataqueries/pageboard/querie_userlocation.sql";
	private static final String QRY_TAG_ALL = "dataqueries/pageboard/querie_tagneighbours_all.sql";
	private static final String QRY_TAG_PAGE = "dataqueries/pageboard/querie_tagneighbours.sql";
	private static final int NO_DATA_AVAILABLE = 99;
	

	private IDatabase database;
	private QuerieLoader querieLoader;
	private HtmlContentExtractor htmlContentExtractor;
	private GenericResolver genericResolver;

	@Autowired
	public PageDashboardQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader,
			@ComponentImport HtmlContentExtractor htmlContentExtractor, @ComponentImport GenericResolver genericResolver) {
		this.database = database;
		this.querieLoader = querieLoader;
		this.htmlContentExtractor = htmlContentExtractor;
		this.genericResolver = genericResolver;
	}

	/**
	 * Queries comment count of page
	 * @param pageId
	 * @return
	 * @throws SQLException
	 */
	public CommentCountDTO querieCommentCountPage(int pageId) throws SQLException {
		log.debug("Executing Querie: " + QRY_COMMENT_COUNT);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_COMMENT_COUNT));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageId);
		}
		CommentCountDTO result = null;
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			result = new CommentCountDTO(rs.getInt("sum"));
		}
		return result;
	}

	/**
	 * Queries Authors and Commentators count of a page
	 * 
	 * @param pageId
	 * @return
	 * @throws SQLException
	 */
	public AuthorsCommentatorsCountDTO querieAuthorsCommentatorsPage(int pageId) throws SQLException {
		log.debug("Executing Querie: " + QRY_COMAUTHORS_COUNT);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_COMAUTHORS_COUNT));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageId);
		}
		AuthorsCommentatorsCountDTO result = null;
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			result = new AuthorsCommentatorsCountDTO(rs.getInt("authors"), rs.getInt("commentators"));
		}
		return result;
	}

	/**
	 * Queries how many different users reacting on a page (likes and comments)
	 * 
	 * @param pageId
	 * @return
	 * @throws SQLException
	 */
	public ReactionsDTO querieReactionsPage(int pageId) throws SQLException {
		log.debug("Executing Querie: " + QRY_REACTIONS);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_REACTIONS));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageId);
		}
		ReactionsDTO result = null;
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			result = new ReactionsDTO(rs.getInt("reactions"));
		}
		return result;
	}

	/**
	 * Queries and calculates word count of a predefined Page
	 * 
	 * @param pageId
	 * @return
	 * @throws SQLException
	 */
	public ContentBodyDTO querieWordCountPage(int pageId) throws SQLException {
		log.debug("Executing Querie: " + QRY_PAGE_BODY);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_PAGE_BODY));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageId);
		}
		ResultSet rs = stmnt.executeQuery();
		ContentBodyDTO result = new ContentBodyDTO("");
		while (rs.next()) {
			result = htmlContentExtractor.countPageWords(new ContentBodyDTO(rs.getString("body")));
		}
		return result;
	}

	/**
	 * Queries users per location (collaboration) of a predefined Page
	 * 
	 * @param pageId
	 * @return
	 * @throws SQLException
	 */
	public List<UserPerLocationDTO> querieUserPerLocationPage(int pageId) throws SQLException {
		log.debug("Executing Querie: " + QRY_USR_LOCATION);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_USR_LOCATION));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageId);
		}
		ResultSet rs = stmnt.executeQuery();
		List<UserPerLocationDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new UserPerLocationDTO(rs.getString("location"), rs.getInt("users")));
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
	public NetworkModel querieCoTagPageNetwork(int pageid) throws SQLException {
		log.debug("Exectuing Querie: " + QRY_TAG_PAGE);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_PAGE));
		for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
			stmnt.setInt(i, pageid);
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
		String pageTitle = genericResolver.resolvePageidToPageTitle(pageid);;

		log.debug("Exectuing Querie: " + QRY_TAG_ALL);
		PreparedStatement allTagStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_ALL));
		for (int i = 1; i <= allTagStmnt.getParameterMetaData().getParameterCount(); i++) {
			allTagStmnt.setInt(i, pageid);
		}
		rs = allTagStmnt.executeQuery();

		List<TagNeighboursDTO> allPageTags = new ArrayList<>();
		while (rs.next()) {
			allPageTags.add(new TagNeighboursDTO(null, rs.getInt("mainid"), rs.getString("tag")));
		}
		Set<Integer> uniquePages = new HashSet<>();
		allPageTags.forEach(item -> uniquePages.add(item.getPageid()));

		Iterator<Integer> itr = uniquePages.iterator();

		Map<Integer, List<String>> tagMap = new HashMap<>();
		while (itr.hasNext()) {
			List<String> tagCollection = new ArrayList<>();
			int currentPage = itr.next();
			for (TagNeighboursDTO item : allPageTags) {
				if (item.getPageid() == currentPage) {
					tagCollection.add(item.getTag());
				}
			}
			tagMap.put(currentPage, tagCollection);
		}

		// remove users who havent go the same tags -- change here if other logic should
		// be applied
		Iterator<Entry<Integer, List<String>>> tagMapItr = tagMap.entrySet().iterator();
		while (tagMapItr.hasNext()) {
			if (!tagMapItr.next().getValue().containsAll(relevantPageTags)) {
				tagMapItr.remove();
			}
		}
		List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(pageTitle, 0, relevantPageTags));
		List<Links> links = new ArrayList<>();
		tagMap.forEach((id, tags) -> nodes.add(new Node(genericResolver.resolvePageidToPageTitle(id), 1, tags)));

		tagMap.entrySet().forEach(item -> links.add(new Links(pageTitle, genericResolver.resolvePageidToPageTitle(item.getKey()), null, 0)));

		// do not add this user to nextLayer userlist -> its already calculated
		List<String> pageList = new ArrayList<>();
		for (Node node : nodes) {
			if (!node.getName().equals(pageTitle)) {
				pageList.add(node.getName());
			}
		}
		NetworkModel secondLayer = getNextLayer(pageList, new NetworkModel(nodes, links), 1);
		for (Node node : secondLayer.getNodes()) {
			if (node.getTags().size() >= 3) {
				node.setTags(node.getTags().subList(0, 3));
			}
		}
		return getNextLayer(secondLayer.getContentList(), secondLayer, 2);

	}

	/**
	 * Queries next layer of networkModel
	 * 
	 * @param contentList
	 *            list of users in next layer
	 * @param prevLayer
	 *            nodes and links from previous layer
	 * @param layer
	 *            number 0..n
	 * @return NetworkModel containing all links and nodes
	 * @throws SQLException
	 */
	private NetworkModel getNextLayer(List<String> contentList, NetworkModel prevLayer, int layer) throws SQLException {
		List<String> layerPageList = new ArrayList<>();
		for (String pageTitle : contentList) {
			log.debug("Exectuing Querie: " + QRY_TAG_PAGE);
			PreparedStatement stmnt = database.getDatabaseConnection()
					.prepareStatement(querieLoader.loadQuerie(QRY_TAG_PAGE));
			for (int i = 1; i <= stmnt.getParameterMetaData().getParameterCount(); i++) {
				stmnt.setInt(i, genericResolver.resolveTitleToPageId(pageTitle));
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
				allTagStmnt.setInt(i, genericResolver.resolveTitleToPageId(pageTitle));
			}
			rs = allTagStmnt.executeQuery();

			List<TagNeighboursDTO> allUserTags = new ArrayList<>();
			while (rs.next()) {
				allUserTags.add(new TagNeighboursDTO(null, rs.getInt("mainid"), rs.getString("tag")));
			}
			Set<Integer> uniquePages = new HashSet<>();
			allUserTags.forEach(item -> uniquePages.add(item.getPageid()));

			Iterator<Integer> itr = uniquePages.iterator();

			Map<Integer, List<String>> tagMap = new HashMap<>();
			while (itr.hasNext()) {
				List<String> tagCollection = new ArrayList<>();
				int currentPage = itr.next();
				for (TagNeighboursDTO item : allUserTags) {
					if (item.getPageid() == currentPage) {
						tagCollection.add(item.getTag());
					}
				}
				tagMap.put(currentPage, tagCollection);
			}

			// remove users who havent go the same tags
			Iterator<Entry<Integer, List<String>>> tagMapItr = tagMap.entrySet().iterator();
			while (tagMapItr.hasNext()) {
				if (!tagMapItr.next().getValue().containsAll(relevantPageTags)) {
					tagMapItr.remove();
				}
			}

			tagMap.keySet().forEach(item -> {
				if (!listContainsNode(prevLayer.getNodes(), genericResolver.resolvePageidToPageTitle(item))) {
					prevLayer.getNodes().add(new Node(genericResolver.resolvePageidToPageTitle(item), layer + 1, relevantPageTags));
				}
			});

			tagMap.entrySet().forEach(item -> {
				String title = genericResolver.resolvePageidToPageTitle(item.getKey());
				if (!listContainsLink(prevLayer.getLinks(), new Links(pageTitle, title, null, layer))) {
					prevLayer.getLinks().add(new Links(pageTitle, title, null, layer));
				}
			});
			if (!listContainsNode(prevLayer.getNodes(), pageTitle)) {
				prevLayer.getNodes().add(new Node(pageTitle, layer + 1));
				layerPageList.add(pageTitle);
			}
		}
		prevLayer.setContentList(layerPageList);
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
