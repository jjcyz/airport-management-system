package model;

/*  This class defines what a cargo aircraft can do. It is an extension of the aircraft class*/

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

public class CargoAircraft extends Aircraft implements Writable {
    private final ArrayList<Cargo> cargoOnBoard;

    public CargoAircraft(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.cargoOnBoard = new ArrayList<>();
    }

    // EFFECTS: adds cargo to the cargo aircraft
    public String addCargoToAircraft(Cargo cargo) {
        EventLog.getInstance().logEvent(new Event("Added cargo: " + cargo));
        cargoOnBoard.add(cargo);
        return "Cargo has been added to " + getName();
    }

    // EFFECTS: returns json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("maxCargoWeight", this.maxCapacity);

        JSONArray cargoArray = new JSONArray();
//        for (Cargo cargo : this.cargoOnBoard) {
//            cargoArray.put(cargo.toJson());
//        }
//        json.put("cargoList", cargoArray);

        return json;
    }
}
