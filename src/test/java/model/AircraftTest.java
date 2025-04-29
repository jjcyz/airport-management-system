package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftTest {
    private Aircraft testAircraft1;

    @BeforeEach
    void runBefore() {
        testAircraft1 = new Aircraft("ABC");
    }

    @Test
    void testConstructor() {
        assertEquals("ABC", testAircraft1.getIdentifier());
        assertEquals(60, testAircraft1.getMaxCapacity());
    }

    @Test
    void getName() {
        assertEquals("ABC", testAircraft1.getIdentifier());
    }

    @Test
    void getMaxCapacityTest() {
        assertEquals(60,testAircraft1.getMaxCapacity());
    }

    @Test
    void toStringTest() {
        assertEquals("Identifier: ABC Max Capacity: 60",testAircraft1.toString());
    }

    @Test
    void addCargoToAircraftTest() {
        Cargo exoticFruits = new Cargo("Exotic fruits", 2);
        assertEquals("Cargo has been added to ABC",
                testAircraft1.addCargoToAircraft(exoticFruits));
    }



}
