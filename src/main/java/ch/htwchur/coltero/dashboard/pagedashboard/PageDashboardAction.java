package ch.htwchur.coltero.dashboard.pagedashboard;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.actions.PageAware;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;

import ch.htwchur.coltero.dashboard.permission.UserPagePermission;
/**
 * Page action base velocity template
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class PageDashboardAction extends ConfluenceActionSupport implements PageAware {

	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private static final String UNAUTHORIZED = "unauthorized";

	private String pageName;
	private long pageId;
	private AbstractPage page;
	private UserPagePermission userPagePermission = new UserPagePermission();

	public String execute() {
		if(userPagePermission.checkPagePermission(AuthenticatedUserThreadLocal.get(), page)) {
		setDashboardValues();
		return SUCCESS;
		}
		return UNAUTHORIZED;
	}

	public void setDashboardValues() {
		pageName = page.getDisplayTitle();
		pageId = page.getId();
	}

	
	public long getPageId() {
		return pageId;
	}

	@Override
	public AbstractPage getPage() {
		return page;
	}

	@Override
	public void setPage(AbstractPage page) {
		this.page = page;

	}

	@Override
	public boolean isPageRequired() {
		return true;
	}

	@Override
	public boolean isLatestVersionRequired() {
		return true;
	}

	@Override
	public boolean isViewPermissionRequired() {
		return true;
	}

	public String getPageName() {
		return pageName;
	}

}
