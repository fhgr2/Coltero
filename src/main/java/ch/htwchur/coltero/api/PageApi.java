package ch.htwchur.coltero.api;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import ch.htwchur.coltero.dashboard.pagedashboard.PageDashboardQueries;
import ch.htwchur.coltero.dashboard.permission.UserPagePermission;

/**
 * Rest Endpoint for Page Dashboard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@Path("/pages")
public class PageApi {

	private PageDashboardQueries pageDashboardQueries;
	private UserPagePermission userPagePermission;

	@Autowired
	public PageApi(@ComponentImport PageDashboardQueries pageDashboardQueries) {
		this.pageDashboardQueries = pageDashboardQueries;
		userPagePermission = new UserPagePermission();
	}

	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response page(@PathParam("key") String key, @QueryParam("pageid") int pageId) throws SQLException {
		if (userPagePermission.checkPagePermission(AuthenticatedUserThreadLocal.get(), pageId)) {
			if (key.equalsIgnoreCase("QRY_COMMENT_COUNT")) {
				return Response.status(200).entity(pageDashboardQueries.querieCommentCountPage(pageId)).build();
			} else if (key.equalsIgnoreCase("QRY_COMAUTHORS_COUNT")) {
				return Response.status(200).entity(pageDashboardQueries.querieAuthorsCommentatorsPage(pageId)).build();
			} else if (key.equalsIgnoreCase("QRY_REACTIONS")) {
				return Response.status(200).entity(pageDashboardQueries.querieReactionsPage(pageId)).build();
			} else if (key.equalsIgnoreCase("QRY_PAGE_BODY")) {
				return Response.status(200).entity(pageDashboardQueries.querieWordCountPage(pageId)).build();
			} else if (key.equalsIgnoreCase("QRY_USR_LOCATION")) {
				return Response.status(200).entity(pageDashboardQueries.querieUserPerLocationPage(pageId)).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_PAGES")) {
				return Response.status(200).entity(pageDashboardQueries.querieCoTagPageNetwork(pageId)).build();
			}
		}
		return Response.status(405).build();
	}
}
