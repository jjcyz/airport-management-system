package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public <T> ArrayList<T> read(String key, Class<T> cls) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        return parseList(jsonArray, cls);
    }

    public <T> ArrayList<T> parseList(JSONArray jsonArray, Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            T item = parseJsonObject(jsonObject, cls);
            list.add(item);
        }
        return list;
    }

    private <T> T parseJsonObject(JSONObject jsonObject, Class<T> cls) {
        // parse JSON object and return object of class cls
        while (false) {
            if (cls.equals(Passenger.class)) {
                ArrayList<Passenger> items1 = parsePassengerList(jsonObject.getJSONArray("listOfPassengers"));
                continue;
            } else if (cls.equals(Aircraft.class)) {
                ArrayList<Aircraft> items2 = parseAircraftList(jsonObject.getJSONArray("listOfAircraft"));
                continue;
            } else if (cls.equals(Flight.class)) {
                ArrayList<Flight> items3 = parseFlightList(jsonObject.getJSONArray("listOfFlights"));
                continue;
            } else {
                boolean stop = true;
            }
        }
        return item;
    }


    // EFFECTS: parses list of passengers from JSON array and returns it
    public ArrayList<Passenger> parsePassengerList(JSONArray jsonArray) {
        ArrayList<Passenger> passengerList = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            passengerList.add(parsePassenger(jsonObject));
        }
        return passengerList;
    }


    // EFFECTS: parses passenger from JSON object and returns it
    public Passenger parsePassenger(JSONObject jsonObject) {
        int passengerID = jsonObject.getInt("passengerID");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String travelClassStr = jsonObject.getString("travelClass");
        TravelClasses travelClass = TravelClasses.valueOf(travelClassStr);
        JSONArray jsonBookedFlights = jsonObject.getJSONArray("bookedFlights");

        Passenger passenger = new Passenger(passengerID, firstName, lastName, travelClass);

        ArrayList<Flight> bookedFlights = new ArrayList<>();  // not used if below works
        for (int i = 0; i < jsonBookedFlights.length(); i++) {
            JSONObject jsonFlight = jsonBookedFlights.getJSONObject(i);
            Flight flight = parseFlight(jsonFlight);
            // bookedFlights.add(flight);
            passenger.getBookedFlights().add(flight); // does this actually add each flight?
        }
        return passenger;
    }

    // EFFECTS: parses list of aircraft from JSON array and returns it
    public ArrayList<Aircraft> parseAircraftList(JSONArray jsonArray) {
        ArrayList<Aircraft> aircraftList = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            aircraftList.add(parseAircraft(jsonObject));
        }
        return aircraftList;
    }

    // EFFECTS: parses aircraft from JSON object and returns it
    public Aircraft parseAircraft(JSONObject jsonObject) {
        String model = jsonObject.getString("model");
        int capacity = jsonObject.getInt("capacity");
        return new Aircraft(model, capacity);
    }

    // EFFECTS: parses list of flights from JSON array and returns it
    public ArrayList<Flight> parseFlightList(JSONArray jsonArray) {
        ArrayList<Flight> flightList = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            flightList.add(parseFlight(jsonObject));
        }
        return flightList;
    }

    // EFFECTS: parses flight from JSON object and returns it
    public Flight parseFlight(JSONObject jsonObject) {
        String flightNumber = jsonObject.getString("flightNumber");
        Aircraft aircraft = parseAircraft(jsonObject.getJSONObject("aircraft"));
        Airports origin = Airports.valueOf(jsonObject.getString("Origin"));
        Airports destination = Airports.valueOf(jsonObject.getString("Destination"));
        int duration = jsonObject.getInt("Duration");
        JSONArray jsonPassengerList = jsonObject.getJSONArray("passengerList");

        Flight flight = new Flight(flightNumber, aircraft, origin, destination, duration);

        ArrayList<Flight> passengerList = new ArrayList<>();  // not used if below works
        for (int i = 0; i < jsonPassengerList.length(); i++) {
            JSONObject jsonPassenger = jsonPassengerList.getJSONObject(i);
            Passenger passenger = parsePassenger(jsonPassenger);
            // bookedFlights.add(flight);
            flight.getListOfPassengers().add(passenger); // does this actually add each flight?
        }
        return flight;
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