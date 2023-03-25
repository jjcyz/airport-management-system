package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DefaultListModel<Passenger> readPassengerList(DefaultListModel<Passenger> listOfPassengers)
            throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        return parsePassengerList(jsonObject.getJSONArray("listOfPassengers"));
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DefaultListModel<Aircraft> readAircraftList(DefaultListModel<Aircraft> listOfAircraft) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        return parseAircraftList(jsonObject.getJSONArray("listOfAircraft"));
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DefaultListModel<Flight> readFlightList(DefaultListModel<Flight> listOfFights) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        return parseFlightList(jsonObject.getJSONArray("listOfFlights"));
    }

    // EFFECTS: reads the source provided
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source))) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses list of passengers from JSON array and returns it
    private DefaultListModel<Passenger> parsePassengerList(JSONArray jsonArray) {
        DefaultListModel<Passenger> passengerList = new DefaultListModel<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            passengerList.addElement(parsePassenger(jsonObject));
        }
        return passengerList;
    }

    // EFFECTS: parses passenger from JSON object and returns it
    private Passenger parsePassenger(JSONObject jsonObject) {
        int passengerID = jsonObject.getInt("passengerID");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String travelClassStr = jsonObject.getString("travelClass");
        TravelClasses travelClass = TravelClasses.valueOf(travelClassStr);
        JSONArray jsonBookedFlights = jsonObject.getJSONArray("bookedFlights");

        Passenger passenger = new Passenger(passengerID, firstName, lastName, travelClass);

        for (int i = 0; i < jsonBookedFlights.length(); i++) {
            JSONObject jsonFlight = jsonBookedFlights.getJSONObject(i);
            Flight flight = parseFlight(jsonFlight);
            flight.addPassenger(passenger); // add passenger to flight
        }
        return passenger;
    }

    // EFFECTS: parses list of aircraft from JSON array and returns it
    private DefaultListModel<Aircraft> parseAircraftList(JSONArray jsonArray) {
        DefaultListModel<Aircraft> aircraftList = new DefaultListModel<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            aircraftList.addElement(parseAircraft(jsonObject));
        }
        return aircraftList;
    }

    // EFFECTS: parses aircraft from JSON object and returns it
    private Aircraft parseAircraft(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int maxCapacity = jsonObject.getInt("maxCapacity");
        return new Aircraft(name, maxCapacity);
    }

    // EFFECTS: parses list of flights from JSON array and returns it
    private DefaultListModel<Flight> parseFlightList(JSONArray jsonArray) {
        DefaultListModel<Flight> flightList = new DefaultListModel<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            flightList.addElement(parseFlight(jsonObject));
        }
        return flightList;
    }

    // EFFECTS: parses flight from JSON object and returns it
    public Flight parseFlight(JSONObject jsonObject) {
        String flightNumber = jsonObject.getString("flightID");
        Aircraft aircraft = parseAircraft(jsonObject.getJSONObject("aircraft"));
        Airports origin = Airports.valueOf(jsonObject.getString("origin"));
        Airports destination = Airports.valueOf(jsonObject.getString("destination"));
        int duration = jsonObject.getInt("duration");
        JSONArray jsonPassengersOnFlight = jsonObject.getJSONArray("passengersOnFlight");

        Flight flight = new Flight(flightNumber, aircraft, origin, destination, duration);
        for (int i = 0; i < jsonPassengersOnFlight.length(); i++) {
//            JSONObject jsonPassenger = jsonPassengersOnFlight.getJSONObject(i);
//            Passenger passenger = parsePassenger(jsonPassenger); // do I need to parse again?
//            //// flight.getListOfPassengers().add(passenger);
        }
        return flight;

    }


}