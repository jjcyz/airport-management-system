package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
        List<Airports> expectedAirports = Arrays.asList(Airports.values());
        assertEquals(expectedAirports, testAirport.getAirports());

    }



}
