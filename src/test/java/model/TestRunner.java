package model;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Running Flight Duration Tests...");

        // Test short flight (Vancouver to Toronto)
        Flight shortFlight = new Flight("AC123", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);
        shortFlight.setDestination(Airports.YYZ);
        System.out.println("Vancouver to Toronto: " + shortFlight.getDuration() + " hours");

        // Test long flight (New York to Hong Kong)
        Flight longFlight = new Flight("CX123", new Aircraft("Boeing777"), Airports.JFK, Airports.HKG, 0);
        longFlight.setDestination(Airports.HKG);
        System.out.println("New York to Hong Kong: " + longFlight.getDuration() + " hours");

        // Test same airport
        Flight sameAirport = new Flight("TEST123", new Aircraft("Boeing737"), Airports.YVR, Airports.YVR, 0);
        sameAirport.setDestination(Airports.YVR);
        System.out.println("Same airport: " + sameAirport.getDuration() + " hours");

        // Test transatlantic flight (London to New York)
        Flight transatlantic = new Flight("BA123", new Aircraft("Boeing787"), Airports.LHR, Airports.JFK, 0);
        transatlantic.setDestination(Airports.JFK);
        System.out.println("London to New York: " + transatlantic.getDuration() + " hours");

        // Test consistency
        Flight flight1 = new Flight("AC123", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);
        Flight flight2 = new Flight("AC456", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);
        flight1.setDestination(Airports.YYZ);
        flight2.setDestination(Airports.YYZ);
        System.out.println("Consistency check (should be equal): " +
                          flight1.getDuration() + " vs " + flight2.getDuration());
    }
}
