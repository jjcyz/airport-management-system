package model;

public class Seat {
    private final String seatIdentifier;
    private final CabinType cabin;
    private final double cost;
    private boolean isBooked;
    private Passenger passenger;

    public enum CabinType {
        BUSINESS, ECONOMY, FIRST_CLASS
    }

    public Seat(String seatIdentifier) {
        this.seatIdentifier = seatIdentifier;
        this.cabin = CabinType.ECONOMY;     // default
        this.cost = calculateCost();
        this.isBooked = false;
        this.passenger = null;
    }

    public String getSeatIdentifier() {
        return seatIdentifier;
    }

    public CabinType getCabin() {
        return cabin;
    }

    public double getCost() {
        return cost;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    // WISHLIST ITEM: calculate the cost that a plan ticket should be
    public double calculateCost() {
        double demandFactor = 4.5;  // (determine by analyzing list of flights that were purchased)
        double durationFactor = 3.2; // location and destination of flight
        double cabinFactor = 2.8; // cabin type

        double[] weights = { 0.2, 0.3, 0.5 }; // respective to factors
        double[] values = {demandFactor, durationFactor, cabinFactor};

        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (int i = 0; i < values.length; i++) {
            weightedSum += values[i] * weights[i];
            totalWeight += weights[i];
        }
        return totalWeight / weightedSum;
    }

    // EFFECTS: books this seat for the given passenger
    public void bookSeat(Passenger passenger) {
        this.isBooked = true;
        this.passenger = passenger;
    }

    // EFFECTS: unbooks this seat
    public void unbookSeat() {
        this.isBooked = false;
        this.passenger = null;
    }
}
