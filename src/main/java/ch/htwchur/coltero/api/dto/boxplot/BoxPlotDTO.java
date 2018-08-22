package ch.htwchur.coltero.api.dto.boxplot;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO class for BoxPlot Datas
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class BoxPlotDTO implements Comparable<BoxPlotDTO>{

	private String date;
	private String name;
	private int value;
	private String stringValue;

	public BoxPlotDTO() {

	}

	public BoxPlotDTO(String name, int value, String date) {
		this.date = date;
		this.name = name;
		this.value = value;
	}

	public BoxPlotDTO(String name, String stringValue, String date) {
		this.date = date;
		this.name = name;
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BoxPlotDTO other = (BoxPlotDTO) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(BoxPlotDTO o) {
		// TODO Auto-generated method stub
		return this.date.compareTo(o.getDate());
	}
	

}
