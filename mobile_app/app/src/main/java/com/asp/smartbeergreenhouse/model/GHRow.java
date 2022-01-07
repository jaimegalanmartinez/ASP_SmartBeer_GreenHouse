package com.asp.smartbeergreenhouse.model;

/**
 * Class representing the row of a section from the Greenhouse
 */
public class GHRow {
    private int id;
    private int roomId;

    public GHRow(int id, int roomId){
        this.id = id;
        this.roomId = roomId;
    }
    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

}
