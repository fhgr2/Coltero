package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Top 10 DTO class for Spaces
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class SpaceTop10DTO {

	private String spaceName;
	private int count;

	public SpaceTop10DTO(String spaceName, int count) {
		super();
		this.spaceName = spaceName;
		this.count = count;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public int getCount() {
		return count;
	}

}
