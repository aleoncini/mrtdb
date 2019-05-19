package it.redhat.mrtool.persistence.db;

import com.mongodb.client.result.DeleteResult;
import it.redhat.mrtool.model.DateOfTrip;
import it.redhat.mrtool.model.Location;
import it.redhat.mrtool.model.Trip;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class TripHelper {
    public final static String COLLECTION_NAME = "trips";

    public String getJsonTrips(String associateId, int year, int month){
        List<Document> docs = DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find(and(eq("associateId", associateId), eq("date.year", year), eq("date.month", month)))
                .into(new ArrayList<Document>());
        int dist = calculateDistanceOfTheYear(associateId, year);
        return listToJson(docs, dist);
    }

    public List<Trip> getTrips(String associateId, int year, int month){
        List<Document> docs = DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find(and(eq("associateId", associateId), eq("date.year", year), eq("date.month", month)))
                .into(new ArrayList<Document>());
        return documentsToTrips(docs);
    }

    public List<Trip> getTripsOfTheYear(String associateId, int year){
        List<Document> docs = DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find(and(eq("associateId", associateId), eq("date.year", year)))
                .into(new ArrayList<Document>());
        return documentsToTrips(docs);
    }

    public List<Trip> documentsToTrips(List<Document> docs){
        List<Trip> trips = new ArrayList<Trip>();
        for (Document doc: docs) {
            Document locationDoc = (Document) doc.get("location");
            Document dateDoc = (Document) doc.get("date");
            Location location = new Location()
                    .setDestination(locationDoc.getString("destination"))
                    .setDistance(locationDoc.getInteger("distance"));
            DateOfTrip date = new DateOfTrip()
                    .setYear(dateDoc.getInteger("year"))
                    .setMonth(dateDoc.getInteger("month"))
                    .setDay(dateDoc.getInteger("day"));
            Trip trip = new Trip()
                    .setAssociateId(doc.getString("associateId"))
                    .setPurpose(doc.getString("purpose"))
                    .setLocation(location)
                    .setDate(date);
            trips.add(trip);
        }
        return trips;
    }

    private String listToJson(List<Document> trips, int distance){
        boolean isFirst = true;

        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append(" \"totalDistance\": ").append(distance).append(",");
        buffer.append(" \"trips\": [ ");

        for (Document trip:trips) {
            if (isFirst){
                isFirst = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(trip.toJson());
        }

        buffer.append(" ] }");
        return buffer.toString();
    }

    public void insertOrUpdate(Trip trip){
        DBTool.getInstance().getCollection(COLLECTION_NAME).insertOne(trip.toDocument());
    }

    public long delete(String associateId, DateOfTrip date){
        Document query = new Document("associateId", associateId).append("date", date.toDocument());
        DeleteResult deleteResult = DBTool.getInstance().getCollection(COLLECTION_NAME).deleteMany(query);
        return deleteResult.getDeletedCount();
    }

    public int distance(List<Trip> trips){
        int distance = 0;
        for (Trip trip : trips) {
            distance += trip.getLocation().getDistance();
        }
        return distance;
    }

    public int getTotalMonthDistance(String associateId, int year, int month){
        return distance(getTrips(associateId, year, month));
    }

    public int getTotalYearDistance(String associateId, int year){
        return distance(getTripsOfTheYear(associateId, year));
    }

    private int calculateDistanceOfTheYear(String associateId, int year){
        List<Document> docs = DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find(and(eq("associateId", associateId), eq("date.year", year)))
                .projection(fields(include("distance"), excludeId()))
                .into(new ArrayList<Document>());
        int total = 0;
        for (Document doc : docs) {
            total += doc.getInteger("distance");
        }
        return total;
    }

}