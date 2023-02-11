package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateJetTest {
    private PrivateJet pj = new PrivateJet("Private", 10);

    @Test
    void getName() {
        assertEquals("Private", pj.getName());
    }

    @Test
    void getMaxCapacityTest() {
        assertEquals(10, pj.getMaxCapacity());
    }

    @Test
    void getClassTest() {
       // assertEquals(, pj.getClass());
    }


}
