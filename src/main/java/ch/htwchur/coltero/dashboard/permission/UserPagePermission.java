package ch.htwchur.coltero.dashboard.permission;

import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.plugin.descriptor.web.WebInterfaceContext;
import com.atlassian.confluence.plugin.descriptor.web.conditions.BaseConfluenceCondition;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.spring.container.ContainerManager;

import bucket.user.UserAccessor;

/**
 * Permission class checks if user has permission to view space dashboard and
 * determines if the Menuitem should be displayed or not (if user is logged in
 * and a page is present)
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UserPagePermission extends BaseConfluenceCondition {

	private PermissionManager pm;
	private UserAccessor ua;
	private PageManager pgm;

	public UserPagePermission() {
		pm = (PermissionManager) ContainerManager.getInstance().getContainerContext().getComponent("permissionManager");
		ua = (UserAccessor) ContainerManager.getInstance().getContainerContext().getComponent("userAccessor");
		pgm = (PageManager) ContainerManager.getInstance().getContainerContext().getComponent("pageManager");
	}

	/**
	 * Check View permission of user on page
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	public boolean checkPagePermission(ConfluenceUser user, AbstractPage page) {
		return pm.hasPermission(ua.getUser(user.getName()), Permission.VIEW, page);
	}
	
	public boolean checkPagePermission(ConfluenceUser user, long page) {
		return pm.hasPermission(ua.getUser(user.getName()), Permission.VIEW, pgm.getAbstractPage(page));
	}

	@Override
	protected boolean shouldDisplay(WebInterfaceContext context) {
		if (context == null || context.getCurrentUser() == null) {
			return false;
		}
		if (context.getPage() == null) {
			return false;
		}
		return true;
	}

}
