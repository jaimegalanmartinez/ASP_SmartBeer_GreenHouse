package com.asp.smartbeergreenhouse.model;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

    // App-specific dataset:
    private static final List<Hop> listOfHops = new ArrayList<>();
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
     * Gets the list of items
     * @return the list of items
     */
    public List<Hop> getHops() {
        return listOfHops;
    }

    public int sizeHopsList() {
        return listOfHops.size();
    }

    public void removeHop(int position){
        if(!listOfHops.isEmpty())
            listOfHops.remove(position);
    }
    public void removeAllItemsHops(){
        if(!listOfHops.isEmpty()){
            listOfHops.clear();
            listOfItemsHopsInitialized = false;
        }
    }

    /**
     * Gets the list of items
     * @return the list of items
     */
    public List<Alarm> getAlarms() {
        return listOfAlarms;
    }

    public int sizeAlarmsList() {
        return listOfAlarms.size();
    }

    public void removeAlarm(int position){
        if(!listOfAlarms.isEmpty())
            listOfAlarms.remove(position);
    }
    public void removeAllItemsAlarms(){
        if(!listOfAlarms.isEmpty()){
            listOfAlarms.clear();
            listOfItemsAlarmsInitialized = false;
        }
    }


}
