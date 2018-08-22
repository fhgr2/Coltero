package ch.htwchur.coltero.api.dto.boxplot;

import org.codehaus.jackson.annotate.JsonAutoDetect;
/**
 * DTO class for Likes Boxplot
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class OtherBoxPlot {

	private int contentid;
	private int labelid;
	private int value;
	private String date;
	
	public OtherBoxPlot(int contentid, int labelid, String date, int value) {
		this.contentid = contentid;
		this.labelid = labelid;
		this.date = date;
		this.value = value; 
	}
	public int getContentid() {
		return contentid;
	}
	public void setContentid(int contentid) {
		this.contentid = contentid;
	}
	public int getLabelid() {
		return labelid;
	}
	public void setLabelid(int labelid) {
		this.labelid = labelid;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contentid;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OtherBoxPlot other = (OtherBoxPlot) obj;
		if (contentid != other.contentid)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}
	
	
}
