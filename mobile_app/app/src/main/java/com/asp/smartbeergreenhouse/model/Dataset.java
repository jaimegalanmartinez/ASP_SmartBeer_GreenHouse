package com.asp.smartbeergreenhouse.model;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

    // App-specific dataset:
    private static final List<Hop> listOfItems = new ArrayList<>();
    private static boolean listOfItemsInitialized = false;
    private ArrayList<Hop> listHops = new ArrayList<>();

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
    public List<Hop> get() {
        return listOfItems;
    }

    public int size() {
        return listOfItems.size();
    }

    public void remove(int position){
        if(!listOfItems.isEmpty())
            listOfItems.remove(position);
    }
    public void removeAllItems(){
        if(!listOfItems.isEmpty()){
            listOfItems.clear();
            listOfItemsInitialized = false;
        }
    }


}
