package model;

import java.util.ArrayList;

public class PassengerAirline extends Aircraft {
    private final ArrayList<Passenger> listOfPassenger;

    // Creates an aircraft for passengers
    public PassengerAirline(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.listOfPassenger = new ArrayList<>();
    }

}
