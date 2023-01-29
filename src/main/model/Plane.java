package model;

/* This class represents a plane. A plane has a name and maximum capacity */

import java.util.ArrayList;

public class Plane {
    // delete or rename this class!
    private final String name;
    private final int maxCapacity;


    // Creates a new plane
    public Plane(String name, int maxCapacity) {
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









}
