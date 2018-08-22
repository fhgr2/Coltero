package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * Sum DTO
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class SumDTO {

	private int commentSum;
	private int pageSum;
	
	public SumDTO(int commentSum, int pageSum) {
		super();
		this.commentSum = commentSum;
		this.pageSum = pageSum;
	}

	public int getCommentSum() {
		return commentSum;
	}

	public int getPageSum() {
		return pageSum;
	}

	public void setCommentSum(int commentSum) {
		this.commentSum = commentSum;
	}

	public void setPageSum(int pageSum) {
		this.pageSum = pageSum;
	}

}
