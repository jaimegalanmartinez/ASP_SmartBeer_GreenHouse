package com.asp.smartbeergreenhouse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dataset class
 * <p>Provides two lists (hops and alarms) and methods that operates in each list. Used in the recycler views</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class Dataset implements Serializable {

    // App-specific dataset:
    /**
     * List of hops
     */
    private static final List<Hop> listOfHops = new ArrayList<>();
    /**
     * List of alarms
     */
    private static final List<Alarm> listOfAlarms = new ArrayList<>();
    private static boolean listOfItemsHopsInitialized = false;
    private static boolean listOfItemsAlarmsInitialized = false;


   /* public void init (ArrayList<String> hopNames) {
        // Populate the list of items if not done before:
        if (!listOfItemsInitialized) {
            for (int i = 0; i < hopNames.size(); ++i) {
                listOfItems.add(new Hop(hopNames.get(i)));
            }
            listOfItemsInitialized = true;
        }
    }*/

    /**
     * getHops
     * <p>Gets the list of hops</p>
     * @return the list of hops
     */
    public List<Hop> getHops() {
        return listOfHops;
    }

    /**
     * sizeHopsList
     * <p>Gets the size of hops list</p>
     * @return list of hops size
     */
    public int sizeHopsList() {
        return listOfHops.size();
    }

    /**
     * removeHop
     * <p>Removes a Hop from list</p>
     * @param position list position
     */
    public void removeHop(int position){
        if(!listOfHops.isEmpty())
            listOfHops.remove(position);
    }
    /**
     * removeAllItemsHops
     * <p>Remove all hops from the list</p>
     */
    public void removeAllItemsHops(){
        if(!listOfHops.isEmpty()){
            listOfHops.clear();
            listOfItemsHopsInitialized = false;
        }
    }

    /**
     * getAlarms
     * <p>Gets the list of alarms</p>
     * @return the list of alarms
     */
    public List<Alarm> getAlarms() {
        return listOfAlarms;
    }

    /**
     * sizeAlarmsList
     * <p>Gets the size of alarms list</p>
     * @return list of alarms size
     */
    public int sizeAlarmsList() {
        return listOfAlarms.size();
    }

    /**
     * removeAlarm
     * <p>Removes an alarm from list</p>
     * @param position list position
     */
    public void removeAlarm(int position){
        if(!listOfAlarms.isEmpty())
            listOfAlarms.remove(position);
    }
    /**
     * removeAllItemsAlarms
     * <p>Remove all alarms from the list</p>
     */
    public void removeAllItemsAlarms(){
        if(!listOfAlarms.isEmpty()){
            listOfAlarms.clear();
            listOfItemsAlarmsInitialized = false;
        }
    }

}
