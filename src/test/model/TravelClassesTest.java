package model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TravelClassesTest {

    @Test
    void getTravelClassesTest() {
        List<TravelClasses> expectedTravelClasses = Arrays.asList(TravelClasses.values());
        assertEquals(expectedTravelClasses, TravelClasses.getTravelClasses());
    }



}


