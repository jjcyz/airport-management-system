package model;

public class TestRunner {
    public static void main(String[] args) {
        AircraftFactory factory = new ConcreteAircraftFactory();

        // Create aircraft once
        Aircraft boeing737 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing737", 150);
        Aircraft boeing777 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing777", 300);
        Aircraft boeing787 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing787", 250);

        // Test short flight (Vancouver to Toronto)
        Flight shortFlight = new Flight("AC123", boeing737, Airports.YVR, Airports.YYZ, 0);
        shortFlight.setDestination(Airports.YYZ);
        System.out.println("Short flight duration: " + shortFlight.getDuration() + " hours");

        // Test long flight (New York to Hong Kong)
        Flight longFlight = new Flight("CX123", boeing777, Airports.JFK, Airports.HKG, 0);
        longFlight.setDestination(Airports.HKG);
        System.out.println("Long flight duration: " + longFlight.getDuration() + " hours");

        // Test same airport
        Flight sameAirport = new Flight("TEST123", boeing737, Airports.YVR, Airports.YVR, 0);
        sameAirport.setDestination(Airports.YVR);
        System.out.println("Same airport duration: " + sameAirport.getDuration() + " hours");

        // Test transatlantic flight (London to New York)
        Flight transatlantic = new Flight("BA123", boeing787, Airports.LHR, Airports.JFK, 0);
        transatlantic.setDestination(Airports.JFK);
        System.out.println("Transatlantic flight duration: " + transatlantic.getDuration() + " hours");

        // Test multiple flights same route
        Flight flight1 = new Flight("AC123", boeing737, Airports.YVR, Airports.YYZ, 0);
        Flight flight2 = new Flight("AC456", boeing737, Airports.YVR, Airports.YYZ, 0);

        flight1.setDestination(Airports.YYZ);
        flight2.setDestination(Airports.YYZ);

        System.out.println("Flight 1 duration: " + flight1.getDuration() + " hours");
        System.out.println("Flight 2 duration: " + flight2.getDuration() + " hours");
        System.out.println("Durations match: " + (flight1.getDuration() == flight2.getDuration()));
    }
}
