package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightDurationTest {
    @Test
    void testShortFlightDuration() {
        // Test a short domestic flight (Vancouver to Toronto)
        Flight flight = new Flight("AC123", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);
        flight.setDestination(Airports.YYZ);

        // Expected duration: ~4.5 hours (actual flight time)
        // Using a range to account for slight variations in calculation
        assertTrue(flight.getDuration() >= 4 && flight.getDuration() <= 5,
                "Vancouver to Toronto flight duration should be approximately 4-5 hours");
    }

    @Test
    void testLongFlightDuration() {
        // Test a long international flight (New York to Hong Kong)
        Flight flight = new Flight("CX123", new Aircraft("Boeing777"), Airports.JFK, Airports.HKG, 0);
        flight.setDestination(Airports.HKG);

        // Expected duration: ~15-16 hours (actual flight time)
        assertTrue(flight.getDuration() >= 15 && flight.getDuration() <= 16,
                "New York to Hong Kong flight duration should be approximately 15-16 hours");
    }

    @Test
    void testSameAirportDuration() {
        // Test flight with same origin and destination
        Flight flight = new Flight("TEST123", new Aircraft("Boeing737"), Airports.YVR, Airports.YVR, 0);
        flight.setDestination(Airports.YVR);

        assertEquals(0, flight.getDuration(),
                "Flight duration should be 0 when origin and destination are the same");
    }

    @Test
    void testTransatlanticFlightDuration() {
        // Test transatlantic flight (London to New York)
        Flight flight = new Flight("BA123", new Aircraft("Boeing787"), Airports.LHR, Airports.JFK, 0);
        flight.setDestination(Airports.JFK);

        // Expected duration: ~7-8 hours (actual flight time)
        assertTrue(flight.getDuration() >= 7 && flight.getDuration() <= 8,
                "London to New York flight duration should be approximately 7-8 hours");
    }

    @Test
    void testDurationConsistency() {
        // Test that duration calculation is consistent
        Flight flight1 = new Flight("AC123", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);
        Flight flight2 = new Flight("AC456", new Aircraft("Boeing737"), Airports.YVR, Airports.YYZ, 0);

        flight1.setDestination(Airports.YYZ);
        flight2.setDestination(Airports.YYZ);

        assertEquals(flight1.getDuration(), flight2.getDuration(),
                "Same route should have consistent duration");
    }
}
