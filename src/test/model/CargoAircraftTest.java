package model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CargoAircraftTest {

    CargoAircraft testCargoAircraft = new CargoAircraft("testCargoAircraft", 100);
    Cargo exoticFruits = new Cargo("Exotic fruits", 2);


    @Test
    void addCargoToAircraftTest() {
        assertEquals("Cargo has been added to testCargoAircraft",
                testCargoAircraft.addCargoToAircraft(exoticFruits));
    }



}
