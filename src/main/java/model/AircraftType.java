package model;

/**
 * Enum representing different types of aircraft
 */
public enum AircraftType {
    PASSENGER_AIRLINE("Standard commercial passenger aircraft"),
    PRIVATE_JET("Luxury private jet for VIPs"),
    CARGO_AIRCRAFT("Dedicated cargo transport aircraft"),
    HELICOPTER("Rotary-wing aircraft for short-distance transport"),
    BUSINESS_JET("Mid-size jet for business travel"),
    REGIONAL_JET("Smaller aircraft for regional routes");

    private final String description;

    AircraftType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
