package ch.htwchur.coltero.api.dto.tag.network;

/**
 * This DTO Class holds Rows from Resultset
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class ResultSetDTO {

	private String name;
	private int labelId;
	private int contentId;

	public ResultSetDTO(String name, int labelId, int contentId) {
		super();
		this.name = name;
		this.labelId = labelId;
		this.contentId = contentId;
	}

	public String getName() {
		return name;
	}

	public int getLabelId() {
		return labelId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

}
