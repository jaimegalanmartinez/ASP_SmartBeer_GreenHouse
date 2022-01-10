package com.asp.smartbeergreenhouse.utils.alarms;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public final class AlarmItemDetailsLookup extends ItemDetailsLookup<Long> {

    private static final String TAG = "ListOfItems, MyItemDetailsLookup";

    private final RecyclerView mRecyclerView;

    @SuppressLint("LongLogTag")
    public AlarmItemDetailsLookup(RecyclerView recyclerView) {
        Log.d(TAG, "MyItemDetailsLookup() called");
        mRecyclerView = recyclerView;
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        Log.d(TAG, "getItemDetails() called");
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof AlarmViewHolder) {
                return ((AlarmViewHolder) holder).getItemDetails();

            }
        }
        return null;
    }

}