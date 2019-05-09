package it.redhat.mrtool.model;

import org.bson.Document;

public class DateOfTrip {
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public DateOfTrip setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public DateOfTrip setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public DateOfTrip setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"day\": ").append(day).append(", ");
        buffer.append("\"month\": ").append(month).append(", ");
        buffer.append("\"year\": ").append(year);
        buffer.append(" }");
        return buffer.toString();
    }

    public Document toDocument(){
        return new Document("day", this.day)
                .append("month", this.month)
                .append("year", this.year);
    }

    public DateOfTrip build(Document document){
        this.day = document.getInteger("day");
        this.month = document.getInteger("month");
        this.year = document.getInteger("year");
        return this;
    }

    public DateOfTrip build(String jsonString){
        Document document = Document.parse(jsonString);
        return this.build(document);
    }
}