package com.comic.mario.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.comic.mario.R;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.fragment.ClassifyFragment;
import com.comic.mario.ui.fragment.RankFragment;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.Util;
import com.comic.mario.util.WebPreference;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.viewpager.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ClassifyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ImageView ig_finish_classify_activity;
    private TabLayout tl_classify_activity;
    private ViewPager vp_classify_activity;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();

    private ArrayList<WebBean.ClassifyBean> items = new ArrayList<>();
    private WebBean mWebBean;

    private Thread thread = new Thread(() -> {
        mWebBean = (WebBean) getIntent().getSerializableExtra("classify");
        items.addAll(mWebBean.getClassify());
        runOnUiThread(() -> {
            init();
        });
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        SystemUtil.setStatusBarTransparent(getWindow());
        SystemUtil.setMyStatusbar(this, findViewById(R.id.v_status_classify_activity));
        ig_finish_classify_activity = findViewById(R.id.ig_finish_classify_activity);
        tl_classify_activity = findViewById(R.id.tl_classify_activity);
        vp_classify_activity = findViewById(R.id.vp_classify_activity);

        thread.start();
    }

    private void init() {
        for (WebBean.ClassifyBean classifyBean : items) {
            tabs.add(classifyBean.getTitle());
            fragments.add(ClassifyFragment.newInstance(classifyBean));
            tl_classify_activity.addTab(tl_classify_activity.newTab().setText(classifyBean.getTitle()));
        }
        if (pagerAdapter == null) {
            pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragments, tabs);
            vp_classify_activity.setAdapter(pagerAdapter);
            vp_classify_activity.addOnPageChangeListener(this);
            vp_classify_activity.setOffscreenPageLimit(fragments.size());
            tl_classify_activity.setupWithViewPager(vp_classify_activity, false);
        }
        ig_finish_classify_activity.setOnClickListener(v -> {
            finish();
        });
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

    public void startActivity(String url) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", (Serializable) mWebBean);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
