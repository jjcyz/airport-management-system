package model;

/* This class represents a plane. A plane has a name and maximum capacity */

import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;
import persistence.Writable;

import java.util.ArrayList;

public abstract class Aircraft implements Writable {
    protected final String identifier;
    protected final int maxCapacity;
    private Seat[][] seats;
    private int rows;
    private int columns;
    private ArrayList<Cargo> cargoOnBoard;

    protected Aircraft(String identifier, int maxCapacity) {
        this.identifier = identifier;
        this.maxCapacity = maxCapacity;
        this.cargoOnBoard = new ArrayList<>();
        initializeSeats();
    }

    private void initializeSeats() {
        // Calculate rows and columns to fit maxCapacity
        // Assuming we want roughly 6 seats per row
        this.columns = 6;
        this.rows = (int) Math.ceil((double) maxCapacity / columns);
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

    // EFFECTS: returns the seat with the given identifier, or null if not found
    public Seat getSeat(String seatIdentifier) {
        for (Seat[] seatRow : seats) {
            for (Seat seat : seatRow) {
                if (seat.getSeatIdentifier().equals(seatIdentifier)) {
                    return seat;
                }
            }
        }
        return null;
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
