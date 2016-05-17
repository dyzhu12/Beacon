package com.example.davidzhu.beacon;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class Beacon {

    /**
     * Barebones Beacon object for testing list view purposes!!
     */
    private String name;
    private String date;
    private String address;
    private String phoneNumber;
    private String website;

    private int rating;



    // TEMPORARY FOR TESTING distance should not be in the beacon class -- it should be calculated
    private double distance;

    Beacon(String name, int rating, double distance) {
        this.name = name;
        this.rating = rating;
        this.distance = distance;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setRating(int rating) { this.rating = rating; }
    public int getRating() { return rating; }

    public void setDate(String date) { this.date = date; }
    public String getDate() { return date; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setWebsite(String website) { this.website = website; }
    public String getWebsite() { return website; }


    public double getDistance() { return distance; }

    public ArrayList<Object> getItems() {
        ArrayList<Object> items = new ArrayList<Object>();
        items.add(rating);
        items.add(new Pair<String, String>("Date", date));
        items.add(new Pair<String, String>("Address", address));
        items.add(new Pair<String, String>("Phone Number", phoneNumber));
        items.add(new Pair<String, String>("Website", website));

        return items;
    }
}
