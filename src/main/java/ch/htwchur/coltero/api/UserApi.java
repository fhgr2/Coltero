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

import ch.htwchur.coltero.dashboard.userdashboard.UserQueries;

/**
 * Rest Endpoint for User-Dashboard
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
@Path("usr")
public class UserApi {

	private UserQueries userQueries;

	@Autowired
	public UserApi(@ComponentImport UserQueries userQueries) {
		this.userQueries = userQueries;
	}

	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response user(@PathParam("key") String key, @QueryParam("userid") String userid) throws SQLException {
		if (AuthenticatedUserThreadLocal.get().getKey().getStringValue().equals(userid)) {
			if (key.equalsIgnoreCase("QRY_USR_CONNECTIONS")) {
				return Response.status(200).entity(userQueries.querieUserConnections(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_USR_NETWORK")) {
				return Response.status(200).entity(userQueries.querieUserConnectionsNetwork(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_COM_LOCATIONS")) {
				return Response.status(200).entity(userQueries.querieCommtoresPerLocation(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_CONTENT_RANGE")) {
				return Response.status(200).entity(userQueries.querieContentRange(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_REACTIONS")) {
				return Response.status(200).entity(userQueries.querieUserReactions(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_CLOUD")) {
				return Response.status(200).entity(userQueries.querieTagCloud(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_TAG_NETWORK")) {
				return Response.status(200).entity(userQueries.querieCoTagNetwork(userid)).build();
			} else if (key.equalsIgnoreCase("QRY_USR_STATAVG")) {
				return Response.status(200).entity(userQueries.querieUserStatistics(userid)).build();
			}
		}
		return Response.status(405).build();

	}
}
