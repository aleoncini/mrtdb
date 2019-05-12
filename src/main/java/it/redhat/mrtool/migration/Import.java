package it.redhat.mrtool.migration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.redhat.mrtool.model.Associate;
import it.redhat.mrtool.model.Location;
import it.redhat.mrtool.model.Trip;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Import {

    MongoClient mongoClient;
    MongoDatabase database;
    private boolean inited = false;
    private String host = "192.168.1.148";
    private String port = "27017";
    private String db = "mrtool";

    public static void main(String[] args) {
        Import tool = new Import().init();
        //System.out.println("======> migrating associates...");
        //tool.migrateAssociates();
        System.out.println("======> migrating locations...");
        tool.migrateLocations();
        System.out.println("======> migrating trips...");
        tool.migrateTrips();
        System.out.println("======> migration complete.");
        tool.shutdown();
    }

    private void shutdown() {
        this.mongoClient.close();
        this.database = null;
    }

    public Import init(){
        String connectionString = "mongodb://" + host + ":" + port + "/" + db;
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(db);
        return this;
    }

    private MongoCollection<Document> getCollection(String collection){
        return database.getCollection(collection);
    }

    public void migrateTrips(){
        List<Trip> trips = getTrips();
        for (Trip trip: trips) {
            System.out.println("======> migrating: " + trip.getPurpose());
            new Remote().setHost("mrtdb-mrtool.127.0.0.1.nip.io").setTrip(trip).insert();
        }
    }

    public void migrateLocations(){
        List<Location> locations = getLocations();
        for (Location location: locations) {
            System.out.println("======> migrating: " + location.getDestination());
            new Remote().setHost("mrtdb-mrtool.127.0.0.1.nip.io").setLocation(location).insert();
        }
    }

    public void migrateAssociates(){
        List<Associate> associates = getAssociates();
        for (Associate associate: associates) {
            System.out.println("======> migrating: " + associate.getName());
            new Remote().setHost("mrtdb-mrtool.127.0.0.1.nip.io").setAssociate(associate).insert();
        }
    }

    public List<Trip> getTrips(){
        List<Trip> trips = new ArrayList<Trip>();
        List<Document> docs = new ArrayList<Document>();
        this.getCollection("trips").find().into(docs);
        for (Document doc : docs) {
            trips.add(new Trip().build(doc));
        }
        return trips;
    }

    public List<Location> getLocations(){
        List<Location> locations = new ArrayList<Location>();
        List<Document> docs = new ArrayList<Document>();
        this.getCollection("locations").find().into(docs);
        for (Document doc : docs) {
            locations.add(new Location().build(doc));
        }
        return locations;
    }

    public List<Associate> getAssociates(){
        List<Associate> associates = new ArrayList<Associate>();
        List<Document> docs = new ArrayList<Document>();
        this.getCollection("associates").find().into(docs);
        for (Document doc : docs) {
            associates.add(new Associate().build(doc));
        }
        return associates;
    }

}
