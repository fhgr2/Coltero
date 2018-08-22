package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO for Space Creation data
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class SpaceDTO {

	private int count;
	private String date;
	private String id = "space";
	
	public SpaceDTO() {} //TODO: delete constructor
	public SpaceDTO(int count, String date) {
		super();
		this.count = count;
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

}
