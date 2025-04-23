package model;

/*  This class represents a passenger. The passenger has a passenger ID, first name,
    last name, and the travel class. A passenger is the smallest abstraction in the program
    that can be added to a plane  */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;
import persistence.Writable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Passenger implements Writable {
    private final int passengerID;
    private final String firstName;
    private final String lastName;
    private final ArrayList<Flight> bookedFlights;

    // Creates a passenger
    public Passenger(int passengerID, String firstName, String lastName) {
        this.passengerID = passengerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookedFlights = new ArrayList<>();
    }

    public int getPassengerID() {
        return passengerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArrayList<Flight> getBookedFlights() {
        return bookedFlights;
    }

    // EFFECT: Adds flights to passenger's booked flights
    public void addToBookedFlights(Flight flight) {
        EventLog.getInstance().logEvent(new Event("Added flight in "
                + getFirstName() + "'s bookings list"));
        bookedFlights.add(flight);
    }

    // EFFECT: returns the json object
    @Override
    public String toString() {
        return "Passenger ID: "
                + passengerID
                + " First Name: "
                + firstName
                + " Last Name: "
                + lastName;
    }

    // EFFECTS: generates a boarding ticket of a passenger's flights
    public StringBuilder getBoardingTickets() {
        StringBuilder s = new StringBuilder();
        if (!bookedFlights.isEmpty()) {
            for (Flight flight : bookedFlights) {
                String seatIdentifier = flight.getPassengerSeat(passengerID);
                Seat seat = flight.getAircraft().getSeat(seatIdentifier);
                String cabinClass = seat != null ? seat.getCabin().toString() : "UNASSIGNED";

                s.append("---------------------------------------------------------------------------\n");
                s.append("                         ELECTRONIC BOARDING PASS                          \n");
                s.append("---------------------------------------------------------------------------\n");
                s.append("Name of Passenger: ").append(lastName).append(" ").append(firstName).append("\n");
                s.append(String.format("%-20s %-20s %-20s %-20s\n", "Origin: ", flight.getOrigin(),
                        "Destination: ", flight.getDestination()));
                s.append(String.format("%-20s %-20s %-20s %-20s\n", "Class: ", cabinClass,
                        "Seat No. ", seatIdentifier != null ? seatIdentifier : "UNASSIGNED"));
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = formatter.format(date);
                s.append(String.format("%-20s %-20s %-20s %-20s\n", "Flight ID: ", flight.getFlightID(),
                        "Date: ", formattedDate));
                s.append(String.format("%-20s %-20s %-20s %-20s\n", "Departure: ", "11:30 am","Arrival: ", "10:00 am"));
                s.append("---------------------------------------------------------------------------\n");
            }
        } else {
            StringBuilder e = new StringBuilder();
            return e.append(firstName).append(" ").append(lastName).append(" is not currently booked on any flights.");
        }
        return s;
    }

    // EFFECTS: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json  = new JSONObject();
        json.put("passengerID", passengerID);
        json.put("firstName", this.firstName);
        json.put("lastName", this.lastName);
        json.put("bookedFlights", this.bookedFlights);
        return json;
    }
}
