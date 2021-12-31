package com.asp.smartbeergreenhouse.model;

import java.io.Serializable;

public class Hop implements Serializable {
    public enum GrowingPhase {Vegetative, Reproductive}

    private String name;
    private String type;
    private String growingPhase;
    private int growingStatus;

    public Hop(String name, String type, GrowingPhase phase, int growingStatus){
        this.name = name;
        this.type = type;
        this.growingPhase = phase.toString();
        this.growingStatus = growingStatus;
    }

    public String getName() { return name;}
    public String getType() { return type;}

    public String getGrowingPhase() {return growingPhase; }

    public int getGrowingStatus() { return growingStatus; }
}
