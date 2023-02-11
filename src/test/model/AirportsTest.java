package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AirportsTest {

    Airports testAirport = Airports.YVR;

    @Test
    void getAirportNameTest() {
        assertEquals("Vancouver International Airport", testAirport.getAirportName());
    }

    @Test
    void getCityTest() {
        assertEquals("Vancouver",testAirport.getCity());
    }

    @Test
    void getCountryTest() {
        assertEquals("Canada", testAirport.getCountry());
    }

    @Test
    void getAirportsTest() {
        // assertEquals(, testAirport.getAirports());

    }



}
