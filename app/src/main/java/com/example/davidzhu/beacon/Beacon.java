package com.example.davidzhu.beacon;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Owner on 5/16/2016.
 */
@ParseClassName("Beacon")
public class Beacon extends ParseObject {
    /*
    String Name
    String Address
    GeoPoint location
    String Arraylist Tags
    String Phone
    String Website
    String StartTime
    String EndTime
    File Photo
    ID?
    Creator ID?
    Int SavedTimes
     */

    /*
    object.getObjectId()
    Id is string
     */

    public Beacon(){

    }

    //Name
    public String getDisplayName(){
        return getString("name");
    }
    public void setDisplayName( String value){
        put("name", value);
    }

    //Address
    public String getAddress(){
        return getString("address");
    }
    public void setAddress(String value){
        put("address", value);
    }

    //Location
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public void setLocation(ParseGeoPoint value){
        put("location", value);
    }

    //Tags (Might need to be List<String> ?, Maybe use .get() instead. or .getJSONArray()
    public ArrayList<String> getTags(){
        return (ArrayList<String>)get("tags");
    }
    public void setTags(ArrayList<String> value){
        addAllUnique("tags", value);
    }

    //Phone
    public String getPhone(){
        return getString("phone");
    }
    public void setPhone(String value){
        put("phone", value);
    }

    //Website
    public String getWebsite(){
        return getString("website");
    }
    public void setWebsite(String value){
        put("website", value);
    }

    //StartDate
    public Date getStartDate(){
        return getDate("startDate");
    }
    public void setStartDate(Date value){
        put("startDate", value);
    }

    //EndDate
    public Date getEndDate(){
        return getDate("endDate");
    }
    public void setEndDate(Date value){
        put("endDate", value);
    }

    //Image
    public ParseFile getImage(){
        return getParseFile("image");
    }
    public void setImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("image.png", image);
        file.saveInBackground();
        put("image",file);
    }

    //Creator
    public ParseUser getCreator(){
        return getParseUser("user");
    }
    public void setCreator(ParseUser user){
        put("user", user);
        user.addUnique("created",getObjectId());
        user.saveInBackground();
    }

    //Times Saved
    public int getTimesSaved(){
        return getInt("timesSaved");
    }
    public void setTimesSaved(){
        put("timesSaved", 0);
    }

    /***
     * Increment the beacon's timesSaved count up or down. N should be 1 or -1.
     *
     * @param n is 1 or -1
     */
    public void incrementTimesSaved(int n){
        increment("timesSaved",n);
    }




}
