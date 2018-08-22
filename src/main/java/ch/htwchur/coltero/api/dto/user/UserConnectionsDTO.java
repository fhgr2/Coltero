package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class for UserConnections Network
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class UserConnectionsDTO {

	private String displayName;
	private String userKey;
	private int group = 0; // layer
	private int occurence = 0; 

	public UserConnectionsDTO(String displayName, String userKey, int occurence) {
		this.displayName = displayName;
		this.userKey = userKey;
		this.occurence = occurence;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getOccurence() {
		return occurence;
	}

	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
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
		UserConnectionsDTO other = (UserConnectionsDTO) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		return true;
	}

}
