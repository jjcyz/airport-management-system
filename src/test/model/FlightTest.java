package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {
    private Flight testFlight1;
    private Flight testFlight2;
    private Plane testPlane1;

    Passenger p1 = new Passenger(123,"p1","a",TravelClasses.FIRSTCLASS);
    Passenger p2 = new Passenger(456,"p2","b",TravelClasses.FIRSTCLASS);


    @BeforeEach
    void runBefore() {
        testFlight1 = new Flight("ID12345",testPlane1,Airports.YVR,Airports.YYZ,4);
        testFlight2 = new Flight("ID67890",testPlane1,Airports.YYZ,Airports.YVR,4);
    }

    @Test
    void constructorTest() {
        assertEquals("ID12345",testFlight1.getFlightID());
        assertEquals(testPlane1, testFlight1.getPlane());
        assertEquals(Airports.YVR,testFlight1.getOrigin());
        assertEquals(Airports.YYZ,testFlight1.getDestination());
        assertEquals(4,testFlight1.getDuration());
    }

    @Test
    void getListOfPassenger() {

    }

    @Test
    void addEmptyBeforeTest() {

    }

    @Test
    void addNotEmptyBeforeTest() {

    }

    @Test
    void removeEmptyTest() {

    }

    @Test
    void removeNotEmptyTest() {

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
}
