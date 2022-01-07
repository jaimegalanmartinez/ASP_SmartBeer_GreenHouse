package com.asp.smartbeergreenhouse.model;

public class Alarm {
    private int id;
    private String description;
    private GHRow row;
    //created time
    //originator - entity alias
    //type
    //severity
    //status
    //operations: ack, clear
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public GHRow getRow() {
        return row;
    }
}
