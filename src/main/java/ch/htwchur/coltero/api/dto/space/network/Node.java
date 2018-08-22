package ch.htwchur.coltero.api.dto.space.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Node Class contains Space-Nodes and its contributing users
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@JsonAutoDetect
public class Node {

	private String name;
	private Map<String, Integer> users = new HashMap<>();
	private int group;
	private int userCount;
	private List<String> tags;
	private String latitude;
	private String longitude;

	public Node(String name, int group) {
		this.name = name;
		this.group = group;
	}

	public Node(String name, int group, List<String> tags) {
		super();
		this.name = name;
		this.group = group;
		this.tags = tags;
	}
	
	public Node(String name, int group, int userCount) {
		super();
		this.name = name;
		this.group = group;
		this.userCount = userCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getName() {
		return name;
	}

	public int getGroup() {
		return group;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public Map<String, Integer> getUsers() {
		return users;
	}

	public void setUsers(Map<String, Integer> users) {
		this.users = users;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + "]";
	}

}
