package model;

public class PrivateJet extends Aircraft {
    public PrivateJet(String name, int maxCapacity) {
        super(name, maxCapacity);
        maxCapacity = 12;
    }
}
