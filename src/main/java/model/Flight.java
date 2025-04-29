package model;

/* This class represents a flight. A flight is made of an aircraft, an origin, a destination,
    the duration of flight, and the list of passengers registered on this flight. */

import org.json.JSONObject;
import org.json.JSONArray;
import persistence.Event;
import persistence.EventLog;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashMap;

public class Flight implements Writable {
    private final String flightID;
    private final Aircraft aircraft;
    private Airports origin;
    private Airports destination;
    private int duration;
    private final ArrayList<Passenger> passengersOnFlight;
    private final HashMap<Integer, Passenger> passengerLookup;
    private final HashMap<Integer, String> passengerSeatAssignments;

    public Flight(String flightID, Aircraft aircraft, Airports origin, Airports destination, int duration) {
        this.flightID = flightID;
        this.aircraft = aircraft;
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.passengersOnFlight = new ArrayList<>();
        this.passengerLookup = new HashMap<>();
        this.passengerSeatAssignments = new HashMap<>();
    }

    public String getFlightID() {
        return flightID;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public Airports getOrigin() {
        return origin;
    }

    public Airports getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    // EFFECTS: returns the list of passengers on this flight
    public ArrayList<Passenger> getPassengersOnFlight() {
        return passengersOnFlight;
    }

    // EFFECTS: add a passenger to flight with a specific seat
    public String addPassenger(Passenger passenger, String seatIdentifier) {
        if (getCurrentCapacity() >= aircraft.getMaxCapacity()) {
            return "The aircraft for this flight is at maximum capacity.";
        }

        Seat seat = aircraft.getSeat(seatIdentifier);
        if (seat == null) {
            return "Invalid seat identifier: " + seatIdentifier;
        }

        if (seat.isBooked()) {
            return "Seat " + seatIdentifier + " is already booked";
        }

        seat.bookSeat(passenger);
        passengersOnFlight.add(passenger);
        passengerLookup.put(passenger.getPassengerID(), passenger);
        passengerSeatAssignments.put(passenger.getPassengerID(), seatIdentifier);
        passenger.addToBookedFlights(this);

        EventLog.getInstance().logEvent(new Event("Added passenger (on flight): " + passenger.getFirstName()));
        return passenger.getFirstName() + " " + passenger.getLastName() + " has been added to this flight in seat " + seatIdentifier;
    }

    // EFFECTS: remove a passenger from flight
    public String removePassenger(int passengerID) {
        Passenger passenger = passengerLookup.get(passengerID);
        if (passenger == null) {
            return "Passenger " + passengerID + " is not found on this flight";
        }

        String seatIdentifier = passengerSeatAssignments.get(passengerID);
        if (seatIdentifier != null) {
            Seat seat = aircraft.getSeat(seatIdentifier);
            if (seat != null) {
                seat.unbookSeat();
            }
            passengerSeatAssignments.remove(passengerID);
        }

        passengersOnFlight.remove(passenger);
        passengerLookup.remove(passengerID);
        passenger.getBookedFlights().remove(this);

        EventLog.getInstance().logEvent(new Event("Removed passenger: " + passenger.getFirstName()));
        return passenger.getFirstName() + " " + passenger.getLastName() + " has been removed from this flight";
    }

    // EFFECTS: checks if a passenger is registered on the aircraft
    public boolean isPassengerOnFlight(int passengerID) {
        return passengerLookup.containsKey(passengerID);
    }

    // EFFECTS: returns the size of passenger on the aircraft
    public int getCurrentCapacity() {
        return passengersOnFlight.size();
    }

    // EFFECTS: returns the number of seats available on the aircraft
    public int getAvailableSeats() {
        return aircraft.getMaxCapacity() - getCurrentCapacity();
    }

    // EFFECTS: sets the origin to given origin
    public void setOrigin(Airports origin) {
        this.origin = origin;
    }

    // EFFECTS: sets destination to given destination and updates duration
    public void setDestination(Airports destination) {
        this.destination = destination;
        this.duration = calculateFlightDuration();
    }

    // EFFECTS: calculates flight duration based on distance between airports
    private int calculateFlightDuration() {
        // Average commercial aircraft speed in km/h
        final int AVERAGE_SPEED = 900;

        // Get coordinates for both airports
        double[] originCoords = origin.getCoordinates();
        double[] destCoords = destination.getCoordinates();

        // Calculate distance using Haversine formula
        double distance = calculateDistance(originCoords[0], originCoords[1],
                                         destCoords[0], destCoords[1]);

        // Calculate duration in hours (rounded up)
        return (int) Math.ceil(distance / AVERAGE_SPEED);
    }

    // EFFECTS: calculates distance between two points using Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // EFFECTS: returns the seat identifier for a passenger, or null if not found
    public String getPassengerSeat(int passengerID) {
        return passengerSeatAssignments.get(passengerID);
    }

    // EFFECTS: return the flight string
    @Override
    public String toString() {
        return "Flight ID: "
                + flightID
                + " Aircraft: "
                + aircraft
                + " Origin: "
                + origin
                + " Destination: "
                + destination
                + " Duration: "
                + duration;
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("flightID", flightID);
        json.put("origin", origin.toString());
        json.put("destination", destination.toString());
        json.put("duration", duration);
        json.put("aircraft", aircraft.toJson());

        JSONArray passengersArray = new JSONArray();
        for (Passenger passenger : passengersOnFlight) {
            JSONObject passengerJson = new JSONObject();
            passengerJson.put("passengerID", passenger.getPassengerID());
            passengerJson.put("firstName", passenger.getFirstName());
            passengerJson.put("lastName", passenger.getLastName());
            passengerJson.put("seat", getPassengerSeat(passenger.getPassengerID()));
            passengersArray.put(passengerJson);
        }
        json.put("passengersOnFlight", passengersArray);

        return json;
    }

    // EFFECTS: returns the passenger lookup map for debugging purposes
    public HashMap<Integer, Passenger> getPassengerLookup() {
        return passengerLookup;
    }
}
