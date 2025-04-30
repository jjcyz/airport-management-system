package model;

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;

/**
 * Represents a regional jet for short-haul routes
 */
public class RegionalJet extends Aircraft {
    private final int maxRange; // in kilometers
    private final boolean hasOverheadBins;
    private final int fuelEfficiency; // liters per 100 km

    public RegionalJet(String identifier, int maxCapacity) {
        super(identifier, maxCapacity);
        // Default values for regional jets
        this.maxRange = 2000; // 2000 km range
        this.hasOverheadBins = true; // Regional jets have overhead bins
        this.fuelEfficiency = 25; // 25 liters per 100 km
        EventLog.getInstance().logEvent(new Event("Added regional jet: " + getIdentifier()));
    }

    public int getMaxRange() {
        return maxRange;
    }

    public boolean hasOverheadBins() {
        return hasOverheadBins;
    }

    public int getFuelEfficiency() {
        return fuelEfficiency;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("maxRange", maxRange);
        json.put("hasOverheadBins", hasOverheadBins);
        json.put("fuelEfficiency", fuelEfficiency);
        return json;
    }

    @Override
    public String toString() {
        return String.format("Regional Jet - %s (Range: %d km, Overhead Bins: %s, Fuel: %d L/100km)",
            super.toString(), maxRange, hasOverheadBins ? "Yes" : "No", fuelEfficiency);
    }
}
