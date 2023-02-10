package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CargoTest {
    private Cargo testCargo;

    @BeforeEach
    void runBefore() {
        testCargo = new Cargo("randomStuff", 10);
    }


    @Test
    void getDescriptionTest() {
        assertEquals("randomStuff", testCargo.getDescription());
    }

    @Test
    void getWeightTest() {
        assertEquals(10, testCargo.getWeight());
    }
}


