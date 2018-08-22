package ch.htwchur.coltero.api.dto.user;

/**
 * DTO class for user tag neighbours
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class TagNeighboursDTO {

	private String username;
	private String tag;
	private String title;
	private int pageid;

	public TagNeighboursDTO(String username, String tag) {
		this.username = username;
		this.tag = tag;
	}

	public TagNeighboursDTO(String title, int pageid, String tag) {
		super();
		this.title = title;
		this.pageid = pageid;
		this.tag = tag;
	}


	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public int getPageid() {
		return pageid;
	}



	public void setPageid(int pageid) {
		this.pageid = pageid;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		TagNeighboursDTO other = (TagNeighboursDTO) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
