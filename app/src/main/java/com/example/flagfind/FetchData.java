package com.example.flagfind;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FetchData extends AsyncTask<String, String, HashMap<String, Object>> {

    private Context mContext;
    private TaskCompleted mCallback;

    private final String URL = "https://raw.githubusercontent.com/Thinura/country-flags/Thinura-patch-1/countries.json";

    public FetchData(Context context) {
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;
    }

    @Override
    protected HashMap<String, Object> doInBackground(String... strings) {
        String data = "";

        try {

//          Creating a connection by using the url.
            URL url = new URL(URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

//          Reading from url using Buffer reader.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject jsonObject = new JSONObject(data);
            return jsonToMap(jsonObject);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
        mCallback.onTaskComplete(stringObjectHashMap);
    }

//    Mapping the JSON Object
    public HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<>();
        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
