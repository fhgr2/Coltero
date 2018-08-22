package ch.htwchur.coltero.dashboard.permission;

import com.atlassian.confluence.plugin.descriptor.web.WebInterfaceContext;
import com.atlassian.confluence.plugin.descriptor.web.conditions.BaseConfluenceCondition;

import ch.htwchur.coltero.dashboard.helper.GroupHelper;

/**
 * This class serves the plugin engine with permission context only for web-item validation
 * it determines if the web-item should be displayed or not, but does not restrict
 * access to the link behind the item
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UserInManagementGroup extends BaseConfluenceCondition {

	private GroupHelper groupHelper;

	public UserInManagementGroup() {
		this.groupHelper = new GroupHelper();
	}

	@Override
	protected boolean shouldDisplay(WebInterfaceContext context) {
		if(context == null || context.getCurrentUser() == null) {
			return false;
		}
		return groupHelper.checkUserInGroup(context.getCurrentUser().getKey());
	}
}
