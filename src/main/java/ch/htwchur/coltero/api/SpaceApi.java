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

import ch.htwchur.coltero.dashboard.permission.UserSpacePermission;
import ch.htwchur.coltero.dashboard.spacedashboard.SpaceQueries;

/**
 * Rest Endpoint for Space Dashboard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@Path("/spc")
public class SpaceApi {

	private SpaceQueries spaceQueries;
	private UserSpacePermission usp;

	@Autowired
	public SpaceApi(@ComponentImport SpaceQueries spaceQueries, @ComponentImport UserSpacePermission usp) {
		this.spaceQueries = spaceQueries;
		this.usp = usp;
	}

	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response space(@PathParam("key") String key, @QueryParam("spaceid") int spaceId) throws SQLException {
		if (usp.checkSpacePermission(AuthenticatedUserThreadLocal.get(), spaceId)) {
			if (key.equalsIgnoreCase("QRY_AUTHORS_PAGES")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_AUTHORS_PAGES, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_AUTHORS_COMMENTS")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_AUTHORS_COMMENTS, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_COUNT_COMMENTS")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_COUNT_COMMENTS, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_COUNT_TAGS")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_COUNT_TAGS, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_COUNT_LIKES")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_COUNT_LIKES, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_COUNT_ATTACHMENTS")) {
				return Response.status(200)
						.entity(spaceQueries.executeBoxPlotQuerie(SpaceQueries.QRY_COUNT_ATTACHMENTS, spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_COUNT_NOTHING")) {
				return Response.status(200).entity(spaceQueries.querieNothingCountPerPageInSpace(spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_SUM_PGNCOM")) {
				return Response.status(200).entity(spaceQueries.querieSumPagesAndCommentsInSpace(spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_USR_LOCATION")) {
				return Response.status(200).entity(spaceQueries.querieUserPerLocationInSpace(spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_CLOUD")) {
				return Response.status(200).entity(spaceQueries.querieTagCloudInSpace(spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_NEIGHBOURS")) {
				return Response.status(200).entity(spaceQueries.querieTagNeighboursFromSpace(spaceId)).build();
			} else if (key.equalsIgnoreCase("QRY_CO_NETWORK")) {
				return Response.status(200).entity(spaceQueries.querieCoSpaceNetworkNew(spaceId)).build();
			}
		}
		return Response.status(405).build();
		// if key not defined
	}
}
