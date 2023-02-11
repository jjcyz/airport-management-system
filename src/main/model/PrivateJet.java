package model;

public class PrivateJet extends Aircraft {
    private int maxCapacity;

    public PrivateJet(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.maxCapacity = 12;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
