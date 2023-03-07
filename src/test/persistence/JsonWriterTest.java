package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{
    private ArrayList<Passenger> pl;
    private ArrayList<Aircraft> al;
    private ArrayList<Flight> fl;


    @BeforeEach
    void runBefore() {
        pl = new ArrayList<>();
        al = new ArrayList<>();
        fl = new ArrayList<>();
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
            ArrayList<Passenger> listOfPassengers = reader.readPassengerList(pl);
            ArrayList<Aircraft>  listOfAircraft = reader.readAircraftList(al);
            ArrayList<Flight> listOfFlights = reader.readFlightList(fl);
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
            pl.add(new Passenger(0, "Jessica", "Zhou", TravelClasses.FIRSTCLASS));
            Aircraft boeing = new Aircraft("Boeing", 100);
            al.add(boeing);
            fl.add(new Flight("1", boeing, Airports.YVR, Airports.YYZ, 3));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.write(pl, al, fl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            reader.readPassengerList(pl);
            reader.readAircraftList(al);
            reader.readFlightList(fl);

            assertEquals(0, pl.get(0).getBookedFlights().size());
            checkPassenger(0, "Jessica", "Zhou", TravelClasses.FIRSTCLASS,
                    new ArrayList<>(), pl.get(0));
            checkAircraft("Boeing", 100, al.get(0));
            checkFlight("1", boeing, Airports.YVR, Airports.YYZ, 3, fl.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
