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

/**
 * ThingsboardService interface
 *
 * <p>Defines the calls to the Thingsboard REST API</p>
 * <p>GET operations:</p>
 * <p>- Call<JsonArray> getAssetServerAttributes (@Header("X-Authorization") String token,@Path("assetID") String id);</p>
 * <p>- Call<JsonObject> getInfoFromAsset (@Header("X-Authorization") String token, @Query("assetName") String name)</p>
 * <p>- Call<JsonObject> getInfoFromDevice (@Header("X-Authorization") String token, @Query("deviceName") String name);</p>
 * <p>- Call<JsonObject> getAlarmsFromDevice (@Header("X-Authorization") String token,</p>
 * <p>  @Path("deviceId") String deviceId, @Query("pageSize") String pageSize, @Query("page") String pageNumber);</p>
 * <p>- Call<JsonObject> getAlarmsFromAsset (@Header("X-Authorization") String token,</p>
 * <p>  @Path("assetId") String assetId, @Query("pageSize") String pageSize, @Query("page") String pageNumber)</p>
 * <p>POST operations:</p>
 * <p>- Call<JsonObject> getToken (@Body JsonObject credentials)</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
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


    @Headers({"Content-Type: application/json"})
    @GET("alarm/ASSET/{assetId}?fetchOriginator=true")
    Call<JsonObject> getSpecificAlarmsFromAsset (@Header("X-Authorization") String token,
                                         @Path("assetId") String assetId,  @Query("pageSize") String pageSize, @Query("page") String pageNumber,
                                                 @Query("status") String status);

}
