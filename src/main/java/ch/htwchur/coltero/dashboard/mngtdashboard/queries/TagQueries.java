package ch.htwchur.coltero.dashboard.mngtdashboard.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.ibm.icu.text.DecimalFormat;

import ch.htwchur.coltero.api.dto.tag.TagAttachmentRatioDTO;
import ch.htwchur.coltero.api.dto.tag.TagCloudDTO;
import ch.htwchur.coltero.api.dto.tag.TagDTO;
import ch.htwchur.coltero.api.dto.tag.network.ResultSetDTO;
import ch.htwchur.coltero.api.dto.tag.network.TagLink;
import ch.htwchur.coltero.api.dto.tag.network.TagNetworkModel;
import ch.htwchur.coltero.api.dto.tag.network.TagNode;
import ch.htwchur.coltero.db.IDatabase;
import ch.htwchur.coltero.db.QuerieLoader;

/**
 * Tags Querie Class
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class TagQueries {

	private static final Logger log = LoggerFactory.getLogger(TagQueries.class);

	private static final String QRY_TAG_OT = "dataqueries/tag/qry_tagsot.sql";
	private static final String QRY_ATT_RATIO = "dataqueries/tag/qry_attachmentsRateTaggedUntagged.sql";
	private static final String QRY_TOP_TAGS = "dataqueries/tag/qry_toptagcount.sql";
	private static final String QRY_TAG_NETWORK = "dataqueries/tag/qry_tagnetwork.sql";

	private IDatabase database;
	private QuerieLoader querieLoader;

	@Autowired
	public TagQueries(@ComponentImport IDatabase database, @ComponentImport QuerieLoader querieLoader) {
		this.database = database;
		this.querieLoader = querieLoader;

	}

	/**
	 * Querie created Tags over Time (monthly)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<TagDTO> querieTagsOT() throws SQLException {
		log.debug("Executing querie: " + QRY_TAG_OT);
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_TAG_OT));
		ResultSet rs = stmnt.executeQuery();
		List<TagDTO> resultList = new ArrayList<>();
		int sum = 0;
		while (rs.next()) {
			sum += rs.getInt(2);
			resultList.add(new TagDTO(sum, rs.getString(1)));
		}
		return resultList;
	}

	/**
	 * Querie Ratio between Tagged and all Attachments inc. count of Tagged and
	 * total Attachments
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<TagAttachmentRatioDTO> querieTagAttRatio() throws SQLException {
		log.debug("Executing querie: " + QRY_ATT_RATIO);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_ATT_RATIO));
		ResultSet rs = stmnt.executeQuery();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		List<TagAttachmentRatioDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new TagAttachmentRatioDTO("untagged", rs.getInt(2) - rs.getInt(1)));
			resultList.add(new TagAttachmentRatioDTO("tagged", rs.getInt(1)));
		}
		return resultList;
	}

	/**
	 * Querie Top Used Tags from all content (top 20)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<TagCloudDTO> querieTopTags() throws SQLException {
		log.debug("Executing querie: " + QRY_TOP_TAGS);
		PreparedStatement stmnt = database.getDatabaseConnection().prepareStatement(querieLoader.loadQuerie(QRY_TOP_TAGS));
		ResultSet rs = stmnt.executeQuery();
		List<TagCloudDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new TagCloudDTO(rs.getString(1), rs.getInt(2)));
		}
		return resultList;
	}

	/**
	 * Queries content and their tags ordered and creates nodes and all links
	 * between tags including node and link weight...
	 * 
	 * @return
	 * @throws SQLException
	 */
	public TagNetworkModel querieAndCalculateTagNetwork() throws SQLException {
		log.debug("Executing querie: " + QRY_TAG_NETWORK);
		PreparedStatement stmnt = database.getDatabaseConnection()
				.prepareStatement(querieLoader.loadQuerie(QRY_TAG_NETWORK));
		ResultSet rs = stmnt.executeQuery();
		// Cache ResultSet for several processing steps
		List<ResultSetDTO> resultList = new ArrayList<>();
		while (rs.next()) {
			resultList.add(new ResultSetDTO(rs.getString(1), rs.getInt(2), rs.getInt(3)));
		}
		
		Iterator<ResultSetDTO> rsItr = resultList.iterator();
		
		// Link-Part: Check if contentIds are labeled with same labels, if so create
		// links between contentIds
		// if several same links existing, increase link-weight
		List<TagLink> links = new ArrayList<>();
		rsItr = resultList.iterator();
		while (rsItr.hasNext()) {
			ResultSetDTO item = rsItr.next();
			Iterator<ResultSetDTO> elements = resultList.iterator();
			while (elements.hasNext()) {
				ResultSetDTO element = elements.next();
				if (element.getContentId() == item.getContentId()) {
					if (element.getLabelId() != item.getLabelId()) {
						TagLink tempLink = new TagLink(element.getName(), item.getName(), 0);
						if (links.contains(tempLink)) {
							int linkIdx = links.indexOf(tempLink);
							int weight = links.get(linkIdx).getWeight();
							links.get(linkIdx).setWeight(weight + 1);
						} else {
							links.add(new TagLink(element.getName(), item.getName(), 1));
						}
					}
				}
			}
		}
		Collections.sort(links, Collections.reverseOrder());
		links = links.subList(0, links.size() >= 1500 ? 1500 : links.size());

		// Add Labels, if several labels with same name available, increase weight of
				// the node
		Map<String, TagNode> nodes = new HashMap<>();
		for (TagLink link : links) {
			if (nodes.containsKey(link.getSourceName())) {
				TagNode thisNode = nodes.get(link.getSourceName());
				int weight = thisNode.getWeight();
				nodes.get(link.getSourceName()).setWeight(weight + 1);
			} else {
				nodes.put(link.getSourceName(), new TagNode(link.getSourceName(), 0, 0));
			}
			if (nodes.containsKey(link.getTargetName())) {
				TagNode thisNode = nodes.get(link.getSourceName());
				int weight = thisNode.getWeight();
				nodes.get(link.getSourceName()).setWeight(weight + 1);
			} else {
				nodes.put(link.getTargetName(), new TagNode(link.getTargetName(), 0, 0));
			}
		}

		List<TagNode> tagNodesList = new ArrayList<TagNode>(nodes.values());
		return new TagNetworkModel(links, tagNodesList);
	}

}
