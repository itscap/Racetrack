package com.example.itscap.racetrack;

/**
 * Created by itscap on 9/5/17.
 */

public class Track {

    private String name, country, city, latitude, longitude, km;

    public Track(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
