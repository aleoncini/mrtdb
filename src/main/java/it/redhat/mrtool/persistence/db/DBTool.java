package it.redhat.mrtool.persistence.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DBTool {
    public final static String DB_NAME = "mrtool";
    private static DBTool theInstance;
    MongoClient mongoClient;
    MongoDatabase database;

    public static DBTool getInstance(){
        if( theInstance == null ){
            theInstance = new DBTool();
        }
        return theInstance;
    }

    private DBTool(){
        mongoClient = MongoClients.create();
        database = mongoClient.getDatabase(DB_NAME);
    }

    public MongoCollection<Document> getCollection(String collection){
        return database.getCollection(collection);
    }

    public void shutdown(){
        database = null;
        mongoClient.close();
    }
}
