package ch.htwchur.coltero.api.common;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Result DTO for Collaboration metrics
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class CollaborationMetricDTO {

	private String type;
	private int count;

	public CollaborationMetricDTO(String type, int count) {
		super();
		this.type = type;
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
