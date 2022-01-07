package com.asp.smartbeergreenhouse.thingsboard;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.model.Hop;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOperations {
    private Dataset datasetList;
    private String tokenAPI;
    private MyAdapter recyclerViewAdapter;

    public GetOperations(Dataset datasetList,String tokenAPI, MyAdapter recyclerViewAdapter){
        this.datasetList = datasetList;
        this.tokenAPI = tokenAPI;
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    public String getToken(JsonObject credentials){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getToken(credentials.toString());
        final String[] token = {new String()};
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    try {
                        //here we get the token from the response
                        token[0] = (new JSONObject((response.body().toString())).getString("token"));
                        Log.d("RESPONSE::", "Token retrieved:"+token[0]);

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });

        return token[0];
    }

    public void getAttributesFromGreenhouseRoom(String token, String assetId){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonArray> respAttributes = tbs.getAssetServerAttributes(token, assetId);
        respAttributes.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200){
                    try {
                        //Get first element of Json Array - Growing phase object
                        JSONObject growingPhaseObj= new JSONObject(response.body().getAsJsonArray().get(0).toString());
                        //Get second element of Json Array - Hop type object
                        JSONObject hopTypeObj= new JSONObject(response.body().getAsJsonArray().get(1).toString());

                        String hopName = "";
                        Hop.GrowingPhase phase = null;
                        if(growingPhaseObj.getString("key").equals("growing_phase")){
                            if(growingPhaseObj.getString("value").equals("Vegetative")){
                                phase = Hop.GrowingPhase.Vegetative;
                            } else if (growingPhaseObj.getString("value").equals("Flowering")) {
                                phase = Hop.GrowingPhase.Flowering;

                            }
                        }

                        if(hopTypeObj.getString("key").equals("hop_type")){
                            hopName = hopTypeObj.getString("value");
                        }

                        datasetList.get().add(new Hop(hopName, "GH01_Room_01",phase, 40));
                        recyclerViewAdapter.notifyDataSetChanged();


                        Log.d("RESPONSE::", "Room Attributes retrieved:"+response.body().getAsJsonArray().toString()+" Hop: "+hopName+" phase:"+phase.name());

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working1");
            }
        });
    }

    public void getAssetAttributes(String token, String assetName){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromAsset(token, assetName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    try {
                        //here we get the asset id from the response
                        String assetId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        //String assetName =  response.body().get("name").toString();
                        Log.d("RESPONSE::", "Asset id retrieved:"+assetId);
                        getAttributesFromGreenhouseRoom(tokenAPI, assetId);


                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });


    }
}
