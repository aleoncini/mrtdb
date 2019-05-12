package it.redhat.mrtool.persistence.db;

import com.mongodb.client.result.DeleteResult;
import it.redhat.mrtool.model.Location;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LocationHelper {
    public final static String COLLECTION_NAME = "locations";

    public Location get(String destination){
        Document query = new Document("destination", destination);
        Document document = DBTool.getInstance().getCollection(COLLECTION_NAME).find(query).first();
        return new Location().build(document);
    }

    public void insertOrUpdate(Location location){
        Location locationFromDB = get(location.getDestination());
        if (locationFromDB != null){
            Document query = new Document("destination", location.getDestination());
            DBTool.getInstance().getCollection(COLLECTION_NAME).updateOne(query, location.toDocument());
        } else {
            DBTool.getInstance().getCollection(COLLECTION_NAME).insertOne(location.toDocument());
        }
    }

    public long delete(String jsonString){
        Location location = new Location().build(jsonString);
        DeleteResult deleteResult = DBTool.getInstance().getCollection(COLLECTION_NAME).deleteOne(location.toDocument());
        return deleteResult.getDeletedCount();
    }

    public List<Document> search(String dest){
        if ((dest == null) || (dest.length() == 0)){
            return this.search();
        }
        List<Document> locations = new ArrayList<Document>();
        Document filter = new Document()
                .append("destination", new Document("$regex", ".*" + dest + ".*").append("$options", "i"));
        DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find(filter)
                .into(locations);
        return locations;
    }

    public List<Document> search(){
        List<Document> locations = new ArrayList<Document>();
        List<Document> docs = DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find()
                .into(locations);
        return locations;
    }

    public String getLocations(String dest){
        List<Document> locations;
        boolean isFirst = true;

        if ((dest == null) || (dest.length() == 0)){
            locations = search();
        } else {
            locations = search(dest);
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append(" \"locations\": [ ");
        for (Document doc : locations) {
            if (isFirst){
                isFirst = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(doc.toJson());
        }
        buffer.append(" ] }");
        return buffer.toString();
    }
}