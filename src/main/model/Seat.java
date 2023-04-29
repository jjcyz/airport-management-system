package model;

public class Seat {
    private String seatIdentifier;
    private CabinType cabin;
    private double cost;
    private Boolean isBooked;
    private Passenger passenger;

    public enum CabinType {
        BUSINESS, ECONOMY, FIRST_CLASS
    }

    public Seat(String seatIdentifier) {
        this.seatIdentifier = seatIdentifier;
        this.cabin = CabinType.ECONOMY;     // default
        this.cost = calculateCost();
        this.isBooked = false;
    }

    public String getSeatIdentifier() {
        return seatIdentifier;
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

}
