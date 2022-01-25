package com.asp.smartbeergreenhouse.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.view.View;
import android.widget.Button;

import com.asp.smartbeergreenhouse.databinding.ActivityFarmerBinding;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.thingsboard.OperationsAPI;
import com.asp.smartbeergreenhouse.thingsboard.ThingsboardApiAdapter;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.asp.smartbeergreenhouse.utils.MyItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.MyItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.MyOnItemActivatedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Farmer Activity
 *
 * Shows the Farmer UI screen
 *
 * <p>The farmer can visualize the hops status</p>
 *
 * <p>Also the farmer has two buttons: one for logout and one for checking alarms generated in Thingsboard</p>
 *
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class FarmerActivity extends AppCompatActivity {
    /**
     * Dataset that has the hops information. Used with hops for recycler view.
     */
    private final Dataset datasetList = new Dataset();
    /**
     * Represents the View binding of the Farmer Activity
     */
    private ActivityFarmerBinding binding;
    /**
     * Represents the recycler view used to show the hops status
     */
    private RecyclerView recyclerView;
    /**
     * Represents the recycler view adapter used for hops status
     */
    private MyAdapter recyclerViewAdapter;
    /**
     * Represents the tracker for recycler view
     */
    private SelectionTracker tracker;
    /**
     * Represents the onItemActivated listener associated to the recycler view used for hops status
     */
    private MyOnItemActivatedListener onItemActivatedListener;
    /**
     * OperationsAPI, class to use the methods that retrieve information from Thingsboard using a REST API
     */
    private OperationsAPI operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Button logoutBtn = binding.farmerLogoutBtn;
        Button alarmsBtn = binding.farmerAlarmsBtn;

        // Prepare the RecyclerView:
        recyclerView = binding.recyclerView;
        recyclerViewAdapter = new MyAdapter(this, datasetList.getHops());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Choose the layout manager to be set.
        // some options for the layout manager:  GridLayoutManager, LinearLayoutManager, StaggeredGridLayoutManager
        // initially, a linear layout is chosen:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Selection tracker (to allow for selection of items):
        onItemActivatedListener = new MyOnItemActivatedListener(this, datasetList.getHops());
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

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerActivity.this, LoginActivity.class);
                startActivity(i);
                datasetList.removeAllItemsHops();
                datasetList.removeAllItemsAlarms();
                finish();
            }
        });

        operation = new OperationsAPI(FarmerActivity.this, datasetList, recyclerViewAdapter);

        ExecutorService es;
        es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable(){
            @Override
            public void run() {
                operation.getAssetAttributes(ThingsboardApiAdapter.getToken(),"GH01_Room_02");
                operation.getAssetAttributes(ThingsboardApiAdapter.getToken(),"GH01_Room_01");
            }
        });

        alarmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService esAlarms;
                esAlarms = Executors.newSingleThreadExecutor();
                Handler handlerAlarms = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(@NonNull Message inputMessage) {
                        super.handleMessage(inputMessage);

                    }
                };

                TaskGetAlarmsFarmer task = new TaskGetAlarmsFarmer(handlerAlarms);
                esAlarms.execute(task);
                datasetList.removeAllItemsHops();
                datasetList.removeAllItemsAlarms();

            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tracker.onSaveInstanceState(outState); // Save state about selections.
    }

    public class TaskGetAlarmsFarmer implements Runnable {
        Handler creator;

        public TaskGetAlarmsFarmer(Handler handler){
            this.creator = handler;
        }

        @Override
        public void run() {
            Message msg;
            Bundle msg_data;

            msg = creator.obtainMessage();
            msg_data = msg.getData();
            operation.getAlarmsFromGreenhouseAssetSync(ThingsboardApiAdapter.getToken(),"GH1_GHR01_Row_01");
            operation.getAlarmsFromGreenhouseAssetSync(ThingsboardApiAdapter.getToken(),"GH1_GHR02_Row_01");
            Intent i = new Intent(operation.getContext(), AlarmsActivity.class);
            i.putExtra("dataset", operation.getDatasetList());
            operation.getContext().startActivity(i);

        }
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
                datasetList.removeHop(pos);
                recyclerView.getAdapter().notifyItemRemoved(pos);
                recyclerView.getAdapter().notifyItemRangeChanged(pos, datasetList.sizeHopsList());
            }
            tracker.clearSelection();
        }
    }

}