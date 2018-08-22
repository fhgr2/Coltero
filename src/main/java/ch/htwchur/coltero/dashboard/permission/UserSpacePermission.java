package ch.htwchur.coltero.dashboard.permission;

import com.atlassian.confluence.plugin.descriptor.web.WebInterfaceContext;
import com.atlassian.confluence.plugin.descriptor.web.conditions.BaseConfluenceCondition;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.spring.container.ContainerManager;

import bucket.user.UserAccessor;

/**
 * Permission class checks if user has permission to view space dashboard and checks if 
 * Menuitem should be displayed (if user is logged in and a page is present in context)
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UserSpacePermission extends BaseConfluenceCondition {

	private PermissionManager pm;
	private UserAccessor ua;
	private SpaceManager sm;

	public UserSpacePermission() {
		pm = (PermissionManager) ContainerManager.getInstance().getContainerContext().getComponent("permissionManager");
		ua = (UserAccessor) ContainerManager.getInstance().getContainerContext().getComponent("userAccessor");
		sm = (SpaceManager) ContainerManager.getInstance().getContainerContext().getComponent("spaceManager");
	}

	/**
	 * Checks if user has permission to view space
	 * @param user
	 * @param space
	 * @return
	 */
	public boolean checkSpacePermission(ConfluenceUser user, Space space) {
		return pm.hasPermission(ua.getUser(user.getName()), Permission.VIEW, space);
	}
	/**
	 * Checks if user has permission to view space
	 * @param user
	 * @param space
	 * @return
	 */
	public boolean checkSpacePermission(ConfluenceUser user, long space) {
		return pm.hasPermission(ua.getUser(user.getName()), Permission.VIEW, sm.getSpace(space));
	}

	@Override
	protected boolean shouldDisplay(WebInterfaceContext context) {
		if(context == null || context.getCurrentUser() == null) {
			return false;
		}
		if(context.getPage() == null) {
			return false;
		}
		return true;
	}

}
