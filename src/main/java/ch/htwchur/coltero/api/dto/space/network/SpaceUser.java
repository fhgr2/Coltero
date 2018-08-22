package ch.htwchur.coltero.api.dto.space.network;

/**
 * Holds Space-Username pairs
 *
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class SpaceUser {

	private String userName;
	private String spacename;
	private int occurence;
	
	public SpaceUser(String spaceName, String userName, int occurence) {
		this.userName = userName;
		this.spacename = spaceName;
		this.occurence = occurence;
	}

	public String getUserName() {
		return userName;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
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
		result = prime * result + ((spacename == null) ? 0 : spacename.hashCode());
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
		SpaceUser other = (SpaceUser) obj;
		if (spacename == null) {
			if (other.spacename != null)
				return false;
		} else if (!spacename.equals(other.spacename))
			return false;
		return true;
	}

	
	

}
