package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaneTest {
    private Plane testPlane1;
    private Plane testPlane2;


    @BeforeEach
    void runBefore() {
        testPlane1 = new Plane("ABC",150);
        testPlane2 = new Plane("CDE",200);
    }

    @Test
    void testConstructor() {
        assertEquals("ABC",testPlane1.getName());
        assertEquals(150,testPlane1.getMaxCapacity());
    }


}
