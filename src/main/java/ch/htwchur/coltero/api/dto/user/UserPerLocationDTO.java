package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for Users per Location data
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class UserPerLocationDTO {

	private String location;
	private int count;

	public UserPerLocationDTO(String location, int count) {
		super();
		this.location = location;
		this.count = count;
	}

	public String getLocation() {
		return location;
	}

	public int getCount() {
		return count;
	}

}
