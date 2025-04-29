package ui;

/* This is the MainDashboard class of the program */

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.*;
import persistence.Event;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

// Represents the airport application
public class MainDashboard extends JFrame implements Writable, ListSelectionListener {
    private static final String JSON_STORE = "./data/airport.json";
    private Scanner input;
    private DefaultListModel<Passenger> listOfPassengers;
    private DefaultListModel<Aircraft> listOfAircraft;
    private DefaultListModel<Flight> listOfFlights;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JScrollPane mainMenu;
    private JDesktopPane desktop;
    private JLabel totalPassengers;
    private JLabel totalAircraft;
    private JLabel totalFlights;

    // Features
    private WorldVisualizer visualizer;   // WISH LIST
    private SearchFlights searchFlights;    // WISH LIST

    // Makes a new JFrame with different attributes
    public MainDashboard() {
        super("Airport Management System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 700));

        initializeBackend();
        initializeHeader();
        initializeButtons();
        pack();
        setLocationRelativeTo(null);
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
    private void addNewObjectsButtons(JPanel buttons) {
        ButtonListener buttonListener = new ButtonListener();

        buttons.add(createButton("Add new passenger", "1. Add new passenger", buttonListener));
        buttons.add(createButton("Add new aircraft", "2. Add new aircraft", buttonListener));
        buttons.add(createButton("Create new flight", "3. Create new flight", buttonListener));
        buttons.add(createButton("View flights", "4. View flights", buttonListener));
        buttons.add(createButton("Save database to file", "5. Save", buttonListener));
        buttons.add(createButton("Log", "6. Log", buttonListener));
        buttons.add(createButton("Search Flights", "7. Search Flight", buttonListener));
    }

    // creates Action Listener for button presses
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleButtonAction(actionEvent.getActionCommand());
        }

        private void handleButtonAction(String actionCommand) {
            switch (actionCommand) {
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
                    visualizer = new WorldVisualizer(listOfFlights);
                    break;
                case "5. Save":
                    save();
                    break;
                case "6. Log":
                    logActionWindow();
                    break;
                case "7. Search Flights":
                    searchFlights = new SearchFlights(listOfFlights);
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
                "Edit or Delete " + aircraft.getIdentifier(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // Edit object code here
        } else if (choice == 1) {
            int confirmDelete = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this " + aircraft.getIdentifier() + "?",
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

        // Create seat selection field
        JTextField seatField = new JTextField(5);
        seatField.setText("A1");  // Default seat

        Object[] fields = {
            "Select Passenger:", passengerDropdown,
            "Enter Seat (e.g., A1):", seatField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Passenger to Flight",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Passenger passenger = (Passenger) passengerDropdown.getSelectedItem();
            String seatIdentifier = seatField.getText().trim();
            assert passenger != null;  // to catch NullPointerExceptions

            if (seatIdentifier.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid seat identifier");
                return;
            }

            passenger.addToBookedFlights(flight);
            String resultMessage = flight.addPassenger(passenger, seatIdentifier);
            JOptionPane.showMessageDialog(null, resultMessage);
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
                for (Event e : EventLog.getInstance()) {
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
                "Last Name:", new JTextField()};
        int result = JOptionPane.showConfirmDialog(null, fields, "Enter Passenger Information",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(((JTextField) fields[1]).getText());
            String firstName = ((JTextField) fields[3]).getText();
            String lastName = ((JTextField) fields[5]).getText();
            listOfPassengers.addElement(new Passenger(id, firstName, lastName));
            updatePassengersWindow();
            System.out.println(firstName + " " + lastName + " is now in the system!");
            // initializeTotalPassengerBox();
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a popup window that adds a new aircraft into the system
    private void addNewAircraft() {
        JComboBox<String> aircraftTypesComboBox = new JComboBox<>(new String[]{"Passenger Aircraft", "Private Jet"});
        Object[] fields = { "Aircraft Identifier:", new JTextField(), "Maximum Capacity:",
                new JTextField(), "Aircraft Type:", aircraftTypesComboBox };
        int result = JOptionPane.showConfirmDialog(null, fields,
                "Enter Aircraft Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String aircraftIdentifier = ((JTextField) fields[1]).getText();
            int maxCapacity = Integer.parseInt(((JTextField) fields[3]).getText());
            String aircraftType = (String) aircraftTypesComboBox.getSelectedItem();

            // Factory Pattern
            AircraftFactory factory = new ConcreteAircraftFactory();
            AircraftType type = aircraftType.equals("Passenger Aircraft") ?
                AircraftType.PASSENGER_AIRLINE : AircraftType.PRIVATE_JET;

            Aircraft passengerPlane = factory.createAircraft(type, aircraftIdentifier, maxCapacity);
            listOfAircraft.addElement(passengerPlane);
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

    private void logActionWindow() {
        JFrame frame = new JFrame("Log Actions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton printLogButton = new JButton("Print log to screen");
        printLogButton.addActionListener(new PrintLogAction());
        panel.add(printLogButton);

        JButton clearLogButton = new JButton("Clear log");
        clearLogButton.addActionListener(new ClearLogAction());
        panel.add(clearLogButton);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
            LogPrinter lp = new ScreenPrinter(MainDashboard.this);
            lp.printLog(EventLog.getInstance());
            // prints the event log at the end
            for (Event e : EventLog.getInstance()) {
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
            MainDashboard.this.requestFocusInWindow();
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
