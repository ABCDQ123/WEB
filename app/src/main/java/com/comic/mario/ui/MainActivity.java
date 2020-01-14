package com.comic.mario.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.comic.mario.MarioApplication;
import com.comic.mario.R;
import com.comic.mario.ui.activity.ClassifyActivity;
import com.comic.mario.ui.activity.DetailActivity;
import com.comic.mario.ui.activity.SearchActivity;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.fragment.CollectFragment;
import com.comic.mario.ui.fragment.HistoryFragment;
import com.comic.mario.ui.fragment.RankFragment;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.Util;
import com.comic.mario.util.WebPreference;
import com.comic.mario.util.dialog.MessageDialog;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.comic.mario.util.viewpager.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, MutiViewHolder {


    private ImageView ig_opendraw_main_activity;
    private ImageView ig_search_main_activity;
    private ImageView ig_type_main_activity;
    private DrawerLayout dl_main_activity;
    private LinearLayout ll_put_web_main_activity;

    private TabLayout tl_main_activity;
    private ViewPager vp_main_activity;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();

    private RecyclerView rv_draw_main_activity;
    private RecyclerView.Adapter adapter;
    private ArrayList<MultiData> listItems = new ArrayList<>();
    private ArrayList<WebBean.RankBean> pagerItems = new ArrayList<>();
    private WebPreference mWebPreference;
    private WebBean mWebBean;

    private String mCurrentWeb = "";

    private MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemUtil.setStatusBarTransparent(getWindow());
        SystemUtil.setMyStatusbar(this, findViewById(R.id.v_status_main_activity));
        vp_main_activity = findViewById(R.id.vp_main_activity);
        tl_main_activity = findViewById(R.id.tl_main_activity);
        ig_opendraw_main_activity = findViewById(R.id.ig_opendraw_main_activity);
        ig_search_main_activity = findViewById(R.id.ig_search_main_activity);
        dl_main_activity = findViewById(R.id.dl_main_activity);
        ll_put_web_main_activity = findViewById(R.id.ll_put_web_main_activity);
        ig_type_main_activity = findViewById(R.id.ig_type_main_activity);
        rv_draw_main_activity = findViewById(R.id.rv_draw_main_activity);

        ig_opendraw_main_activity.setOnClickListener(this);
        ig_search_main_activity.setOnClickListener(this);
        ig_type_main_activity.setOnClickListener(this);
        ll_put_web_main_activity.setOnClickListener(this);
        messageDialog = new MessageDialog(this);

        requestPermissions();

        initData();
    }

    private void initData() {
        new Thread(() -> {
            if (mWebPreference == null) {
                mWebPreference = new WebPreference(this);
            }
            if (mWebPreference.get() == null) {
                mWebPreference.commit("web_ddmmcc");
                mWebPreference.commit("web_ssoonn");
            }
            List<String> ohterFiles = Util.getFilesAllName(MarioApplication.WebFilePath);//外部文件名列表
            String errorFile = "";//发生错误时记录错误的外部文件名
            try {//初始化外部Web文件
                mCurrentWeb = mWebPreference.getSelect();
                if (mWebBean == null && ohterFiles != null || mCurrentWeb.isEmpty()) {
                    for (String string : ohterFiles) {
                        if (string.equals(mCurrentWeb)) {
                            errorFile = string;
                            mCurrentWeb = string;
                            mWebBean = new Gson().fromJson(Util.readTxtFile(MarioApplication.WebFilePath + "/" + string), WebBean.class);
                        }
                    }
                }
            } catch (Exception e) {
                File file = new File(MarioApplication.WebFilePath + "/" + errorFile);
                file.delete();
                runOnUiThread(() -> {
                    Toast.makeText(this, "所选项目发生错误，已删除", Toast.LENGTH_SHORT).show();
                });
            } finally {//初始化APP内部默认Web
                String files[] = mWebPreference.get().split("@");
                if (mWebBean == null || mWebBean.getRank() == null || mWebBean.getRank().size() == 0 || mCurrentWeb.isEmpty()) {//初始化app内部web文件
                    mCurrentWeb = mWebPreference.getSelect();
                    for (String string : files) {
                        if (string.equals(mCurrentWeb)) {
                            mCurrentWeb = string;
                            mWebBean = new Gson().fromJson(Util.getJsonfromAsset(this, string), WebBean.class);
                        }
                    }
                }
                if (mWebBean == null || mWebBean.getRank() == null || mWebBean.getRank().size() == 0 || mCurrentWeb.isEmpty()) {//文件加载出问题时
                    mCurrentWeb = "web_ssoonn";
                    mWebBean = new Gson().fromJson(Util.getJsonfromAsset(this, mCurrentWeb), WebBean.class);
                }
                runOnUiThread(() -> {
                    //viewpager数据
                    pagerItems.clear();
                    pagerItems.addAll(mWebBean.getRank());
                    //recyclerView数据
                    listItems.clear();
                    listItems.add(new MultiData(0, R.layout.item_drawlayout_head, null));
                    for (String string : files) {
                        listItems.add(new MultiData(1, R.layout.item_drawlayout, string));
                    }
                    for (String string : ohterFiles) {
                        listItems.add(new MultiData(1, R.layout.item_drawlayout, string));
                    }
                    initPager();
                    initRecycler();
                });
            }
        }).start();
    }

    private void initPager() {
        //initViewPager
        fragments.clear();
        tabs.clear();
        vp_main_activity.removeAllViews();
        tl_main_activity.removeAllTabs();
        for (WebBean.RankBean rankBean : pagerItems) {
            fragments.add(RankFragment.newInstance(rankBean));
        }
        fragments.add(CollectFragment.newInstance(mWebBean));
        fragments.add(HistoryFragment.newInstance(mWebBean));
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
        for (WebBean.RankBean rankBean : pagerItems) {
            tabs.add(rankBean.getTitle());
            tl_main_activity.addTab(tl_main_activity.newTab().setText(rankBean.getTitle()));
        }
        tabs.add("收藏");
        tabs.add("历史");
        tl_main_activity.addTab(tl_main_activity.newTab().setText("收藏"));
        tl_main_activity.addTab(tl_main_activity.newTab().setText("历史"));
        if (pagerAdapter == null) {
            pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragments, tabs);
            vp_main_activity.setAdapter(pagerAdapter);
            vp_main_activity.addOnPageChangeListener(this);
            vp_main_activity.setOffscreenPageLimit(fragments.size());
            tl_main_activity.setupWithViewPager(vp_main_activity, false);
        } else {
            pagerAdapter.reset();
        }
    }

    private void initRecycler() {
        //initRecyclerView
        adapter = new AdapterManager().getLinearMultiAdapter(this, listItems, this);
        rv_draw_main_activity.setLayoutManager(new LinearLayoutManager(this));
        rv_draw_main_activity.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 0) {
            ImageView imageView = holder.itemView.findViewById(R.id.ig_draw_img_main_activity);
            Glide.with(this).load(R.drawable.navibg).centerCrop().into(imageView);
        } else {
            TextView textView = holder.itemView.findViewById(R.id.tv_item_drawlayout);
            ImageView imageView = holder.itemView.findViewById(R.id.ig_item_drawlayout);
            String string = "" + listItems.get(position).getData();
            string = string.replace("web_", "");
            textView.setText("" + string);
            if (mCurrentWeb.equals("" + listItems.get(position).getData())) {
                textView.setTextColor(ContextCompat.getColor(this, R.color.txt_hint));
            } else {
                textView.setTextColor(ContextCompat.getColor(this, R.color.txt_black));
            }
            if (position % 2 == 1) {
                Glide.with(this).load(R.drawable.addressone).centerInside().into(imageView);
            } else {
                Glide.with(this).load(R.drawable.addresstwo).centerInside().into(imageView);
            }
            holder.itemView.setOnClickListener(v -> {
                mCurrentWeb = "" + listItems.get(position).getData();
                mWebPreference.commitSelect("" + listItems.get(position).getData());
                initData();
            });
            String webName = string;
            holder.itemView.setOnLongClickListener(v -> {
                if (!webName.equals("ddmmcc") && !webName.equals("ssoonn")) {
                    messageDialog.setText("删除：" + webName, "确定", "取消");
                    messageDialog.setOnClick((MessageDialog.MessageDialogInterface) () -> {
                        File file = new File(MarioApplication.WebFilePath + "/web_" + webName);
                        file.delete();
                        initData();
                    });
                    messageDialog.show();
                }
                return false;
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ig_opendraw_main_activity) {
            dl_main_activity.openDrawer(Gravity.LEFT);
        } else if (v.getId() == R.id.ig_search_main_activity) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("search", (Serializable) mWebBean);
            startActivity(intent);
        } else if (v.getId() == R.id.ig_type_main_activity) {
            Intent intent = new Intent(this, ClassifyActivity.class);
            intent.putExtra("classify", (Serializable) mWebBean);
            startActivity(intent);
        } else if (v.getId() == R.id.ll_put_web_main_activity) {
            Intent intent = new Intent(this, WebNewActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1000);
        }
    }

    public void startActivity(String url) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", (Serializable) mWebBean);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
