package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
    private Flight flight;
    private Aircraft aircraft;
    private Passenger passenger1;
    private Passenger passenger2;
    private AircraftFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ConcreteAircraftFactory();
        aircraft = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Boeing737", 150);
        flight = new Flight("AC123", aircraft, Airports.YVR, Airports.YYZ, 0);
        passenger1 = new Passenger(1, "John", "Doe");
        passenger2 = new Passenger(2, "Jane", "Smith");
    }

    @Test
    void testConstructor() {
        assertEquals("AC123", flight.getFlightID());
        assertEquals(aircraft, flight.getAircraft());
        assertEquals(Airports.YVR, flight.getOrigin());
        assertEquals(Airports.YYZ, flight.getDestination());
        assertEquals(0, flight.getDuration());
        assertTrue(flight.getPassengersOnFlight().isEmpty());
    }

    @Test
    void testAddPassengerSuccess() {
        String result = flight.addPassenger(passenger1, "A1");
        assertEquals("John Doe has been added to this flight in seat A1", result);
        assertEquals(1, flight.getCurrentCapacity());
        assertTrue(flight.isPassengerOnFlight(passenger1.getPassengerID()));
        assertTrue(passenger1.getBookedFlights().contains(flight));
    }

    @Test
    void testAddPassengerInvalidSeat() {
        String result = flight.addPassenger(passenger1, "Z99");
        assertEquals("Invalid seat identifier: Z99", result);
        assertEquals(0, flight.getCurrentCapacity());
        assertFalse(flight.isPassengerOnFlight(passenger1.getPassengerID()));
        assertFalse(passenger1.getBookedFlights().contains(flight));
    }

    @Test
    void testAddPassengerSeatAlreadyBooked() {
        flight.addPassenger(passenger1, "A1");
        String result = flight.addPassenger(passenger2, "A1");
        assertEquals("Seat A1 is already booked", result);
        assertEquals(1, flight.getCurrentCapacity());
        assertFalse(flight.isPassengerOnFlight(passenger2.getPassengerID()));
        assertFalse(passenger2.getBookedFlights().contains(flight));
    }

    @Test
    void testAddPassengerFullCapacity() {
        // Fill up the flight
        int seatNumber = 1;
        char seatLetter = 'A';
        for (int i = 0; i < aircraft.getMaxCapacity(); i++) {
            Passenger p = new Passenger(i + 100, "Test", "Passenger" + i);
            String seatId = String.valueOf(seatLetter) + seatNumber;
            flight.addPassenger(p, seatId);

            // Move to next seat
            seatNumber++;
            if (seatNumber > 6) {  // After 6 seats, move to next row
                seatNumber = 1;
                seatLetter++;
            }
        }

        String result = flight.addPassenger(passenger1, "Z1");
        assertEquals("The aircraft for this flight is at maximum capacity.", result);
        assertEquals(aircraft.getMaxCapacity(), flight.getCurrentCapacity());
    }

    @Test
    void testRemovePassengerSuccess() {
        flight.addPassenger(passenger1, "A1");
        String result = flight.removePassenger(passenger1.getPassengerID());
        assertEquals("John Doe has been removed from this flight", result);
        assertEquals(0, flight.getCurrentCapacity());
        assertFalse(flight.isPassengerOnFlight(passenger1.getPassengerID()));
        assertFalse(passenger1.getBookedFlights().contains(flight));
        assertNull(flight.getPassengerSeat(passenger1.getPassengerID()));
    }

    @Test
    void testRemovePassengerNotFound() {
        String result = flight.removePassenger(999);
        assertEquals("Passenger 999 is not found on this flight", result);
    }

    @Test
    void testGetAvailableSeats() {
        assertEquals(150, flight.getAvailableSeats());
        flight.addPassenger(passenger1, "A1");
        assertEquals(149, flight.getAvailableSeats());
    }

    @Test
    void testSetOrigin() {
        flight.setOrigin(Airports.LAX);
        assertEquals(Airports.LAX, flight.getOrigin());
    }

    @Test
    void testSetDestination() {
        flight.setDestination(Airports.LAX);
        assertEquals(Airports.LAX, flight.getDestination());
        assertTrue(flight.getDuration() > 0); // Duration should be recalculated
    }

    @Test
    void testCalculateFlightDuration() {
        // Test short flight (Vancouver to Toronto)
        flight.setDestination(Airports.YYZ);
        assertTrue(flight.getDuration() >= 4 && flight.getDuration() <= 5);

        // Test long flight (Vancouver to Hong Kong)
        flight.setDestination(Airports.HKG);
        assertTrue(flight.getDuration() >= 12 && flight.getDuration() <= 13);
    }

    @Test
    void testSameAirportDuration() {
        flight.setDestination(Airports.YVR);
        assertEquals(0, flight.getDuration());
    }

    @Test
    void testToString() {
        String expected = "Flight ID: AC123 Aircraft: " + aircraft + " Origin: YVR Destination: YYZ Duration: 0";
        assertEquals(expected, flight.toString());
    }

    @Test
    void testToJson() {
        flight.addPassenger(passenger1, "A1");
        org.json.JSONObject json = flight.toJson();

        assertEquals("AC123", json.getString("flightID"));
        assertEquals(Airports.YVR.toString(), json.getString("origin"));
        assertEquals(Airports.YYZ.toString(), json.getString("destination"));
        assertEquals(0, json.getInt("duration"));
        assertNotNull(json.getJSONObject("aircraft"));
        assertNotNull(json.get("passengersOnFlight"));
    }

    @Test
    void testMultiplePassengers() {
        flight.addPassenger(passenger1, "A1");
        flight.addPassenger(passenger2, "B1");

        assertEquals(2, flight.getCurrentCapacity());
        assertTrue(flight.isPassengerOnFlight(passenger1.getPassengerID()));
        assertTrue(flight.isPassengerOnFlight(passenger2.getPassengerID()));
        assertEquals("A1", flight.getPassengerSeat(passenger1.getPassengerID()));
        assertEquals("B1", flight.getPassengerSeat(passenger2.getPassengerID()));
    }

    @Test
    void testPassengerLookupEfficiency() {
        // Add a passenger with a specific seat
        Passenger p = new Passenger(1500, "Test", "Passenger");
        String result = flight.addPassenger(p, "A1");
        System.out.println("Add passenger result: " + result);

        // Debug: Print the contents of passengerLookup
        System.out.println("Passenger lookup contents: " + flight.getPassengerLookup());
        System.out.println("Passenger ID being checked: 1500");

        // Test lookup time for the passenger
        long startTime = System.nanoTime();
        boolean lookupResult = flight.isPassengerOnFlight(1500);
        long endTime = System.nanoTime();
        long lookupTime = endTime - startTime;

        System.out.println("Lookup result: " + lookupResult);

        // The lookup should be very fast (O(1))
        assertTrue(lookupTime < 10000000); // Less than 10 milliseconds
        assertEquals("A1", flight.getPassengerSeat(1500));
    }
}
