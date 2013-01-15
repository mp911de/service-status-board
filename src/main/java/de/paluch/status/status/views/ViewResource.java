package de.paluch.status.status.views;

import de.paluch.status.status.control.UIEventsModelBuilder;
import de.paluch.status.status.control.UIOverviewModelBuilder;
import de.paluch.status.status.model.UIEventsModel;
import de.paluch.status.status.model.UIOverviewModel;
import org.apache.commons.dbcp.BasicDataSource;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 25.11.12 10:02
 */
@Path("/")
@Controller
public class ViewResource {

    @Autowired
    private UIOverviewModelBuilder uiOverviewModelBuilder;

    @Autowired
    private UIEventsModelBuilder uiEventsModelBuilder;

    @Autowired
    private BasicDataSource basicDataSource;


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/")
    public ModelAndView getIndexDefault2(@Context UriInfo uriInfo) {
        return getIndex(uriInfo);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("index.vm")
    public ModelAndView getIndex(@Context UriInfo uriInfo) {
        ModelAndView mav = new ModelAndView("index", "model", uiOverviewModelBuilder.createModel());
        mav.getModel().put("contextPath", uriInfo.getBaseUri().toString());
        return mav;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("index.json")
    public UIOverviewModel getIndexJson() {
        return uiOverviewModelBuilder.createModel();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("index.xml")
    public UIOverviewModel getIndexXml() {
        return getIndexJson();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("index.sql")
    public String postSQL(String sql) throws Exception {


        Connection conn = basicDataSource.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();

        return "OK";
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("status/{serviceKey}/{yyyy}/{mm}/{dd}")
    public ModelAndView getService(@PathParam("serviceKey") String serviceKey, @PathParam("yyyy") int yyyy,
                                   @PathParam("mm") int mm, @PathParam("dd") int dd, @Context UriInfo uriInfo) {


        LocalDateTime ldt = new LocalDateTime(yyyy, mm, dd, 0, 0, 0);
        ModelAndView mav = new ModelAndView("status", "model", uiEventsModelBuilder.createModel(serviceKey, ldt.toDate()));
        mav.getModel().put("contextPath", uriInfo.getBaseUri().toString());
        return mav;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("status/{serviceKey}/{yyyy}/{mm}/{dd}.json")
    public UIEventsModel getServiceJSON(@PathParam("serviceKey") String serviceKey, @PathParam("yyyy") int yyyy,
                                        @PathParam("mm") int mm, @PathParam("dd") int dd) {

        LocalDateTime ldt = new LocalDateTime(yyyy, mm, dd, 0, 0, 0);
        return uiEventsModelBuilder.createModel(serviceKey, ldt.toDate());
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("status/{serviceKey}/{yyyy}/{mm}/{dd}.xml")
    public UIEventsModel getServiceXML(@PathParam("serviceKey") String serviceKey, @PathParam("yyyy") int yyyy,
                                       @PathParam("mm") int mm, @PathParam("dd") int dd) {

        LocalDateTime ldt = new LocalDateTime(yyyy, mm, dd, 0, 0, 0);
        return uiEventsModelBuilder.createModel(serviceKey, ldt.toDate());
    }
}
