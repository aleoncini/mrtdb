package it.redhat.mrtool.migration;

import it.redhat.mrtool.model.Associate;
import it.redhat.mrtool.model.Location;
import it.redhat.mrtool.model.Trip;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

public class Remote {

    private String host;
    private String port;
    private Associate associate;
    private Location location;
    private Trip trip;

    public Remote setHost(String host) {
        this.host = host;
        return this;
    }

    public Remote setPort(String port) {
        this.port = port;
        return this;
    }

    public Remote setAssociate(Associate associate) {
        this.associate = associate;
        return this;
    }

    public Remote setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Remote setTrip(Trip trip) {
        this.trip = trip;
        return this;
    }

    public void insert() {
        String jsonString = "";

        StringBuffer url = new StringBuffer("http://" + host);
        if (port != null) {
            url.append(":" + port);
        }
        url.append("/rs/");

        if (associate != null){
            System.out.println("=====> inserting Associate " + associate.getName());
            url.append("associates/add");
            jsonString = associate.toString();
        }
        if (location != null){
            System.out.println("=====> inserting Location " + location.getDestination());
            url.append("locations/add");
            jsonString = location.toString();
        }
        if (trip != null){
            System.out.println("=====> inserting Trip " + trip.getPurpose());
            url.append("trips/add");
            jsonString = trip.toString();
        }

        Response response = null;
        System.out.println("=====> invoking: " + url.toString());

        try {
            Client client = new ResteasyClientBuilder()
                    .establishConnectionTimeout(5, TimeUnit.SECONDS)
                    .socketTimeout(5, TimeUnit.SECONDS)
                    .build();
            ResteasyWebTarget target = (ResteasyWebTarget) client.target(url.toString());
            response = target.request().post(
                    Entity.entity(jsonString, "application/json"));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("=====> entity saved on remote service.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();  // You should close connections!
            }
        }
    }
}
