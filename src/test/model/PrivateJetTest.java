package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateJetTest {
    private final PrivateJet pj = new PrivateJet("Private", 10);

    @Test
    void getName() {
        assertEquals("Private", pj.getIdentifier());
    }

    @Test
    void getMaxCapacityTest() {
        assertEquals(60, pj.getMaxCapacity());
    }

    @Test
    void getClassTest() {
        assertEquals(PrivateJet.class, pj.getClass());
    }


}
