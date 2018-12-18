package com.continuum.cucumber.utils;


import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Properties;

public class JsonUtility {
    public static Properties properties;
    private static Object gotValue = null;
    private static JSONObject jsonObj = null;
    private static Object value = null;

    static {
        properties = new Properties();
    }

    public JsonUtility() {
    }

    public static Object getValueOfKeyFromJson(JSONObject jsonObject, String searchKey, String value) throws ParseException {
        Iterator iterator = jsonObject.keySet().iterator();

        Object key;
        try {
            while (iterator.hasNext()) {
                key = iterator.next();
                if (searchKey.equals(key) && jsonObject.get(key).toString().equalsIgnoreCase(value)) {
                    gotValue = jsonObject;
                    break;
                }

                if (jsonObject.get(key) instanceof JSONObject) {
                    getValueOfKeyFromJson((JSONObject) jsonObject.get(key), searchKey, value);
                } else if (jsonObject.get(key) instanceof JSONArray) {
                    getArray(jsonObject.get(key), searchKey, value);
                }
            }

            key = gotValue;
        } finally {
            gotValue = null;
        }

        return key;
    }

    public static void getArray(Object object2, String searchKey, String value) throws ParseException {
        JSONArray jsonArr = (JSONArray) object2;
        for (int k = 0; k < jsonArr.size(); ++k) {
            if (jsonArr.get(k) instanceof JSONObject) {
                if (value.isEmpty()) {
                    getValueOfKeyFromJson((JSONObject) jsonArr.get(k), searchKey);
                } else {
                    getValueOfKeyFromJson((JSONObject) jsonArr.get(k), searchKey, value);
                }
            }
        }

    }

    public static Object getValueOfKeyFromJson(JSONObject jsonObject, String searchKey) throws ParseException {
        Iterator iterator = jsonObject.keySet().iterator();

        Object key;
        try {
            while (iterator.hasNext()) {
                key = iterator.next();
                if (searchKey.equals(key)) {
                    value = jsonObject.get(key);
                    break;
                }

                if (jsonObject.get(key) instanceof JSONObject) {
                    getValueOfKeyFromJson((JSONObject) jsonObject.get(key), searchKey);
                } else if (jsonObject.get(key) instanceof JSONArray) {
                    getArray(jsonObject.get(key), searchKey, "");
                }
            }

            key = value;
        } finally {
            value = null;
        }

        return key;
    }

    public static JSONObject iterateOverJSONArray(JSONArray jsonArr, String searchKey, String value) throws ParseException {
        for (int k = 0; k < jsonArr.size(); ++k) {
            if (jsonArr.get(k) instanceof JSONObject) {
                if (value.isEmpty()) {
                    jsonObj = (JSONObject) getValueOfKeyFromJson((JSONObject) jsonArr.get(k), searchKey);
                    if (jsonObj != null) {
                        break;
                    }
                } else {
                    jsonObj = (JSONObject) getValueOfKeyFromJson((JSONObject) jsonArr.get(k), searchKey, value);
                    if (jsonObj != null) {
                        break;
                    }
                }
            }
        }

        return jsonObj;
    }

    public static JSONObject convertResponseToJsonObject(Response res) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(res.asString());
        } catch (org.json.simple.parser.ParseException var4) {
            var4.printStackTrace();
        }
        return obj;
    }

    public static JSONArray convertResponseToJsonArray(Response res) {
        JSONArray obj = null;
        try {
            obj = (JSONArray) new JSONParser().parse(res.asString());
        } catch (org.json.simple.parser.ParseException var4) {
            var4.printStackTrace();
        }
        return obj;
    }

    public static void assertJsonsEquality(String json1, String json2) throws JSONException {
        JSONAssert.assertEquals(json2, json1, false);
    }

    public static String getFormattedJson(Object json) {
        Gson jsonObj = (new GsonBuilder()).setPrettyPrinting().create();
        return jsonObj.toJson(json);
    }

    public static void verifyKeyPresent(JSONObject specificPatchPolicyDetailsData, String keyToCheck) {
        Assert.assertTrue(keyToCheck + " feld is not present in the response: " + specificPatchPolicyDetailsData,
                specificPatchPolicyDetailsData.containsKey(keyToCheck));
    }
}