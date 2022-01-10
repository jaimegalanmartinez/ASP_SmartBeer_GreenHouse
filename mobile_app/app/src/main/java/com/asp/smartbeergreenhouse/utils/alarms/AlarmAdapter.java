package com.asp.smartbeergreenhouse.utils.alarms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.model.Alarm;
import com.asp.smartbeergreenhouse.model.Hop;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private static final String TAG = "ListOfItems, MyAdapter";

    private List<Alarm> items;
    private SelectionTracker selectionTracker;
    Context context;


    public AlarmAdapter(Context ctxt, List<Alarm> listofitems) {
        super();
        context = ctxt;
        items = listofitems;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method has to actually inflate the item view and return the view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(context, v);
    }


    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        // this method actually gives values to the elements of the view holder
        // (values corresponding to the item in 'position')
        final Alarm item = items.get(position);
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
