package com.example.travelmantics;
import com.google.firebase.database.IgnoreExtraProperties;
public class Deals {
    public String Location;
    public String price;
    public String resort;
    public Deals() {
    }
    public Deals(String Location, String price,String resort) {
        this.Location = Location;
        this.price = price;
        this.resort=resort;
    }
}
