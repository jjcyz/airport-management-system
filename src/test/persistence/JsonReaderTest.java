package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    private Aircraft aircraft;
    private DefaultListModel<Passenger> listOfPassengers;
    private DefaultListModel<Aircraft> listOfAircraft;
    private DefaultListModel<Flight> listOfFlights;


    @BeforeEach
    void runBefore() {
        aircraft = new Aircraft("Boeing");
        listOfPassengers = new DefaultListModel<>();
        listOfAircraft = new DefaultListModel<>();
        listOfFlights = new DefaultListModel<>();

        Passenger testPassenger1 = new Passenger(1,"Elon", "Musk"
        );
        Passenger testPassenger2 = new Passenger(2, "Ada", "Lovelace"
        );

        Aircraft testAircraft = new Aircraft("Airplane");
        Flight testFlight = new Flight("A", testAircraft, Airports.YVR, Airports.YYZ, 3);
        testPassenger1.addToBookedFlights(testFlight);

        listOfPassengers.addElement(testPassenger1);
        listOfPassengers.addElement(testPassenger2);

        listOfFlights.addElement(testFlight);

    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DefaultListModel<Passenger> passengers = reader.readPassengerList(listOfPassengers);
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFlightList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPassengerList.json");
        try {
            DefaultListModel<Passenger> listOfPassenger = reader.readPassengerList(listOfPassengers);
            assertEquals(0, listOfPassenger.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPassengerList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPassengerList.json");
        try {
            DefaultListModel<Passenger> passengers = reader.readPassengerList(listOfPassengers);

            ArrayList<Flight> bookedFlight = passengers.get(0).getBookedFlights();

            ArrayList<Flight> elonsFlights = new ArrayList<>();
            Aircraft elonsJet = new Aircraft("Airplane");
            elonsFlights.add(new Flight("A", elonsJet, Airports.YVR, Airports.YYZ, 3));

            assertEquals(1, bookedFlight.size());
            assertEquals(2, passengers.size());
            checkPassenger(1, "Elon", "Musk",
                    bookedFlight, passengers.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }




}
