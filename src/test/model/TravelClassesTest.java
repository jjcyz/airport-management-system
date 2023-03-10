package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TravelClassesTest {

    @Test
    void getTravelClassesTest() {
        List<TravelClasses> expectedTravelClasses = Arrays.asList(TravelClasses.values());
        assertEquals(expectedTravelClasses, TravelClasses.getTravelClasses());
    }

    @Test
    void getTicketCostTest() {
        TravelClasses[] actualTravelClassesCosts = TravelClasses.values();
        List<Integer> expectedCosts = new ArrayList<>();
        expectedCosts.add(300);
        expectedCosts.add(400);
        expectedCosts.add(600);
        expectedCosts.add(1000);

        ArrayList<Integer> actualCosts = new ArrayList<>();
        for (TravelClasses t : actualTravelClassesCosts) {
            actualCosts.add(t.getTicketCost());
        }
        assertEquals(expectedCosts, actualCosts);
    }




}


