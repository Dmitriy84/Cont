package com.continuum.cucumber.be;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    public static String toPrettyFormat(String jsonString) {
        JsonObject json = jsonParser.parse(jsonString).getAsJsonObject();
        return gson.toJson(json);
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            jsonParser.parse(jsonInString).getAsJsonObject();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}