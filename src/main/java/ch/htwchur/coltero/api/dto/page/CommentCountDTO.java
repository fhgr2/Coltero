package ch.htwchur.coltero.api.dto.page;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * DTO Class for Sums
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class CommentCountDTO {

	private int comments;
	
	public CommentCountDTO(int comments) {
		this.comments = comments;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}
	
	
}
