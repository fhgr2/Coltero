package ch.htwchur.coltero.api.dto.page;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for Authors and Commentators count
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class AuthorsCommentatorsCountDTO {

	private int authors;
	private int commentators;

	public AuthorsCommentatorsCountDTO(int authors, int commentators) {
		this.authors = authors;
		this.commentators = commentators;
	}

	public int getAuthors() {
		return authors;
	}

	public int getCommentators() {
		return commentators;
	}

	public void setAuthors(int authors) {
		this.authors = authors;
	}

	public void setCommentators(int commentators) {
		this.commentators = commentators;
	}

}
