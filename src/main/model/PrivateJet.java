package model;

import persistence.Writable;

public class PrivateJet extends Aircraft implements Writable {
    private int maxCapacity;

    public PrivateJet(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
