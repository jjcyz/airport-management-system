package ui;

/* This is the Main class of the program */


import model.Aircraft;
import model.AircraftFactory;
import model.AircraftType;
import model.ConcreteAircraftFactory;

public class Main {

    public static void main(String[] args) {
        AircraftFactory factory = new ConcreteAircraftFactory();
        Aircraft ac = factory.createAircraft(AircraftType.PASSENGER_AIRLINE, "Jessica", 60);
        ac.printSeats();
        new MainDashboard();
    }



}
