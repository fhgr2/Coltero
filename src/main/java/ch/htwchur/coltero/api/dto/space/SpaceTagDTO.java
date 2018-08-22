package ch.htwchur.coltero.api.dto.space;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO class for SpaceTagQueries
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class SpaceTagDTO implements Comparable<SpaceTagDTO> {

	private String date;
	private float overallPrecentage;
	private int taggedSpaces;
	private int totalSpaces;
	private boolean processed = false;

	public SpaceTagDTO(String date, float overallPrecentage, int taggedSpaces, int totalSpaces) {
		super();
		this.date = date;
		this.overallPrecentage = overallPrecentage;
		this.taggedSpaces = taggedSpaces;
		this.totalSpaces = totalSpaces;
	}

	public SpaceTagDTO(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public float getOverallPrecentage() {
		return overallPrecentage;
	}

	public int getTaggedSpaces() {
		return taggedSpaces;
	}

	public int getTotalSpaces() {
		return totalSpaces;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setOverallPrecentage(float overallPrecentage) {
		this.overallPrecentage = overallPrecentage;
	}

	public void setTaggedSpaces(int taggedSpaces) {
		this.taggedSpaces = taggedSpaces;
	}

	public void setTotalSpaces(int totalSpaces) {
		this.totalSpaces = totalSpaces;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		SpaceTagDTO other = (SpaceTagDTO) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SpaceTagDTO [date=" + date + ", overallPrecentage=" + overallPrecentage + ", taggedSpaces="
				+ taggedSpaces + ", totalSpaces=" + totalSpaces + "]";
	}

	@Override
	public int compareTo(SpaceTagDTO o) {
		SpaceTagDTO other = o;
		return this.date.compareTo(other.date);
	}

}
