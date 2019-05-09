package it.redhat.mrtool.persistence.rest;

import it.redhat.mrtool.model.Associate;
import it.redhat.mrtool.model.Car;
import it.redhat.mrtool.persistence.db.AssociateHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/testdb")
public class TestDBService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {
        return Response.status(200).entity("DB Test Service. try '/testdb/connect' to test Mongo DB Connection.").build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("connect")
    public Response testConnection() {
        Car car = new Car().setRegistryNumber("FB214ZM").setMileageRate(0.89);
        Associate associate = new Associate()
                .setCar(car)
                .setRedhatId("9999")
                .setName("Paolino Paperino")
                .setEmail("paolino.paperino@acme.com")
                .setCostCenter("520");
        new AssociateHelper().insertOrUpdate(associate);
        return Response.status(200).entity("DB Test Service. Test OK!").build();
    }

}
