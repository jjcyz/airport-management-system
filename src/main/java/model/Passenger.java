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
        return String.format("Passenger ID: %d First Name: %s Last Name: %s",
                passengerID, firstName, lastName);
    }

    // EFFECTS: generates a boarding ticket of a passenger's flights
    public String getBoardingTickets() {
        if (bookedFlights.isEmpty()) {
            return "No flights booked.";
        }

        StringBuilder tickets = new StringBuilder();
        for (Flight flight : bookedFlights) {
            String seatIdentifier = flight.getPassengerSeat(this);
            String cabinClass = flight.getPassengerCabinClass(this);
            BoardingTicket ticket = new BoardingTicket(this, flight, seatIdentifier, cabinClass);
            tickets.append(ticket.generate()).append("\n");
        }
        return tickets.toString();
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
