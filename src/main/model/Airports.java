package model;

/*  This class defines all the airports used in this program. Each airport has a full name, location,
    and country */


import java.util.Arrays;
import java.util.List;

public enum Airports {
    YVR("Vancouver International Airport","Vancouver","Canada"),
    YYZ("Toronto Pearson International Airport","Toronto","Canada"),
    ATL("Hartsfield–Jackson Atlanta International Airport","Atlanta","United States"),
    LHR("London Heathrow Airport","London","United Kingdom"),
    PEK("Beijing Capital International Airport","Beijing","China"),
    SZX("Shenzhen Bao'an International Airport","Bao'an","China"),
    LAX("Los Angeles International Airport","los Angeles","United States"),
    ORD("Chicago O'Hare International Airport", "Chicago","United States"),
    DFW("Dallas Fort Worth International Airport","Dallas","China"),
    JFK("John F Kennedy International","New York City","United States"),
    CDG("Paris Charles de Gaulle Airport","Paris","France"),
    CGK("Soekarno–Hatta International Airport","Banten","Indonesia"),
    SIN("Singapore International Airport","Changi","Singapore"),
    HKG("Hong Kong International Airport","Hong Kong","Republic of China"),
    MIA("Miami International Airport","Miami","United States");

    private String name;
    private String city;
    private String country;

    Airports(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
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

    public static List<Airports> getAirports() {
        return Arrays.asList(values());
    }





}
