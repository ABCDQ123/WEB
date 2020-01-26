package com.comic.mario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.comic.mario.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebIntroActivity extends AppCompatActivity {

    private TextView tv_web_intro;
    private ImageView tv_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_intro);
        tv_finish = findViewById(R.id.tv_finish);
        tv_web_intro = findViewById(R.id.tv_web_intro);
        tv_finish.setOnClickListener(v -> {
            finish();
        });
        tv_web_intro.setText("" + getfromAsset(this, "introduce"));
    }

    public String getfromAsset(Context context, String file) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(file), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
