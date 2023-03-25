package model;


/**
 * Defines behaviours that event log printers must support.
 * taken from UBC CPSC
 */
public interface LogPrinter {
    /**
     * Prints the log
     * @param el  the event log to be printed
     *
     */
    void printLog(EventLog el);
}
