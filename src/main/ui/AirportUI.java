package ui;


import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirportUI {
    private Scanner input;
    private ArrayList<Passenger> listOfPassengers;
    private ArrayList<Plane> listOfPlanes;
    private ArrayList<Flight> listOfFlights;
    boolean error = false;

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
            try {
                displayMainMenu();
                userInput = input.next();
                userInput = userInput.toLowerCase();

                if (userInput.equals("x")) {
                    exitApplication = true;
                } else {
                    processUserInput(Integer.valueOf(userInput));
                }
            } catch (Exception e) {
                System.out.println("Error 1");
                error = true;
                input.next();
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
                + "1. Add new passenger info\n"
                + "2. Add new plane info\n"
                + "3. Create new flight\n"
                + "4. Change existing flight info\n"
                + "5. View data\n"
                + "x - exit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processUserInput(int userInput) {
        try {
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
            }

        } catch (Exception e) {
            System.out.println("Error: processUserInput");
            error = true;
        }
    }

    private void addNewPassenger() {
        System.out.println("Please enter the following:");
        int id = getValidInteger("Passenger ID: ");
        String firstName = getValidString("First Name: ");
        String lastName = getValidString("Last Name: ");
        TravelClasses chosenClass = getTravelClasses("Please select a travel class: ");
        listOfPassengers.add(new Passenger(id, firstName, lastName, chosenClass));
        System.out.println(firstName + " " + lastName + " is now in the system!");
    }

    private int getValidInteger(String prompt) {
        int userInputInt = 0;
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                userInputInt = input.nextInt();
                input.nextLine();
                break;
            } else {
                System.out.println("Error: Input must be an integer.");
                input.next();
            }
        }
        return userInputInt;
    }

    private String getValidString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = input.next();
            if (name.matches("[a-zA-Z]+")) {
                return name;
            } else {
                System.out.println("Error: Name must contain only letters.");
            }
        }
    }

    private TravelClasses getTravelClasses(String prompt) {
        System.out.println(prompt);
        List<TravelClasses> availableClasses = TravelClasses.getTravelClasses();
        for (int i = 0; i < availableClasses.size(); i++) {
            System.out.println((i + 1) + ". " + availableClasses.get(i));
        }
        while (true) {
            int userChoice = input.nextInt();
            if (userChoice >= 1 && userChoice <= 4) {
                TravelClasses chosenClass = availableClasses.get(userChoice - 1);
                return chosenClass;
            } else {
                System.out.println("Error: Invalid choice");
            }
        }
    }

    private void addNewPlane() {
        System.out.println("Please enter the following:");
        System.out.print("Enter the name of the plane: ");
        String planeName = input.next();
        int maxCapacity = getValidInteger("Maximum Capacity: ");
        listOfPlanes.add(new Plane(planeName, maxCapacity));
        System.out.println(planeName + " is now in the system!");

    }

    private void addNewFlight() {
        try {
            System.out.print("Create a unique FlightID: ");
            String flightID = input.next();

            System.out.print("Assign a plane: ");
            Plane workPlane = searchForPlane(input.next());

            System.out.print("Origin: ");
            System.out.println("(ie. YVR, YYZ, BEK, LAX)");
            Airports origin = getAirports(input.next());

            System.out.print("Destination: ");
            Airports destination = getAirports(input.next());

            int duration = getValidInteger("Duration of flight: ");

            listOfFlights.add(new Flight(flightID, workPlane, origin, destination, duration));
            System.out.println("Flight " + flightID + " is now in the system!");
        } catch (Exception e) {
            System.out.println("Invalid. Please try again");
            error = true;
        }

    }

    private Airports getAirports(String userInput) {
        List<Airports> availableAirports = Airports.getAirports();
        while (true) {
            try {
                Airports airport = Airports.valueOf(userInput);
                if (availableAirports.contains(airport)) {
                    System.out.println("Airport found");
                    return airport;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Sorry. This airport does not currently have routes there. Try again.");
                userInput = input.next();
            }
        }
    }

    private void modifyFlight() {
        System.out.println("Search up a flight to change. Enter the FlightID: ");
        Flight workFlight = searchForFlight(input.next());
        System.out.println("What would you like to do?");
        System.out.println("1. Modify Origin");
        System.out.println("2. Modify Destination");
        int userInput = input.nextInt();
        while (true) {
            if (userInput == 1) {
                System.out.println("New origin: ");
                workFlight.setOrigin(getAirports(input.next()));
                System.out.print("Flight origin successfully changed.");
                break;
            } else if (userInput == 2) {
                System.out.println("New destination: ");
                workFlight.setDestination(getAirports(input.next()));
                System.out.print("Flight destination successfully changed.");
                break;
            }
            System.out.println("Invalid entry. Try again");
            userInput = input.nextInt();
        }
    }

    // following 2 methods needs to be refactored
    private Flight searchForFlight(String userInput) {
        while (true) {
            for (Flight flight : listOfFlights) {
                if (userInput.equals(flight.getFlightID())) {
                    System.out.println("Flight " + flight.getFlightID() + " found.");
                    return flight;
                }
            }
            System.out.println("Flight " + userInput + " not found.");
            //userInput = input.nextLine();
        }
    }


    private Plane searchForPlane(String userInput) {
        while (true) {
            for (Plane plane : listOfPlanes) {
                if (userInput.equals(plane.getName())) {
                    System.out.println("Plane found.");
                    return plane;
                }
            }
            System.out.println("Plane not found. Try again");
            userInput = input.next();
        }
    }

    private void viewInfo() {
        System.out.println("\nWhat would you like to view?"
                + "\n1. Passenger Info"
                + "\n2. Plane Info"
                + "\n3. All Flight Info"
                + "\n4. Main menu");

        int userInput = input.nextInt();
        if (userInput == 1) {
            viewListOfPassengers();
        } else if (userInput == 2) {
            viewListOfPlanes();
        } else if (userInput == 3) {
            viewListOfFlights();
        } else if (userInput == 4) {
            displayMainMenu();
        } else {
            System.out.println("Invalid entry. Try Again");
        }
    }

    // can be refactored, code repeats
    private ArrayList<Passenger> viewListOfPassengers() {
        for (Passenger passenger : listOfPassengers) {
            System.out.println(passenger);
        }
        return listOfPassengers;
    }

    private ArrayList<Plane> viewListOfPlanes() {
        System.out.println(listOfPlanes);
        return listOfPlanes;
    }

    private ArrayList<Flight> viewListOfFlights() {
        System.out.print(listOfFlights);
        return listOfFlights;
    }

}
