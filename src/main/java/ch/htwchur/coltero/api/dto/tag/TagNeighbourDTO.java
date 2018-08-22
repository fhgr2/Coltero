package ch.htwchur.coltero.api.dto.tag;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for Tag-Neighbour processing
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagNeighbourDTO {

	private String tagName;
	private String entityName;
	private int tagId;
	private int entityId;

	public TagNeighbourDTO(String tagName, int tagId, String entityName, int entityId) {
		this.tagName = tagName;
		this.tagId = tagId;
		this.entityName = entityName;
		this.entityId = entityId;
	}

	public String getTagName() {
		return tagName;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tagId;
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
		TagNeighbourDTO other = (TagNeighbourDTO) obj;
		if (tagId != other.tagId)
			return false;
		return true;
	}

}
