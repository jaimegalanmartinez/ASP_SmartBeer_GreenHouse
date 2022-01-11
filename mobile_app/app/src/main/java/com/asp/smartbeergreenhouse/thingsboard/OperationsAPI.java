package com.asp.smartbeergreenhouse.thingsboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.asp.smartbeergreenhouse.activities.AlarmsActivity;
import com.asp.smartbeergreenhouse.activities.FarmerActivity;
import com.asp.smartbeergreenhouse.model.Alarm;
import com.asp.smartbeergreenhouse.model.Dataset;
import com.asp.smartbeergreenhouse.model.Hop;
import com.asp.smartbeergreenhouse.utils.MyAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationsAPI {

    private String tokenAPI;
    private final JsonObject credentialsAPI;
    private Dataset datasetList;
    private MyAdapter recyclerViewAdapter;
    private Context context;

    public OperationsAPI(Context context, Dataset datasetList, MyAdapter recyclerViewAdapter) {
        this.datasetList = datasetList;
        this.credentialsAPI = setCredentialsAPI("jaime.galan.martinez@alumnos.upm.es", "134things!A");
        this.tokenAPI = getToken(credentialsAPI);
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.context = context;
    }


    private JsonObject setCredentialsAPI(String username, String password) {
        JsonObject userAPIcredentials = new JsonObject();
        try {
            userAPIcredentials.addProperty("username", username);
            userAPIcredentials.addProperty("password", password);
            Log.d("JSON_api: ", userAPIcredentials.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return userAPIcredentials;
    }

    private String getToken(JsonObject credentials) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getToken(credentials);
        String token ="";
        try{
            Response<JsonObject> response = resp.execute();

            if(response.code() == 200){
                try {
                    //here we get the token from the response
                    token = "Bearer " + (new JSONObject((response.body().toString())).getString("token"));

                    Log.d("RESPONSE::", "Token retrieved:" + token);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));

        }catch (IOException e){
            e.printStackTrace();
        }


        return token;
        /*resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the token from the response
                        tokenAPI = "Bearer " + (new JSONObject((response.body().toString())).getString("token"));

                        Log.d("RESPONSE::", "Token retrieved:" + tokenAPI);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else Log.d("RESPONSE:ERROR code: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE:ERROR", "Error, not working");
            }
        });
        */

    }

    public String getTokenAPI() {
        return tokenAPI;
    }

    public void getAttributesFromGreenhouseRoom(String token, String assetId) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonArray> respAttributes = tbs.getAssetServerAttributes(token, assetId);
        respAttributes.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    try {
                        //Get first element of Json Array - Growing phase object
                        JSONObject growingPhaseObj = new JSONObject(response.body().getAsJsonArray().get(0).toString());
                        //Get second element of Json Array - Hop type object
                        JSONObject hopTypeObj = new JSONObject(response.body().getAsJsonArray().get(1).toString());

                        String hopName = "";
                        Hop.GrowingPhase phase = null;
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

                        datasetList.getHops().add(new Hop(hopName, "GH01_Room_01", phase, 40));
                        recyclerViewAdapter.notifyDataSetChanged();


                        Log.d("RESPONSE::", "Room Attributes retrieved:" + response.body().getAsJsonArray().toString() + " Hop: " + hopName + " phase:" + phase.name());

                    } catch (Exception ex) {
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

    public void getAssetAttributes(String token, String assetName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromAsset(token, assetName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                        String assetId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        //String assetName =  response.body().get("name").toString();
                        Log.d("RESPONSE::", "Asset id retrieved:" + assetId);
                        getAttributesFromGreenhouseRoom(tokenAPI, assetId);


                    } catch (Exception ex) {
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

    private void getAlarmsFromDeviceId(String token, String deviceId, String pageSize, String pageNumber ){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getAlarmsFromDevice(token, deviceId, pageSize, pageNumber);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                        Log.d("RESPONSE::", "Alarm 0, created time " + listAlarms.get(0).getCreatedTime());

                    } catch (Exception ex) {
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

    private void getAlarmsFromAssetId(String token, String assetId, String pageSize, String pageNumber){
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getAlarmsFromAsset(token, assetId, pageSize, pageNumber);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
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
                        Intent i = new Intent(context, AlarmsActivity.class);
                        i.putExtra("dataset", datasetList);
                        context.startActivity(i);



                    } catch (Exception ex) {
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

    public void getAlarmsFromGreenhouseAsset(String token, String assetName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromAsset(token, assetName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                        String assetId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        Log.d("RESPONSE::", "Asset id retrieved:" + assetId);
                        getAlarmsFromAssetId(tokenAPI, assetId, "100", "0");


                    } catch (Exception ex) {
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
    
    public void getAlarmsFromDevice(String token, String deviceName) {
        ThingsboardService tbs = ThingsboardApiAdapter.getApiService();
        Call<JsonObject> resp = tbs.getInfoFromDevice(token, deviceName);
        //this enqueue of the Callback means we are making an asynchronous request (which won't
        //block the UI-thread)
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        //here we get the asset id from the response
                        String deviceId = (new JSONObject((response.body().get("id").toString())).getString("id"));
                        //String assetName =  response.body().get("name").toString();
                        Log.d("RESPONSE::", "Device id retrieved:" + deviceId);
                        getAlarmsFromDeviceId(tokenAPI, deviceId, "100", "0");



                    } catch (Exception ex) {
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

    public Dataset getDatasetList() {
        return datasetList;
    }
}
