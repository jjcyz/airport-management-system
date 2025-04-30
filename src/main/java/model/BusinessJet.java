package model;

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

/**
 * Represents a business jet for corporate travel
 */
public class BusinessJet extends Aircraft {
    private final int maxRange; // in kilometers
    private final boolean hasConferenceRoom;
    private final boolean hasPrivateBedroom;

    public BusinessJet(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        // Default values for business jets
        this.maxRange = 6000; // 6000 km range
        this.hasConferenceRoom = true; // Business jets typically have conference rooms
        this.hasPrivateBedroom = true; // Business jets typically have private bedrooms
        EventLog.getInstance().logEvent(new Event("Added business jet: " + getIdentifier()));
    }

    public int getMaxRange() {
        return maxRange;
    }

    public boolean hasConferenceRoom() {
        return hasConferenceRoom;
    }

    public boolean hasPrivateBedroom() {
        return hasPrivateBedroom;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("maxRange", maxRange);
        json.put("hasConferenceRoom", hasConferenceRoom);
        json.put("hasPrivateBedroom", hasPrivateBedroom);
        return json;
    }

    @Override
    public String toString() {
        return String.format("Business Jet - %s (Range: %d km, Conference: %s, Bedroom: %s)",
            super.toString(), maxRange, hasConferenceRoom ? "Yes" : "No",
            hasPrivateBedroom ? "Yes" : "No");
    }
}
