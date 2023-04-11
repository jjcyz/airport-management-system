package model;

/* This class represents a private jet object, a subtype of aircraft */

import org.json.JSONObject;

import java.util.ArrayList;

public class PrivateJet extends Aircraft {
    private final ArrayList<Passenger> listOfPassenger;
    private final int maxCapacity;

    public PrivateJet(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.maxCapacity = maxCapacity;
        this.listOfPassenger = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Added private jet aircraft: " + getName()));
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
