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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.asp.smartbeergreenhouse.databinding.ActivityBreweryBinding;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.model.Hop;
import com.asp.smartbeergreenhouse.thingsboard.GetOperations;
import com.asp.smartbeergreenhouse.thingsboard.ThingsboardApiAdapter;
import com.asp.smartbeergreenhouse.thingsboard.ThingsboardService;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.asp.smartbeergreenhouse.utils.MyItemDetailsLookup;
import com.asp.smartbeergreenhouse.utils.MyItemKeyProvider;
import com.asp.smartbeergreenhouse.utils.MyOnItemActivatedListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BreweryActivity extends AppCompatActivity {
    private ActivityBreweryBinding binding;
    private String tokenAPI = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYWltZS5nYWxhbi5tYXJ0aW5lekBhbHVtbm9zLnVwbS5lcyIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiOWY5MTc2MjAtNWQwNi0xMWVjLWI2MDctY2Q1ODU1ZWI2OTlhIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjlmODg1MjAwLTVjOTAtMTFlYy1iNjA3LWNkNTg1NWViNjk5YSIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY0MTU3MTIwNCwiZXhwIjoxNjQxNTgwMjA0fQ.j2J9m_BWmKL8a4a4i7iATxJ-2ksUmP4pRkx5cmZGzfiFDI2VDOeKbiNSdgnhe1ANkVil_g2ysoBWi2ILXcjIBg";
    private final Dataset datasetList = new Dataset();
    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;
    private SelectionTracker tracker;
    private MyOnItemActivatedListener onItemActivatedListener;
    private GetOperations operation;

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

        try {
            JsonObject userAPIcredentials = new JsonObject();
            userAPIcredentials.addProperty("username","");
            userAPIcredentials.addProperty("password","");
            Log.d("JSON_api: ", userAPIcredentials.toString());
            //tokenAPI ="Bearer " + getToken(userAPIcredentials);
            //Get Server attributes from greenhouse Room_01 (hop_type and growing phase)
            operation = new GetOperations(datasetList, tokenAPI,recyclerViewAdapter);
            operation.getAssetAttributes(tokenAPI,"GH01_Room_01");

            //getAttributesFromGreenhouseRoom(tokenAPI,"b0855880-6c82-11ec-9a04-591db17ccd5b");
            //Log.d("JSON_api: ", testID);
        }catch (JsonParseException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tracker.onSaveInstanceState(outState); // Save state about selections.
    }

}