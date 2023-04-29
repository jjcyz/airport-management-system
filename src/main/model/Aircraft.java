package model;

/* This class represents a plane. A plane has a name and maximum capacity */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;
import persistence.Writable;

import java.util.ArrayList;

public class Aircraft implements Writable  {  // make aircraft abstract later

    protected final String identifier;
    protected final int maxCapacity; // total seats
    private Seat[][] seats;
    private int rows = 10;
    private int columns = 6;
    private ArrayList<Cargo> cargoOnBoard;

    // Creates a new aircraft
    public Aircraft(String identifier) {
        this.identifier = identifier;
        this.maxCapacity = rows * columns;
        this.cargoOnBoard = new ArrayList<>();
        this.seats = new Seat[rows][columns];

        // Generates the seats
        char seatLetter = 'A';
        int seatNumber = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new Seat(String.valueOf(seatLetter) + seatNumber);
                seatNumber++;
            }
            seatLetter++;
            seatNumber = 1;
        }
    }

    public int getRow() {
        return rows;
    }

    public int getColumn() {
        return columns;
    }

    // EFFECTS: returns the name of the plane
    public String getIdentifier() {
        return identifier;
    }

    public void printSeats() {
        for (Seat[] seatRow : seats) {
            for (Seat seat : seatRow) {
                System.out.print(seat.getSeatIdentifier() + " ");
            }
            System.out.println(); // Start a new line after each row
        }
    }

    // EFFECTS: returns the max capacity of the plane
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // EFFECTS: adds cargo to the cargo aircraft
    public String addCargoToAircraft(Cargo cargo) {
        EventLog.getInstance().logEvent(new Event("Added cargo: " + cargo));
        cargoOnBoard.add(cargo);
        return "Cargo has been added to " + getIdentifier();
    }

    @Override
    public String toString() {
        return "Identifier: "
                + identifier
                + " Max Capacity: "
                + maxCapacity;
    }

    // EFFECTS: returns json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("identifier", this.identifier);
        json.put("maxCapacity", this.maxCapacity);
        return json;
    }
}
