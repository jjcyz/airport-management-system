package model;

/* This class represents a private jet object, a subtype of aircraft */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

import java.util.ArrayList;

public class PrivateJet extends Aircraft {
    private Passenger owner;
    private final ArrayList<Passenger> listOfPassenger;

    public PrivateJet(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        this.listOfPassenger = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Added private jet aircraft: " + getIdentifier()));
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        if (owner != null) {
            json.put("owner", owner.toJson());
        }
        return json;
    }

    public void setOwner(Passenger owner) {
        this.owner = owner;
    }

    public Passenger getOwner() {
        return owner;
    }
}
