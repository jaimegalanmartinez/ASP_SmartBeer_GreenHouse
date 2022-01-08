package com.asp.smartbeergreenhouse.thingsboard;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThingsboardApiAdapter {

    private static final String BASE_URL = "https://srv-iot.diatel.upm.es/api/";
    private static ThingsboardService API_SERVICE;
    //https://programacionymas.com/blog/consumir-una-api-usando-retrofit

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

}
