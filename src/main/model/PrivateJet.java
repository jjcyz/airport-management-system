package model;

/* This class represents a private jet object, a subtype of aircraft */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

import java.util.ArrayList;

public class PrivateJet extends Aircraft {
    private Passenger owner;
    private final ArrayList<Passenger> listOfPassenger;
    private final int maxCapacity;

    public PrivateJet(String identifier, int maxCapacity) {
        super(identifier);
        this.maxCapacity = maxCapacity;
        this.listOfPassenger = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Added private jet aircraft: " + getIdentifier()));
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("identifier", this.identifier);
        json.put("maxCapacity", this.maxCapacity);
        return json;
    }
}
