package it.redhat.mrtool.persistence.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DBTool {
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
        String host = System.getenv("MONGODB_SERVICE_HOST");
        String port = System.getenv("MONGODB_SERVICE_PORT");
        String user = System.getenv("MONGODB_database-user");
        String pass = System.getenv("MONGODB_database-password");
        String name = System.getenv("MONGODB_database-name");
        String connectionString = "mongodb://" + user + ":" + pass + "@" + host + ":" + port + "/" + name;
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(name);
    }

    public MongoCollection<Document> getCollection(String collection){
        return database.getCollection(collection);
    }

    public void shutdown(){
        database = null;
        mongoClient.close();
    }
}
