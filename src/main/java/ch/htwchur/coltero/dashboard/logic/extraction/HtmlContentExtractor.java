package ch.htwchur.coltero.dashboard.logic.extraction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.common.ContentBodyDTO;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * This class extracts and processes body content of pages while it removes HTML
 * tags
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class HtmlContentExtractor {

	private static final Logger log = LoggerFactory.getLogger(HtmlContentExtractor.class);
	private static IDatabase database;
	private QuerieLoader querieLoader;

	private static final String AGG_SPC_WORDCOUNT = "dataqueries/space/insert_spacewordcount.sql";
	private static final String QRY_SPC_WORDCOUNT = "dataqueries/space/querie_spacewordcount.sql";
	private static final String QRY_SPC_BODYCONTENT = "dataqueries/top10/querie_spaces_pagecontent.sql";
	private static final String QRY_SPC_COMMENTBODYCONTENT = "dataqueries/top10/querie_spaces_commentcontent.sql";
	private static final String AGG_SPC_COMMENTCOUNT = "dataqueries/space/insert_spacecommentwordcount.sql";
	private static final String QRY_SPC_COMMENTCOUNT = "dataqueries/space/querie_spacecommentwordcount.sql";
	private static final String QRY_USR_CONTENT_ALL = "dataqueries/userboard/querie_user_content.sql";
	private static final String INS_USR_WORDCOUNT = "dataqueries/userboard/insert_user_wordcount.sql";

	private static final String REG_ACTAGS = "<ac:.*>|<\\/ac:.*>";

	@Autowired
	public HtmlContentExtractor(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		HtmlContentExtractor.database = database;
		this.querieLoader = querieLoader;
	}

	/**
	 * Read space data (comments) from database and process word calculation and
	 * persist to db
	 * 
	 * @throws SQLException
	 */
	public void aggregateSpaceCommentWordCount() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_COMMENTBODYCONTENT));
		log.debug("Executing querie: " + QRY_SPC_COMMENTBODYCONTENT);
		ResultSet rs = stmnt.executeQuery();
		List<ContentBodyDTO> resultList = new ArrayList<ContentBodyDTO>();
		while (rs.next()) {
			resultList.add(new ContentBodyDTO(rs.getString(1), rs.getString(2)));
		}
		aggregateResultToDatabase(countWords(stripHTMLTags(resultList)), AGG_SPC_COMMENTCOUNT);

	}
	
	/**
	 * Read space data (pages)from database and process word calculation and persist
	 * to db
	 * 
	 * @throws SQLException
	 */
	public void aggregateSpaceWordCount() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_BODYCONTENT));
		log.debug("Executing querie: " + QRY_SPC_BODYCONTENT);
		ResultSet rs = stmnt.executeQuery();
		List<ContentBodyDTO> resultList = new ArrayList<ContentBodyDTO>();
		while (rs.next()) {
			resultList.add(new ContentBodyDTO(rs.getString(1), striptHTMLTags(rs.getString(2))));
		}
		aggregateResultToDatabase(countWords(resultList), AGG_SPC_WORDCOUNT);
	}
	
	/**
	 * Aggregate userWordCount to database
	 * @throws SQLException
	 */
	public void aggregateUserWordCount() throws SQLException{
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_USR_CONTENT_ALL));
		log.debug("Executing querie: " + QRY_USR_CONTENT_ALL);
		ResultSet rs = stmnt.executeQuery();
		List<ContentBodyDTO> resultList = new ArrayList<>();
		while(rs.next()) {
			resultList.add(new ContentBodyDTO(rs.getString("user_key"), striptHTMLTags(rs.getString("body"))));
		}
		aggregateWordCountToDatabase(countWords(resultList));
	}

	/**
	 * Strips all Html Tags from body content List
	 * 
	 * @param spaceContentList
	 * @return
	 */
	private List<ContentBodyDTO> stripHTMLTags(List<ContentBodyDTO> spaceContentList) {
		List<ContentBodyDTO> cleanedContentBody = new ArrayList<>();
		for (ContentBodyDTO content : spaceContentList) {
			String regexedContent = cleanStringWithRegex(content.getBodyContent());
			Document doc = Jsoup.parse(regexedContent);
			content.setBodyContent(doc.text());
			cleanedContentBody.add(content);
			
		}
		return cleanedContentBody;
	}
	
	private String striptHTMLTags(String content) {
		content = cleanStringWithRegex(content);
		Document doc = Jsoup.parse(content);
		return doc.text();
	}
	
	/**
	 * Strips all Html Tags from body content and count all words
	 * 
	 * @param bodyContent
	 * @return
	 */
	public ContentBodyDTO countPageWords(ContentBodyDTO bodyContent) {
		String regexedContent = cleanStringWithRegex(bodyContent.getBodyContent());
		Document doc = Jsoup.parse(regexedContent);
		bodyContent.setBodyContent(doc.text());
		bodyContent.setWordCount(bodyContent.getBodyContent().split(" ").length + 1);
		return bodyContent;
	}

	/**
	 * Remove confluence specific <ac> tags and reduce spaces between words
	 * 
	 * @param content
	 * @return
	 */
	private String cleanStringWithRegex(String content) {
		content = content.replaceAll(REG_ACTAGS, " ");
		return content.trim().replaceAll(" +", " ");
	}
	
	/**
	 * Count words in Bodycontent and merge all corresponding pages (corresponding
	 * by same SpaceName) together (summarize word count per spaceName)
	 * 
	 * @param spaceContentList
	 * @return
	 */
	private List<ContentBodyDTO> countWords(List<ContentBodyDTO> spaceContentList) {
		Iterator<ContentBodyDTO> itr = spaceContentList.iterator();
		while (itr.hasNext()) {
			ContentBodyDTO element = itr.next();
			// remove all elements with lenght == 1 -> due to body is empty...
			if (element.getBodyContent().split(" ").length == 1) {
				itr.remove();
				continue;
			}
			// +1 while array.lenght starts at 0
			element.setWordCount(element.getBodyContent().split(" ").length + 1);
		}
		// Merge equal spaceNames and corresponding word-count together
		Map<String, ContentBodyDTO> reducedContent = new HashMap<>();
		for (ContentBodyDTO element : spaceContentList) {
			if (reducedContent.containsKey(element.getSpaceName())) {
				ContentBodyDTO space = reducedContent.get(element.getSpaceName());
				space.setWordCount(space.getWordCount() + element.getWordCount());
				space.setBodyContent(null);
			} else {
				reducedContent.put(element.getSpaceName(), element);
				element.setBodyContent(null);
			}
		}
		return new ArrayList<ContentBodyDTO>(reducedContent.values());
	}

	/**
	 * Inserts pre-aggregated values for further processing (over time)
	 * 
	 * @param spaceContentList
	 * @throws SQLException
	 */
	private void aggregateResultToDatabase(List<ContentBodyDTO> spaceContentList, String querie) throws SQLException {
		String qri = querieLoader.loadQuerie(querie);
		log.debug("Executing querie: " + qri);
		for (ContentBodyDTO content : spaceContentList) {
			PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(qri);
			stmnt.setString(1, content.getSpaceName());
			stmnt.setInt(2, content.getWordCount());
			stmnt.execute();
		}
		database.closeConnection();
	}
	
	/**
	 * Inserts userWordCount to database
	 * @param userContentList
	 * @throws SQLException
	 */
	private void aggregateWordCountToDatabase(List<ContentBodyDTO> userContentList) throws SQLException {
		log.debug("Executing querie: " + INS_USR_WORDCOUNT);
		for(ContentBodyDTO content : userContentList) {
			PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(INS_USR_WORDCOUNT));
			stmnt.setInt(1, content.getWordCount());
			stmnt.setString(2, content.getSpaceName());
			stmnt.execute();
		}
	}

	/**
	 * Reads pre-aggregated Word-Count per Space (latest entries, top 10)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ContentBodyDTO> getAggSpaceWordCount() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_WORDCOUNT));
		log.debug("Executing querie: " + QRY_SPC_WORDCOUNT);
		ResultSet rs = stmnt.executeQuery();
		List<ContentBodyDTO> wordCount = new ArrayList<>();
		while (rs.next()) {
			wordCount.add(new ContentBodyDTO(rs.getString(1), rs.getInt(2)));
		}
		return wordCount;

	}

	/**
	 * Reads pre-aggregated Word-Count per Space (latest entries, top 10)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ContentBodyDTO> getAggSpaceCommentWordCount() throws SQLException {
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_SPC_COMMENTCOUNT));
		log.debug("Executing querie: " + QRY_SPC_COMMENTCOUNT);
		ResultSet rs = stmnt.executeQuery();
		List<ContentBodyDTO> wordCount = new ArrayList<>();
		while (rs.next()) {
			wordCount.add(new ContentBodyDTO(rs.getString(1), rs.getInt(2)));
		}
		return wordCount;

	}

}
