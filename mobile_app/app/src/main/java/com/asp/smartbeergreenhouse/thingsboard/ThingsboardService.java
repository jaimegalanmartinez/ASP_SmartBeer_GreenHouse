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
    Call<JsonObject> getToken (@Body String credentials);


    @Headers({"Content-Type: application/json"})
    @GET("tenant/assetInfos?page=0&pageSize=100&type=green_house")
    Call<JsonObject> getAssetsTen (@Header("X-Authorization") String token);

    @Headers({"Content-Type: application/json"})
    @GET("alarm/")
    Call<JsonObject> getAlarm (@Header("X-Authorization") String token,
                               @Path("alarmId") String idAlarm);


    @Headers({"Content-Type: application/json"})
    @GET("plugins/telemetry/ASSET/{assetID}/values/attributes")
    Call<JsonArray> getAssetServerAttributes (@Header("X-Authorization") String token,
                                              @Path("assetID") String id);


    @Headers({"Content-Type: application/json"})
    @GET("tenant/assets?")
    Call<JsonObject> getInfoFromAsset (@Header("X-Authorization") String token,
                                               @Query("assetName") String name);

    //asset id: b0855880-6c82-11ec-9a04-591db17ccd5b - GH01_Room_01
    //asset id: d61abbe0-6c81-11ec-9a04-591db17ccd5b - GH1_GHR01_Row_01
    //asset id: f83df100-6c82-11ec-9a04-591db17ccd5b - Greenhouse_01
    /*@Headers({"Content-Type: application/json"})
    @GET("entityView/info/")
    Call<JsonObject> getInfoEntityView (@Header("X-Authorization") String token,
                                        @Path("entityViewId") String entityViewId);
    */

}
