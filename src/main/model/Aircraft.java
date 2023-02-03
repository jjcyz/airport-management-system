package model;

/* This class represents a plane. A plane has a name and maximum capacity */

public class Aircraft {

    private final String name;
    private final int maxCapacity;

    // Creates a new aircraft
    public Aircraft(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    // EFFECTS: returns the name of the plane
    public String getName() {
        return name;
    }

    // EFFECTS: returns the max capacity of the plane
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public String toString() {
        return "Name of Aircraft: "
                + name
                + " Maximum Capacity: "
                + maxCapacity;
    }











}
