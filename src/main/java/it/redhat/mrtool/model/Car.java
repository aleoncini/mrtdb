package it.redhat.mrtool.model;

import org.bson.Document;

public class Car {
    private String registryNumber;
    private double mileageRate;

    public String getRegistryNumber() {
        return registryNumber;
    }

    public Car setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
        return this;
    }

    public double getMileageRate() {
        return mileageRate;
    }

    public Car setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"registryNumber\":\"").append(registryNumber).append("\", ");
        buffer.append("\"mileageRate\": ").append(mileageRate);
        buffer.append(" }");
        return buffer.toString();
    }

    public Document toDocument(){
        if ((registryNumber == null) || (registryNumber.length() == 0)){
            return null;
        }
        return new Document("registryNumber", registryNumber)
                .append("mileageRate", mileageRate);
    }

    public Car build(Document document){
        if (document == null){
            return null;
        }
        this.registryNumber = document.getString("registryNumber");
        this.mileageRate = document.getDouble("mileageRate");
        return this;
    }

    public Car build(String jsonString){
        Document document = Document.parse(jsonString);
        return this.build(document);
    }

}
