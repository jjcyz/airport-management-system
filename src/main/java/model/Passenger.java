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
    public StringBuilder getBoardingTickets() {
        if (bookedFlights.isEmpty()) {
            return new StringBuilder().append(firstName).append(" ").append(lastName)
                    .append(" is not currently booked on any flights.");
        }

        StringBuilder tickets = new StringBuilder();
        for (Flight flight : bookedFlights) {
            tickets.append(generateBoardingTicket(flight));
        }
        return tickets;
    }

    private StringBuilder generateBoardingTicket(Flight flight) {
        StringBuilder ticket = new StringBuilder();
        String seatIdentifier = flight.getPassengerSeat(passengerID);
        Seat seat = flight.getAircraft().getSeat(seatIdentifier);
        String cabinClass = seat != null ? seat.getCabin().toString() : "UNASSIGNED";

        appendHeader(ticket);
        appendPassengerInfo(ticket);
        appendFlightInfo(ticket, flight, cabinClass, seatIdentifier);
        appendFooter(ticket);

        return ticket;
    }

    private void appendHeader(StringBuilder ticket) {
        ticket.append("---------------------------------------------------------------------------\n")
              .append("                         ELECTRONIC BOARDING PASS                          \n")
              .append("---------------------------------------------------------------------------\n");
    }

    private void appendPassengerInfo(StringBuilder ticket) {
        ticket.append("Name of Passenger: ").append(lastName).append(" ").append(firstName).append("\n");
    }

    private void appendFlightInfo(StringBuilder ticket, Flight flight, String cabinClass, String seatIdentifier) {
        ticket.append(String.format("%-20s %-20s %-20s %-20s\n", "Origin: ", flight.getOrigin(),
                "Destination: ", flight.getDestination()))
              .append(String.format("%-20s %-20s %-20s %-20s\n", "Class: ", cabinClass,
                "Seat No. ", seatIdentifier != null ? seatIdentifier : "UNASSIGNED"))
              .append(String.format("%-20s %-20s %-20s %-20s\n", "Flight ID: ", flight.getFlightID(),
                "Date: ", getFormattedDate()))
              .append(String.format("%-20s %-20s %-20s %-20s\n", "Departure: ", "11:30 am",
                "Arrival: ", "10:00 am"));
    }

    private void appendFooter(StringBuilder ticket) {
        ticket.append("---------------------------------------------------------------------------\n");
    }

    private String getFormattedDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
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
