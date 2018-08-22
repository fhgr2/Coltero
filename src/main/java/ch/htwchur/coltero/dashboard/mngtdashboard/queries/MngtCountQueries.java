package ch.htwchur.coltero.dashboard.mngtdashboard.queries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import ch.htwchur.coltero.api.dto.boxplot.AuthorsPlotDTO;
import ch.htwchur.coltero.api.dto.boxplot.BoxPlotDTO;
import ch.htwchur.coltero.api.dto.boxplot.OtherBoxPlot;
import ch.htwchur.coltero.api.dto.counts.CountsDTO;
import ch.htwchur.coltero.dashboard.helper.DateHelper;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * This class holds queries for Management Dasbhoard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class MngtCountQueries {

	private static final Logger log = LoggerFactory.getLogger(MngtCountQueries.class);

	/**
	 * Querie aggregated dashboard-count values
	 */
	public static final String QRY_COUNTS = "dataqueries/querie_count_statistics.sql";
	public static final String QRY_BOX_AUTHORS = "dataqueries/querie_authorsboxplot.sql";
	public static final String QRY_BOX_COMMENTS = "dataqueries/querie_commentboxplot.sql";
	public static final String QRY_BOX_COMMENTATORS = "dataqueries/querie_commentatorsboxplot.sql";
	public static final String QRY_BOX_LIKES = "dataqueries/querie_likesboxplot.sql";
	public static final String QRY_BOX_TAGS = "dataqueries/querie_tagsboxplot.sql";
	public static final String QRY_BOX_ATTACHMENTS = "dataqueries/querie_attachmentsboxplot.sql";

	public static IDatabase database;
	private QuerieLoader querieLoader;

	@Autowired
	public MngtCountQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		MngtCountQueries.database = database;
		this.querieLoader = querieLoader;
	}

	/**
	 * Generalized Querie-Executer for Boxplot datas
	 * 
	 * @param querieId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws SQLException
	 */
	public List<BoxPlotDTO> querieBoxPlotData(String querieId, Date fromDate, Date toDate) throws SQLException {
		log.debug("Executing Querie: " + querieId);
		PreparedStatement countQuerieStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(querieId));
		ResultSet rs = countQuerieStmnt.executeQuery();
		int sum = 0;
		List<BoxPlotDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			sum += rs.getInt(2);
			resultList.add(new BoxPlotDTO(rs.getString(1), sum, DateHelper.df.format(rs.getDate(3))));
		}
		return resultList;
	}

	/**
	 * Queries Plot data with resultset formed like int,int,String
	 * @return
	 * @throws SQLException
	 */
	public List<OtherBoxPlot> querieBoxPlotRolledUpIntIntString(String querie) throws SQLException{
		log.debug("Executing Querie: " + querie);
		PreparedStatement countQuerieStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(querie));
		ResultSet rs = countQuerieStmnt.executeQuery();
		List<OtherBoxPlot> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new OtherBoxPlot(rs.getInt("id"),rs.getInt("contentid"), DateHelper.df.format(rs.getDate("date")), 1));
		}
		List<OtherBoxPlot> content = new ArrayList<>();
		for(OtherBoxPlot dto : resultList) {
			OtherBoxPlot temp = new OtherBoxPlot(dto.getLabelid(), dto.getContentid(), dto.getDate(),1);
			if(content.contains(temp)) {
				OtherBoxPlot current = content.get(content.indexOf(temp));
				current.setValue(current.getValue() + 1);}
				else {
					content.add(temp);
			}
		}
		Set<String> quarterSet = new HashSet<>();
		content.forEach(item -> quarterSet.add(item.getDate()));
		List<String> quarterList = new ArrayList<String>();
		quarterSet.forEach(item -> quarterList.add(item));
		Collections.sort(quarterList);
		Map<Integer, Integer> numberOfContent = new HashMap<>();
		for (String quarter : quarterList) {
			for (OtherBoxPlot current : content) {
				if (current.getDate().equals(quarter)) {
					if (numberOfContent.containsKey(current.getContentid())) {
						int count = numberOfContent.get(current.getContentid());
						numberOfContent.put(current.getContentid(), current.getValue() + count);
						current.setValue(current.getValue() + count);
					}
					else {
						numberOfContent.put(current.getContentid(), current.getValue());
						
					}
				}

			}
		}
		return content;
	}
	/**
	 * Queries Plot data with resultset formed like String,int,String
	 * @return 
	 * @throws SQLException
	 */
	public List<AuthorsPlotDTO> querieBoxPlotRolledUpStringIntString(String querie) throws SQLException {
		log.debug("Executing Querie: " + querie);
		PreparedStatement countQuerieStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(querie));
		ResultSet rs = countQuerieStmnt.executeQuery();
		List<BoxPlotDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new BoxPlotDTO(rs.getString("lastmodifier"), rs.getInt("mainid"),
					DateHelper.df.format(rs.getDate("date"))));

		}
		List<AuthorsPlotDTO> contentAuthors = new ArrayList<>();
		for (BoxPlotDTO dto : resultList) {
			AuthorsPlotDTO temp = new AuthorsPlotDTO(dto.getValue(), 0, dto.getDate());

			if (contentAuthors.contains(temp)) {
				AuthorsPlotDTO current = contentAuthors.get(contentAuthors.indexOf(temp));
				current.setCount(current.getCount() + 1);
			} else {
				contentAuthors.add(new AuthorsPlotDTO(dto.getValue(), 1, dto.getDate()));
			}
		}
		Set<String> quarterSet = new HashSet<>();
		contentAuthors.forEach(item -> quarterSet.add(item.getDate()));
		List<String> quarterList = new ArrayList<String>();
		quarterSet.forEach(item -> quarterList.add(item));
		Collections.sort(quarterList);
		Map<Integer, Integer> contentNumberAuthors = new HashMap<>();
		for (String quarter : quarterList) {
			for (AuthorsPlotDTO current : contentAuthors) {
				if (current.getDate().equals(quarter)) {
					if (contentNumberAuthors.containsKey(current.getPage())) {
						int count = contentNumberAuthors.get(current.getPage());
						contentNumberAuthors.put(current.getPage(), current.getCount() + count);
						current.setCount(current.getCount() + count);
					}
					else {
						contentNumberAuthors.put(current.getPage(), current.getCount());
						
					}
				}

			}
		}

		return contentAuthors;
	}

	/**
	 * Queries count Data
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws SQLException
	 */
	public CountsDTO querieCountStatistics(java.sql.Date fromDate, java.sql.Date toDate) throws SQLException {
		log.debug("Executing Querie: " + QRY_COUNTS);
		PreparedStatement countQuerieStmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_COUNTS));
		countQuerieStmnt.setString(1, null);// DateHelper.df.format(fromDate)+"-01");
		countQuerieStmnt.setString(2, null);// DateHelper.df.format(toDate)+"-01");
		ResultSet rs = countQuerieStmnt.executeQuery();
		CountsDTO countResult = new CountsDTO();
		while (rs.next()) {
			countResult = new CountsDTO(rs.getInt("page_count"), rs.getInt("comment_count"), rs.getInt("user_count"),
					rs.getInt("commentators_count"), rs.getInt("authors_count"), rs.getInt("uploaders_count"),
					rs.getInt("slackers_count"), rs.getInt("taggers_count"), rs.getInt("likers_count"),
					rs.getTimestamp("time").toString());

		}
		return countResult;
	}
}