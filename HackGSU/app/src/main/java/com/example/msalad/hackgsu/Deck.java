package com.example.msalad.hackgsu;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by msalad on 3/31/2017.

/**
 * Created by mohamed on 12/20/16.
 */


public class Deck {
    private String name;
    private int availability;
    private int id;
    private String lotType;
    private float lat;
    private float lon;


    public LatLng getLatLng(){
       return new LatLng(lat, lon);
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Deck(int id, String name, int availability, String lotType, float lat, float lon) {
        this.name = name;
        this.availability = availability;
        this.lotType = lotType;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    Deck(){

    }

    public int getAvailability() {
        return availability;
    }

    //public void setavailability(int availability) {
    //  this.availability = availability;
    //}

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}