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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.asp.smartbeergreenhouse.databinding.ActivityFarmerBinding;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.thingsboard.OperationsAPI;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.asp.smartbeergreenhouse.utils.MyItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.MyItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.MyOnItemActivatedListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmerActivity extends AppCompatActivity {

    private static final String TAG = "ListOfItems, MainActivity";
    private final Dataset datasetList = new Dataset();
    private ActivityFarmerBinding binding;
    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;
    private SelectionTracker tracker;
    private MyOnItemActivatedListener onItemActivatedListener;
    private OperationsAPI operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //datasetList.init(listHops);
        //datasetList.get().add(new Hop("Hop1", "Bitter",Hop.GrowingPhase.Vegetative, 40));
        //datasetList.get().add(new Hop("Hop2", "pi",Hop.GrowingPhase.Flowering, 62));
        //datasetList.get().add(new Hop("Hop3", "Bitter",Hop.GrowingPhase.Flowering, 100));

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
                finish();
            }
        });

        alarmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation.getAlarmsFromGreenhouseAsset(operation.getTokenAPI(), "GH1_GHR01_Row_01");
                Intent i = new Intent(FarmerActivity.this, AlarmsActivity.class);
                startActivity(i);
                //datasetList.removeAllItemsHops();

            }
        });

        ExecutorService es;
        es = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message inputMessage) {
                super.handleMessage(inputMessage);
                String tokenRetrieved = inputMessage.getData().getString("token");
                //Get Server attributes from greenhouse Room_01 (hop_type and growing phase)
                operation.getAssetAttributes(tokenRetrieved,"GH01_Room_01");

            }
        };

        TaskGetTokenFarmer task = new TaskGetTokenFarmer(handler);
        es.execute(task);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tracker.onSaveInstanceState(outState); // Save state about selections.
    }

    public class TaskGetTokenFarmer implements Runnable {
        Handler creator;

        public TaskGetTokenFarmer(Handler handler){
            this.creator = handler;
        }

        @Override
        public void run() {
            Message msg;
            Bundle msg_data;

            msg = creator.obtainMessage();
            msg_data = msg.getData();
            operation = new OperationsAPI(datasetList,recyclerViewAdapter);
            msg_data.putString("token", operation.getTokenAPI());
            msg.sendToTarget();
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