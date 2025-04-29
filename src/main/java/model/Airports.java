package model;

/*  This class defines all the airports used in this program. Each airport has a full name, location,
    country, and coordinates */

import java.util.Arrays;
import java.util.List;

public enum Airports {
    YVR("Vancouver International Airport","Vancouver","Canada", 49.1947, -123.1792),
    YYZ("Toronto Pearson International Airport","Toronto","Canada", 43.6777, -79.6248),
    ATL("Hartsfield–Jackson Atlanta International Airport","Atlanta","United States", 33.6407, -84.4277),
    LHR("London Heathrow Airport","London","United Kingdom", 51.4700, -0.4543),
    PEK("Beijing Capital International Airport","Beijing","China", 40.0799, 116.6031),
    SZX("Shenzhen Bao'an International Airport","Bao'an","China", 22.6392, 113.8100),
    LAX("Los Angeles International Airport","los Angeles","United States", 33.9416, -118.4085),
    ORD("Chicago O'Hare International Airport", "Chicago","United States", 41.9742, -87.9073),
    DFW("Dallas Fort Worth International Airport","Dallas","United States", 32.8998, -97.0403),
    JFK("John F Kennedy International","New York City","United States", 40.6413, -73.7781),
    CDG("Paris Charles de Gaulle Airport","Paris","France", 49.0097, 2.5478),
    CGK("Soekarno–Hatta International Airport","Banten","Indonesia", -6.1256, 106.6558),
    SIN("Singapore International Airport","Changi","Singapore", 1.3644, 103.9915),
    HKG("Hong Kong International Airport","Hong Kong","China", 22.3080, 113.9185),
    MIA("Miami International Airport","Miami","United States", 25.7933, -80.2906);

    private final String name;
    private final String city;
    private final String country;
    private final double latitude;
    private final double longitude;

    Airports(String name, String city, String country, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAirportName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double[] getCoordinates() {
        return new double[]{latitude, longitude};
    }

    public static List<Airports> getAirports() {
        return Arrays.asList(values());
    }
}
