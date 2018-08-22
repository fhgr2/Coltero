package ch.htwchur.coltero.api.dto.space.network;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * Model class for Network-Visualization containing SpaceNodes and Links between them
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class NetworkModel {

	private List<Node> nodes = new ArrayList<>();
	private List<Links> links = new ArrayList<>();
	private List<String> contentList;

	public NetworkModel() {
	}

	public NetworkModel(List<Node> nodes, List<Links> links) {
		this.nodes = nodes;
		this.links = links;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Links> getLinks() {
		return links;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void setLinks(List<Links> links) {
		this.links = links;
	}

	public List<String> getContentList() {
		return contentList;
	}

	public void setContentList(List<String> userList) {
		this.contentList = userList;
	}
	
	

}
