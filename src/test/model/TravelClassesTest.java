package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import java.util.Arrays;

public class TravelClassesTest {

    @Test
    void getTravelClassesTest() {
        List<TravelClasses> expectedTravelClasses = Arrays.asList(TravelClasses.values());
        assertEquals(expectedTravelClasses, TravelClasses.getTravelClasses());
    }



}


