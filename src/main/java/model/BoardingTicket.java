package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardingTicket {
    private static final String HEADER = "---------------------------------------------------------------------------\n" +
            "                         ELECTRONIC BOARDING PASS                          \n" +
            "---------------------------------------------------------------------------\n";
    private static final String FOOTER = "---------------------------------------------------------------------------\n";

    private final Passenger passenger;
    private final Flight flight;
    private final String seatIdentifier;
    private final String cabinClass;

    public BoardingTicket(Passenger passenger, Flight flight, String seatIdentifier, String cabinClass) {
        this.passenger = passenger;
        this.flight = flight;
        this.seatIdentifier = seatIdentifier;
        this.cabinClass = cabinClass;
    }

    public String generate() {
        StringBuilder ticket = new StringBuilder();
        appendHeader(ticket);
        appendPassengerInfo(ticket);
        appendFlightInfo(ticket);
        appendFooter(ticket);
        return ticket.toString();
    }

    private void appendHeader(StringBuilder ticket) {
        ticket.append(HEADER);
    }

    private void appendPassengerInfo(StringBuilder ticket) {
        ticket.append("Name of Passenger: ")
              .append(passenger.getLastName())
              .append(" ")
              .append(passenger.getFirstName())
              .append("\n");
    }

    private void appendFlightInfo(StringBuilder ticket) {
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
        ticket.append(FOOTER);
    }

    private String getFormattedDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
}
