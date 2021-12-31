package com.asp.smartbeergreenhouse.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.model.Hop;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "ListOfItems, MyAdapter";

    private List<Hop> items;
    private SelectionTracker selectionTracker;
    Context context;


    public MyAdapter(Context ctxt, List<Hop> listofitems) {
        super();
        context = ctxt;
        items = listofitems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method has to actually inflate the item view and return the view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hop_item, parent, false);
        return new MyViewHolder(context, v);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // this method actually gives values to the elements of the view holder
        // (values corresponding to the item in 'position')
        final Hop item = items.get(position);
        holder.bindValues(item, selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
        Log.d(TAG, "onBindViewHolder() called for element in position " + position +
                ", Selected? = " + selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

}
