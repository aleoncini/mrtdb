package it.redhat.mrtool.persistence.db;

import com.mongodb.client.result.DeleteResult;
import it.redhat.mrtool.model.Associate;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class AssociateHelper {
    public final static String COLLECTION_NAME = "associates";

    public Associate get(String rhid){
        Document query = new Document("rhid", rhid);
        Document document = DBTool.getInstance().getCollection(COLLECTION_NAME).find(query).first();
        return new Associate().build(document);
    }

    public void insertOrUpdate(String jsonString)  {
        this.insertOrUpdate(new Associate().build(jsonString));
    }

    public void insertOrUpdate(Associate associate){
        Associate associateFromDB = get(associate.getRedhatId());
        if (associateFromDB != null){
            Document query = new Document("rhid", associate.getRedhatId());
            DBTool.getInstance().getCollection(COLLECTION_NAME).updateOne(query, associate.toDocument());
        } else {
            DBTool.getInstance().getCollection(COLLECTION_NAME).insertOne(associate.toDocument());
        }
    }

    public long deleteByRedHatID(String rhid){
        Document query = new Document("rhid", rhid);
        DeleteResult deleteResult = DBTool.getInstance().getCollection(COLLECTION_NAME).deleteOne(query);
        return deleteResult.getDeletedCount();
    }

    public String getAssociateList(){
        boolean isFirst = true;
        Document projection = new Document("rhid", 1).append("name", 1);
        List<Document> associates = new ArrayList<Document>();
        DBTool.getInstance().getCollection(COLLECTION_NAME)
                .find()
                .projection(projection)
                .into(associates);
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append(" \"associates\": [ ");
        for (Document associate : associates) {
            if (isFirst){
                isFirst = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(getAssociateShortJsonString(associate));
        }
        buffer.append(" ] }");
        return buffer.toString();
    }

    public String getAssociateShortJsonString(Document document){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"rhid\": \"").append(document.getString("rhid")).append("\", ");
        buffer.append("\"name\": \"").append(document.getString("name")).append("\"");
        buffer.append(" }");
        return buffer.toString();
    }
}