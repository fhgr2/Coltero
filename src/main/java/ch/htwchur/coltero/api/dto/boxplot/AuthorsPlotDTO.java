package ch.htwchur.coltero.api.dto.boxplot;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * DTO class for Authors-Boxplot
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class AuthorsPlotDTO implements Comparable<String> {

	private int page;
	private int count = 0;
	private String date;

	public AuthorsPlotDTO(int page, int count, String date) {
		this.page = page;
		this.count = count;
		this.date = date;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int compareTo(String o) {
		return this.date.compareTo(o);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + page;
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
		AuthorsPlotDTO other = (AuthorsPlotDTO) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (page != other.page)
			return false;
		return true;
	}
	
}
