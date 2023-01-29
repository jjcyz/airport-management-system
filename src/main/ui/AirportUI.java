package ui;


import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class AirportUI {
    private Scanner input;
    private ArrayList<Passenger> listOfPassengers;
    private ArrayList<Plane> listOfPlanes;
    private ArrayList<Flight> listOfFlights;

    // EFFECTS: runs the airport management information system
    public AirportUI() {
        runAirportUI();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runAirportUI() {
        boolean exitApplication = false;
        String userInput;

        init();

        while (!exitApplication) {
            displayMainMenu();
            userInput = input.next();
            userInput = userInput.toLowerCase();

            if (userInput.equals("x")) {
                exitApplication = true;
            } else {
                processUserInput(Integer.valueOf(userInput));
            }
        }

        System.out.println("\nYou have successfully logged out");
    }

    // MODIFIES: this
    // EFFECTS: initiates the system
    private void init() {
        listOfPassengers = new ArrayList<>();
        listOfPlanes = new ArrayList<>();
        listOfFlights = new ArrayList<>();
        input = new Scanner(System.in);

    }

    // MODIFIES: this ???? why not needed?
    // EFFECTS: displays options for the user
    private void displayMainMenu() {
        System.out.println("\nNavigate by selecting the number\n"
                + "1-Add new passenger info\n"
                + "2-Add new plane info\n"
                + "3-Add new flight info\n"
                + "4-Change flight info\n"
                + "5-View info\n"
                + "x-exit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processUserInput(int userInput) {
        if (userInput == 1) {
            addNewPassenger();
        } else if (userInput == 2) {
            addNewPlane();
        } else if (userInput == 3) {
            addNewFlight();
        } else if (userInput == 4) {
            modifyFlight();
        } else if (userInput == 5) {
            viewInfo();  // fix later, does not process userInput after
        } else {
            System.out.println("Input is invalid. Try again");
        }
    }

    private void addNewPassenger() {
        System.out.println("Please enter the following:");
        System.out.print("Passenger ID: ");
        int id = input.nextInt();
        System.out.print("First Name: ");
        String firstName = input.next();
        System.out.print("Last Name: ");
        String lastName = input.next();
        listOfPassengers.add(new Passenger(id, firstName, lastName, TravelClasses.BUSINESSCLASS));
        System.out.println(firstName + " " + lastName + " is now in the system!");
    }

    private void addNewPlane() {
        System.out.println("Please enter the following:");
        System.out.print("Name: ");
        String planeName = input.next();
        System.out.print("Maximum Capacity: ");
        int maxCapacity = input.nextInt();
        listOfPlanes.add(new Plane(planeName, maxCapacity));
        System.out.println(planeName + " is now in the system!");
    }

    private void addNewFlight() {
        System.out.println("Please enter the following:");
        System.out.print("FlightID: ");
        String flightID = input.next();

        System.out.print("Assign a plane: ");
        Plane workPlane = null;
        while (workPlane == null) {
            workPlane = searchForPlane(input.next());
            if (workPlane == null) {
                System.out.println("Plane not found. Try again");
            }
        }
        System.out.println("Plane found");
        System.out.println("Plane is assigned to the flight");

        System.out.print("Origin: ");
        Airports origin = Airports.valueOf(input.next());
        System.out.print("Destination: ");
        Airports destination = Airports.valueOf(input.next());
        System.out.print("Duration of flight: ");
        int duration = input.nextInt();
        listOfFlights.add(new Flight(flightID, workPlane, origin, destination, duration));
        System.out.println(flightID + " is now in the system!");
    }

    private void modifyFlight() {
        System.out.println("Search up a flight to change");
        Flight workFlight = searchForFlight(input.next());
        if (workFlight != null) {
            System.out.println("What would you like to do?");
            System.out.println("1-Modify Origin");
            System.out.println("2-Modify Destination");
            String userInput = input.next();
            if (userInput.equals("1")) {
                System.out.println("What would you like to change the origin to?");
                workFlight.setOrigin(Airports.valueOf(input.next()));
                System.out.print("Flight Origin successfully changed.");
            } else if (userInput.equals("2")) {
                System.out.println("What would you like to change the destination to?");
                workFlight.setDestination(Airports.valueOf(input.next()));
                System.out.print("Flight Origin successfully changed.");
            } else {
                System.out.println("Invalid entry. Try again");
            }
        }
    }

    // following 2 methods needs to be refactored
    private Flight searchForFlight(String userInput) {
        for (Flight flight : listOfFlights) {
            if (userInput.equals(flight.getFlightID())) {
                System.out.println("Flight " + flight.getFlightID() + " found.");
                return flight;
            }
        }
        System.out.println("Flight " + userInput + " not found.");
        return null;
    }

    private Plane searchForPlane(String userInput) {
        for (Plane plane : listOfPlanes) {
            if (userInput.equals(plane.getName())) {
                System.out.println("Flight " + plane.getName() + " found.");
                return plane;
            }
        }
        System.out.println("Plane " + userInput + " not found.");
        return null;
    }

    private void viewInfo() {
        System.out.println("\nWhat would you like to view?"
                + "\n1-Passenger Info"
                + "\n2-Plane Info"
                + "\n3-All Flight Info"
                + "\n4-Main menu");

        int userInput = input.nextInt();
        if (userInput == 1) {
            getListOfPassengers();
        } else if (userInput == 2) {
            getListOfPlanes();
        } else if (userInput == 3) {
            getListOfFlights();
        } else {
            System.out.println("Invalid entry. Try Again");
        }
    }


    private ArrayList<Passenger> getListOfPassengers() {
        return listOfPassengers;
    }

    private ArrayList<Plane> getListOfPlanes() {
        return listOfPlanes;
    }

    private ArrayList<Flight> getListOfFlights() {
        return listOfFlights;
    }

}
