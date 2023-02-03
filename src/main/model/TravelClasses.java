package model;
/*  This class defines the travel classes that a passenger can have. Each travel class has a
    ticket cost (in CAD) */

import java.util.Arrays;
import java.util.List;

public enum TravelClasses {
    ECONOMY(300),
    PREMIUMECONOMY(400),
    BUSINESSCLASS(600),
    FIRSTCLASS(1000);

    private int cost;

    TravelClasses(int ticketCost) {
        this.cost = ticketCost;

    }

    public static List<TravelClasses> getTravelClasses() {
        return Arrays.asList(values());
    }

}
