package persistence;

import model.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest{
    private DefaultListModel<Passenger> pl;
    private DefaultListModel<Aircraft> al;
    private DefaultListModel<Flight> fl;
    private Passenger testPassenger;
    private Aircraft testAircraft;
    private Flight testFlight;

    @BeforeEach
    void runBefore() {
        pl = new DefaultListModel<>();
        al = new DefaultListModel<>();
        fl = new DefaultListModel<>();
        this.testPassenger = new Passenger(1,"Jessica", "Zhou",
                TravelClasses.FIRSTCLASS);
        this.testAircraft = new Aircraft("Airplane",100);
        this.testFlight = new Flight("A", testAircraft, Airports.YVR, Airports.YYZ, 3);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(pl, al, fl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            DefaultListModel<Passenger> listOfPassengers = reader.readPassengerList(pl);
            DefaultListModel<Aircraft>  listOfAircraft = reader.readAircraftList(al);
            DefaultListModel<Flight> listOfFlights = reader.readFlightList(fl);
            assertEquals(0, listOfPassengers.size());
            assertEquals(0, listOfAircraft.size());
            assertEquals(0, listOfFlights.size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            pl.addElement(new Passenger(0, "Jessica", "Zhou", TravelClasses.FIRSTCLASS));
            Aircraft boeing = new Aircraft("Boeing", 100);
            al.addElement(boeing);
            fl.addElement(new Flight("1", boeing, Airports.YVR, Airports.YYZ, 3));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.write(pl, al, fl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            reader.readPassengerList(pl);
            reader.readAircraftList(al);
            reader.readFlightList(fl);

            testPassengerToJson();
            testAircraftToJson();
            testFlightToJson();
            testCargoAircraftToJson();
            testPassengerAirlineToJson();
            testPrivateJetToJson();
            testCargo();

            assertEquals(0, pl.get(0).getBookedFlights().size());
            checkPassenger(0, "Jessica", "Zhou", TravelClasses.FIRSTCLASS,
                    new ArrayList<>(), pl.get(0));
            checkAircraft("Boeing", 100, al.get(0));
            checkFlight("1", boeing, Airports.YVR, Airports.YYZ, 3, fl.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void testFlightToJson() {
        JSONObject expectedFlight = new JSONObject()
                .put("flightID", "A")
                .put("aircraft", testAircraft.toJson())
                .put("origin", "YVR")
                .put("destination", "YYZ")
                .put("duration", 3)
                .put("passengersOnFlight", new ArrayList<>());
        JSONObject actualFlight = testFlight.toJson();
        assertEquals(expectedFlight.toString(), actualFlight.toString());
    }

    private void testAircraftToJson() {
        JSONObject expectedAircraft = new JSONObject()
                .put("name", "Airplane")
                .put("maxCapacity", 100);
        JSONObject actualAircraft = testAircraft.toJson();
        assertEquals(expectedAircraft.toString(), actualAircraft.toString());
    }

    private void testPassengerToJson() {
        JSONObject expectedPassenger = new JSONObject()
                .put("firstName", "Jessica")
                .put("lastName", "Zhou")
                .put("passengerID", 1)
                .put("bookedFlights", new ArrayList<>())
                .put("travelClass", "FIRSTCLASS");
        JSONObject actualPassenger = testPassenger.toJson();
        assertEquals(expectedPassenger.toString(), actualPassenger.toString());
    }

    private void testCargoAircraftToJson() {
        JSONObject expectedCargoAirline = new JSONObject()
                .put("name", "forCargo")
                .put("maxCargoWeight", 100);
        JSONObject actualCargoAirline = new CargoAircraft("forCargo",100).toJson();
        assertEquals(expectedCargoAirline.toString(), actualCargoAirline.toString());
    }

    private void testPassengerAirlineToJson() {
        JSONObject expectedPassengerAirline = new JSONObject()
                .put("name", "forPassenger")
                .put("maxCapacity", 100);
        JSONObject actualPassengerAirline = new PassengerAirline("forPassenger",100).toJson();
        assertEquals(expectedPassengerAirline.toString(), actualPassengerAirline.toString());
    }

    private void testPrivateJetToJson() {
        JSONObject expectedPrivateJet = new JSONObject()
                .put("name", "forPrivate")
                .put("maxCapacity", 100);
        JSONObject actualPrivateJet = new PrivateJet("forPrivate", 100).toJson();
        assertEquals(expectedPrivateJet.toString(), actualPrivateJet.toString());
    }

    private void testCargo() {
        JSONObject expectedCargoAircraft = new JSONObject()
                .put("name", "bananasPlane")
                .put("maxCargoWeight", 100);
        JSONObject actualCargo = new CargoAircraft("bananasPlane", 100).toJson();

        JSONObject expectedCargo = new JSONObject()
                .put("description", "bananas")
                .put("weight", 50);
        Cargo cargoCargo = new Cargo("bananas", 50);

        assertEquals(expectedCargoAircraft.toString(), actualCargo.toString());
        assertEquals(expectedCargo.toString(), cargoCargo.toJson().toString());
    }
}
