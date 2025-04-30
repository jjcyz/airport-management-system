package ui;

/* This is the MainDashboard class of the program */

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.*;
import persistence.Event;
import com.formdev.flatlaf.FlatLightLaf;

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
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));

        initializeBackend();
        initializeHeader();
        initializeMainScreen();
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
        JPanel headerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalPassengers = createHeaderLabel("Total Passengers", listOfPassengers.size());
        totalAircraft = createHeaderLabel("Total Aircraft", listOfAircraft.size());
        totalFlights = createHeaderLabel("Total Flights", listOfFlights.size());

        headerPanel.add(totalPassengers);
        headerPanel.add(totalAircraft);
        headerPanel.add(totalFlights);

        add(headerPanel, BorderLayout.PAGE_START);
    }

    private JLabel createHeaderLabel(String label, int value) {
        JLabel headerLabel = new JLabel(label + ": " + value, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        headerLabel.setBackground(new Color(240, 240, 240));
        headerLabel.setOpaque(true);
        return headerLabel;
    }

    // MODIFIES: this
    // EFFECTS: initializes triple split panes
    private void initializeMainScreen() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane passengerScrollPane = createStyledListPane(listOfPassengers, "Passengers");
        JScrollPane aircraftScrollPane = createStyledListPane(listOfAircraft, "Aircraft");
        JScrollPane flightsScrollPane = createStyledListPane(listOfFlights, "Flights");

        mainPanel.add(passengerScrollPane);
        mainPanel.add(aircraftScrollPane);
        mainPanel.add(flightsScrollPane);

        // Add pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nextPage = new JButton("Next");
        JButton prevPage = new JButton("Previous");
        paginationPanel.add(prevPage);
        paginationPanel.add(nextPage);

        // Add to your main panel
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private <T> JScrollPane createStyledListPane(DefaultListModel<T> items, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JList<T> list = new JList<>(items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(15);
        list.setCellRenderer(new CellRenderer());

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);
        return new JScrollPane(panel);
    }

    // MODIFIES: this
    // EFFECTS: initializes buttons
    private void initializeButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttonLabels = {
            "Add new passenger",
            "Add new aircraft",
            "Create new flight",
            "View flights",
            "Save database",
            "View log",
            "Search Flights"
        };

        String[] commands = {
            "1. Add new passenger",
            "2. Add new aircraft",
            "3. Create new flight",
            "4. View flights",
            "5. Save",
            "6. Log",
            "7. Search Flight"
        };

        ButtonListener buttonListener = new ButtonListener();

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createStyledButton(buttonLabels[i], commands[i], buttonListener);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private JButton createStyledButton(String label, String actionCommand, ActionListener listener) {
        JButton button = new JButton(label);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
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
        // Find the passenger scroll pane in the main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(1);
        JScrollPane passengerScrollPane = (JScrollPane) mainPanel.getComponent(0);

        // Create a new list with the updated model
        JList<Passenger> passengerList = new JList<>(listOfPassengers);
        passengerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passengerList.addListSelectionListener(this);
        passengerList.setCellRenderer(new CellRenderer());

        // Create a new panel with the title border
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Passengers"));
        panel.add(passengerList, BorderLayout.CENTER);

        // Update the scroll pane's view
        passengerScrollPane.setViewportView(panel);
        passengerScrollPane.revalidate();
        passengerScrollPane.repaint();

        // Update the header count
        totalPassengers.setText("Total Passengers: " + listOfPassengers.size());
    }

    private JScrollPane createScrollPane(JList<?> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.createVerticalScrollBar();
        list.setCellRenderer(new CellRenderer());
        return scrollPane;
    }

    private void updateAircraftWindow() {
        // Find the aircraft scroll pane in the main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(1);
        JScrollPane aircraftScrollPane = (JScrollPane) mainPanel.getComponent(1);

        // Create a new list with the updated model
        JList<Aircraft> aircraftList = new JList<>(listOfAircraft);
        aircraftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aircraftList.addListSelectionListener(this);
        aircraftList.setCellRenderer(new CellRenderer());

        // Update the scroll pane's view
        aircraftScrollPane.setViewportView(aircraftList);
        aircraftScrollPane.revalidate();
        aircraftScrollPane.repaint();

        // Update the header count
        totalAircraft.setText("Total Aircraft: " + listOfAircraft.size());
    }

    private void updateFlightsWindow() {
        // Find the flights scroll pane in the main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(1);
        JScrollPane flightsScrollPane = (JScrollPane) mainPanel.getComponent(2);

        // Create a new list with the updated model
        JList<Flight> flightList = new JList<>(listOfFlights);
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightList.addListSelectionListener(this);
        flightList.setCellRenderer(new CellRenderer());

        // Update the scroll pane's view
        flightsScrollPane.setViewportView(flightList);
        flightsScrollPane.revalidate();
        flightsScrollPane.repaint();

        // Update the header count
        totalFlights.setText("Total Flights: " + listOfFlights.size());
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

                // Remove the existing main panel
                Component[] components = getContentPane().getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        getContentPane().remove(component);
                    }
                }

                // Reinitialize the UI with loaded data
                initializeHeader();
                initializeMainScreen();
                initializeButtons();
                revalidate();
                repaint();

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
