package model;

public class Flight {
    private Plane plane;
    private Airports origin;
    private Airports destination;
    private int duration;

    public Flight(Plane plane, Airports origin, Airports destination, int duration) {
        this.plane= plane;
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
    }

    public Plane getPlane() {
        return plane;
    }

    public Airports getOrigin() {
        return origin;
    }

    public Airports getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }
}
