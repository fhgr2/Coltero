package ch.htwchur.coltero.api.dto.tag.network;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * Link DTO for Tag Network Diagram
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagLink implements Comparable<TagLink>{

	private String sourceName;
	private String targetName;
	private int source;
	private int target;
	private int weight;

	public TagLink(String sourceName, String targetName, int weight) {
		this.sourceName = sourceName;
		this.targetName = targetName;
		this.weight = weight;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getTargetName() {
		return targetName;
	}

	public int getWeight() {
		return weight;
	}

	public void setSourceName(String source) {
		this.sourceName = source;
	}

	public void setTargetName(String target) {
		this.targetName = target;
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
		result = prime * result + ((targetName == null) ? 0 : targetName.hashCode());
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
		TagLink other = (TagLink) obj;
		if (sourceName == null) {
			if (other.sourceName != null)
				return false;
		} else if (!sourceName.equals(other.sourceName))
			return false;
		if (targetName == null) {
			if (other.targetName != null)
				return false;
		} else if (!targetName.equals(other.targetName))
			return false;
		return true;
	}

	@Override
	public int compareTo(TagLink o) {
		if(o.weight == this.weight) {return 0;}
		if(o.weight < this.weight) {return 1;}
		return -1;
	}


}
