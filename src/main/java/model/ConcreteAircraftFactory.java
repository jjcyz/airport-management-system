package model;

/**
 * Concrete implementation of the AircraftFactory interface
 */
public class ConcreteAircraftFactory implements AircraftFactory {
    @Override
    public Aircraft createAircraft(AircraftType type, String identifier, int maxCapacity) {
        switch (type) {
            case PASSENGER_AIRLINE:
                return new PassengerAirline(identifier, maxCapacity);
            case PRIVATE_JET:
                return new PrivateJet(identifier, maxCapacity);
            case CARGO_AIRCRAFT:
                return new CargoAircraft(identifier, maxCapacity);
            case HELICOPTER:
                return new Helicopter(identifier, maxCapacity);
            case BUSINESS_JET:
                return new BusinessJet(identifier, maxCapacity);
            case REGIONAL_JET:
                return new RegionalJet(identifier, maxCapacity);
            default:
                throw new IllegalArgumentException("Unsupported aircraft type: " + type);
        }
    }
}
