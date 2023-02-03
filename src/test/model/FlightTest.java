package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Airports.*;
import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {
    private Passenger p1;
    private Passenger p2;
    private Flight testFlight1;
    private Flight testFlight2;
    private Aircraft testAircraft1;


    @BeforeEach
    void runBefore() {
        p1 = new Passenger(123,"p1","a",TravelClasses.FIRSTCLASS);
        p2 = new Passenger(456,"p2","b",TravelClasses.FIRSTCLASS);

        testAircraft1 = new Aircraft("ID12345", 1);

        testFlight1 = new Flight("ID12345", testAircraft1, YVR,Airports.YYZ,4);
        testFlight2 = new Flight("ID67890", testAircraft1,Airports.YYZ, YVR,4);

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
        assertEquals("Passenger ID: 123 First Name: p1 Last Name: a Travel Class: FIRSTCLASS",
                testFlight1.getListOfPassengers());

    }

    @Test
    void addPassengerTest() {
        testFlight1.addPassenger(p1);
        assertTrue(testFlight1.isPassengerOnFlight(123));
        assertFalse(testFlight1.isPassengerOnFlight(999));
        testFlight1.addPassenger(p2);
        assertFalse(testFlight1.isPassengerOnFlight(456));
    }

    @Test
    void removePassengerTest() {
        testFlight1.addPassenger(p1);
        assertTrue(testFlight1.isPassengerOnFlight(123));
        testFlight1.removePassenger(123);
        assertFalse(testFlight1.isPassengerOnFlight(123));
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
        assertEquals(1, testFlight1.getAvailableSeats());
        testFlight1.addPassenger(p1);
        assertEquals(0, testFlight1.getAvailableSeats());
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
}
