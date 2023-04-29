package persistence;

import persistence.Event;
import persistence.EventLog;
import persistence.LogPrinter;

import javax.swing.*;
import java.awt.*;

public class ScreenPrinter extends JInternalFrame implements LogPrinter {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea logArea;

    /**
     * Constructor sets up window in which log will be printed on screen
     * @param parent  the parent component
     * taken from UBC CPSC
     */
    public ScreenPrinter(Component parent) {
        super("Event log", false, true, false, false);
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
    }

    @Override
    public void printLog(EventLog el) {
        for (Event event : el) {
            logArea.setText(logArea.getText() + event.toString() + "\n\n");
        }
        repaint();
    }

    /**
     * Sets the position of window in which log will be printed relative to
     * parent
     * @param parent  the parent component
     */
    private void setPosition(Component parent) {
        setLocation(parent.getWidth() - getWidth() - 20,
                parent.getHeight() - getHeight() - 20);
    }
}
