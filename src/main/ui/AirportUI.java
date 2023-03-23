package ui;

/* This is the UI class of the program */

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Represents the airport application
public class AirportUI extends JFrame implements Writable {
    private static final String JSON_STORE = "./data/airport.json";
    private Scanner input;
    private DefaultListModel<Passenger> listOfPassengers;
    private DefaultListModel<Aircraft> listOfAircraft;
    private DefaultListModel<Flight> listOfFlights;
    boolean error = false;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel mainWindow;
    private JLabel label;

    private ImageIcon airportImage;

    // Makes a new JFrame with different attributes
    public AirportUI() {
        super("Airport Management System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));

        initializeBackend();
        initializeHeader();
        initializeMainScreen();
        initializeButtons();
        pack();
        setVisible(true);

        startLoadPrompt();
        exitSavePrompt();

        setVisible(true);
        JScrollPane scrollPane = setUpPassengerPane();
        add(scrollPane);
    }

    // MODIFIES: this
    // EFFECTS: initiates the system
    private void initializeBackend() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        listOfPassengers = new DefaultListModel<>();
        listOfAircraft = new DefaultListModel<>();
        listOfFlights = new DefaultListModel<>();

        airportImage = new ImageIcon(".fill in later");

    }

    // MODIFIES: this
    // EFFECTS: initializes the 3 header panes
    private void initializeHeader() {
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new FlowLayout());

        JPanel header1 = initializeTotalPassengerBox();
        JPanel header2 = initializeTotalAircraftBox();
        JPanel header3 = initializeTotalFlightsBox();

        mainMenu.add(header1);
        mainMenu.add(header2);
        mainMenu.add(header3);

