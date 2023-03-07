package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    private Aircraft aircraft;
    private ArrayList<Passenger> listOfPassengers;
    private ArrayList<Aircraft> listOfAircraft;
    private ArrayList<Flight> listOfFlights;


    @BeforeEach
    void runBefore() {
        aircraft = new Aircraft("Boeing", 200);
        listOfPassengers = new ArrayList<>();
        listOfAircraft = new ArrayList<>();
        listOfFlights = new ArrayList<>();
        listOfPassengers.add(new Passenger(1,"Elon", "Musk",
                TravelClasses.FIRSTCLASS));
        listOfPassengers.add(new Passenger(2, "Ada", "Lovelace",
                TravelClasses.FIRSTCLASS));

    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Passenger> passengers = reader.readPassengerList(listOfPassengers);
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFlightList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPassengerList.json");
        try {
            ArrayList<Passenger> listOfPassenger = reader.readPassengerList(listOfPassengers);
            assertEquals(0, listOfPassenger.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFlightList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPassengerList.json");
        try {
            List<Passenger> passengers = reader.readPassengerList(listOfPassengers);
            assertEquals(2, passengers.size());
            checkPassenger(1, "Elon", "Musk", TravelClasses.FIRSTCLASS,
                    new ArrayList<>(), passengers.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
