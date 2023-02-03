package model;

/* This class represents a cargo shipment. Each cargo has a description of what it is
 and the weight of the cargo in tons, 1 ton = 2205 lbs */

public class Cargo {
    private String description;
    private int weight;

    public Cargo(String description, int weight) {
        this.description = description;
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }


}
