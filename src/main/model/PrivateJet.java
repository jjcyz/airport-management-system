package model;
/* This class represents a private jet object, a subtype of aircraft */

import org.json.JSONObject;
import persistence.Writable;

public class PrivateJet extends Aircraft implements Writable {
    private int maxCapacity;

    public PrivateJet(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("maxCapacity", this.maxCapacity);
        return json;
    }
}
