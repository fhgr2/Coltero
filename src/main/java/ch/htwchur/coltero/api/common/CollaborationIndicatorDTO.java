package ch.htwchur.coltero.api.common;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * DTO class for Collaboration vs Documentation Inication
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class CollaborationIndicatorDTO {

	private String title;
	private int authors;
	private int mainid;
	private String date;
	
	public CollaborationIndicatorDTO(String title, int authors, int mainid, String date) {
		super();
		this.title = title;
		this.authors = authors;
		this.mainid = mainid;
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public int getAuthors() {
		return authors;
	}

	public int getMainid() {
		return mainid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthors(int authors) {
		this.authors = authors;
	}

	public void setMainid(int mainid) {
		this.mainid = mainid;
	}

}
