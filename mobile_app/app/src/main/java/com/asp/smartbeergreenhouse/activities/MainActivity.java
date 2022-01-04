package com.asp.smartbeergreenhouse.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.model.Hop;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.asp.smartbeergreenhouse.utils.MyItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.MyItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.MyOnItemActivatedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ListOfItems, MainActivity";

    private final Dataset datasetList = new Dataset();

    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;
    private SelectionTracker tracker;
    private MyOnItemActivatedListener onItemActivatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //datasetList.init(listHops);
        datasetList.get().add(new Hop("Hop1", "Bitter",Hop.GrowingPhase.Vegetative, 40));
        datasetList.get().add(new Hop("Hop2", "pi",Hop.GrowingPhase.Flowering, 62));
        datasetList.get().add(new Hop("Hop3", "Bitter",Hop.GrowingPhase.Flowering, 100));
        // Prepare the RecyclerView:
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new MyAdapter(this, datasetList.get());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Choose the layout manager to be set.
        // some options for the layout manager:  GridLayoutManager, LinearLayoutManager, StaggeredGridLayoutManager
        // initially, a linear layout is chosen:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Selection tracker (to allow for selection of items):
        onItemActivatedListener = new MyOnItemActivatedListener(this, datasetList.get());
        tracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                recyclerView,
                new MyItemKeyProvider(ItemKeyProvider.SCOPE_MAPPED),
//                new StableIdKeyProvider(recyclerView), // This caused the app to crash on long clicks
                new MyItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener(onItemActivatedListener)
                .build();
        recyclerViewAdapter.setSelectionTracker(tracker);

        if (savedInstanceState != null) {
            // Restore state on selections
            tracker.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tracker.onSaveInstanceState(outState); // Save state about selections.
    }


    public void seeCurrentSelection(View view) {
        // Button "see current selection" clicked
        Iterator iterator = tracker.getSelection().iterator();
        String text = "";
        while (iterator.hasNext()) {
            text += iterator.next().toString();
            if (iterator.hasNext()) {
                text += ", ";
            }
        }
        text = "Currently selected items = \n" + text;
        //Intent i = new Intent(this, SecondActivity.class);
        //i.putExtra("text", text);
        //startActivity(i);
    }

    public void deleteCurrentSelectionItems(View view) {
        ArrayList<Integer> positionsSelected = new ArrayList<>();
        if (tracker.getSelection() != null) {
            Iterator iterator = tracker.getSelection().iterator();
            //Iterate from selected items and store them in the positionsSelected ArrayList
            while (iterator.hasNext()) {
                positionsSelected.add(Integer.parseInt(iterator.next().toString()));
            }
            Collections.sort(positionsSelected);
            Collections.reverse(positionsSelected);
            for (Integer pos : positionsSelected) {
                //Log.d("hello", "PositionR: " + pos);
                //recyclerViewAdapter.removeItem(pos); same behaviour that with datasetList.remove()
                datasetList.remove(pos);
                recyclerView.getAdapter().notifyItemRemoved(pos);
                recyclerView.getAdapter().notifyItemRangeChanged(pos, datasetList.size());
            }
            tracker.clearSelection();
        }
    }


}