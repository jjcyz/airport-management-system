package ui;

/* This is the Flight visualizer class of the program */

import model.Flight;

import javax.swing.*;
import java.awt.*;

public class FlightVisualizer extends JFrame {
    private final JPanel flightPanel;
    private final JPanel mapPanel;
    private final DefaultListModel<Flight> flights;

    public FlightVisualizer(DefaultListModel<Flight> flights) {
        this.flights = flights;
        setTitle("Flight Visualizer");
        setSize(800, 600);
        setContentPane(new JLabel(new ImageIcon("data/pictures/aircraft.png")));

        flightPanel = new JPanel(new GridLayout(flights.size(), 1));
        for (int i = 0; i < flights.getSize(); i++) {
            Flight flight = flights.getElementAt(i);
            JLabel flightLabel = new JLabel(flight.toString());
            flightPanel.add(flightLabel);
        }
        mapPanel = new JPanel();
        mapPanel.setBackground(Color.WHITE);
        mapPanel.setLayout(null);

        placeFlights();

        JScrollPane scrollPane = new JScrollPane(flightPanel);
        getContentPane().add(scrollPane, BorderLayout.WEST);
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void placeFlights() {
        for (int i = 0; i < flights.getSize(); i++) {
            Flight flight = flights.getElementAt(i);
            ImageIcon airplaneIcon = new ImageIcon("data/pictures/aircraft.png");
            Image img = airplaneIcon.getImage().getScaledInstance(airplaneIcon.getIconWidth() / 4,
                    airplaneIcon.getIconHeight() / 4, Image.SCALE_SMOOTH);
            ImageIcon scaledAirplaneIcon = new ImageIcon(img);
            JLabel airplaneLabel = new JLabel(scaledAirplaneIcon);
            airplaneLabel.setSize(scaledAirplaneIcon.getIconWidth(), scaledAirplaneIcon.getIconHeight());
            int x = (int) (Math.random() * mapPanel.getWidth() * 0.9);
            int y = (int) (Math.random() * mapPanel.getHeight() * 0.9);
            airplaneLabel.setLocation(x, y);
            mapPanel.add(airplaneLabel);
        }

    }

}

