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
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// Represents the airport application
public class AirportUI extends JFrame implements Writable, ListSelectionListener {
    private static final String JSON_STORE = "./data/airport.json";
    private Scanner input;
    private DefaultListModel<Passenger> listOfPassengers;
    private DefaultListModel<Aircraft> listOfAircraft;
    private DefaultListModel<Flight> listOfFlights;
    private FlightVisualizer visualizer;  // wish list
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JScrollPane mainMenu;
    private JDesktopPane desktop;
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
        mainMenu = new JScrollPane();
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
    }

    // MODIFIES: this
    // EFFECTS: initializes the 3 header panes
    private void initializeHeader() {
        mainMenu.setLayout(new ScrollPaneLayout());
        JPanel header1 = makeTotalCounter("Total Passengers", listOfPassengers.size());
        JPanel header2 = makeTotalCounter("Total Aircraft", listOfAircraft.size());
        JPanel header3 = makeTotalCounter("Total Flights", listOfFlights.size());
        mainMenu.add(header1);
        mainMenu.add(header2);
        mainMenu.add(header3);

        add(mainMenu, BorderLayout.PAGE_START);
    }

    // MODIFIES: this
// EFFECTS: Makes a total counter with the given label and value
    private JPanel makeTotalCounter(String label, int value) {
        JLabel totalLabel = new JLabel(label + ": " + value);
        JPanel totalBox = new JPanel();
        totalBox.add(totalLabel);

        totalBox.setBackground(Color.lightGray);
        totalBox.setPreferredSize(new Dimension(200, 30));
        add(totalBox);

        return totalBox;
    }

    // MODIFIES: this
    // EFFECTS: initializes triple split panes
    private void initializeMainScreen() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        JScrollPane passengerScrollPane = setUpListPane(listOfPassengers);
        JScrollPane aircraftScrollPane = setUpListPane(listOfAircraft);
        JScrollPane flightsScrollPane = setUpListPane(listOfFlights);

        mainPanel.add(passengerScrollPane);
        mainPanel.add(aircraftScrollPane);
        mainPanel.add(flightsScrollPane);

        mainPanel.setMinimumSize(new Dimension(700, 300));
        mainPanel.setPreferredSize(new Dimension(1000, 300));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(mainPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: setup configurations for a list pane
    private <T> JScrollPane setUpListPane(DefaultListModel<T> items) {
        JList<T> list = new JList<>(items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        list.setCellRenderer(new CellRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.createVerticalScrollBar();

        return scrollPane;
    }


    // MODIFIES: this
    // EFFECTS: initializes buttons
    private void initializeButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        addNewObjectsButtons(buttons);

        JButton viewFlights = new JButton("View flights");  // wishlist feature
        viewFlights.setActionCommand("4. View flights");
        viewFlights.addActionListener(new ButtonListener());

        buttons.add(viewFlights);
        add(buttons, BorderLayout.PAGE_END);
    }

    // EFFECTS: creates a new JButton with the given label, action command, and ActionListener
    private JButton createButton(String label, String actionCommand, ActionListener listener) {
        JButton button = new JButton(label);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        return button;
    }

    // EFFECTS: adds buttons that create new objects
    public void addNewObjectsButtons(JPanel buttons) {
        JButton addNewPassenger = createButton("Add new passenger",
                "1. Add new passenger", new ButtonListener());
        JButton addNewAircraft = createButton("Add new aircraft",
                "2. Add new aircraft", new ButtonListener());
        JButton createNewFlight = createButton("Create new flight",
                "3. Create new flight", new ButtonListener());
        JButton saveDatabaseToFile = createButton("Save database to file",
                "5. Save", new ButtonListener());
        JButton viewFlights = createButton("View flights",
                "4. View flights", new ButtonListener());
        JButton printLog = createButton("Print log",
                "6. Print log", new ButtonListener());
        printLog.setAction(new PrintLogAction());
        JButton clearLog = createButton("Clear log",
                "7. Clear log", new ButtonListener());
        clearLog.setAction(new ClearLogAction());
        clearLog.setAction(new ClearLogAction());

        buttons.add(addNewPassenger);
        buttons.add(addNewAircraft);
        buttons.add(createNewFlight);
        buttons.add(saveDatabaseToFile);
        buttons.add(clearLog);
        buttons.add(printLog);
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
                case "6. Print log":
                    new PrintLogAction();
                case "7. Clear log":
                    new ClearLogAction();
            }
        }
    }

    // EFFECTS: updates the passenger panel with new the entry
    private void updatePassengersWindow() {
        JList<Passenger> passengerList = new JList<>(listOfPassengers);
        passengerList.setCellRenderer(new CellRenderer());
        JScrollPane passengerScrollPane = new JScrollPane(passengerList);
        passengerScrollPane.createVerticalScrollBar();
        for (Component component : mainMenu.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                mainMenu.remove(scrollPane);
            } else {
                // handle non-JScrollPane components here, if needed
            }
        }
        mainMenu.add(passengerScrollPane, 0);
        mainMenu.revalidate();
        mainMenu.repaint();
    }

    private JScrollPane createScrollPane(JList<?> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.createVerticalScrollBar();
        list.setCellRenderer(new CellRenderer());
        return scrollPane;
    }

    private void updatePanel(JScrollPane scrollPane, int index) {
        for (Component component : mainMenu.getComponents()) {
            if (component instanceof JScrollPane) {
                mainMenu.remove(component);
            }
        }
        mainMenu.add(scrollPane, index);
        mainMenu.revalidate();
        mainMenu.repaint();
    }

    private void updateAircraftWindow() {
        JList<Aircraft> aircraftList = new JList<>(listOfAircraft);
        JScrollPane aircraftScrollPane = createScrollPane(aircraftList);
        updatePanel(aircraftScrollPane, 1);
    }

    private void updateFlightsWindow() {
        JList<Flight> flightList = new JList<>(listOfFlights);
        JScrollPane flightScrollPane = createScrollPane(flightList);
        updatePanel(flightScrollPane, 2);
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
            if (selectedObject.getClass().equals(Passenger.class)) {
                Passenger passenger = (Passenger) selectedObject;
                passengerDashboard(passenger, popupPanel, list);
            } else if (selectedObject.getClass().equals(Aircraft.class)) {
                Aircraft aircraft = (Aircraft)  selectedObject;
                aircraftDashboard(aircraft, popupPanel, list);
            } else if (selectedObject.getClass().equals(Flight.class)) {
                Flight flight = (Flight) selectedObject;
                flightDashboard(flight, popupPanel, list);
            }
        }
    }

    // EFFECTS: creates passenger popup window
    private void passengerDashboard(Passenger passenger, JPanel popupPanel, JList list) {
        String[] options = {"Edit", "Delete"};
        int choice = JOptionPane.showOptionDialog(null, popupPanel,
                "Edit or Delete " + passenger.getPassengerID(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // Edit object code here
        } else if (choice == 1) {
            int confirmDelete = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this " + passenger.getPassengerID() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                removeObjectFromSystem(list, passenger);
            }
        }
    }

    // EFFECTS: creates aircraft popup window
    private void aircraftDashboard(Aircraft aircraft, JPanel popupPanel, JList list) {
        String[] options = {"Edit", "Delete"};
        int choice = JOptionPane.showOptionDialog(null, popupPanel,
                "Edit or Delete " + aircraft.getName(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // Edit object code here
        } else if (choice == 1) {
            int confirmDelete = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this " + aircraft.getName() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                removeObjectFromSystem(list, aircraft);
            }
        }
    }

    // EFFECTS: creates flight popup window
    private void flightDashboard(Flight flight, JPanel popupPanel, JList list) {
        String[] options = {"Add Passenger", "Remove Passenger", "Delete"};
        int choice = JOptionPane.showOptionDialog(null, popupPanel,
                "Edit or Delete " + flight.getFlightID(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            addPassengerToFlight(flight);
        } else if (choice == 1) {
            removePassengerFromFlight(flight);
        } else if (choice == 2) {
            int confirmDelete = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this " + flight.getFlightID() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                removeObjectFromSystem(list, flight);
            }
        }
    }

    // EFFECTS: creates a dropdown window to add a passenger on a flight
    private void addPassengerToFlight(Flight flight) {
        JComboBox<Passenger> passengerDropdown = new JComboBox<>();
        for (int i = 0; i < listOfPassengers.getSize(); i++) {
            passengerDropdown.addItem(listOfPassengers.getElementAt(i));
        }
        Object[] fields = {"Select Passenger:", passengerDropdown};
        int result = JOptionPane.showConfirmDialog(null, fields, "Passengers",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Passenger passenger = (Passenger) passengerDropdown.getSelectedItem();
            assert passenger != null;  // to catch NullPointerExceptions
            passenger.addToBookedFlights(flight);
            flight.addPassenger(passenger);
        }
    }

    // EFFECTS: creates a dropdown window to remove a passenger from a flight
    private void removePassengerFromFlight(Flight flight) {
        JComboBox<Passenger> passengerDropdown = new JComboBox<>();
        for (int i = 0; i < flight.getPassengersOnFlight().size(); i++) {
            passengerDropdown.addItem(flight.getPassengersOnFlight().get(i));
        }
        Object[] fields = {"Select Passenger:", passengerDropdown};
        int result = JOptionPane.showConfirmDialog(null, fields, "Passengers",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Passenger passenger = (Passenger) passengerDropdown.getSelectedItem();
            assert passenger != null; // to catch NullPointerExceptions
            flight.removePassenger(passenger.getPassengerID());
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
                if (saveOption == JOptionPane.NO_OPTION) {
                    return;
                }
                // prints the event log
                for (model.Event e : model.EventLog.getInstance()) {
                    System.out.println(e);
                }
                save();
                dispose();
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
            // initializeTotalPassengerBox();
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
            switch (Objects.requireNonNull(aircraftType)) {
                case "Cargo Aircraft":
                    listOfAircraft.addElement(new CargoAircraft(aircraftName, maxCapacity));
                    break;
                case "Passenger Aircraft":
                    listOfAircraft.addElement(new PassengerAirline(aircraftName, maxCapacity));
                    break;
                case "Private Jet":
                    listOfAircraft.addElement(new PrivateJet(aircraftName, maxCapacity));
                    break;
            }
            updateAircraftWindow();
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

    //wish list feature
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

    /**
     * Represents the action to be taken when the user wants to
     * print the event log.
     * modified from UBC Computer Science
     */
    private class PrintLogAction extends AbstractAction {
        PrintLogAction() {
            super("Print log to screen");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            LogPrinter lp = new ScreenPrinter(AirportUI.this);
            lp.printLog(EventLog.getInstance());
            // prints the event log at the end
            for (model.Event e : model.EventLog.getInstance()) {
                System.out.println(e);
            }
        }
    }

    /**
     * Represents the action to be taken when the user wants to
     * clear the event log.
     * UBC Computer Science
     */
    private class ClearLogAction extends AbstractAction {
        ClearLogAction() {
            super("Clear log");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            EventLog.getInstance().clear();
        }
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     * UBC Computer Science
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            AirportUI.this.requestFocusInWindow();
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
