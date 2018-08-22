package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for Active Users per Location
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class ActiveUsersPerLocationDTO {
	private int activeUsersInLocation;
	private int allActiveUsers;
	private String location;
	private String date;

	public ActiveUsersPerLocationDTO(int activeUsersInLocation, int allActiveUsers, String location, String date) {
		this.activeUsersInLocation = activeUsersInLocation;
		this.allActiveUsers = allActiveUsers;
		this.location = location;
		this.date = date;
	}

	public int getActiveUsersInLocation() {
		return activeUsersInLocation;
	}

	public int getAllActiveUsers() {
		return allActiveUsers;
	}

	public String getLocation() {
		return location;
	}

	public String getDate() {
		return date;
	}

}
