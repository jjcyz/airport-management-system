package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Airports.*;
import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {
    private Passenger p1;
    private Passenger p2;
    private Passenger p3;
    private Flight testFlight1;
    private Aircraft testAircraft1;


    @BeforeEach
    void runBefore() {
        p1 = new Passenger(123,"Ada","Lovelace",TravelClasses.FIRSTCLASS);
        p2 = new Passenger(456,"Alan","Turing",TravelClasses.FIRSTCLASS);
        p3 = new Passenger(789, "Charles", "Babbage", TravelClasses.FIRSTCLASS);

        testAircraft1 = new Aircraft("ID12345", 2);

        testFlight1 = new Flight("ID12345", testAircraft1, YVR,Airports.YYZ,4);

    }

    @Test
    void constructorTest() {
        assertEquals("ID12345",testFlight1.getFlightID());
        assertEquals(testAircraft1, testFlight1.getAircraft());
        assertEquals(YVR,testFlight1.getOrigin());
        assertEquals(Airports.YYZ,testFlight1.getDestination());
        assertEquals(4,testFlight1.getDuration());
    }

    @Test
    void getListOfPassenger() {
        testFlight1.addPassenger(p1);
        assertEquals(testFlight1.getPassengersOnFlight(),
                testFlight1.getPassengersOnFlight());
    }

    @Test
    void addPassengerTest() {
        assertEquals("Ada Lovelace has been added to this flight",testFlight1.addPassenger(p1));
        assertTrue(testFlight1.isPassengerOnFlight(123));
        assertFalse(testFlight1.isPassengerOnFlight(999));
        assertEquals("Alan Turing has been added to this flight",testFlight1.addPassenger(p2));
        assertEquals("The aircraft for this flight is at maximum capacity.",testFlight1.addPassenger(p3));
        assertTrue(testFlight1.isPassengerOnFlight(456));
        assertFalse(testFlight1.isPassengerOnFlight(789));
        assertFalse(testFlight1.isPassengerOnFlight(888));
    }

    @Test
    void removePassengerTest() {
        testFlight1.addPassenger(p2);
        testFlight1.addPassenger(p1);
        assertTrue(testFlight1.isPassengerOnFlight(123));
        int before = testFlight1.getPassengersOnFlight().size();
        assertEquals("Ada Lovelace has been removed from this flight",
                testFlight1.removePassenger(123));
        assertEquals(before - 1, testFlight1.getPassengersOnFlight().size());
        assertEquals("Passenger 999 is not found on this flight",
                testFlight1.removePassenger(999));
    }

    @Test
    void isPassengerOnFlightTest() {
        testFlight1.addPassenger(p1);
        assertTrue(testFlight1.isPassengerOnFlight(p1.getPassengerID()));
        assertFalse(testFlight1.isPassengerOnFlight(p2.getPassengerID()));
    }

    @Test
    void getCurrentCapacityTest() {
        assertEquals(0,testFlight1.getCurrentCapacity());
        testFlight1.addPassenger(p1);
        assertEquals(1,testFlight1.getCurrentCapacity());
    }

    @Test
    void getAvailableSeats() {
        assertEquals(2, testFlight1.getAvailableSeats());
        testFlight1.addPassenger(p1);
        assertEquals(1, testFlight1.getAvailableSeats());
    }

    @Test
    void setOriginTest() {
        assertEquals(YVR,testFlight1.getOrigin());
        testFlight1.setOrigin(PEK);
        assertEquals(PEK,testFlight1.getOrigin());
    }

    @Test
    void setDestinationTest() {
        assertEquals(YYZ,testFlight1.getDestination());
        testFlight1.setDestination(PEK);
        assertEquals(PEK,testFlight1.getDestination());
    }

    @Test
    void toStringTest() {
        assertEquals("Flight ID: ID12345 Aircraft: "
                + "name: ID12345 "
                + "maxCapacity: 2 "
                + "Origin: YVR "
                + "Destination: YYZ" +
                " Duration: 4", testFlight1.toString());
    }
}
