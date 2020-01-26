package com.comic.mario.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import com.comic.mario.MarioApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ComicPreference {

    private Context mContext;

    private final String Collect_File = "Collect.json";
    private final String History_File = "History.json";
    private final String Data_FilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ComicMario/MSource/Json";

    public ComicPreference(Context context) {
        mContext = context;
    }

    public void commitCollect(String webName, String url, String title, String image) {
        try {
            String string = Util.readTxtFile(Data_FilePath + "/" + Collect_File);
            JSONArray jsonArray;
            if (string == null || string.isEmpty()) {
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
            jsonArray.put(jsonObject);
            Util.writeTxtToFile(jsonArray.toString(), Data_FilePath, Collect_File, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCollect() {
        String string = Util.readTxtFile(Data_FilePath + "/" + Collect_File);
        return string;
    }

    public void removeCollect(String webName, String title) {
        try {
            String string = Util.readTxtFile(Data_FilePath + "/" + Collect_File);
            JSONArray jsonArray = new JSONArray(string);
            f:
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.toString().contains(title) && jsonObject.toString().contains(webName)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(i);
                        Util.writeTxtToFile(jsonArray.toString(), Data_FilePath, Collect_File, true);
                        break f;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void commitHist(String webName, String url, String title, String episode, int index, String image) {
        try {
            String string = Util.readTxtFile(Data_FilePath + "/" + History_File);
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
            Util.writeTxtToFile(jsonArray.toString(), Data_FilePath, History_File, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHist() {
        String string = Util.readTxtFile(Data_FilePath + "/" + History_File);
        return string;
    }

    public void removeHist(String webname, String title) {
        try {
            String string = Util.readTxtFile(Data_FilePath + "/" + History_File);
            JSONArray jsonArray = new JSONArray(string);
            f:
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.toString().contains(title) && jsonObject.toString().contains(webname)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(i);
                        Util.writeTxtToFile(jsonArray.toString(), Data_FilePath, History_File, true);
                        break f;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
