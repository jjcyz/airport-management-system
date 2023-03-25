package model;

/* This class represents a passenger airline object, a subtype of aircraft */

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

public class PassengerAirline extends Aircraft implements Writable {
    private final ArrayList<Passenger> listOfPassenger;

    // Creates an aircraft for passengers
    public PassengerAirline(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.listOfPassenger = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Added passenger aircraft: " + getName()));
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("maxCapacity", this.maxCapacity);

        JSONArray passengerArray = new JSONArray();
//        for (Passenger passenger : this.listOfPassenger) {
//            passengerArray.put(passenger.toJson());
//        }
//        json.put("listOfPassenger", passengerArray);

        return json;
    }


}
