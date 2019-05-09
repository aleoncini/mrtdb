package it.redhat.mrtool.persistence.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/testdb")
public class TestDBService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response list() {
        return Response.status(200).entity("DB Test Service. try '/testdb/connect' to test Mongo DB Connection.").build();
    }

}
