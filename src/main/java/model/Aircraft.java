package model;

/* This class represents a plane. A plane has a name and maximum capacity */

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Event;
import persistence.EventLog;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Aircraft implements Writable {
    protected final String identifier;
    protected final int maxCapacity;
    private final SeatLayout seatLayout;
    private final CargoManager cargoManager;
    private final Map<String, Seat> seatMap;

    protected Aircraft(String identifier, int maxCapacity) {
        this.identifier = identifier;
        this.maxCapacity = maxCapacity;
        this.cargoManager = new CargoManager();
        this.seatLayout = new SeatLayout(maxCapacity);
        this.seatMap = initializeSeatMap();
    }

    private Map<String, Seat> initializeSeatMap() {
        Map<String, Seat> map = new HashMap<>();
        for (Seat[] row : seatLayout.getSeats()) {
            for (Seat seat : row) {
                map.put(seat.getSeatIdentifier(), seat);
            }
        }
        return map;
    }

    public int getRow() {
        return seatLayout.getRows();
    }

    public int getColumn() {
        return seatLayout.getColumns();
    }

    // EFFECTS: returns the name of the plane
    public String getIdentifier() {
        return identifier;
    }

    public void printSeats() {
        seatLayout.printSeats();
    }

    // EFFECTS: returns the max capacity of the plane
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // EFFECTS: returns the seat with the given identifier, or null if not found
    public Seat getSeat(String seatIdentifier) {
        return seatMap.get(seatIdentifier);
    }

    // EFFECTS: adds cargo to the cargo aircraft
    public String addCargoToAircraft(Cargo cargo) {
        return cargoManager.addCargo(cargo, this);
    }

    public ArrayList<Cargo> getCargoOnBoard() {
        return cargoManager.getCargoList();
    }

    @Override
    public String toString() {
        return String.format("Identifier: %s, Max Capacity: %d", identifier, maxCapacity);
    }

    // EFFECTS: returns json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("identifier", identifier);
        json.put("maxCapacity", maxCapacity);
        json.put("cargo", cargoManager.toJsonArray());
        json.put("type", getClass().getSimpleName());
        return json;
    }

    private static class SeatLayout {
        private final Seat[][] seats;
        private final int rows;
        private final int columns;

        public SeatLayout(int maxCapacity) {
            this.columns = 6;
            this.rows = (int) Math.ceil((double) maxCapacity / columns);
            this.seats = initializeSeats();
        }

        private Seat[][] initializeSeats() {
            Seat[][] seatArray = new Seat[rows][columns];
            char seatLetter = 'A';
            int seatNumber = 1;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    seatArray[i][j] = new Seat(String.valueOf(seatLetter) + seatNumber);
                    seatNumber++;
                }
                seatLetter++;
                seatNumber = 1;
            }
            return seatArray;
        }

        public void printSeats() {
            for (Seat[] row : seats) {
                for (Seat seat : row) {
                    System.out.print(seat.getSeatIdentifier() + " ");
                }
                System.out.println();
            }
        }

        public Seat[][] getSeats() {
            return seats;
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }
    }

    private static class CargoManager {
        private final ArrayList<Cargo> cargoList;

        public CargoManager() {
            this.cargoList = new ArrayList<>();
        }

        public String addCargo(Cargo cargo, Aircraft aircraft) {
            EventLog.getInstance().logEvent(new Event("Added cargo: " + cargo));
            cargoList.add(cargo);
            return "Cargo has been added to " + aircraft.getIdentifier();
        }

        public ArrayList<Cargo> getCargoList() {
            return cargoList;
        }

        public JSONArray toJsonArray() {
            JSONArray jsonArray = new JSONArray();
            for (Cargo cargo : cargoList) {
                jsonArray.put(cargo.toJson());
            }
            return jsonArray;
        }
    }
}
