package model;

/*  This class represents a passenger. The passenger has a passenger ID, first name,
    last name, and the travel class. A passenger is the smallest abstraction in the program
    that can be added to a plane  */

public class Passenger {
    private final int passengerID;
    private final String firstName;
    private final String lastName;
    private TravelClasses travelClass;

    public Passenger(int passengerID, String firstName, String lastName, TravelClasses travelClass) {
        this.passengerID = passengerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.travelClass = travelClass;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public TravelClasses getTravelClass() {
        return travelClass;
    }

    @Override
    public String toString() {
        return "Passenger ID: "
                + passengerID
                + " First Name: "
                + firstName
                + " Last Name: "
                + lastName
                + " Travel Class: "
                + travelClass;

    }


}
