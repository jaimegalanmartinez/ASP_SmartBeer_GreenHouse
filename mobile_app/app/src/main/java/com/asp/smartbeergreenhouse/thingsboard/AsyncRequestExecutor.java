package com.asp.smartbeergreenhouse.thingsboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class AsyncRequestExecutor {

    private static final String KEY_SUCCESS = "SUCCESS";
    private static final String KEY_PAYLOAD = "PAYLOAD";

    private final Executor executor = Executors.newSingleThreadExecutor();

    public void execute(Call<JsonObject> request, ResponseHandler responseHandler) {

        Handler messageHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                boolean success = message.getData().getBoolean(KEY_SUCCESS, false);

                if (success) {
                    try {
                        responseHandler.onResponse(true, new JSONObject(message.getData().getString(KEY_PAYLOAD)));
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                responseHandler.onResponse(false, null);
            }
        };

        Runnable requestTask = new Runnable() {
            public void run() {
                boolean success;
                String payload = null;

                // Execute request
                try {
                    Response<JsonObject> response = request.execute();
                    success = (response.code() == 200);
                    if (success)
                        payload = response.body().toString();
                } catch (Exception e) {
                    success = false;
                    payload = e.toString();
                    e.printStackTrace();
                }

                // Send response message to Handler
                Message msg;
                Bundle data;

                msg = messageHandler.obtainMessage();
                data = msg.getData();
                data.putBoolean(KEY_SUCCESS, success);
                data.putString(KEY_PAYLOAD, payload);
                msg.sendToTarget();
            }
        };

        executor.execute(requestTask);
    }

    public interface ResponseHandler {
        void onResponse(boolean success, JSONObject payloadJson);
    }
}
