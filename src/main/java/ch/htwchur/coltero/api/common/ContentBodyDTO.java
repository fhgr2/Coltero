package ch.htwchur.coltero.api.common;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO for Body content from pages in Spaces
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class ContentBodyDTO {

	private String spaceName;
	private String bodyContent;
	private int wordCount;

	
	public ContentBodyDTO(String bodyContent) {
		super();
		this.bodyContent = bodyContent;
	}

	public ContentBodyDTO(String spaceName, String bodyContent) {
		this.spaceName = spaceName;
		this.bodyContent = bodyContent;
	}

	public ContentBodyDTO(String spaceName, int wordCount) {
		this.spaceName = spaceName;
		this.wordCount = wordCount;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ContentBodyDTO other = (ContentBodyDTO) obj;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		return true;
	}

}
