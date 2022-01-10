package com.asp.smartbeergreenhouse.thingsboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ThingsboardService {

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @POST("auth/login")
    Call<JsonObject> getToken (@Body JsonObject credentials);


    @Headers({"Content-Type: application/json"})
    @GET("plugins/telemetry/ASSET/{assetID}/values/attributes")
    Call<JsonArray> getAssetServerAttributes (@Header("X-Authorization") String token,
                                              @Path("assetID") String id);


    @Headers({"Content-Type: application/json"})
    @GET("tenant/assets?")
    Call<JsonObject> getInfoFromAsset (@Header("X-Authorization") String token,
                                               @Query("assetName") String name);


    @Headers({"Content-Type: application/json"})
    @GET("tenant/devices?")
    Call<JsonObject> getInfoFromDevice (@Header("X-Authorization") String token,
                                       @Query("deviceName") String name);


    @Headers({"Content-Type: application/json"})
    @GET("alarm/DEVICE/{deviceId}?fetchOriginator=true")
    Call<JsonObject> getAlarmsFromDevice (@Header("X-Authorization") String token,
                               @Path("deviceId") String deviceId, @Query("pageSize") String pageSize, @Query("page") String pageNumber);

    @Headers({"Content-Type: application/json"})
    @GET("alarm/ASSET/{assetId}?fetchOriginator=true")
    Call<JsonObject> getAlarmsFromAsset (@Header("X-Authorization") String token,
                                          @Path("assetId") String assetId, @Query("pageSize") String pageSize, @Query("page") String pageNumber);

    //asset id: b0855880-6c82-11ec-9a04-591db17ccd5b - GH01_Room_01
    //asset id: d61abbe0-6c81-11ec-9a04-591db17ccd5b - GH1_GHR01_Row_01
    //asset id: f83df100-6c82-11ec-9a04-591db17ccd5b - Greenhouse_01

}
