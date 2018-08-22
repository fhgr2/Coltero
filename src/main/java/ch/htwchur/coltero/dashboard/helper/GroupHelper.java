package ch.htwchur.coltero.dashboard.helper;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.EntityException;
import com.atlassian.user.Group;
import com.atlassian.user.GroupManager;

public class GroupHelper {

	private GroupManager groupManager;
	private UserAccessor userAccessor;
	public static final String GROUP_NAME = "management";

	public GroupHelper() {
		groupManager = (GroupManager) ContainerManager.getInstance().getContainerContext().getComponent("groupManager");
		userAccessor = (UserAccessor) ContainerManager.getInstance().getContainerContext().getComponent("userAccessor");
	}

	/**
	 * Returns true if the group existed, false if not
	 * 
	 * @param groupName
	 * @return
	 * @throws EntityException
	 */
	public Group checkAndCreateGroup(String groupName) throws EntityException {
		if (groupManager.getGroup(groupName) == null) {
			return groupManager.createGroup(groupName);
		}
		return groupManager.getGroup(groupName);
	}

	/**
	 * Check if user exists in Management-Group
	 * 
	 * @param user
	 *            Context-User
	 * @return true if in group, else false
	 */
	public boolean checkUserInGroup(UserKey userKey) {
		Group managementGroup = null;
		try {
			managementGroup = (Group) groupManager.getGroup(GROUP_NAME);
		} catch (EntityException e) {
			System.out.println("Exception occured: " + e.getMessage());
		}
		Iterable<ConfluenceUser> users = userAccessor.getMembers(managementGroup);
		java.util.Iterator<ConfluenceUser> itr = users.iterator();
		while (itr.hasNext()) {
			ConfluenceUser thisUser = itr.next();
			if (thisUser.getKey().equals(userKey)) {
				return true;
			}
		}
		return false;
	}
}
