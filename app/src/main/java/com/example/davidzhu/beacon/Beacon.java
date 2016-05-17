package com.example.davidzhu.beacon;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class Beacon {

    /**
     * Barebones Beacon object for testing list view purposes!!
     */
    private String name;
    private int rating;
    private double distance;

    Beacon(String name, int rating, double distance) {
        this.name = name;
        this.rating = rating;
        this.distance = distance;
    }

    public String getName() { return name; }
    public int getRating() { return rating; }
    public double getDistance() { return distance; }
}
