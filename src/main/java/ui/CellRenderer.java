package ui;

/* This is the Cell Renderer class of the program */

import model.Aircraft;
import model.Flight;
import model.Passenger;

import javax.swing.*;
import java.awt.*;

public class CellRenderer extends DefaultListCellRenderer {

    // EFFECTS: Renders the cell for each object
    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Passenger) {
            Passenger passenger = (Passenger) value;
            setText(passenger.toString());
        } else if (value instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) value;
            setText(aircraft.toString());
        } else if (value instanceof Flight) {
            Flight flight = (Flight) value;
            setText(flight.toString());
        } else {
            setText(value.toString());
        }
        return this;
    }

}
