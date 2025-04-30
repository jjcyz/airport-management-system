package model;

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

/**
 * Represents a helicopter for short-distance transport
 */
public class Helicopter extends Aircraft {
    private final int maxRange; // in kilometers
    private final int maxAltitude; // in meters
    private boolean hasMedicalEquipment;

    public Helicopter(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        // Default values for helicopters
        this.maxRange = 500; // 500 km range
        this.maxAltitude = 3000; // 3000 meters
        this.hasMedicalEquipment = false; // Default to no medical equipment
        EventLog.getInstance().logEvent(new Event("Added helicopter: " + getIdentifier()));
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public boolean hasMedicalEquipment() {
        return hasMedicalEquipment;
    }

    public void setMedicalEquipment(boolean hasMedicalEquipment) {
        this.hasMedicalEquipment = hasMedicalEquipment;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("maxRange", maxRange);
        json.put("maxAltitude", maxAltitude);
        json.put("hasMedicalEquipment", hasMedicalEquipment);
        return json;
    }

    @Override
    public String toString() {
        return String.format("Helicopter - %s (Range: %d km, Altitude: %d m, Medical: %s)",
            super.toString(), maxRange, maxAltitude, hasMedicalEquipment ? "Yes" : "No");
    }
}
