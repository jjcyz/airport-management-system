package ui;

import model.Airports;
import model.Flight;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class SearchFlights {
    private DefaultListModel<Flight> listOfFlights;
    private Scanner scanner;

    public SearchFlights(DefaultListModel<Flight> listOfFlights) {
        this.listOfFlights = listOfFlights;
    }

    // MODIFIES: this
    // EFFECTS: searches for the flight in the system
    private Flight searchForFlight(String userInput) {
        while (true) {
            for (int i = 0; i < listOfFlights.size(); i++) {
                Flight flight = listOfFlights.getElementAt(i);
                if (userInput.equals(flight.getFlightID())) {
                    System.out.println("Flight " + flight.getFlightID() + " found.");
                    return flight;
                }
            }
            System.out.println("Flight " + userInput + " not found.");
            // userInput = input.nextLine();
        }
    }

    // MODIFIES: this
    // EFFECTS: finds the airport in the enum Airports class
    private Airports getAirports(String userInput) {
        List<Airports> availableAirports = Airports.getAirports();
        while (true) {
            try {
                Airports airport = Airports.valueOf(userInput);
                if (availableAirports.contains(airport)) {
                    System.out.println("Airport found!");
                    return airport;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Sorry. This airport does not currently have routes there. Try again.");
                // userInput = input.next();
            }
        }
    }
}
