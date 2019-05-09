package it.redhat.mrtool.model;

import org.bson.Document;

public class Associate {
    private String name;
    private String email;
    private String costCenter;
    private String redhatId;
    private Car car;

    public String getName() {
        return name;
    }

    public Associate setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Associate setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public Associate setCostCenter(String costCenter) {
        this.costCenter = costCenter;
        return this;
    }

    public String getRedhatId() {
        return redhatId;
    }

    public Associate setRedhatId(String id) {
        this.redhatId = id;
        return this;
    }

    public Car getCar() {
        return car;
    }

    public Associate setCar(Car car) {
        this.car = car;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"rhid\": \"").append(redhatId).append("\"");
        if (name != null){
            buffer.append(", \"name\": \"").append(name).append("\"");
        }
        if (email != null){
            buffer.append(", \"email\": \"").append(email).append("\"");
        }
        if (costCenter != null){
            buffer.append(", \"costCenter\": \"").append(costCenter).append("\"");
        }
        if (car != null){
            buffer.append(", \"car\": ").append(car.toString());
        }
        buffer.append(" }");

        return buffer.toString();
    }

    public Document toDocument(){
        if ((redhatId == null) || (redhatId.length() == 0) || (name == null) || (name.length() == 0) || (costCenter == null) || (costCenter.length() == 0) || (car == null)){
            return null;
        }
        Document carDoc = new Car().toDocument();
        return new Document("rhid", redhatId)
                .append("name", name)
                .append("costCenter", costCenter)
                .append("email", email)
                .append("car", carDoc);
    }

    public Associate build(Document document){
        if (document == null){
            return null;
        }
        Document carDoc = (Document) document.get("car");
        Car car = new Car()
                .setRegistryNumber(carDoc.getString("registryNumber"))
                .setMileageRate(carDoc.getDouble("mileageRate"));

        this.redhatId = document.getString("rhid");
        this.name = document.getString("name");
        this.costCenter = document.getString("costCenter");
        this.email = document.getString("email");
        this.car = car;
        return this;
    }

    public Associate build(String jsonString){
        Document document = Document.parse(jsonString);
        return this.build(document);
    }

}
