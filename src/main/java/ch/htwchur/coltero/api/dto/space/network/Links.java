package ch.htwchur.coltero.api.dto.space.network;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Links Class contains links between Spaces and corresponding metadata like
 * name, user and weight for visualizing network diagram
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class Links implements Comparable<Links> {

	private int source;
	private int target;
	private int weight;
	private String sourceName;
	private String targetName;
	private String userName;

	public Links(int source, int target, int weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public Links(String sourceName, String targetName, String userName, int weight) {
		this.weight = weight;
		this.sourceName = sourceName;
		this.targetName = targetName;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

	public int getWeight() {
		return weight;
	}

	public void setSource(int source) {
		this.source = source;
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
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		Links other = (Links) obj;
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
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public int compareTo(Links o) {
		int weight = o.getWeight();
		if(weight == this.weight) {
			return 0;
		}
		if (weight > this.weight) {
			return 1;
		}
		return -1;
	}

}
