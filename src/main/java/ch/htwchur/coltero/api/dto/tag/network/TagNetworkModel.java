package ch.htwchur.coltero.api.dto.tag.network;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * Model Class holding links and nodes for Tag Network Diagram
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagNetworkModel {

	private List<TagLink> links = new ArrayList<>();
	private List<TagNode> nodes = new ArrayList<>();

	public TagNetworkModel(List<TagLink> links, List<TagNode> nodes) {
		super();
		this.links = links;
		this.nodes = nodes;
	}

	public List<TagLink> getLinks() {
		return links;
	}

	public List<TagNode> getNodes() {
		return nodes;
	}

	public void setLinks(List<TagLink> links) {
		this.links = links;
	}

	public void setNodes(List<TagNode> nodes) {
		this.nodes = nodes;
	}

}
