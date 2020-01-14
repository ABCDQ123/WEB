package com.comic.mario.util;

import android.content.Context;
import android.content.SharedPreferences;

public class WebPreference {

    private Context mContext;

    public WebPreference(Context context) {
        mContext = context.getApplicationContext();
    }

    private final String WEB_FILE = "WEB_FILE";
    private final String WEB_FILES = "WEB_FILES";

    public void commit(String webFileName) {
        SharedPreferences spRead = mContext.getSharedPreferences(WEB_FILE, Context.MODE_PRIVATE);
        String files = spRead.getString(WEB_FILES, null);
        if (files == null) {
            SharedPreferences spWrite = mContext.getSharedPreferences(WEB_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spWrite.edit();
            editor.putString(WEB_FILES, "" + webFileName);
            editor.commit();
        } else {
            if (files.contains(webFileName)) {
                return;
            }
            SharedPreferences spWrite = mContext.getSharedPreferences(WEB_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spWrite.edit();
            editor.putString(WEB_FILES, "" + files + "@" + webFileName);
            editor.commit();
        }
    }

    public String get() {
        SharedPreferences sp = mContext.getSharedPreferences(WEB_FILE, Context.MODE_PRIVATE);
        String files = sp.getString(WEB_FILES, null);
        return files;
    }

    private final String WEB_SELECT_FILE = "WEB_SELECT_FILE";
    private final String WEB_SELECT_FILE_NAME = "WEB_SELECT_FILE_NAME";

    public void commitSelect(String selectName) {
        SharedPreferences spWrite = mContext.getSharedPreferences(WEB_SELECT_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spWrite.edit();
        editor.clear();
        editor.putString(WEB_SELECT_FILE_NAME, selectName);
        editor.commit();
    }

    public String getSelect() {
        SharedPreferences sp = mContext.getSharedPreferences(WEB_SELECT_FILE, Context.MODE_PRIVATE);
        String files = sp.getString(WEB_SELECT_FILE_NAME, "ssoon");
        return files;
    }
}
