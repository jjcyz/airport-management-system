package model;

/*  This class defines what a cargo aircraft can do. It is an extension of the aircraft class*/

import java.util.ArrayList;

public class CargoAircraft extends Aircraft {
    private ArrayList<Cargo> cargoOnBoard;

    // Creates a new aircraft for cargos
    public CargoAircraft(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.cargoOnBoard = new ArrayList<>();
    }

    public String addCargoToAircraft(Cargo cargo) {
        cargoOnBoard.add(cargo);
        return "Cargo has been added to " + getName();
    }

}
