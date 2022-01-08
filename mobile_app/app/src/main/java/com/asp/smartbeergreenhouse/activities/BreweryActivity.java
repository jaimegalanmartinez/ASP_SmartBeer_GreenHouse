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

import com.asp.smartbeergreenhouse.databinding.ActivityBreweryBinding;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.thingsboard.OperationsAPI;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.asp.smartbeergreenhouse.utils.MyItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.MyItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.MyOnItemActivatedListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BreweryActivity extends AppCompatActivity {
    private ActivityBreweryBinding binding;
    private final Dataset datasetList = new Dataset();
    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;
    private SelectionTracker tracker;
    private MyOnItemActivatedListener onItemActivatedListener;
    private OperationsAPI operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBreweryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button logoutBtn = binding.breweryLogoutBtn;
        // Prepare the RecyclerView:
        recyclerView = binding.recyclerView;
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

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BreweryActivity.this, LoginActivity.class);
                startActivity(i);
                datasetList.removeAllItems();
                finish();
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

        TaskGetTokenBrewery task = new TaskGetTokenBrewery(handler);
        es.execute(task);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tracker.onSaveInstanceState(outState); // Save state about selections.
    }


    public class TaskGetTokenBrewery implements Runnable {
        Handler creator;

        public TaskGetTokenBrewery(Handler handler){
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

}

