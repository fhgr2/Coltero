package ch.htwchur.coltero.api.dto.counts;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for simple coltero metric data
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class CountsDTO {

	private int pageCount;
	private int commentCount;
	private int userCount;
	private int commentatorsCount;
	private int authorsCount;
	private int uploadersCount;
	private int slackersCount;
	private int traggersCount;
	private int likersCount;
	private String aggregationDate;
	
	public CountsDTO() {}
	public CountsDTO(int pageCount, int commentCount, int userCount, int commentatorsCount, int authorsCount, int uploadersCount,
			int slackersCount, int traggersCount, int likersCount, String aggregationDate) {
		this.pageCount = pageCount;
		this.commentCount = commentCount;
		this.userCount = userCount;
		this.commentatorsCount = commentatorsCount;
		this.authorsCount = authorsCount;
		this.uploadersCount = uploadersCount;
		this.slackersCount = slackersCount;
		this.traggersCount = traggersCount;
		this.likersCount = likersCount;
		this.aggregationDate = aggregationDate;
		
	}

	public int getCommentCount() {
		return commentCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public int getCommentatorsCount() {
		return commentatorsCount;
	}

	public int getAuthorsCount() {
		return authorsCount;
	}

	public int getUploadersCount() {
		return uploadersCount;
	}

	public int getSlackersCount() {
		return slackersCount;
	}

	public int getTraggersCount() {
		return traggersCount;
	}

	public int getLikersCount() {
		return likersCount;
	}

	public String getAggregationDate() {
		return aggregationDate;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public void setCommentatorsCount(int commentatorsCount) {
		this.commentatorsCount = commentatorsCount;
	}

	public void setAuthorsCount(int authorsCount) {
		this.authorsCount = authorsCount;
	}

	public void setUploadersCount(int uploadersCount) {
		this.uploadersCount = uploadersCount;
	}

	public void setSlackersCount(int slackersCount) {
		this.slackersCount = slackersCount;
	}

	public void setTraggersCount(int traggersCount) {
		this.traggersCount = traggersCount;
	}

	public void setLikersCount(int likersCount) {
		this.likersCount = likersCount;
	}

	public void setAggregationDate(String aggregationDate) {
		this.aggregationDate = aggregationDate;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}
