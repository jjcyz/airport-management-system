package ui;

import model.Aircraft;
import model.Flight;
import model.Passenger;

import javax.swing.*;
import java.awt.*;

public class CellRenderer extends DefaultListCellRenderer {

    // EFFECTS: configures passenger rendering
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
