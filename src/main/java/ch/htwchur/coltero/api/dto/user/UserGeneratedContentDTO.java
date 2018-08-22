package ch.htwchur.coltero.api.dto.user;

/**
 * DTO for user content processing
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UserGeneratedContentDTO {

	private String userId;
	private String bodycontent;
	private int wordCount;

	public UserGeneratedContentDTO(String userId, String bodycontent) {
		super();
		this.userId = userId;
		this.bodycontent = bodycontent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBodycontent() {
		return bodycontent;
	}

	public void setBodycontent(String bodycontent) {
		this.bodycontent = bodycontent;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

}
