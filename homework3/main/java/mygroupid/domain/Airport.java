package mygroupid.domain;

import lombok.Data;

@Data
public class Airport {
    private final String airportCode;
    private final String airportName;
    private final String city;
    private final String coordinates;
    private final String timezone;
    private String cityExtracted;

    public Airport(String airportCode, String airportName, String city, String coordinates, String timezone) {
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.coordinates = coordinates;
        this.timezone = timezone;
        this.cityExtracted = city.split("\"")[3];
    }

    public void setCityExtracted(String cityExtracted) {
        this.cityExtracted = cityExtracted;
    }
}

