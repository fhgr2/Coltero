package ch.htwchur.coltero.api.dto.interaction;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO for a Workinteraction-Row in weekday steps
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class InteractionsDTO {

	private String dow;
	private int interactions;
	private int hour;
	
	/**
	 * 
	 * @param dow
	 * @param interactions
	 */
	public InteractionsDTO(String dow, int interactions) {
		this.dow = dow;
		this.interactions = interactions;
	}
	
	/**
	 * 
	 * @param hour
	 * @param interactions
	 */
	public InteractionsDTO(int hour, int interactions) {
		this.hour = hour;
		this.interactions = interactions;
	}
	

	public String getDow() {
		return dow;
	}

	public int getCount() {
		return interactions;
	}

	public int getWeekDay() {
		return -0;
	}
	
	public int getHour() {
		return hour;
	}
	
}