package ch.htwchur.coltero.dashboard.userdashboard;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
/**
 * action class for user dashbaord
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class UsrDashboardAction extends ConfluenceActionSupport  {

	private static final String UNAUTHORIZED = "unauthorized";
	private static final String SUCCESS = "success";
	private static final long serialVersionUID = 1L;
	private String username;
	private String userid;

	public String execute() {
		// check if user is logged in
		if (AuthenticatedUserThreadLocal.get() != null) {
			setDashboardValues();
			return SUCCESS;
		}
		return UNAUTHORIZED;
	}

	private void setDashboardValues() {
		username = AuthenticatedUserThreadLocal.get().getFullName();
		userid = AuthenticatedUserThreadLocal.get().getKey().getStringValue();
	}
	
	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

}
