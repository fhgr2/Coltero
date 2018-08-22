package ch.htwchur.coltero.api.dto.tag.network;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Tag Node DTO for Tag Network Diagram
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagNode {

	private String tag;
	private int group;
	private int weight;

	public TagNode(String tag, int group, int weight) {
		this.tag = tag;
		this.group = group;
		this.weight = weight;
	}

	public String getTag() {
		return tag;
	}

	public int getGroup() {
		return group;
	}

	public int getWeight() {
		return weight;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagNode other = (TagNode) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

}
