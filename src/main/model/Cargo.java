package model;

/* This class represents a cargo shipment. Each cargo has a description of what it is
 and the weight of the cargo in tons, 1 ton = 2205 lbs */

import org.json.JSONObject;
import persistence.Writable;

public class Cargo implements Writable {
    private final String description;
    private final int weight;

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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("description", this.description);
        json.put("weight", this.weight);
        return json;
    }
}
