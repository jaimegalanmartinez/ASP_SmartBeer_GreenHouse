package com.asp.smartbeergreenhouse.model;

import java.io.Serializable;

/**
 * Hop class
 * <p>Represents a Hop (name, type, growing phase and growing status)</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class Hop implements Serializable {
    /**
     * Enum GrowingPhase
     * <p>The growing phases are the following: Vegetative or Flowering.</p>
     */
    public enum GrowingPhase {Vegetative, Flowering}

    /**
     * Represents the hop's name
     */
    private String name;
    /**
     * Represents the greenhouse room where an specific hop is growing. Example: "GH01_Room_01"
     */
    private String location;
    /**
     * Represents the growing phase
     */
    private String growingPhase;
    /**
     * Represents the growing status (measured in %)
     */
    private int growingStatus;

    /**
     * Represents the date when hop was planted (date)
     */
    private String plantedAt;

    /**
     * Represents the expected harvesting date
     */
    private String harvestExpectedAt;

    /**
     * Represents the expected quality of hop (measured in %)
     */
    private int quality;

    /**
     * Hop class constructor
     * @param name Hop's name
     * @param location Greenhouse room where the specific hop is growing
     * @param phase growing phase
     * @param growingStatus growing status
     */
    public Hop(String name, String location, GrowingPhase phase, int growingStatus, String plantedDate){
        this.name = name;
        this.location = location;
        this.growingPhase = phase.toString();
        this.growingStatus = growingStatus;
        this.plantedAt = plantedDate;
    }

    /**
     * getName
     * @return hop's name
     */
    public String getName() { return name;}

    /**
     * getLocation
     * @return hop's location room
     */
    public String getLocation() { return location;}

    /**
     * getGrowingPhase
     * @return hop's growing phase
     */
    public String getGrowingPhase() {return growingPhase; }

    /**
     * getGrowingStatus
     * @return hop's growing status
     */
    public int getGrowingStatus() { return growingStatus; }

    public String getPlantedAt() {
        return plantedAt;
    }

    public String getHarvestExpectedAt() {
        return harvestExpectedAt;
    }

    public int getQuality() {
        return quality;
    }

    public void setHarvestExpectedAt(String harvestExpectedAt) {
        this.harvestExpectedAt = harvestExpectedAt;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
