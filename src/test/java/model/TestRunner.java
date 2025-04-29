package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestRunner {
    private AircraftFactory factory;
    private Aircraft boeing737;
    private Aircraft boeing777;
    private Aircraft boeing787;

    @BeforeEach
    void setUp() {
        factory = new ConcreteAircraftFactory();
        boeing737 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing737", 150);
        boeing777 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing777", 300);
        boeing787 = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing787", 250);
    }

    @Test
    void testShortFlightDuration() {
        Flight flight = createFlight("AC123", boeing737, Airports.YVR, Airports.YYZ);
        assertTrue(flight.getDuration() >= 4 && flight.getDuration() <= 5,
                "Vancouver to Toronto flight duration should be approximately 4-5 hours");
    }

    @Test
    void testLongFlightDuration() {
        Flight flight = createFlight("CX123", boeing777, Airports.JFK, Airports.HKG);
        assertTrue(flight.getDuration() >= 15 && flight.getDuration() <= 16,
                "New York to Hong Kong flight duration should be approximately 15-16 hours");
    }

    @Test
    void testSameAirportDuration() {
        Flight flight = createFlight("TEST123", boeing737, Airports.YVR, Airports.YVR);
        assertEquals(0, flight.getDuration(),
                "Flight duration should be 0 when origin and destination are the same");
    }

    @Test
    void testTransatlanticFlightDuration() {
        Flight flight = createFlight("BA123", boeing787, Airports.LHR, Airports.JFK);
        assertTrue(flight.getDuration() >= 7 && flight.getDuration() <= 8,
                "London to New York flight duration should be approximately 7-8 hours");
    }

    @Test
    void testMultipleFlightsSameRoute() {
        Flight flight1 = createFlight("AC123", boeing737, Airports.YVR, Airports.YYZ);
        Flight flight2 = createFlight("AC456", boeing737, Airports.YVR, Airports.YYZ);

        assertEquals(flight1.getDuration(), flight2.getDuration(),
                "Same route should have consistent duration");
    }

    private Flight createFlight(String flightNumber, Aircraft aircraft, Airports origin, Airports destination) {
        Flight flight = new Flight(flightNumber, aircraft, origin, destination, 0);
        flight.setDestination(destination);
        return flight;
    }
}
