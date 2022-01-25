package com.asp.smartbeergreenhouse.thingsboard;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asp.smartbeergreenhouse.model.Alarm;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.model.Hop;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * OperationsAPI class
 *
 * <p>It provides the methods to interact with REST API using methods defined in ThingsboardService interface</p>
 * <p>It is used to get Thingsboard token and retrieved information from Thingsboard</p>
 * @see ThingsboardService
 * @see ThingsboardApiAdapter
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class OperationsAPI {
    /**
     * Represents a Dataset
     * @see Dataset
     */
    private Dataset datasetList;
    /**
     * Represents a recyclerViewAdapter
     * @see MyAdapter
     */
    private MyAdapter recyclerViewAdapter;
    /**
     * Represents the activity context where the class is used
     * @see Context
     */
    private Context context;

    /**
     * Operations API class constructor
     * @param context Activity context
     * @param datasetList Dataset
     * @param recyclerViewAdapter recyclerViewAdapter
     */
    public OperationsAPI(Context context, Dataset datasetList, MyAdapter recyclerViewAdapter) {
        this.datasetList = datasetList;
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.context = context;
    }

    /**
     * getContext from OperationsAPI
     * <p>Gets the context </p>
     * @return API's context
     */
    public Context getContext() {
        return context;
    }

    /**
     * getAttributesFromGreenhouseRoom
     *
     * Get the attributes (growing phase and hop's name) from the asset id specified (greenhouse room)
     * @param token token API
     * @param assetId asset id
     * @param nameRoom greenhouse room name
     */
    public void getAttributesFromGreenhouseRoom(String token, String assetId, String nameRoom) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonArray> respAttributes = tbs.getAssetServerAttributes(token, assetId);
        respAttributes.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    try {
                        //control_mode - element 0
                        //expected_harvest_date- element 1
                        //expected_hop_quality - element 2
                        //growing_phase - element 3
                        //growing_status - element 4
                        //hop_type - element 5
                        //impact alarms - element 6
                        //planting_date - element 7
                        JSONObject harvestHopDateObj = new JSONObject(response.body().getAsJsonArray().get(1).toString());
                        JSONObject hopQualityObj = new JSONObject(response.body().getAsJsonArray().get(2).toString());
                        JSONObject plantedDateObj = new JSONObject(response.body().getAsJsonArray().get(7).toString());
                        //Get first element of Json Array - Growing phase object
                        JSONObject growingPhaseObj = new JSONObject(response.body().getAsJsonArray().get(3).toString());
                        JSONObject growingStatusObj = new JSONObject(response.body().getAsJsonArray().get(4).toString());
                        //Get second element of Json Array - Hop type object
                        JSONObject hopTypeObj = new JSONObject(response.body().getAsJsonArray().get(5).toString());

                        int hopQuality = 0;
                        int growingStatus = 0;
                        String hopName = "";
                        Hop.GrowingPhase phase = null;
                        String plantedAt = "";
                        String expectedHarvestDate = "";

                        if (harvestHopDateObj.getString("key").equals("expected_harvest_date")) {
                            String day = harvestHopDateObj.getString("value").substring(8,10);
                            String month = harvestHopDateObj.getString("value").substring(4,7);
                            String year = harvestHopDateObj.getString("value").substring(11,15);
                            expectedHarvestDate = month + " " + day + ", "+year;

                        }
                        if (hopQualityObj.getString("key").equals("expected_hop_quality")) {
                            hopQuality = hopQualityObj.getInt("value");
                        }

                        if (growingStatusObj.getString("key").equals("growing_status")) {
                            growingStatus = growingStatusObj.getInt("value");
                        }

                        if (growingPhaseObj.getString("key").equals("growing_phase")) {
                            if (growingPhaseObj.getString("value").equals("Vegetative")) {
                                phase = Hop.GrowingPhase.Vegetative;
                            } else if (growingPhaseObj.getString("value").equals("Flowering")) {
                                phase = Hop.GrowingPhase.Flowering;

                            }
                        }

                        if (hopTypeObj.getString("key").equals("hop_type")) {
                            hopName = hopTypeObj.getString("value");
                        }

                        if (plantedDateObj.getString("key").equals("planting_date")) {
                            String day = plantedDateObj.getString("value").substring(8,10);
                            String month = plantedDateObj.getString("value").substring(4,7);
                            String year = plantedDateObj.getString("value").substring(11,15);
                            plantedAt = month + " " + day + ", "+year;
                        }
                        Hop hopRetrieved = new Hop(hopName, nameRoom, phase, growingStatus, plantedAt);
                        hopRetrieved.setHarvestExpectedAt(expectedHarvestDate);
                        hopRetrieved.setQuality(hopQuality);
                        datasetList.getHops().add(hopRetrieved);
                        recyclerViewAdapter.notifyDataSetChanged();

                        Log.d("RESPONSE::", "Room Attributes retrieved:" + response.body().getAsJsonArray().toString() + " Hop: " + hopName + " phase:" + phase.name());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working1");
            }
        });
    }

    /**
     * getAssetAttributes
     *
     * <p>Gets the asset id from Thingsboard, and use it to call the getAttributesFromGreenhouseRoom</p>
     * @param token token API
     * @param assetName assetName
     */
    public void getAssetAttributes(String token, String assetName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromAsset(token, assetName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id and name from the response
                        String assetId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        String name = response.body().get("name").getAsString();

                        Log.d("RESPONSE::", "Asset id retrieved:" + assetId);
                        getAttributesFromGreenhouseRoom(ThingsboardApiAdapter.getToken(), assetId, name);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });

    }

    /**
     * getAlarmsFromDeviceId
     *
     * Get all alarms from device id, and stores the active alarms in dataset's alarms list
     * @param token token API
     * @param deviceId device ID
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     */
    private void getAlarmsFromDeviceId(String token, String deviceId, String pageSize, String pageNumber ){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getAlarmsFromDevice(token, deviceId, pageSize, pageNumber);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                       // String assetId = (new JSONObject((response.body().get("data").toString())).getString("id"));
                        JSONArray alarms_data = new JSONArray(response.body().getAsJsonArray("data").toString());
                        ArrayList<Alarm> listAlarms = new ArrayList<>();
                        String alarmId;
                        String type;
                        String originatorName;
                        long createdTime;
                        long ackTs;
                        String severity;
                        String status;
                        for(int i = 0; i < alarms_data.length(); i++){
                            JSONObject alarmIdObj = new JSONObject(alarms_data.getJSONObject(i).get("id").toString());
                            alarmId = alarmIdObj.get("id").toString();
                            type = alarms_data.getJSONObject(i).get("type").toString();
                            originatorName = alarms_data.getJSONObject(i).get("originatorName").toString();
                            createdTime = alarms_data.getJSONObject(i).getLong("createdTime");
                            ackTs = alarms_data.getJSONObject(i).getLong("ackTs");
                            severity = alarms_data.getJSONObject(i).get("severity").toString();
                            status = alarms_data.getJSONObject(i).get("status").toString();
                            if (status.equals("ACTIVE_UNACK") || status.equals("ACTIVE_ACK")){
                                listAlarms.add(new Alarm(alarmId,type, originatorName, createdTime, ackTs,severity,status));
                            }
                            Log.d("RESPONSE::", "Alarm " + i + ":");
                            Log.d("RESPONSE::", "alarm id: " + alarmId);
                            Log.d("RESPONSE::", "type: " + type);
                            Log.d("RESPONSE::", "originatorName: " + originatorName);
                            Log.d("RESPONSE::", "createdTime: " + createdTime);
                            Log.d("RESPONSE::", "ackTs: " + ackTs);
                            Log.d("RESPONSE::", "severity: " + severity);
                            Log.d("RESPONSE::", "status: " + status);

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });
    }

    /**
     * getAlarmsFromAssetId
     *
     * Get all alarms from asset id, and stores the active alarms in dataset's alarms list
     * @param token token API
     * @param assetId asset ID
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     */
    private void getAlarmsFromAssetId(String token, String assetId, String pageSize, String pageNumber){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getAlarmsFromAsset(token, assetId, pageSize, pageNumber);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray alarms_data = new JSONArray(response.body().getAsJsonArray("data").toString());
                        String alarmId;
                        String type;
                        String originatorName;
                        long createdTime;
                        long ackTs;
                        String severity;
                        String status;
                        for(int i = 0; i < alarms_data.length(); i++){
                            JSONObject alarmIdObj = new JSONObject(alarms_data.getJSONObject(i).get("id").toString());
                            alarmId = alarmIdObj.get("id").toString();
                            type = alarms_data.getJSONObject(i).get("type").toString();
                            originatorName = alarms_data.getJSONObject(i).get("originatorName").toString();
                            createdTime = alarms_data.getJSONObject(i).getLong("createdTime");
                            ackTs = alarms_data.getJSONObject(i).getLong("ackTs");
                            severity = alarms_data.getJSONObject(i).get("severity").toString();
                            status = alarms_data.getJSONObject(i).get("status").toString();

                            if (status.equals("ACTIVE_UNACK") || status.equals("ACTIVE_ACK")){
                                datasetList.getAlarms().add(new Alarm(alarmId,type, originatorName, createdTime, ackTs,severity,status));
                            }

                        }
                        /*for(int j = 0; j < datasetList.sizeAlarmsList(); j++){
                            Log.d("RESPONSE::", "Alarm " + j + ":");
                            Log.d("RESPONSE::", "alarm id: " + datasetList.getAlarms().get(j).getId());
                            Log.d("RESPONSE::", "type: " + datasetList.getAlarms().get(j).getType());
                            Log.d("RESPONSE::", "originatorName: " + datasetList.getAlarms().get(j).getOriginatorName());
                            Log.d("RESPONSE::", "createdTime: " + datasetList.getAlarms().get(j).getCreatedTime());
                            Log.d("RESPONSE::", "ackTs: " + datasetList.getAlarms().get(j).getAckTime());
                            Log.d("RESPONSE::", "severity: " + datasetList.getAlarms().get(j).getSeverity());
                            Log.d("RESPONSE::", "status: " + datasetList.getAlarms().get(j).getStatus());
                        }*/


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });
    }

    /**
     * getSpecificAlarmsFromAssetIdSync
     *
     * Get all alarms from asset id with a specific alarm status, and stores the active alarms in dataset's alarms list
     * @param token token API
     * @param assetId asset ID
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     */
    private ArrayList<Alarm> getSpecificAlarmsFromAssetIdSync(String token, String assetId, String pageSize, String pageNumber,String statusAlarms){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getSpecificAlarmsFromAsset(token, assetId, pageSize, pageNumber, statusAlarms);
        ArrayList<Alarm> assetAlarmsList = new ArrayList<>();
        try{
            Response<JsonObject> response = resp.execute();

            if (response.code() == 200) {
                try {

                    JSONArray alarms_data = new JSONArray(response.body().getAsJsonArray("data").toString());
                    String alarmId;
                    String type;
                    String originatorName;
                    long createdTime;
                    long ackTs;
                    String severity;
                    String status;
                    for(int i = 0; i < alarms_data.length(); i++){
                        JSONObject alarmIdObj = new JSONObject(alarms_data.getJSONObject(i).get("id").toString());
                        alarmId = alarmIdObj.get("id").toString();
                        type = alarms_data.getJSONObject(i).get("type").toString();
                        originatorName = alarms_data.getJSONObject(i).get("originatorName").toString();
                        createdTime = alarms_data.getJSONObject(i).getLong("createdTime");
                        ackTs = alarms_data.getJSONObject(i).getLong("ackTs");
                        severity = alarms_data.getJSONObject(i).get("severity").toString();
                        status = alarms_data.getJSONObject(i).get("status").toString();

                        if (status.equals("ACTIVE_UNACK") || status.equals("ACTIVE_ACK")){
                            assetAlarmsList.add(new Alarm(alarmId,type, originatorName, createdTime, ackTs,severity,status));
                        }

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

        }else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));

        }catch (IOException e){
            e.printStackTrace();
        }
        return  assetAlarmsList;
    }

    /**
     * getAlarmsFromGreenhouseAsset
     * Get asset id, and call the getAlarmsFromAssetId()
     * @param token token API
     * @param assetName asset name
     */
    public void getAlarmsFromGreenhouseAsset(String token, String assetName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromAsset(token, assetName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                        String assetId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        Log.d("RESPONSE::", "Asset id retrieved:" + assetId);
                        getAlarmsFromAssetId(ThingsboardApiAdapter.getToken(), assetId, "100", "0");


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });

    }
    /**
     * getAlarmsFromGreenhouseAsset
     * Get asset id, and call the getSpecificAlarmsFromAssetIdSync()
     * @param token token API
     * @param assetId ID of asset
     */
    public void getAlarmsFromGreenhouseAssetSync(String token, String assetId) {
        datasetList.getAlarms().addAll(getSpecificAlarmsFromAssetIdSync(ThingsboardApiAdapter.getToken(), assetId,"100", "0","ACTIVE_ACK"));
        datasetList.getAlarms().addAll(getSpecificAlarmsFromAssetIdSync(ThingsboardApiAdapter.getToken(), assetId,"100", "0","ACTIVE_UNACK"));
    }

    /**
     * getAlarmsFromDevice
     * Get device id, and call the getAlarmsFromDeviceId()
     * @param token token API
     * @param deviceName device name
     */
    public void getAlarmsFromDevice(String token, String deviceName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromDevice(token, deviceName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                        String deviceId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        //String assetName =  response.body().get("name").toString();
                        Log.d("RESPONSE::", "Device id retrieved:" + deviceId);
                        getAlarmsFromDeviceId(ThingsboardApiAdapter.getToken(), deviceId, "100", "0");



                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });

    }

    /**
     * getDatasetList
     * @return dataset
     */
    public Dataset getDatasetList() {
        return datasetList;
    }
}
