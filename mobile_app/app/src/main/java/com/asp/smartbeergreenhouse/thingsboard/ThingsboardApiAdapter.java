package com.asp.smartbeergreenhouse.thingsboard;

import android.util.Log;

import com.asp.smartbeergreenhouse.model.Asset;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ThingsboardApiAdapter
 * <p>Class that uses singleton pattern, to have an only instance for the API Service</p>
 * @see ThingsboardService
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class ThingsboardApiAdapter {

    private static final String BASE_URL = "https://srv-iot.diatel.upm.es/api/";
    private static ThingsboardService API_SERVICE;
    //https://programacionymas.com/blog/consumir-una-api-usando-retrofit

    /**
     * Represents the tokenAPI retrieved from Thingsboard in order to use the Thingsboard REST API
     */
    private static String tokenAPI = null;

    private static List<Asset> assets = null;

    public static ThingsboardService getApiService() {

        // Create an interceptor and indicate log level to use
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        // Link interceptor to the requests
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- Using log level
                    .build();
            API_SERVICE = retrofit.create(ThingsboardService.class);
        }

        return API_SERVICE;
    }

    public static void login(String username, String password, LoginHandler handler) {
        JsonObject credentials = new JsonObject();
        try {
            credentials.addProperty("username", username);
            credentials.addProperty("password", password);
            Log.d("JSON_api: ", credentials.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        AsyncRequestExecutor executor = new AsyncRequestExecutor();
        executor.execute(getApiService().getToken(credentials), (success, payloadJson) -> {
            if (success) {
                try {
                    tokenAPI = "Bearer " + payloadJson.getString("token");
                    Log.d("RESPONSE::", "Token retrieved:" + tokenAPI);
                    handler.onResponse(true);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            handler.onResponse(false);
        });
    }

    public static void fetchTenantAssets(LoginHandler handler) {
        fetchAssets(getApiService().getTenantAssets(getToken(), "100", "0"), handler);
    }

    static void fetchAssets(Call<JsonObject> fetcher, LoginHandler handler) {
        AsyncRequestExecutor executor = new AsyncRequestExecutor();

        executor.execute(fetcher, (success, payloadJson) -> {
            if (success)
                try {
                    assets = Asset.parseJsonArray(payloadJson.getJSONArray("data"));
                    handler.onResponse(true);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            handler.onResponse(false);
        });
    }

    /**
     * getTokenString
     * <p>Gets the token attribute to use Thingsboard REST API </p>
     *
     * @return API's token
     */
    public static String getToken() {
        if (tokenAPI == null)
            throw new RuntimeException("Log in first");
        return tokenAPI;
    }

    public static List<Asset> getAssets() {
        return assets;
    }

    public interface LoginHandler {
        void onResponse(boolean success);
    }
}
