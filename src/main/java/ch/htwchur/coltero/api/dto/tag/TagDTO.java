package ch.htwchur.coltero.api.dto.tag;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO for Tag OT querie
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class TagDTO {

	private int count;
	private String date;

	public TagDTO(int count, String date) {
		super();
		this.count = count;
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public String getDate() {
		return date;
	}

}
