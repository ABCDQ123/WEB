package com.comic.mario.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String getJsonfromAsset(Context context, String file) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(file), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String readTxtFile(String strFilePath) {
        String res = "";
        try {
            File file = new File(strFilePath);
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            int length = fis.available();

            byte[] buffer = new byte[length];
            fis.read(buffer);
            res = new String(buffer, "UTF-8");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        if (file == null) {
            return new ArrayList<>();
        }
        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getName());
        }
        return s;
    }
}
