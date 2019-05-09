package it.redhat.mrtool.persistence.rest;

import it.redhat.mrtool.model.Associate;
import it.redhat.mrtool.persistence.db.AssociateHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/associates")
public class AssociateService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.status(200).entity(new AssociateHelper().getAssociateList()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response insert(String jsonString) {
        new AssociateHelper().insertOrUpdate(jsonString);
        return Response.status(200).entity("{\"result\":\"success\"}").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        AssociateHelper helper = new AssociateHelper();
        Associate associate = helper.get(id);
        if (associate == null){
            return Response.status(404).build();
        }
        return Response.status(200).entity(associate.toString()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("remove/{id}")
    public Response remove(@PathParam("id") String id) {
        AssociateHelper helper = new AssociateHelper();
        long result = helper.deleteByRedHatID(id);
        if (result <= 0){
            return Response.status(404).build();
        }
        return Response.status(200).entity("{\"result\":\"success\"}").build();
    }

}
