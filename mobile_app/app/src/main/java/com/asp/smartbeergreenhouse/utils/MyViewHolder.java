package com.asp.smartbeergreenhouse.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.model.Hop;

/**
 * MyViewHolder class
 * <p>Viewholder specific for hops recycler view</p>
 * @see RecyclerView.ViewHolder
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {

    // Holds references to individual item views
    Context context;
    TextView hopName;
    TextView hopType;
    TextView hopGrowingPhase;
    TextView hopGrowingStatus;
    ImageView image;

    private static final String TAG = "ListOfItems, MyViewHolder";

    public MyViewHolder(Context ctxt, View itemView) {
        super(itemView);
        context = ctxt;
        hopName = itemView.findViewById(R.id.cv_item_name);
        hopType = itemView.findViewById(R.id.cv_item_hop_type);
        hopGrowingPhase = itemView.findViewById(R.id.cv_item_phase_status);
        hopGrowingStatus = itemView.findViewById(R.id.cv_item_status);
        image = itemView.findViewById(R.id.img_hopPlantStatus);
    }

    void bindValues(Hop item, Boolean isSelected) {
        // give values to the elements contained in the item view
        hopName.setText(item.getName());
        hopType.setText(item.getLocation());
        hopGrowingPhase.setText("Growing phase: "+item.getGrowingPhase()+"\n"+item.getGrowingStatus()+"%");
        if(item.getGrowingPhase().equals("Vegetative")){
            image.setImageResource(R.drawable.growing_plant);
            hopGrowingStatus.setText(R.string.plant_status_growing);
        }else if (item.getGrowingPhase().equals("Reproductive")) {

            if (item.getGrowingStatus() == 100) {
                image.setImageResource(R.drawable.harvest_plant);
                hopGrowingStatus.setText(R.string.plant_status_to_harvest);
            }else {
                image.setImageResource(R.drawable.growing_plant);
                hopGrowingStatus.setText(R.string.plant_status_growing);
            }
        }


        if(isSelected) {
            hopName.setTextColor(Color.BLUE);
        } else {
            hopName.setTextColor(Color.BLACK);
        }
    }

    @SuppressLint("LongLogTag")
    @Nullable
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {

        Log.d(TAG, "getItemDetails() called");

        ItemDetailsLookup.ItemDetails<Long> itemdet = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                Log.d(TAG, "ItemDetailsLookup.ItemDetails<Long>.getPosition() called");
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                Log.d(TAG, "ItemDetailsLookup.ItemDetails<Long>.getSelectionKey() called");
                return (long) (getAdapterPosition());
            }

        };

        return itemdet;
    }
}