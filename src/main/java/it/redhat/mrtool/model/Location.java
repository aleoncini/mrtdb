package it.redhat.mrtool.model;

import org.bson.Document;

public class Location {
    private String destination;
    private int distance;

    public String getDestination() {
        return destination;
    }

    public Location setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public Location setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"destination\": \"").append(destination).append("\", ");
        buffer.append("\"distance\": ").append(distance);
        buffer.append(" }");
        return buffer.toString();
    }

    public Document toDocument(){
        if ((destination == null) || (destination.length() == 0)){
            return null;
        }
        return new Document("destination", destination)
                .append("distance", distance);
    }

    public Location build(Document document){
        if (document == null){
            return null;
        }
        this.destination = document.getString("destination");
        this.distance = document.getInteger("distance");
        return this;
    }

    public Location build(String jsonString){
        Document document = Document.parse(jsonString);
        return this.build(document);
    }

}
