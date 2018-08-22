package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class for all Spacetags
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class SpaceAllOtherTagsDTO {

	private String spaceTagName;
	private int spaceCount;
	private int totalSpaces;
	private float precentage;
	private String date;

	
	public SpaceAllOtherTagsDTO(String spaceTagName) {
		this.spaceTagName = spaceTagName;
	}

	public SpaceAllOtherTagsDTO(String spaceTagName, int spaceCount, int totalSpaces, float precentage, String date) {
		this.spaceTagName = spaceTagName;
		this.spaceCount = spaceCount;
		this.totalSpaces = totalSpaces;
		this.precentage = precentage;
		this.date = date;
	}

	public String getSpaceTagName() {
		return spaceTagName;
	}

	public int getSpaceCount() {
		return spaceCount;
	}

	public int getTotalSpaces() {
		return totalSpaces;
	}

	public float getPrecentage() {
		return precentage;
	}

	public String getDate() {
		return date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((spaceTagName == null) ? 0 : spaceTagName.hashCode());
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
		SpaceAllOtherTagsDTO other = (SpaceAllOtherTagsDTO) obj;
		if (spaceTagName == null) {
			if (other.spaceTagName != null)
				return false;
		} else if (!spaceTagName.equals(other.spaceTagName))
			return false;
		return true;
	}
	
	

}
