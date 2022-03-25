package com.example.litterapp;

public class ConnectionInfo {

    private static String address = "http://192.168.0.73:5000/";

    /*
    getAddress(): returns the address for connecting to the flask server
    Parameters: NA
     */
    public String getAddress(){
        return address;
    }
}
