package model;

/* This class represents a flight. A flight is made of a plane, an origin, a destination,
    the duration of flight, and the list of passengers registered on this flight. */

import java.util.ArrayList;

public class Flight {
    private String flightID;
    private final Aircraft aircraft;
    private Airports origin;
    private Airports destination;
    private int duration;
    private ArrayList<Passenger> passengersOnFlight;

    public Flight(String flightID, Aircraft aircraft, Airports origin, Airports destination, int duration) {
        this.flightID = flightID;
        this.aircraft = aircraft;
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.passengersOnFlight = new ArrayList<>();
    }

    public String getFlightID() {
        return flightID;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public Airports getOrigin() {
        return origin;
    }

    public Airports getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    // EFFECTS: returns the list of passengers on this flight
    public ArrayList<Passenger> getListOfPassengers() {
        return passengersOnFlight;
    }

    // EFFECTS: add a passenger from the plane
    public void addPassenger(Passenger passenger) {
        passengersOnFlight.add(passenger);
    }

    // EFFECTS: remove a passenger from the plane
    public void removePassenger(int passengerID) {
        for (Passenger passenger : passengersOnFlight) {
            if (passenger.getPassengerID() == passengerID) {
                System.out.println(passenger.getFirstName() + " has been removed from this flight");
                passengersOnFlight.remove(passenger);
            }
        }
    }

    // EFFECTS: checks if a passenger is registered on the plane
    public boolean isPassengerOnFlight(int passengerID) {
        for (Passenger passenger : passengersOnFlight) {
            if (passenger.getPassengerID() == passengerID) {
                System.out.println(passenger.getFirstName() + " is on registered on this flight");
                return true;
            }
        }
        return false;
    }


    // EFFECTS: returns the size of passenger on the plane
    public int getCurrentCapacity() {
        return passengersOnFlight.size();
    }

    // EFFECTS: returns the number of seats available on the plane
    public int getAvailableSeats() {
        return aircraft.getMaxCapacity() - passengersOnFlight.size();
    }

    public void setOrigin(Airports origin) {
        this.origin = origin;
    }

    public void setDestination(Airports destination) {
        this.destination = destination;
        // chgDuration(); (calculate this)
    }


//    public void chgDuration() {
//        this.duration =
//    }





}
