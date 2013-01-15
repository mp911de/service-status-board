package de.paluch.status.status.jenkins;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 26.11.12 08:36
 */
public interface JenkinsAPI {

    @GET
    @Path("/api/xml")
    @Produces(MediaType.APPLICATION_XML)
    JenkinsProject getProject();

    @GET
    @Path("/{buildNumber}/api/xml")
    @Produces(MediaType.APPLICATION_XML)
    JenkinsBuild getBuild(@PathParam("buildNumber") int buildNumber);

}
