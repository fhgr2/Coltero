package ch.htwchur.coltero.dashboard.spacedashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.actions.PageAware;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.dashboard.permission.UserSpacePermission;
/**
 * XWork action for SpaceDashboard, includes permission checking and basic value handover
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class SpaceDashboardAction extends ConfluenceActionSupport implements PageAware {

	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private static final String UNAUTHORIZED = "unauthorized";

	private AbstractPage thisPage;
	private UserSpacePermission usp;
	private String spaceName;
	private long spaceId;

	@Autowired
	public SpaceDashboardAction(@ComponentImport UserSpacePermission usp) {
		this.usp = usp;
	}

	/**
	 * Checks if user has permission to view space
	 */
	public String execute() {
		if (usp.checkSpacePermission(AuthenticatedUserThreadLocal.get(), thisPage.getSpace())) {
			setDashboardValues();
			return SUCCESS;
		} else {
			return UNAUTHORIZED;
		}
	}

	/**
	 * Sets values for velocity usage
	 */
	private void setDashboardValues() {
		spaceName = thisPage.getSpace().getName();
		spaceId = thisPage.getSpace().getId();

	}

	public long getSpaceId() {
		return spaceId;
	}

	@Override
	public boolean isViewPermissionRequired() {
		return true;
	}

	@Override
	public AbstractPage getPage() {
		return thisPage;
	}

	@Override
	public void setPage(AbstractPage page) {
		thisPage = page;
	}

	@Override
	public boolean isPageRequired() {
		return true;
	}

	@Override
	public boolean isLatestVersionRequired() {
		return true;
	}
	
	public String getSpaceName() {
		return spaceName;
	}
}
