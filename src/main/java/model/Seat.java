package model;

public class Seat {
    private final String seatIdentifier;
    private final CabinType cabin;
    private final double cost;
    private boolean isBooked;
    private Passenger passenger;

    private static final double DEFAULT_DEMAND_FACTOR = 4.5;
    private static final double DEFAULT_DURATION_FACTOR = 3.2;
    private static final double DEFAULT_CABIN_FACTOR = 2.8;
    private static final double[] DEFAULT_WEIGHTS = {0.2, 0.3, 0.5};

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
        double[] factors = {
            DEFAULT_DEMAND_FACTOR,  // demand factor
            DEFAULT_DURATION_FACTOR, // duration factor
            DEFAULT_CABIN_FACTOR    // cabin factor
        };

        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (int i = 0; i < factors.length; i++) {
            weightedSum += factors[i] * DEFAULT_WEIGHTS[i];
            totalWeight += DEFAULT_WEIGHTS[i];
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
