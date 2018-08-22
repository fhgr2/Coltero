package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class for ActiveVsInactive User-Statistics
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class ActiveInactiveUsersDTO {

	private int active;
	private int inactive;
	private float precentage;
	private String date;

	public ActiveInactiveUsersDTO(int active, int inactive, float precentage, String date) {
		this.active = active;
		this.inactive = inactive;
		this.precentage = precentage;
		this.date = date;
	}

	public int getActive() {
		return active;
	}

	public int getInactive() {
		return inactive;
	}

	public float getPrecentage() {
		return precentage;
	}

	public String getDate() {
		return date;
	}

}
