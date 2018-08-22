package ch.htwchur.coltero.api;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import ch.htwchur.coltero.dashboard.helper.GroupHelper;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.InteractionQueries;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.MngtCountQueries;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.SpaceQueries;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.TagQueries;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.UserQueries;

/**
 * This class provides a RestApi for quering and putting Dashboard
 * querieparameters and querie-responses
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@Path("/")
public class MngtApi {
	// private static final Logger log =
	// LoggerFactory.getLogger(ManagerDashboardAction.class);
	private GroupHelper groupHelper;

	private MngtCountQueries mngtCountQueries;
	private InteractionQueries interactionQueries;
	private SpaceQueries spaceQueries;
	private UserQueries userQueries;
	private TagQueries tagQueries;
	static final DateFormat df = new SimpleDateFormat("yyyy-MM");

	@Autowired
	public MngtApi(@ComponentImport MngtCountQueries mngtCountQueries,
			@ComponentImport InteractionQueries interactionQueries, @ComponentImport SpaceQueries spaceQueries,
			@ComponentImport UserQueries userQueries, @ComponentImport TagQueries tagQueries) {
		this.mngtCountQueries = mngtCountQueries;
		this.interactionQueries = interactionQueries;
		this.spaceQueries = spaceQueries;
		this.userQueries = userQueries;
		this.tagQueries = tagQueries;
		groupHelper = new GroupHelper();
	}

	@GET
	@Path("/spaces/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response spacesApi(@PathParam("key") String key) throws SQLException, ParseException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_SPC_CREATION")) {
				return Response.status(200).entity(spaceQueries.querieSpaceCreationsOverTime()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_TYPES")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTypesOverTime()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_PROJECT")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("projects")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_DEVELOPMENT")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("development")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_COLLABORATION")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("collaboration")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_DEPARTMENT")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("department")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_DOCUMENTATION")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("documentation")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_KNOWLEDGE-BASE")) {
				return Response.status(200).entity(spaceQueries.querieSpaceTagsOverTime("knowledge-base")).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_OTHERPSACES")) {
				return Response.status(200).entity(spaceQueries.querieAllOtherSpaceTagsOverTime()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_TOPUSERCOUNT")) {
				return Response.status(200).entity(spaceQueries.querieTop10SpacesUserBased()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_TOPAUTHCOMUPL")) {
				return Response.status(200).entity(spaceQueries.querieTop10SpacesAuthCommAttBased()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_TOPCOLLABORATIVE")) {
				return Response.status(200).entity(spaceQueries.querieTop10SpacesCollaborative()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_TOPCOMMENTED")) {
				return Response.status(200).entity(spaceQueries.querieTop10SpacesCommented()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_WORDCOUNT")) {
				return Response.status(200).entity(spaceQueries.querieSpacePageBodyContent()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_COMMENTCOUNT")) {
				return Response.status(200).entity(spaceQueries.querieSpaceCommentBodyContent()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_ISOLATED")) {
				return Response.status(200).entity(spaceQueries.querieIsolatedSpaceCount()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_NETWORK")) {
				return Response.status(200).entity(spaceQueries.querieAndCalculateSpaceNetwork()).build();
			} else if (key.equalsIgnoreCase("QRY_SPC_SHAREDLOCATION")) {
				return Response.status(200).entity(spaceQueries.querieSharedLocatedSpaces()).build();
			}

		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();

	}

	@GET
	@Path("/users/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response usersApi(@PathParam("key") String key) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_USR_LOCATION")) {
				return Response.status(200).entity(userQueries.querieUserPerLocation()).build();
			} else if (key.equalsIgnoreCase("QRY_USR_INACTIVE")) {
				return Response.status(200).entity(userQueries.querieActiveAndInactiveUsers()).build();
			} else if (key.equalsIgnoreCase("QRY_USR_ACTIVEPERLOCATION")) {
				return Response.status(200).entity(userQueries.querieActiveUsersPerLocation()).build();
			} else if (key.equalsIgnoreCase("QRY_USR_INTERACTIONS")) {
				return Response.status(200).entity(userQueries.querieUserInteractions()).build();
			}
		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();
	}

	@GET
	@Path("/interactions/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response interactionsApi(@PathParam("key") String key) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_INT_DOW")) {
				return Response.status(200).entity(interactionQueries.querieInteractionsPerDOW()).build();
			} else if (key.equalsIgnoreCase("QRY_INT_HOURS")) {
				return Response.status(200).entity(interactionQueries.querieInteractionsPerHour()).build();
			} else if (key.equalsIgnoreCase("QRY_GET_WORLDDATA")) {
				return Response.status(200).entity(interactionQueries.querieLocationDataForWoldmap()).build();
			}
		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();
	}

	@GET
	@Path("/boxplots/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response boxPlotApi(@PathParam("key") String key, @QueryParam("fromdate") long fromDate,
			@QueryParam("todate") long toDate) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_BOX_AUTHORS")) {
				return Response.status(200)
						.entity(mngtCountQueries.querieBoxPlotRolledUpStringIntString(MngtCountQueries.QRY_BOX_AUTHORS))
						.build();
			} else if (key.equalsIgnoreCase("QRY_BOX_COMMENTS")) {
				return Response.status(200)
						.entity(mngtCountQueries.querieBoxPlotRolledUpIntIntString(MngtCountQueries.QRY_BOX_COMMENTS))
						.build();
			} else if (key.equalsIgnoreCase("QRY_BOX_COMMENTATORS")) {
				return Response.status(200).entity(
						mngtCountQueries.querieBoxPlotRolledUpStringIntString(MngtCountQueries.QRY_BOX_COMMENTATORS))
						.build();
			} else if (key.equalsIgnoreCase("QRY_BOX_LIKES")) {
				return Response.status(200)
						.entity(mngtCountQueries.querieBoxPlotRolledUpIntIntString(MngtCountQueries.QRY_BOX_LIKES))
						.build();
			} else if (key.equalsIgnoreCase("QRY_BOX_TAGS")) {
				return Response.status(200)
						.entity(mngtCountQueries.querieBoxPlotRolledUpIntIntString(MngtCountQueries.QRY_BOX_TAGS))
						.build();
			} else if (key.equalsIgnoreCase("QRY_BOX_ATTACHMENTS")) {
				return Response.status(200).entity(
						mngtCountQueries.querieBoxPlotRolledUpIntIntString(MngtCountQueries.QRY_BOX_ATTACHMENTS))
						.build();
			}
		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();
	}

	@GET
	@Path("/counts/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response simpleColteroData(@PathParam("key") String key, @QueryParam("fromdate") long fromDate,
			@QueryParam("todate") long toDate) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_COUNTS")) {
				return Response.status(200)
						.entity(mngtCountQueries.querieCountStatistics(fromDate == 0 ? null : new Date(fromDate),
								toDate == 0 ? null : new Date(toDate)))
						.build();
			}
		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();
	}

	@GET
	@Path("tags/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response tagsApi(@PathParam("key") String key) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_TAG_OT")) {
				return Response.status(200).entity(tagQueries.querieTagsOT()).build();
			} else if (key.equalsIgnoreCase("QRY_ATT_RATIO")) {
				return Response.status(200).entity(tagQueries.querieTagAttRatio()).build();
			} else if (key.equalsIgnoreCase("QRY_TOP_TAGS")) {
				return Response.status(200).entity(tagQueries.querieTopTags()).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_NETWORK")) {
				return Response.status(200).entity(tagQueries.querieAndCalculateTagNetwork()).build();
			}
		} else {
			return Response.status(403).build();
		}
		// if key not defined
		return Response.status(405).build();
	}

	@GET
	@Path("collaboration/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response collaborationApi(@PathParam("key") String key) throws SQLException {
		if (groupHelper.checkUserInGroup(AuthenticatedUserThreadLocal.get().getKey())) {
			if (key.equalsIgnoreCase("QRY_SPC_COLLABVSDOCUMEN")) {
				return Response.status(200).entity(spaceQueries.querieCollaborationIndication()).build();
			}
		} else {
			return Response.status(403).build();
		}
		return Response.status(405).build();
	}
}
