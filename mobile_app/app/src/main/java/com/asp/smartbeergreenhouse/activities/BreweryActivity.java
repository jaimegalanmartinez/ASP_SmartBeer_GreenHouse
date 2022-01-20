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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Brewery Activity
 *
 * Shows the Brewery UI screen
 *
 * <p>The brewery can visualize the hops status</p>
 *
 * <p>Also the brewery has two buttons: one for logout and one for checking more information
 * (not implemented already) generated in Thingsboard</p>
 *
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class BreweryActivity extends AppCompatActivity {
    /**
     * Represents the View binding of the Brewery Activity
     */
    private ActivityBreweryBinding binding;
    /**
     * Dataset that has the hops information. Used with hops for recycler view.
     */
    private final Dataset datasetList = new Dataset();
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
        binding = ActivityBreweryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button logoutBtn = binding.breweryLogoutBtn;
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
                Intent i = new Intent(BreweryActivity.this, LoginActivity.class);
                startActivity(i);
                datasetList.removeAllItemsHops();
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
                operation.getAssetAttributes(tokenRetrieved,"GH01_Room_02");
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
            operation = new OperationsAPI(BreweryActivity.this,datasetList,recyclerViewAdapter);
            msg_data.putString("token", operation.getTokenAPI());
            msg.sendToTarget();
        }
    }

}

