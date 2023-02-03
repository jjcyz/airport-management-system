package model;

/* This class represents a flight. A flight is made of an aircraft, an origin, a destination,
    the duration of flight, and the list of passengers registered on this flight. */

import java.util.ArrayList;

public class Flight {
    private final String flightID;
    private final Aircraft aircraft;
    private Airports origin;
    private Airports destination;
    private int duration;
    private final ArrayList<Passenger> passengersOnFlight;

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
    public String getListOfPassengers() {
        for (Passenger passenger : passengersOnFlight) {
            return passenger.toString();
        }
        return passengersOnFlight.toString();
    }

    // EFFECTS: add a passenger from the aircraft
    public void addPassenger(Passenger passenger) {
        if (getCurrentCapacity() < aircraft.getMaxCapacity()) {
            passengersOnFlight.add(passenger);
            passenger.getBookedFlights().add(this);
            System.out.println(passenger.getFirstName() + " has been added to this flight");
        } else {
            System.out.println("The aircraft for this flight is at maximum capacity.");
        }

    }

    // EFFECTS: remove a passenger from the aircraft
    public void removePassenger(int passengerID) {
        for (Passenger passenger : passengersOnFlight) {
            if (passenger.getPassengerID() == passengerID) {
                passengersOnFlight.remove(passenger);
                passenger.getBookedFlights().remove(this);
                System.out.println(passenger.getFirstName() + " has been removed from this flight");
            }
        }
    }

    // EFFECTS: checks if a passenger is registered on the aircraft
    public boolean isPassengerOnFlight(int passengerID) {
        for (Passenger passenger : passengersOnFlight) {
            if (passenger.getPassengerID() == passengerID) {
                System.out.println(passenger.getFirstName() + " is on registered on this flight");
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns the size of passenger on the aircraft
    public int getCurrentCapacity() {
        return passengersOnFlight.size();
    }

    // EFFECTS: returns the number of seats available on the aircraft
    public int getAvailableSeats() {
        return aircraft.getMaxCapacity() - passengersOnFlight.size();
    }

    public void setOrigin(Airports origin) {
        this.origin = origin;
    }

    public void setDestination(Airports destination) {
        this.destination = destination;
        this.duration += 2; // or chgDuration(); (calculate this, would be a cool feature)
    }

    @Override
    public String toString() {
        return "Flight ID: "
                + flightID
                + " Aircraft: "
                + aircraft
                + " Origin: "
                + origin
                + " Destination: "
                + destination
                + " Duration: "
                + duration;
    }






}
