package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class SpaceTypeDTO {

	private String date;
	private String type;
	private int count;

	public SpaceTypeDTO(String date, int count, String type) {
		this.date = date;
		this.count = count;
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

}
