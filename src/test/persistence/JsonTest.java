package persistence;

import model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {

    protected void checkPassenger(int passengerID, String firstName, String lastName,
                               ArrayList<Flight> bookedFlights, Passenger passenger) {
        assertEquals(passengerID, passenger.getPassengerID());
        assertEquals(firstName, passenger.getFirstName());
        assertEquals(lastName, passenger.getLastName());
        for (Flight flight : bookedFlights) {
            assertTrue(passenger.getBookedFlights().toString().contains(flight.toString()));
        }
    }

    protected void checkAircraft(String name, int maxCapacity, Aircraft aircraft) {
        assertEquals(name, aircraft.getIdentifier());
        assertEquals(maxCapacity, aircraft.getMaxCapacity());
    }

    protected void checkFlight(String flightID, Aircraft aircraft, Airports origin,
                               Airports destination, int duration, Flight flight) {
        assertEquals(flightID, flight.getFlightID());
        assertEquals(aircraft, flight.getAircraft());
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
        assertEquals(duration, flight.getDuration());
    }
}
