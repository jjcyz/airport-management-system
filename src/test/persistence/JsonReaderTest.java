package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static model.Airports.YVR;
import static model.Airports.YYZ;
import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    private Aircraft aircraft;
    private ArrayList<Passenger> passengersOnBoard;

    @BeforeEach
    void runBefore() {
        aircraft = new Aircraft("Boeing", 200);
        passengersOnBoard = new ArrayList<>();
        passengersOnBoard.add(new Passenger(1234,"Elon", "Musk", TravelClasses.FIRSTCLASS));
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Flight> flights = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFlightList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFlightList.json");
        try {
            List<Flight> flights = reader.read();
            assertEquals(0, flights.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFlightList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFlightList.json");
        try {
            List<Flight> flights = reader.read();
            assertEquals(2, flights.size());
            checkFlight("AC100", aircraft, YYZ, YVR, passengersOnBoard, flights.get(0));
            checkFlight("AC101", aircraft, YVR, YYZ, passengersOnBoard, flights.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
