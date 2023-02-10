package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

import static model.TravelClasses.FIRSTCLASS;
import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {
    private Passenger testPassenger;
    private Flight testFlight;

    @BeforeEach
    void runBefore() {
        testPassenger = new Passenger(12345,"Jessica",
                "Zhou", FIRSTCLASS);
        testFlight = new Flight("123",
                new PassengerAirline("test", 100),
                Airports.YVR, Airports.PEK, 2);
        testFlight.addPassenger(testPassenger);
    }

    @Test
    void constructorTest() {
        assertEquals(12345,testPassenger.getPassengerID());
        assertEquals("Jessica",testPassenger.getFirstName());
        assertEquals("Zhou",testPassenger.getLastName());
        assertEquals(FIRSTCLASS,testPassenger.getTravelClass());
    }

    @Test
    void getPassengerIDTest() {
        assertEquals(12345, testPassenger.getPassengerID());
    }

    @Test
    void getFirstNameTest() {
        assertEquals("Jessica",testPassenger.getFirstName());
    }

    @Test
    void getLastNameTest() {
        assertEquals("Zhou", testPassenger.getLastName());
    }

    @Test
    void getTravelClassTest() {
        assertEquals(FIRSTCLASS, testPassenger.getTravelClass());
    }

    @Test
    void getBookedFlights() {
        // cannot test []
    }

    @Test
    void toStringTest() {
        assertEquals("Passenger ID: 12345 First Name: Jessica Last Name: Zhou Travel Class: FIRSTCLASS", testPassenger.toString());
    }

    @Test
    void getBoardingTicketsTest() {

    }


}