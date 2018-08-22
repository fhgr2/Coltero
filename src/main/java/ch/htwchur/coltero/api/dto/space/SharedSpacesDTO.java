package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class SharedSpacesDTO {

	private String location;
	private int spaceid;
	private int occ;
	private String displayName;
	private int ratio = 0;
	private String spacename;

	public SharedSpacesDTO(String location, int spaceid, String displayName) {
		this.location = location;
		this.spaceid = spaceid;
		this.displayName = displayName;
	}

	public SharedSpacesDTO(String location, int spaceid, int occ) {
		this.location = location;
		this.spaceid = spaceid;
		this.occ = occ;
	}
	

	public SharedSpacesDTO(String location, String spacename, int occ) {
		this.location = location;
		this.occ = occ;
		this.spacename = spacename;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public String getLocation() {
		return location;
	}

	public int getSpaceid() {
		return spaceid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setSpaceid(int spaceid) {
		this.spaceid = spaceid;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getRatio() {
		return ratio;
	}

	public void increaseRatio() {
		ratio = ratio + 1;
	}

	public int getOcc() {
		return occ;
	}

	public void setOcc(int occ) {
		this.occ = occ;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + spaceid;
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
		SharedSpacesDTO other = (SharedSpacesDTO) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (spaceid != other.spaceid)
			return false;
		return true;
	}

}
