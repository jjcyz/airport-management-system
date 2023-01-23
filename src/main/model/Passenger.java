package model;

public class Passenger {
    private int passengerID;
    private String fName;
    private String lName;
    private TravelClasses travelClass;

    public Passenger(int passengerID, String fName, String lName, TravelClasses travelClass) {
        this.passengerID = passengerID;
        this.fName = fName;
        this.lName = lName;
        this.travelClass = travelClass;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public TravelClasses getTravelClass() {
        return travelClass;
    }

    @Override
    public String toString(){
        return "Passenger ID: " + passengerID +
                " First Name: " + fName +
                " Last Name: " + lName +
                " Travel Class: " + travelClass;

    }


}
