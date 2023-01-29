package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {
    private Passenger testPassenger;

    @BeforeEach
    void runBefore() {
        testPassenger = new Passenger(12345,"Jessica","Zhou",TravelClasses.FIRSTCLASS);
    }

    @Test
    void testConstructor() {
        assertEquals(12345,testPassenger.getPassengerID());
        assertEquals("Jessica",testPassenger.getFirstName());
        assertEquals("Zhou",testPassenger.getLastName());
        assertEquals(TravelClasses.FIRSTCLASS,testPassenger.getTravelClass());
    }
    // delete or rename this class!
}