package com.comic.mario.ui;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comic.mario.MarioApplication;
import com.comic.mario.R;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.util.Util;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WebEditActivity extends AppCompatActivity implements MutiViewHolder {

    private ImageView ig_finish;
    private TextView tv_rank;
    private TextView tv_classify;
    private TextView tv_search;
    private TextView tv_detail;
    private TextView tv_read;

    private RecyclerView rv;
    private RecyclerView.Adapter adapter;

    private List<MultiData> items;

    private WebBean mWebBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_edit);
        tv_rank = findViewById(R.id.tv_rank);
        tv_classify = findViewById(R.id.tv_classify);
        tv_search = findViewById(R.id.tv_search);
        tv_detail = findViewById(R.id.tv_detail);
        tv_read = findViewById(R.id.tv_read);
        ig_finish = findViewById(R.id.ig_finish);
        rv = findViewById(R.id.rv);
        String web = getIntent().getStringExtra("web");
        String webContentAsset = Util.getJsonfromAsset(this, web);
        String webContentSd = Util.readTxtFile(MarioApplication.WebFilePath + "/" + web);
        if (webContentAsset != null && !webContentAsset.isEmpty()) {
            mWebBean = new Gson().fromJson(webContentAsset, WebBean.class);
        } else {
            mWebBean = new Gson().fromJson(webContentSd, WebBean.class);
        }
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterManager().getLinearMultiAdapter(this, items = new ArrayList<>(), this);
        rv.setAdapter(adapter);

        ig_finish.setOnClickListener(v -> {
            finish();
        });
        tv_rank.setOnClickListener(v -> {
            if (mWebBean == null) {
                return;
            }
            charge(0);
        });
        tv_classify.setOnClickListener(v -> {
            if (mWebBean == null) {
                return;
            }
            charge(1);
        });
        tv_search.setOnClickListener(v -> {
            if (mWebBean == null) {
                return;
            }
            charge(2);
        });
        tv_detail.setOnClickListener(v -> {
            if (mWebBean == null) {
                return;
            }
            charge(3);
        });
        tv_read.setOnClickListener(v -> {
            if (mWebBean == null) {
                return;
            }
            charge(4);
        });
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {

    }

    private void charge(int position) {
        tv_rank.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv_classify.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv_search.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv_detail.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv_read.setTextColor(ContextCompat.getColor(this, R.color.white));
        if (position == 0) {
            tv_rank.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
        } else if (position == 1) {
            tv_classify.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
        } else if (position == 2) {
            tv_search.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
        } else if (position == 3) {
            tv_detail.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
        } else if (position == 4) {
            tv_read.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
        }
    }
}
