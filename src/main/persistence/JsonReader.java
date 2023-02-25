package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseArrayList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private ArrayList parseArrayList(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        ArrayList wr = new ArrayList();
        addArrayList(wr, jsonObject);
        return wr;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addArrayList(ArrayList list, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            addItem(list, nextItem);
        }
    }

    // MODIFIES: this
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addItem(ArrayList<Writable> list, JSONObject jsonObject) {
        String type = jsonObject.getString("type");

        switch (type) {
            case "Passenger":
                Passenger passenger = createPassenger(jsonObject);
                list.add(passenger);
                break;
            case "Aircraft":
                Aircraft aircraft = createAircraft(jsonObject);
                list.add(aircraft);
                break;
            case "Flight":
                Flight flight = createFlight(jsonObject, list);
                list.add(flight);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    // EFFECTS: creates the Passenger Object to add in addItem
    private Passenger createPassenger(JSONObject jsonObject) {
        return new Passenger(
                jsonObject.getInt("Passenger ID"),
                jsonObject.getString("First Name"),
                jsonObject.getString("Last Name"),
                TravelClasses.valueOf(jsonObject.getString("Travel Class")));
    }

    // EFFECTS: creates the Aircraft Object to add in addItem
    private Aircraft createAircraft(JSONObject jsonObject) {
        return new Aircraft(
                jsonObject.getString("Name"),
                jsonObject.getInt("Max Capacity"));
    }

    // EFFECTS: creates the Flight Object to add in addItem
    private Flight createFlight(JSONObject jsonObject, ArrayList<Writable> list) {
        Aircraft aircraft = findAircraft(jsonObject.getString("Aircraft Name"), list);

        Flight flight = new Flight(
                jsonObject.getString("Flight ID"),
                aircraft,
                Airports.valueOf(jsonObject.getString("Origin")),
                Airports.valueOf(jsonObject.getString("Destination")),
                jsonObject.getInt("Duration"));

        JSONArray passengerArray = jsonObject.getJSONArray("Passengers");
        for (int i = 0; i < passengerArray.length(); i++) {
            JSONObject passengerObj = passengerArray.getJSONObject(i);
            Passenger passenger = new Passenger(
                    passengerObj.getInt("Passenger ID"),
                    passengerObj.getString("First Name"),
                    passengerObj.getString("Last Name"),
                    TravelClasses.valueOf(passengerObj.getString("Travel Class")));
            flight.addPassenger(passenger);
        }
        return flight;
    }

    // EFFECTS: Find the Aircraft Object for createFlight
    private Aircraft findAircraft(String aircraftName, ArrayList<Writable> list) {
        return list.stream()
                .filter(w -> w instanceof Aircraft && ((Aircraft) w).getName().equals(aircraftName))
                .map(w -> (Aircraft) w)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found: " + aircraftName));
    }
}