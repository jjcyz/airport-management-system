package model;

import java.util.ArrayList;

public class Plane {
    // delete or rename this class!
    private String name;
    private int maxCapacity;
    private ArrayList<Passenger> listOfPassengers;

    // Creates a new plane
    public Plane(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = 0;
        this.listOfPassengers = new ArrayList<Passenger>();
    }

    // EFFECTS: returns the name of the plane
    public String getName() {
        return name;
    }

    // EFFECTS: returns the max capacity of the plane
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // EFFECTS: add a passenger from the plane
    public void addPassenger(Passenger passenger) {
        listOfPassengers.add(passenger);
    }

    // EFFECTS: remove a passenger from the plane
    public void remove(int passengerID) {
        for (int i = 0; i <= listOfPassengers.size(); i++)
            if (listOfPassengers.get(i).getPassengerID() == passengerID) {
                System.out.println(listOfPassengers.get(i).getfName() + " has been removed from " + this.name);
                listOfPassengers.remove(listOfPassengers.get(i));
        }
    }

    // EFFECTS: returns the size of passenger on the plane
    public int getCurrentCapacity() {
        return listOfPassengers.size();
    }

    // EFFECTS: returns the list of passengers on the plane
    public void getListOfPassengers() {
        for (int i = 0; i < listOfPassengers.size(); i++) {
            System.out.println(listOfPassengers.get(i).toString());
        }

    }




}