        add(mainMenu, BorderLayout.PAGE_START);
    }

    // MODIFIES: this
    // EFFECTS: Makes the main menu panel and changes the background color
    public JPanel initializeTotalPassengerBox() {
        JLabel totalPassengers = new JLabel("Total Passengers: " + listOfPassengers.size());
        JPanel totalPassengersBox = new JPanel();
        totalPassengersBox.add(totalPassengers);

        totalPassengersBox.setBackground(Color.lightGray);
        totalPassengersBox.setPreferredSize(new Dimension(200, 30));
        add(totalPassengersBox);

        return totalPassengersBox;
    }

    public JPanel initializeTotalAircraftBox() {
        JLabel totalAircraft = new JLabel("Total Aircraft: " + listOfAircraft.size());
        JPanel totalAircraftBox = new JPanel();
        totalAircraftBox.add(totalAircraft);

        totalAircraftBox.setBackground(Color.lightGray);
        totalAircraftBox.setPreferredSize(new Dimension(200, 30));
        add(totalAircraftBox);

        return totalAircraftBox;
    }

    public JPanel initializeTotalFlightsBox() {
        JLabel totalFlights = new JLabel("Total Flights: " + listOfFlights.size());
        JPanel totalFlightsBox = new JPanel();
        totalFlightsBox.add(totalFlights);

        totalFlightsBox.setBackground(Color.lightGray);
        totalFlightsBox.setPreferredSize(new Dimension(200, 30));
        add(totalFlightsBox);

        return totalFlightsBox;
    }

    // MODIFIES: this
    // EFFECTS: initializes triple split panes
    private void initializeMainScreen() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));

        JScrollPane passengerScrollPane = setUpPassengerPane();
        JScrollPane aircraftScrollPane = setUpAircraftPane();
        JScrollPane flightsScrollPane = setUpFlightsPane();

        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1.setOneTouchExpandable(true);
        splitPane1.setLeftComponent(passengerScrollPane);
        splitPane1.setRightComponent(aircraftScrollPane);
        splitPane1.setDividerLocation(0.33);

        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setLeftComponent(splitPane1);
        splitPane2.setRightComponent(flightsScrollPane);
        splitPane1.setDividerLocation(0.67);

        mainPanel.add(splitPane2);

        mainPanel.add(passengerScrollPane);
        mainPanel.add(aircraftScrollPane);
        mainPanel.add(flightsScrollPane);

        mainPanel.setMinimumSize(new Dimension(700, 300));
        mainPanel.setPreferredSize(new Dimension(1000, 300));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(mainPanel, BorderLayout.CENTER);

    }

    // MODIFIES: this
    // EFFECTS: setup configurations for passenger pane
    private JScrollPane setUpPassengerPane() {
        JList<Passenger> passengerJList = new JList<>(listOfPassengers);
        passengerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passengerJList.setSelectedIndex(0);
        // passengerJList.addListSelectionListener(this);
        passengerJList.setVisibleRowCount(10);
        passengerJList.setCellRenderer(new CellRenderer());
        JScrollPane passengerScrollPane = new JScrollPane(passengerJList);

        return passengerScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: setup configurations for aircraft pane
    private JScrollPane setUpAircraftPane() {
        JList<Aircraft> aircraftJList = new JList<>(listOfAircraft);
        aircraftJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aircraftJList.setSelectedIndex(0);
        aircraftJList.setVisibleRowCount(10);
        aircraftJList.setCellRenderer(new CellRenderer());

        JScrollPane aircraftScrollPane = new JScrollPane(aircraftJList);
        aircraftScrollPane.createVerticalScrollBar();
        aircraftScrollPane.setHorizontalScrollBar(null);

        return aircraftScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: setup configurations for flights pane
    private JScrollPane setUpFlightsPane() {
        JList<Flight> flightsJList = new JList<>(listOfFlights);
        flightsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsJList.setSelectedIndex(0);
        flightsJList.setVisibleRowCount(10);
        flightsJList.setCellRenderer(new CellRenderer());

        JScrollPane flightsScrollPane = new JScrollPane(flightsJList);
        flightsScrollPane.createVerticalScrollBar();
        flightsScrollPane.setHorizontalScrollBar(null);

        return flightsScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: initializes buttons
    private void initializeButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton addNewPassenger = new JButton("Add new passenger");
        addNewPassenger.setActionCommand("1. Add new passenger");
        addNewPassenger.addActionListener(new ButtonListener());

        JButton addNewAircraft = new JButton("Add new aircraft");
        addNewAircraft.setActionCommand("2. Add new aircraft");
        addNewAircraft.addActionListener(new ButtonListener());

        JButton createNewFlight = new JButton("Create new flight");
        createNewFlight.setActionCommand("3. Create new flight");
        createNewFlight.addActionListener(new ButtonListener());

        // add other methods later

        JButton saveDatabaseToFile = new JButton("Save database to file");
        saveDatabaseToFile.setActionCommand("7. Save");
        saveDatabaseToFile.addActionListener(new ButtonListener());

        buttons.add(addNewPassenger);
        buttons.add(addNewAircraft);
        buttons.add(createNewFlight);
        buttons.add(saveDatabaseToFile);
        add(buttons, BorderLayout.PAGE_END);
    }

    // updating stuff goes here, backend and gui
    private void updateMainScreen() {

    }

    // creates Action Listener for button presses
    class ButtonListener implements ActionListener {

        // EFFECTS: processes button clicks and runs appropriate methods
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            switch (actionEvent.getActionCommand()) {
                case "1. Add new passenger":
                    addNewPassenger();
                    break;
                case "2. Add new aircraft":
                    addNewAircraft();
                    break;
                case "3. Create new flight":
                    addNewFlight();
                    break;
                case "4. Change existing flight":
                    modifyFlight();   // continue here
                    break;
                case "5. Manage passenger and flight(s)":
                    managePassengerFlights();
                    break;
                case "6. View data":
                    viewData();
                    break;
                case "7. Save":
                    save();
                    break;
            }
        }
    }

    // EFFECTS: initializes load prompt popup window on start
    private void startLoadPrompt() {
        int loadOption = JOptionPane.showConfirmDialog(null,
                "Would you like to load your last log?", "Load File",
                JOptionPane.YES_NO_OPTION);
        if (loadOption == JOptionPane.YES_OPTION) {
            try {
                JsonReader reader = new JsonReader(JSON_STORE);
                listOfPassengers = reader.readPassengerList(listOfPassengers);
                listOfAircraft = reader.readAircraftList(listOfAircraft);
                listOfFlights = reader.readFlightList(listOfFlights);
                System.out.println("Data from " + JSON_STORE + " is loaded");
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }
    }

    // EFFECTS: initializes save prompt popup window when quitting
    private void exitSavePrompt() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                int saveOption = JOptionPane.showConfirmDialog(null,
                        "Would you like to save your log before exiting?", "Save File Prompt",
                        JOptionPane.YES_NO_OPTION);
                if (saveOption == JOptionPane.YES_OPTION) {
                    try {
                        jsonWriter.open();
                        jsonWriter.write(listOfPassengers, listOfAircraft, listOfFlights);
                        jsonWriter.close();
                        System.out.print("Saved to " + JSON_STORE);
                    } catch (FileNotFoundException e) {
                        System.out.println("Unable to write to file" + JSON_STORE);
                    }
                    dispose();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: generates a popup window that adds new passenger into system and updates gui
    private void addNewPassenger() {
        Object[] fields = { "Passenger ID:", new JTextField(), "First Name:", new JTextField(),
                "Last Name:", new JTextField(), "Travel Class:", new JComboBox<>(TravelClasses.values())
        };
        int result = JOptionPane.showConfirmDialog(null, fields, "Enter Passenger Information",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(((JTextField) fields[1]).getText());
            String firstName = ((JTextField) fields[3]).getText();
            String lastName = ((JTextField) fields[5]).getText();
            TravelClasses travelClass = (TravelClasses) ((JComboBox) fields[7]).getSelectedItem();
            listOfPassengers.addElement(new Passenger(id, firstName, lastName, travelClass));
            System.out.println(firstName + " " + lastName + " is now in the system!");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new aircraft into the system
    private void addNewAircraft() {
        JComboBox<String> aircraftTypesComboBox = new JComboBox<>(new String[]{"Cargo Aircraft",
                "Passenger Aircraft", "Private Jet"});
        Object[] fields = { "Aircraft Name:", new JTextField(), "Maximum Capacity:",
                new JTextField(), "Aircraft Type:", aircraftTypesComboBox };
        int result = JOptionPane.showConfirmDialog(null, fields,
                "Enter Aircraft Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String aircraftName = ((JTextField) fields[1]).getText();
            int maxCapacity = Integer.parseInt(((JTextField) fields[3]).getText());
            String aircraftType = (String) aircraftTypesComboBox.getSelectedItem();
            if (aircraftType.equals("Cargo Aircraft")) {
                listOfAircraft.addElement(new CargoAircraft(aircraftName, maxCapacity));
            } else if (aircraftType.equals("Passenger Aircraft")) {
                listOfAircraft.addElement(new PassengerAirline(aircraftName, maxCapacity));
            } else if (aircraftType.equals("Private Jet")) {
                listOfAircraft.addElement(new PrivateJet(aircraftName, maxCapacity));
            }
            System.out.println(aircraftName + " is now in the system!");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new flight into the system
    private void addNewFlight() {
        JComboBox<Aircraft> aircraftDropdown = new JComboBox<>();
        for (int i = 0; i < listOfAircraft.getSize(); i++) {
            aircraftDropdown.addItem(listOfAircraft.getElementAt(i));
        }
        JComboBox<Airports> originDropdown = new JComboBox<>(Airports.values());
        JComboBox<Airports> destinationDropdown = new JComboBox<>(Airports.values());
        JTextField flightIDField = new JTextField();
        JTextField durationField = new JTextField();

        Object[] fields = {"Flight ID:", flightIDField, "Aircraft:", aircraftDropdown, "Origin:", originDropdown,
                "Destination:", destinationDropdown, "Duration:", durationField};
        int result = JOptionPane.showConfirmDialog(null, fields, "Add New Flight",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String flightID = flightIDField.getText();
            Aircraft workAircraft = (Aircraft) aircraftDropdown.getSelectedItem();
            Airports origin = (Airports) originDropdown.getSelectedItem();
            Airports destination = (Airports) destinationDropdown.getSelectedItem();
            int duration = Integer.parseInt(durationField.getText());
            listOfFlights.addElement(new Flight(flightID, workAircraft, origin, destination, duration));
            System.out.println("Flight " + flightID + " is now in the system!");
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
            for (int i = 0; i < listOfFlights.size(); i++) {
                Flight flight = listOfFlights.getElementAt(i);
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
                    break;
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
        System.out.println(passenger.getFirstName() + " has been booked onto " + flight.getFlightID());
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
            for (int i = 0; i < listOfPassengers.size(); i++) {
                Passenger passenger = listOfPassengers.getElementAt(i);
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
        input.nextLine(); // newline character
        if (userInput == 1) {
            getListOfPassengers();
        } else if (userInput == 2) {
            getListOfAircraft();
        } else if (userInput == 3) {
            getListOfFlights();
        } else {
            System.out.println("Invalid entry. Try Again");
        }
    }

    // EFFECTS: saves the airport database to file
    private void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(listOfPassengers, listOfAircraft, listOfFlights);
            jsonWriter.close();
            System.out.print("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file" + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void load() {
        try {
            JsonReader reader = new JsonReader(JSON_STORE);
            listOfPassengers = reader.readPassengerList(listOfPassengers);
            listOfAircraft = reader.readAircraftList(listOfAircraft);
            listOfFlights = reader.readFlightList(listOfFlights);
            System.out.println("Data from " + JSON_STORE + " is loaded");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private void getListOfPassengers() {
        int numPassengers = listOfPassengers.size();

        if (numPassengers == 0) {
            System.out.println("Passenger list is empty");
        } else {
            for (int i = 0; i < numPassengers; i++) {
                Passenger passenger = listOfPassengers.getElementAt(i);
                System.out.println(passenger.toString());
            }
        }
    }

    private void getListOfAircraft() {
        int numAircraft = listOfAircraft.size();

        if (numAircraft == 0) {
            System.out.println("Aircraft list is empty");
        } else {
            for (int i = 0; i < numAircraft; i++) {
                Aircraft aircraft = listOfAircraft.getElementAt(i);
                System.out.println(aircraft.toString());
            }
        }
    }

    private void getListOfFlights() {
        int numFlights = listOfFlights.size();

        if (numFlights == 0) {
            System.out.println("Flight list is empty");
        } else {
            for (int i = 0; i < numFlights; i++) {
                Flight flight = listOfFlights.getElementAt(i);
                System.out.println(flight.toString());
            }
        }
    }

    // EFFECT: returns the json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray aircraftArray = new JSONArray();
        JSONArray flightArray = new JSONArray();
        JSONArray passengerArray = new JSONArray();

        for (int i = 0; i < listOfPassengers.size(); i++) {
            Passenger passenger = listOfPassengers.getElementAt(i);
            passengerArray.put(passenger.toJson());
        }

        for (int i = 0; i < listOfAircraft.size(); i++) {
            Aircraft aircraft = listOfAircraft.getElementAt(i);
            aircraftArray.put(aircraft.toJson());
        }

        for (int i = 0; i < listOfFlights.size(); i++) {
            Flight flight = listOfFlights.getElementAt(i);
            flightArray.put(flight.toJson());
        }

        json.put("aircraft", aircraftArray);
        json.put("flights", flightArray);
        json.put("passengers", passengerArray);

        return json;
    }


}
