package ui;

/* This is the UI class of the program */

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
public class AirportUI extends JFrame implements Writable, ListSelectionListener {
    private static final String JSON_STORE = "./data/airport.json";
    private Scanner input;
    private DefaultListModel<Passenger> listOfPassengers;
    private DefaultListModel<Aircraft> listOfAircraft;
    private DefaultListModel<Flight> listOfFlights;
    private FlightVisualizer visualizer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel mainMenu;
    private JLabel totalPassengers;
    private JLabel totalAircraft;
    private JLabel totalFlights;

    // Makes a new JFrame with different attributes
    public AirportUI() {
        super("Airport Management System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 700));

        initializeBackend();
        initializeHeader();
        initializeButtons();
        pack();
        setVisible(true);

        startLoadPrompt();
        exitSavePrompt();

        setVisible(true);
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
        mainMenu = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: initializes the 3 header panes
    private void initializeHeader() {
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
    // EFFECTS: Makes the total passenger counter
    public JPanel initializeTotalPassengerBox() {
        totalPassengers = new JLabel("Total Passengers: " + listOfPassengers.size());
        JPanel totalPassengersBox = new JPanel();
        totalPassengersBox.add(totalPassengers);

        totalPassengersBox.setBackground(Color.lightGray);
        totalPassengersBox.setPreferredSize(new Dimension(200, 30));
        add(totalPassengersBox);

        return totalPassengersBox;
    }

    // MODIFIES: this
    // EFFECTS: Makes the total aircraft counter
    public JPanel initializeTotalAircraftBox() {
        totalAircraft = new JLabel("Total Aircraft: " + listOfAircraft.size());
        JPanel totalAircraftBox = new JPanel();
        totalAircraftBox.add(totalAircraft);

        totalAircraftBox.setBackground(Color.lightGray);
        totalAircraftBox.setPreferredSize(new Dimension(200, 30));
        add(totalAircraftBox);

        return totalAircraftBox;
    }

    // MODIFIES: this
    // EFFECTS: Makes the total flights counter
    public JPanel initializeTotalFlightsBox() {
        totalFlights = new JLabel("Total Flights: " + listOfFlights.size());
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

        System.out.println(listOfPassengers);

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
        passengerJList.addListSelectionListener(this);
        passengerJList.setVisibleRowCount(10);
        passengerJList.setCellRenderer(new CellRenderer());
        JScrollPane passengerScrollPane = new JScrollPane(passengerJList);
        passengerScrollPane.createVerticalScrollBar();

        return passengerScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: setup configurations for aircraft pane
    private JScrollPane setUpAircraftPane() {
        JList<Aircraft> aircraftJList = new JList<>(listOfAircraft);
        aircraftJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aircraftJList.setSelectedIndex(0);
        aircraftJList.addListSelectionListener(this);
        aircraftJList.setVisibleRowCount(10);
        aircraftJList.setCellRenderer(new CellRenderer());
        JScrollPane aircraftScrollPane = new JScrollPane(aircraftJList);
        aircraftScrollPane.createVerticalScrollBar();

        return aircraftScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: setup configurations for flights pane
    private JScrollPane setUpFlightsPane() {
        JList<Flight> flightsJList = new JList<>(listOfFlights);
        flightsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsJList.setSelectedIndex(0);
        flightsJList.addListSelectionListener(this);
        flightsJList.setVisibleRowCount(10);
        flightsJList.setCellRenderer(new CellRenderer());
        JScrollPane flightsScrollPane = new JScrollPane(flightsJList);
        flightsScrollPane.createVerticalScrollBar();

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

        JButton viewFlights = new JButton("View flights");
        viewFlights.setActionCommand("4. View flights");
        viewFlights.addActionListener(new ButtonListener());

        JButton saveDatabaseToFile = new JButton("Save database to file");
        saveDatabaseToFile.setActionCommand("5. Save");
        saveDatabaseToFile.addActionListener(new ButtonListener());

        buttons.add(addNewPassenger);
        buttons.add(addNewAircraft);
        buttons.add(createNewFlight);
        buttons.add(viewFlights);
        buttons.add(saveDatabaseToFile);
        add(buttons, BorderLayout.PAGE_END);
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
                case "4. View flights":
                    visualizer = new FlightVisualizer(listOfFlights);
                case "5. Save":
                    save();
                    break;
            }
        }
    }

    // EFFECTS: updates the passenger panel with new the entry
    private void updatePassengersWindow() {
        JList<Passenger> passengerList = new JList<>(listOfPassengers);
        passengerList.setCellRenderer(new CellRenderer());
        JScrollPane passengerScrollPane = new JScrollPane(passengerList);
        passengerScrollPane.createVerticalScrollBar();

        Component[] components = mainMenu.getComponents();
        JScrollPane currentPassengerScrollPane = (JScrollPane) components[0];
        mainMenu.remove(currentPassengerScrollPane);
        mainMenu.add(passengerScrollPane, 0);
        mainMenu.revalidate();
        mainMenu.repaint();
    }

    // EFFECTS: updates the aircraft panel with new the entry
    private void updateAircraftWindow() {
        JList<Aircraft> aircraftList = new JList<>(listOfAircraft);
        aircraftList.setCellRenderer(new CellRenderer());
        JScrollPane aircraftScrollPane = new JScrollPane(aircraftList);
        aircraftScrollPane.createVerticalScrollBar();

        Component[] components = mainMenu.getComponents();
        JScrollPane currentAircraftScrollPane = (JScrollPane) components[1];
        mainMenu.remove(currentAircraftScrollPane);
        mainMenu.add(aircraftScrollPane, 1);
        mainMenu.revalidate();
        mainMenu.repaint();
    }

    // EFFECTS: updates the flight panel with new the entry
    private void updateFlightsWindow() {
        JList<Flight> flightList = new JList<>(listOfFlights);
        flightList.setCellRenderer(new CellRenderer());
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.createVerticalScrollBar();

        Component[] components = mainMenu.getComponents();
        JScrollPane currentFlightScrollPane = (JScrollPane) components[2];
        mainMenu.remove(currentFlightScrollPane);
        mainMenu.add(flightScrollPane, 2);
        mainMenu.revalidate();
        mainMenu.repaint();
    }

    // EFFECTS: Makes the popup window when cells are clicked
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        JList<?> list = (JList<?>) e.getSource();
        Object selectedObject = list.getSelectedValue();
        if (selectedObject != null) {
            JPanel popupPanel = new JPanel(new BorderLayout());
            addImage(selectedObject, popupPanel);

            // Show the popup dashboard
            String[] options = {"Edit", "Delete"};
            int choice = JOptionPane.showOptionDialog(null, popupPanel,
                    "Edit or Delete " + selectedObject.getClass().getSimpleName(),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == 0) {
                // Edit object code here
            } else if (choice == 1) {
                int confirmDelete = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this " + selectedObject.getClass().getSimpleName() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    removeObjectFromSystem(list, selectedObject);
                }
            }
        }
    }

    // EFFECTS: Helper method that adds the image to the popup window
    public void addImage(Object selectedObject, JPanel popupPanel) {
        ImageIcon imageIcon = null;
        if (selectedObject instanceof Aircraft) {
            imageIcon = new ImageIcon("data/pictures/passengerAircraft.PNG");
        } else if (selectedObject instanceof Passenger) {
            imageIcon = new ImageIcon("data/pictures/elonMusk.PNG");
        } else if (selectedObject instanceof Flight) {
            imageIcon = new ImageIcon("data/pictures/cargoAircraft.PNG");
        }
        if (imageIcon != null) {
            JLabel imageLabel = new JLabel(imageIcon);
            popupPanel.add(imageLabel, BorderLayout.NORTH);
        }
    }

    // EFFECTS: Helper method that removes the Object in the backend
    public void removeObjectFromSystem(JList list, Object selectedObject) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedObject instanceof Passenger) {
            listOfPassengers.remove(selectedIndex);
            updatePassengersWindow();
        } else if (selectedObject instanceof Aircraft) {
            listOfAircraft.remove(selectedIndex);
            updateAircraftWindow();
        } else if (selectedObject instanceof Flight) {
            listOfFlights.remove(selectedIndex);
            updateFlightsWindow();
        }
    }

    // EFFECTS: initializes load prompt popup window on start
    private void startLoadPrompt() {
        int loadOption = JOptionPane.showConfirmDialog(null,
                "Would you like to load your last log?", "Load File",
                JOptionPane.YES_NO_OPTION);
        if (loadOption == JOptionPane.YES_OPTION) {
            try {
                jsonReader = new JsonReader(JSON_STORE);
                listOfPassengers = jsonReader.readPassengerList(listOfPassengers);
                listOfAircraft = jsonReader.readAircraftList(listOfAircraft);
                listOfFlights = jsonReader.readFlightList(listOfFlights);
                initializeMainScreen();
                System.out.println("Data from " + JSON_STORE + " is loaded");
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        } else {
            initializeMainScreen();
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
                    save();
                    dispose();
                }
            }
        });
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
    // EFFECTS: generates a popup window that adds new passenger into system
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
            updatePassengersWindow();
            System.out.println(firstName + " " + lastName + " is now in the system!");
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a popup window that adds a new aircraft into the system
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
            updateAircraftWindow();
            System.out.println(aircraftName + " is now in the system!");
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a popup window that adds a new flight into the system
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
            updateFlightsWindow();
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
