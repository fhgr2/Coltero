package ch.htwchur.coltero.api.dto.tag;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO Class for Tag-Cloud
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */

@JsonAutoDetect
public class TagCloudDTO {

	private String name;
	private int count;

	public TagCloudDTO(String name, int count) {
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

}
