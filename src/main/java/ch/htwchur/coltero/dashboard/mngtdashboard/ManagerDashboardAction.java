package ch.htwchur.coltero.dashboard.mngtdashboard;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.api.dto.counts.CountsDTO;
import ch.htwchur.coltero.dashboard.helper.GroupHelper;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.MngtCountQueries;

/**
 * Action Class for Management-Dashboard, this is the place for methods to
 * return variables to the velocity-engine and for basic action validation
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class ManagerDashboardAction extends ConfluenceActionSupport {

	private static final Logger log = LoggerFactory.getLogger(ManagerDashboardAction.class);

	private static final long serialVersionUID = 1L;
	private GroupHelper groupHelper;

	private static final String UNAUTHORIZED = "unauthorized";
	private static final String SUCCESS = "success";

	private MngtCountQueries mngtCountQueries;
	private static boolean startup = true;
	private int pageCount;
	private int commentCount;
	private int userCount;
	private int authorsCount;
	private int commentatorsCount;
	private int uploadersCount;
	private int slackersCount;
	private int taggersCount;
	private int likersCount;
	private String aggregationDate;

	@Autowired
	public ManagerDashboardAction(@ComponentImport MngtCountQueries mngtCountQueries) {
		groupHelper = new GroupHelper();
		this.mngtCountQueries = mngtCountQueries;
	}

	/**
	 * Basic validation for Velocity-template call and dataqueries
	 */
	@Override
	public String execute() throws Exception {
		if (AuthenticatedUserThreadLocal.get() == null) {
			return UNAUTHORIZED;
		}

		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (startup) {
				log.debug("Dashboard Startup: Aggregation Tables not existing, creating...");
				startup = false;
			}
			querieDashboardData();

			return SUCCESS;
		} else {
			return UNAUTHORIZED;
		}
	}

	/**
	 * Queries dashboardData and fills properties for template access can be limited
	 * by date
	 * @throws SQLException 
	 */
	private void querieDashboardData() throws SQLException {
			CountsDTO counts = mngtCountQueries.querieCountStatistics(null, null);
			pageCount = counts.getPageCount();
			commentCount = counts.getCommentCount();
			userCount = counts.getUserCount();
			commentatorsCount = counts.getCommentatorsCount();
			authorsCount = counts.getAuthorsCount();
			uploadersCount = counts.getUploadersCount();
			slackersCount = counts.getSlackersCount();
			taggersCount = counts.getTraggersCount();
			likersCount = counts.getLikersCount();
			aggregationDate = counts.getAggregationDate();
	}

	
	

	// Getter methods for velocity rendering

	public int getPageCount() {
		return pageCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public int getAuthorsCount() {
		return authorsCount;
	}

	public int getCommentatorsCount() {
		return commentatorsCount;
	}

	public int getUploadersCount() {
		return uploadersCount;
	}

	public int getSlackersCount() {
		return slackersCount;
	}

	public int getTaggersCount() {
		return taggersCount;
	}

	public int getLikersCount() {
		return likersCount;
	}

	public String getAggregationDate() {
		return aggregationDate;
	}

}
