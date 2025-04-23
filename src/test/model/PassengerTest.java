package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {
    private Passenger testPassenger;
    private Passenger testPassenger2;
    private Flight testFlight;

    @BeforeEach
    void runBefore() {
        testPassenger = new Passenger(12345,"Jessica",
                "Zhou");
        testFlight = new Flight("123",
                new PassengerAirline("test", 100),
                Airports.YVR, Airports.PEK, 2);
        testFlight.addPassenger(testPassenger, "A1");

        testPassenger2 = new Passenger(67890, "Elon", "Musk");
    }


    @Test
    void constructorTest() {
        assertEquals(12345,testPassenger.getPassengerID());
        assertEquals("Jessica",testPassenger.getFirstName());
        assertEquals("Zhou",testPassenger.getLastName());
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
    void getBookedFlights() {
        ArrayList<Flight> testForBookedFlight = new ArrayList<>();
        testForBookedFlight.add(testFlight);
        assertEquals(testForBookedFlight, testPassenger.getBookedFlights());
    }

    @Test
    void toStringTest() {
        assertEquals("Passenger ID: 12345 First Name: Jessica Last Name: Zhou",
                testPassenger.toString());
    }

    @Test
    void getBoardingTicketsTest() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(date);

        assertEquals("---------------------------------------------------------------------------\n"
                +"                         ELECTRONIC BOARDING PASS                          \n"
                +"---------------------------------------------------------------------------\n"
                +"Name of Passenger: Zhou Jessica\n"
                +"Origin:              YVR                  Destination:         PEK                 \n"
                +"Class:               ECONOMY              Seat No.             A1                  \n"
                +"Flight ID:           123                  Date:                " + formattedDate + "          \n"
                +"Departure:           11:30 am             Arrival:             10:00 am            \n"
                +"---------------------------------------------------------------------------\n"
                , testPassenger.getBoardingTickets().toString());
        assertEquals("Elon Musk is not currently booked on any flights.",
                testPassenger2.getBoardingTickets().toString());

    }


}
