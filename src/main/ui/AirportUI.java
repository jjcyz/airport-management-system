package ui;

/* This is the UI class of the program */

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirportUI {
    private Scanner input;
    private ArrayList<Passenger> listOfPassengers;
    private ArrayList<Aircraft> listOfAircraft;
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
            testMethod();
            try {
                displayMainMenu();
                userInput = input.next();
                userInput = userInput.toLowerCase();

                if (userInput.equals("x")) {
                    exitApplication = true;
                } else {
                    processUserInput(Integer.parseInt(userInput));
                }
            } catch (Exception e) {
                System.out.println("Error: Main Menu");
                error = true;
                input.next();
            }

        }
        System.out.println("\nYou have successfully logged out");
    }

    private void testMethod() {
        Passenger testPassenger = new Passenger(12345,"Jessica","Zhou",TravelClasses.FIRSTCLASS);
        Aircraft boeing = new PassengerAirline("Boeing", 120);
        Flight testFlight = new Flight("1234", boeing, Airports.PEK, Airports.PEK, 4);
        testFlight.addPassenger(testPassenger);
        System.out.println(testPassenger.getBoardingTickets());
    }

    // MODIFIES: this
    // EFFECTS: initiates the system
    private void init() {
        listOfPassengers = new ArrayList<>();
        listOfAircraft = new ArrayList<>();
        listOfFlights = new ArrayList<>();
        input = new Scanner(System.in);

    }

    // EFFECTS: displays options for the user
    private void displayMainMenu() {
        System.out.println("\nNavigate by entering the number\n"
                + "1. Add new passenger\n"
                + "2. Add new aircraft\n"
                + "3. Create new flight\n"
                + "4. Change existing flight\n"
                + "5. Manage passenger and flight(s)\n"
                + "6. View data\n"
                + "Press x to exit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processUserInput(int userInput) {
        try {
            if (userInput == 1) {
                addNewPassenger();
            } else if (userInput == 2) {
                addNewAircraft();
            } else if (userInput == 3) {
                addNewFlight();
            } else if (userInput == 4) {
                modifyFlight();
            } else if (userInput == 5) {
                managePassengerFlights();
                addPassengerToFlight();
            } else if (userInput == 6) {
                viewData(); // fix later, does not process userInput after
            }

        } catch (Exception e) {
            System.out.println("Error: processUserInput");
            error = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new passenger into system
    private void addNewPassenger() {
        System.out.println("Please enter the following:");
        int id = getValidInteger("PassengerID: ");
        String firstName = getValidString("First Name: ");
        String lastName = getValidString("Last Name: ");
        TravelClasses chosenClass = getTravelClasses();
        listOfPassengers.add(new Passenger(id, firstName, lastName, chosenClass));
        System.out.println(firstName + " " + lastName + " is now in the system!");
    }

    // MODIFIES: this
    // EFFECTS: checks that user input is an integer
    private int getValidInteger(String prompt) {
        int userInputInt;
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

    // MODIFIES: this
    // EFFECTS: checks that user input is a string
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

    // EFFECTS: gets the seat class options on an aircraft
    private TravelClasses getTravelClasses() {
        System.out.println("Please select a travel class: ");
        List<TravelClasses> availableClasses = TravelClasses.getTravelClasses();
        for (int i = 0; i < availableClasses.size(); i++) {
            System.out.println((i + 1) + ". " + availableClasses.get(i));
        }
        while (true) {
            int userChoice = input.nextInt();
            if (userChoice >= 1 && userChoice <= 4) {
                return availableClasses.get(userChoice - 1);
            } else {
                System.out.println("Error: Invalid choice");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new aircraft into the system
    private void addNewAircraft() {
        System.out.println("Please enter the following:");
        System.out.print("Enter the name of the aircraft: ");
        String aircraftName = input.next();
        int maxCapacity = getValidInteger("Maximum Capacity: ");
        System.out.print("Select the type of aircraft: ");
        System.out.println("\n1. Cargo Aircraft" + "\n2. Passenger Aircraft" + "\n3. Private Jet");
        int userInput = input.nextInt();
        while (true) {
            try {
                if (userInput == 1) {
                    listOfAircraft.add(new CargoAircraft(aircraftName, maxCapacity));
                } else if (userInput == 2) {
                    listOfAircraft.add(new PassengerAirline(aircraftName, maxCapacity));
                } else {
                    listOfAircraft.add(new PrivateJet(aircraftName, maxCapacity));
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid Aircraft type. Please try again");
                error = true;
                userInput = input.nextInt();
            }
            System.out.println(aircraftName + " is now in the system!");

        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new flight in the system
    private void addNewFlight() {
        try {
            System.out.print("Create a unique FlightID: ");
            String flightID = input.next();

            System.out.print("Assign an aircraft: ");
            Aircraft workAircraft = searchForAircraft(input.next());

            System.out.print("Origin: ");
            System.out.println("(ie. YVR, YYZ, BEK, LAX, HKG)");
            Airports origin = getAirports(input.next());

            System.out.print("Destination: ");
            Airports destination = getAirports(input.next());

            int duration = getValidInteger("Duration of flight: ");

            listOfFlights.add(new Flight(flightID, workAircraft, origin, destination, duration));
            System.out.println("Flight " + flightID + " is now in the system!");
        } catch (Exception e) {
            System.out.println("Invalid. Please try again");
            error = true;
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
                userInput = input.next();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the origin/destination of a flight
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

    // MODIFIES: this
    // EFFECTS: searches for the flight in the system
    private Flight searchForFlight(String userInput) {
        while (true) {
            for (Flight flight : listOfFlights) {
                if (userInput.equals(flight.getFlightID())) {
                    System.out.println("Flight " + flight.getFlightID() + " found.");
                    return flight;
                }
            }
            System.out.println("Flight " + userInput + " not found.");
            userInput = input.nextLine();
        }
    }

    // MODIFIES: this
    // EFFECTS: searches for the aircraft in the system
    private Aircraft searchForAircraft(String userInput) {
        while (true) {
            for (Aircraft aircraft : listOfAircraft) {
                if (userInput.equals(aircraft.getName())) {
                    System.out.println("Aircraft found.");
                    return aircraft;
                }
            }
            System.out.println("Aircraft not found. Try again");
            userInput = input.next();
        }
    }

    // MODIFIES: this
    // EFFECTS: manages the options regarding passengers and flights
    private void managePassengerFlights() {
        while (true) {
            System.out.print("\nWhat would you like to do: ");
            System.out.println("\n1. Add passenger on a flight"
                    + "\n2. Remove passenger from a flight"
                    + "\n3. Print boarding tickets"
                    + "\n4. Go back to Main Menu");
            int userInput = input.nextInt();
            try {
                if (userInput == 1) {
                    addPassengerToFlight();
                } else if (userInput == 2) {
                    removePassengerFromFlight();
                } else if (userInput == 3) {
                    viewPassengerFlights();
                } else if (userInput == 4) {
                    displayMainMenu();
                }
            } catch (Exception e) {
                System.out.println("Error: UserInput invalid");
            }

        }
    }

    // MODIFIES: the list of passengers on a flight
    // EFFECTS: add a passenger to a flight
    private void addPassengerToFlight() {
        System.out.println("Search for a flight (FlightID): ");
        Flight flight = searchForFlight(input.next());
        System.out.println("Enter passenger (passengerID) to book: ");
        Passenger passenger = searchForPassenger(input.nextInt());
        flight.addPassenger(passenger);
        System.out.println(passenger + " has been booked onto " + flight);
    }

    // MODIFIES: the list of passengers on a flight
    // EFFECTS: removes a passenger to a flight
    private void removePassengerFromFlight() {
        System.out.println("Search for the flight (FlightID): ");
        Flight flight = searchForFlight(input.next());
        System.out.println("Enter the passenger (passengerID) to remove: ");
        Passenger passenger = searchForPassenger(input.nextInt());
        flight.addPassenger(passenger);
        System.out.println(passenger + " has been removed from " + flight);
    }

    // EFFECTS: gets information about each flight about a passenger
    private void viewPassengerFlights() {
        System.out.println("Enter the PassengerID: ");
        Passenger passenger = searchForPassenger(input.nextInt());
        System.out.println(passenger.getBoardingTickets());
    }

    // MODIFIES: this
    // EFFECTS: searches for the passenger in the system
    private Passenger searchForPassenger(int userInput) {
        while (true) {
            for (Passenger passenger : listOfPassengers) {
                if (userInput == passenger.getPassengerID()) {
                    System.out.println("Passenger " + passenger.getPassengerID() + " found.");
                    return passenger;
                }
            }
            System.out.println("Passenger " + userInput + " not found.");
            userInput = input.nextInt();
        }
    }

    // EFFECTS: outputs all data in the system based on category
    private void viewData() {
        System.out.println("\nWhat would you like to view?"
                + "\n1. Passenger Info"
                + "\n2. Aircraft Info"
                + "\n3. All Flight Info"
                + "\n4. Go back to Main Menu");

        int userInput = input.nextInt();
        if (userInput == 1) {
            getListOfPassengers();
        } else if (userInput == 2) {
            getListOfAircraft();
        } else if (userInput == 3) {
            getListOfFlights();
        } else if (userInput == 4) {
            displayMainMenu();
        } else {
            System.out.println("Invalid entry. Try Again");
        }
    }

    // can be refactored, 3 methods repeat here
    // EFFECTS: returns the list of all existing passengers
    private void getListOfPassengers() {
        for (Passenger passenger : listOfPassengers) {
            System.out.println(passenger.toString());
        }
    }

    // EFFECTS: returns the list of all existing aircraft
    private void getListOfAircraft() {
        for (Aircraft aircraft : listOfAircraft) {
            System.out.println(aircraft.toString());
        }
    }

    // EFFECTS: returns the list of all existing flights
    private void getListOfFlights() {
        for (Flight flight : listOfFlights) {
            System.out.println(flight.toString());
        }
    }



}
