package persistence;

import model.Aircraft;
import model.Airports;
import model.Flight;
import model.Passenger;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    public void checkFlight(String flightNum, Aircraft aircraft, Airports origin, Airports destination,
                               ArrayList<Passenger> listOfPassengers, Flight flight) {
        assertEquals(flightNum, flight.getFlightID());
        assertEquals(aircraft, flight.getAircraft());
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
        assertEquals(listOfPassengers, flight.getListOfPassengers());
    }
}
