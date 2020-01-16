package com.comic.mario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comic.mario.MarioApplication;
import com.comic.mario.R;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

public class WebNewActivity extends AppCompatActivity {

    private ImageView ig_finish_webnew_activity;
    private TextView ig_sure_webnew_activity;

    private EditText et_content_webnew_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_new);
        ig_finish_webnew_activity = findViewById(R.id.ig_finish_webnew_activity);
        ig_sure_webnew_activity = findViewById(R.id.ig_sure_webnew_activity);
        et_content_webnew_activity = findViewById(R.id.et_content_webnew_activity);

        ig_finish_webnew_activity.setOnClickListener(v -> {
            finish();
        });
        ig_sure_webnew_activity.setOnClickListener(v -> {
            if (et_content_webnew_activity.getText().toString().trim().isEmpty())
                return;
            new Thread(() -> {
                try {
                    JSONObject jsonArray = new JSONObject(et_content_webnew_activity.getText().toString());
                    WebBean webBean = new Gson().fromJson(jsonArray.toString(), WebBean.class);
                    if (webBean.getWeb().equals("ddmmcc") || webBean.getWeb().equals("ssoonn")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "已存在相同web", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    List<String> ohterFiles = Util.getFilesAllName(MarioApplication.WebFilePath);
                    for (String string : ohterFiles) {
                        if (webBean.getWeb().equals(string.replace("web_", ""))) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "已存在相同web名", Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }
                    }
                    String storePath = MarioApplication.WebFilePath;
                    File file = new File(storePath);
                    Util.writeTxtToFile(jsonArray.toString(), file.getPath(), "web_" + webBean.getWeb(), true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "存储失败,确认写入内容正确性", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "已存储", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

}
