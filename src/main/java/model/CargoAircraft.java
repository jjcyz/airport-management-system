package model;

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

/**
 * Represents a cargo aircraft specialized for transporting goods
 */
public class CargoAircraft extends Aircraft {
    private final double maxCargoWeight; // in tons
    private final double cargoVolume; // in cubic meters

    public CargoAircraft(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        // Default values for cargo aircraft
        this.maxCargoWeight = maxCapacity * 0.5; // Assuming 0.5 tons per unit capacity
        this.cargoVolume = maxCapacity * 2.0; // Assuming 2 cubic meters per unit capacity
        EventLog.getInstance().logEvent(new Event("Added cargo aircraft: " + getIdentifier()));
    }

    public double getMaxCargoWeight() {
        return maxCargoWeight;
    }

    public double getCargoVolume() {
        return cargoVolume;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("maxCargoWeight", maxCargoWeight);
        json.put("cargoVolume", cargoVolume);
        return json;
    }

    @Override
    public String toString() {
        return String.format("Cargo Aircraft - %s (Max Weight: %.1f tons, Volume: %.1f m³)",
            super.toString(), maxCargoWeight, cargoVolume);
    }
}
