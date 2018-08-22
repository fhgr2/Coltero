package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for User-Interactions overall
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class UserInteractionsDTO {

	private String displayName;
	private String date;
	private String spaceName;
	private int interactions = 0;

	public UserInteractionsDTO(String displayName, String date, String spaceName, int interactions) {
		super();
		this.displayName = displayName;
		this.date = date;
		this.spaceName = spaceName;
		this.interactions = interactions;
	}
	
	public UserInteractionsDTO(String displayName, String date, String spaceName) {
		super();
		this.displayName = displayName;
		this.date = date;
		this.spaceName = spaceName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDate() {
		return date;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	
	public void increaseInteractions() {
		interactions = interactions + 1;
	}
	

	public int getInteractions() {
		return interactions;
	}

	public void setInteractions(int interactions) {
		this.interactions = interactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((spaceName == null) ? 0 : spaceName.hashCode());
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
		UserInteractionsDTO other = (UserInteractionsDTO) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		return true;
	}

}
