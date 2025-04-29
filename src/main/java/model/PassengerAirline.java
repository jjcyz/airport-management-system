package model;

/* This class represents a passenger airline object, a subtype of aircraft */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

import java.util.ArrayList;

public class PassengerAirline extends Aircraft {
    private final ArrayList<Passenger> listOfPassenger;

    // Creates an aircraft for passengers
    public PassengerAirline(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        this.listOfPassenger = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Added passenger aircraft: " + getIdentifier()));
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        return json;
    }
}
