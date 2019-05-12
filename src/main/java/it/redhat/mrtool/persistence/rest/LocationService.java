package it.redhat.mrtool.persistence.rest;

import it.redhat.mrtool.model.Location;
import it.redhat.mrtool.persistence.db.LocationHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/locations")
public class LocationService {
    private static final Logger logger = Logger.getLogger("org.beccaria.domotics");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("dest") String dest) {
        return Response.status(200).entity(new LocationHelper().getLocations(dest)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response insert(String jsonString) {
        logger.info("[LocationService] requested insert for " + jsonString);
        new LocationHelper().insertOrUpdate(new Location().build(jsonString));
        return Response.status(200).entity("{\"result\":\"success\"}").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("remove")
    public Response remove(String jsonString) {
        LocationHelper helper = new LocationHelper();
        long result = new LocationHelper().delete(jsonString);
        if (result <= 0){
            return Response.status(404).build();
        }
        return Response.status(200).entity("{\"result\":\"success\"}").build();
    }

}
