package com.comic.mario.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComicPreference {

    private Context mContext;

    public ComicPreference(Context context) {
        mContext = context;
    }

    private final String COMIC_COLLECT_FILE = "COMIC_COLLECT_FILE";
    private final String COMIC_COLLECT_JSON = "COMIC_COLLECT_JSON";

    public void commitCollect(String webName, String url, String title, String image) {
        try {
            SharedPreferences spRead = mContext.getSharedPreferences(COMIC_COLLECT_FILE, Context.MODE_PRIVATE);
            String string = spRead.getString(COMIC_COLLECT_JSON, null);
            JSONArray jsonArray;
            if (string == null) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(string);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("WebName", webName);
            jsonObject.put("Url", url);
            jsonObject.put("Title", title);
            jsonObject.put("Image", image);
            jsonArray.put(jsonObject);
            SharedPreferences spWrite = mContext.getSharedPreferences(COMIC_COLLECT_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spWrite.edit();
            editor.putString(COMIC_COLLECT_JSON, jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCollect() {
        SharedPreferences sp = mContext.getSharedPreferences(COMIC_COLLECT_FILE, Context.MODE_PRIVATE);
        String json = sp.getString(COMIC_COLLECT_JSON, null);
        return json;
    }

    public void removeCollect(String webName, String title) {
        try {
            SharedPreferences spRead = mContext.getSharedPreferences(COMIC_COLLECT_FILE, Context.MODE_PRIVATE);
            String string = spRead.getString(COMIC_COLLECT_JSON, null);
            JSONArray jsonArray = new JSONArray(string);
            f:
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.toString().contains(title) && jsonObject.toString().contains(webName)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(i);
                        SharedPreferences spWrite = mContext.getSharedPreferences(COMIC_COLLECT_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spWrite.edit();
                        editor.putString(COMIC_COLLECT_JSON, jsonArray.toString());
                        editor.commit();
                        break f;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private final String COMIC_HIST_FILE = "COMIC_HIST_FILE";
    private final String COMIC_HIST_JSON = "COMIC_HIST_JSON";

    public void commitHist(String webName, String url, String title, String episode, int index, String image) {
        try {
            SharedPreferences spRead = mContext.getSharedPreferences(COMIC_HIST_FILE, Context.MODE_PRIVATE);
            String string = spRead.getString(COMIC_HIST_JSON, null);
            JSONArray jsonArray;
            if (string == null) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(string);
            }
            f:
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.toString().contains(title) && jsonObject.toString().contains(webName)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(i);
                        break f;
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("WebName", webName);
            jsonObject.put("Url", url);
            jsonObject.put("Title", title);
            jsonObject.put("Image", image);
            jsonObject.put("Episode", episode);
            jsonObject.put("index", index);
            jsonArray.put(jsonObject);
            SharedPreferences spWrite = mContext.getSharedPreferences(COMIC_HIST_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spWrite.edit();
            editor.putString(COMIC_HIST_JSON, jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHist() {
        SharedPreferences sp = mContext.getSharedPreferences(COMIC_HIST_FILE, Context.MODE_PRIVATE);
        String json = sp.getString(COMIC_HIST_JSON, null);
        return json;
    }

    public void removeHist(String webname, String title) {
        try {
            SharedPreferences spRead = mContext.getSharedPreferences(COMIC_HIST_FILE, Context.MODE_PRIVATE);
            String string = spRead.getString(COMIC_HIST_JSON, null);
            JSONArray jsonArray = new JSONArray(string);
            f:
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.toString().contains(title) && jsonObject.toString().contains(webname)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(i);
                        SharedPreferences spWrite = mContext.getSharedPreferences(COMIC_HIST_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spWrite.edit();
                        editor.putString(COMIC_HIST_JSON, jsonArray.toString());
                        editor.commit();
                        break f;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
