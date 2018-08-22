package ch.htwchur.coltero.api.dto.tag;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO class for Ratio between Tagged and Untagged Attachments
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagAttachmentRatioDTO {
	
	private String type;
	private int count;

	public TagAttachmentRatioDTO(String type, int count) {
		this.type = type;
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
