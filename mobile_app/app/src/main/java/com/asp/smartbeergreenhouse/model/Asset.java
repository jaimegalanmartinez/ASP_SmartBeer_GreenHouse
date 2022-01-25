package com.asp.smartbeergreenhouse.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Asset {
    String id;
    String type; // TODO use enum;
    String name;

    public Asset(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static List<Asset> parseJsonArray(JSONArray array) throws JSONException {
        List<Asset> assets = new LinkedList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            String id = json.getJSONObject("id").getString("id");
            String type = json.getString("type");
            String name = json.getString("name");

            assets.add(new Asset(id, type, name));
        }

        return assets;
    }
}
