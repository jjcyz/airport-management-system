package model;

/* This class represents a plane. A plane has a name and maximum capacity */

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

public class Aircraft implements Writable {

    protected final String name;
    protected final int maxCapacity;

    // Creates a new aircraft
    public Aircraft(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    // EFFECTS: returns the name of the plane
    public String getName() {
        return name;
    }

    // EFFECTS: returns the max capacity of the plane
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public String toString() {
        return "Name of Aircraft: "
                + name
                + " Maximum Capacity: "
                + maxCapacity;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("maxCapacity", this.maxCapacity);
        return json;
    }
}
