package com.asp.smartbeergreenhouse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.asp.smartbeergreenhouse.databinding.ActivityAlarmsBinding;

import com.asp.smartbeergreenhouse.model.Dataset;

import com.asp.smartbeergreenhouse.utils.alarms.AlarmAdapter;
import com.asp.smartbeergreenhouse.utils.alarms.AlarmItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.alarms.AlarmItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.alarms.AlarmOnItemActivatedListener;

/**
 * Alarms Activity
 *
 * Shows the Alarms UI screen
 *
 * <p>The farmer can visualize the active alarms (acknowledged and not acknowledged) retrieved from Thingsboard</p>
 *
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class AlarmsActivity extends AppCompatActivity {

    /**
     * Represents the View binding of the Alarms Activity
     */
    private ActivityAlarmsBinding binding;
    /**
     * Represents the recycler view used to show the active alarms
     */
    private RecyclerView recyclerView;
    /**
     * Represents the recycler view adapter used for alarms
     */
    private AlarmAdapter recyclerViewAdapter;
    /**
     * Represents the tracker for recycler view
     */
    private SelectionTracker tracker;
    /**
     * Represents the onItemActivated listener associated to the recycler view used for alarms
     */
    private AlarmOnItemActivatedListener onItemActivatedListener;
    /**
     * Dataset that has the alarms information.
     */
    private Dataset datasetList = new Dataset();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        datasetList = (Dataset) i.getSerializableExtra("dataset");
        Log.d("DATASET", String.valueOf(datasetList.sizeAlarmsList()));
        //Button to go back to hops information
        Button hopInfoBtn = binding.alarmHopsBtn;

        // Prepare the RecyclerView:
        recyclerView = binding.recyclerView;
        recyclerViewAdapter = new AlarmAdapter(this, datasetList.getAlarms());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Choose the layout manager to be set.
        // some options for the layout manager:  GridLayoutManager, LinearLayoutManager, StaggeredGridLayoutManager
        // initially, a linear layout is chosen:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Selection tracker (to allow for selection of items):
        onItemActivatedListener = new AlarmOnItemActivatedListener(this, datasetList.getAlarms());
        tracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                recyclerView,
                new AlarmItemKeyProvider(ItemKeyProvider.SCOPE_MAPPED),
//                new StableIdKeyProvider(recyclerView), // This caused the app to crash on long clicks
                new AlarmItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener(onItemActivatedListener)
                .build();
        recyclerViewAdapter.setSelectionTracker(tracker);

        if (savedInstanceState != null) {
            // Restore state on selections
            tracker.onRestoreInstanceState(savedInstanceState);
        }
        hopInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}