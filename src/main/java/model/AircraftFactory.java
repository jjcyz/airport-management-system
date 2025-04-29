package model;

/**
 * Factory interface for creating different types of aircraft
 */
public interface AircraftFactory {
    /**
     * Creates a new aircraft of the specified type
     * @param type The type of aircraft to create
     * @param identifier The unique identifier for the aircraft
     * @param maxCapacity The maximum capacity of the aircraft
     * @return A new aircraft instance
     * @throws IllegalArgumentException if the aircraft type is not supported
     */
    Aircraft createAircraft(AircraftType type, String identifier, int maxCapacity);
}
