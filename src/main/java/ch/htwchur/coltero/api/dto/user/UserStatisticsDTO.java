package ch.htwchur.coltero.api.dto.user;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO class for UserStatistics and avaerage calculation
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class UserStatisticsDTO {

	private String userKey;
	private int userConnectionsAvg;
	private int userRangeAvg;
	private int reactionsAvg;
	private int userConnections;
	private int userRange;
	private int reactions;
	private int writtenWords;
	private int writtenWordsAvg;

	public UserStatisticsDTO() {
	}

	public UserStatisticsDTO(int userConnections, int userRange, int reactions, int writtenWords) {
		this.userConnections = userConnections;
		this.userRange = userRange;
		this.reactions = reactions;
		this.writtenWords = writtenWords;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public int getUserConnections() {
		return userConnections;
	}

	public void setUserConnections(int userConnections) {
		this.userConnections = userConnections;
	}

	public int getUserRange() {
		return userRange;
	}

	public void setUserRange(int userRange) {
		this.userRange = userRange;
	}

	public int getReactions() {
		return reactions;
	}

	public void setReactions(int reactions) {
		this.reactions = reactions;
	}

	public int getUserConnectionsAvg() {
		return userConnectionsAvg;
	}

	public void setUserConnectionsAvg(int userConnectionsAvg) {
		this.userConnectionsAvg = userConnectionsAvg;
	}

	public int getUserRangeAvg() {
		return userRangeAvg;
	}

	public void setUserRangeAvg(int userRangeAvg) {
		this.userRangeAvg = userRangeAvg;
	}

	public int getReactionsAvg() {
		return reactionsAvg;
	}

	public void setReactionsAvg(int reactionsAvg) {
		this.reactionsAvg = reactionsAvg;
	}

	public int getWrittenWords() {
		return writtenWords;
	}

	public void setWrittenWords(int writtenWords) {
		this.writtenWords = writtenWords;
	}

	public int getWrittenWordsAvg() {
		return writtenWordsAvg;
	}

	public void setWrittenWordsAvg(int writtenWordsAvg) {
		this.writtenWordsAvg = writtenWordsAvg;
	}

}
