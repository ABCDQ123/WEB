package com.comic.mario.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comic.mario.R;
import com.comic.mario.ui.bean.ComicDetailBean;
import com.comic.mario.ui.bean.ComicEpisodeBean;
import com.comic.mario.ui.bean.ComicReadBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.present.ImpKiKyo;
import com.comic.mario.ui.present.KiKyoReadClient;
import com.comic.mario.util.ComicPreference;
import com.comic.mario.util.LogUtil;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.dialog.MessageDialog;
import com.comic.mario.util.eventview.TouchView;
import com.comic.mario.util.viewpager.ComicViewPager;
import com.comic.mario.util.viewpager.ComicViewPagerAdapter;
import com.comic.mario.util.viewpager.ViewPagerScroller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ReadActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ImpKiKyo {
    private ImageView ig_finish_comic_read_activity;
    private TextView tv_comicname_comic_read_activity;

    private LinearLayout ll_comics_comic_read_activity;
    private LinearLayout ll_back_comic_read_activity;
    private LinearLayout ll_next_comic_read_activity;
    private LinearLayout ll_setting_comic_read_activity;

    private RelativeLayout rl_topview_comic_read_activity;
    private LinearLayout ll_bottomview_comic_read_activity;

    private TouchView v_touch_comic_read_activity;

    private TextView tv_comicepisode_comic_read_activity;
    private TextView tv_pagenumber_comic_read_activity;
    private TextView tv_sysytemtime_comic_read_activity;
    private TextView tv_sysytembattery_comic_read_activity;

    private ComicViewPager vp_comic_read_activity;
    private ComicViewPagerAdapter adapter;
    private ArrayList<ReadView> items = new ArrayList<>();

    private int mEpisode;
    private ArrayList<ComicEpisodeBean> mEpisodes;
    private ComicDetailBean mDetail;
    private WebBean mWebBean;
    private String mDetailUrl;
    private int mHistIndex = 1;

    private KiKyoReadClient clientCurrent;

    private int mCount = 0;
    private int mIndex = 1;

    private ComicPreference comicPreference;
    private ExecutorService thread = Executors.newFixedThreadPool(1);

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SystemUtil.setStatusBarVisible(getWindow(), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clientCurrent != null) {
            clientCurrent.cancel();
            clientCurrent = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ig_finish_comic_read_activity = findViewById(R.id.ig_finish_comic_read_activity);
        tv_comicname_comic_read_activity = findViewById(R.id.tv_comicname_comic_read_activity);
        v_touch_comic_read_activity = findViewById(R.id.v_touch_comic_read_activity);
        ll_comics_comic_read_activity = findViewById(R.id.ll_comics_comic_read_activity);
        ll_back_comic_read_activity = findViewById(R.id.ll_back_comic_read_activity);
        ll_next_comic_read_activity = findViewById(R.id.ll_next_comic_read_activity);
        ll_setting_comic_read_activity = findViewById(R.id.ll_setting_comic_read_activity);

        rl_topview_comic_read_activity = findViewById(R.id.rl_topview_comic_read_activity);
        ll_bottomview_comic_read_activity = findViewById(R.id.ll_bottomview_comic_read_activity);

        tv_comicepisode_comic_read_activity = findViewById(R.id.tv_comicepisode_comic_read_activity);
        tv_pagenumber_comic_read_activity = findViewById(R.id.tv_pagenumber_comic_read_activity);
        tv_sysytemtime_comic_read_activity = findViewById(R.id.tv_sysytemtime_comic_read_activity);
        tv_sysytembattery_comic_read_activity = findViewById(R.id.tv_sysytembattery_comic_read_activity);

        vp_comic_read_activity = findViewById(R.id.vp_comic_read_activity);

        comicPreference = new ComicPreference(this);
        mWebBean = (WebBean) getIntent().getSerializableExtra("read");
        mEpisode = getIntent().getIntExtra("episode", 0);
        mEpisodes = (ArrayList<ComicEpisodeBean>) getIntent().getSerializableExtra("episodes");
        mDetail = (ComicDetailBean) getIntent().getSerializableExtra("detail");
        mDetailUrl = getIntent().getStringExtra("detailUrl");
        mHistIndex = getIntent().getIntExtra("histIndex", 1);

        init();
        initListener();
    }

    private void init() {
        tv_comicname_comic_read_activity.setText("" + mDetail.getTitle());

        ViewPagerScroller scroller = new ViewPagerScroller(this);
        scroller.setScrollDuration(1000);
        scroller.initViewPagerScroll(vp_comic_read_activity);
        adapter = new ComicViewPagerAdapter(items);
        vp_comic_read_activity.setAdapter(adapter);
        vp_comic_read_activity.addOnPageChangeListener(this);
        vp_comic_read_activity.setOffscreenPageLimit(12);
        tv_comicepisode_comic_read_activity.setText("????/????");

        clientCurrent = new KiKyoReadClient(this, mEpisodes.get(mEpisode).getLink(), mWebBean.getRead(), this);
    }

    private void initListener() {
        new Thread(() -> {
            while (!isDestroyed()) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ig_finish_comic_read_activity.setOnClickListener(v -> {
            finish();
        });
        ll_comics_comic_read_activity.setOnClickListener(v -> {

        });
        ll_back_comic_read_activity.setOnClickListener(v -> {
            if (reloading)
                return;
            reloading = true;
            if (clientCurrent != null) {
                clientCurrent.cancel();
                clientCurrent = null;
            }
            reloadData(false);
            rl_topview_comic_read_activity.setVisibility(View.GONE);
            ll_bottomview_comic_read_activity.setVisibility(View.GONE);
        });
        ll_next_comic_read_activity.setOnClickListener(v -> {
            if (reloading)
                return;
            reloading = true;
            if (clientCurrent != null) {
                clientCurrent.cancel();
                clientCurrent = null;
            }
            reloadData(true);
            rl_topview_comic_read_activity.setVisibility(View.GONE);
            ll_bottomview_comic_read_activity.setVisibility(View.GONE);
        });
        ll_setting_comic_read_activity.setOnClickListener(v -> {
            Toast.makeText(this, " 还没做! ", Toast.LENGTH_SHORT).show();
        });
        v_touch_comic_read_activity.setLeftClick(() -> {
            rl_topview_comic_read_activity.setVisibility(View.GONE);
            ll_bottomview_comic_read_activity.setVisibility(View.GONE);
            vp_comic_read_activity.setCurrentItem(vp_comic_read_activity.getCurrentItem() - 1, true);
        });
        v_touch_comic_read_activity.setRightClick(() -> {
            rl_topview_comic_read_activity.setVisibility(View.GONE);
            ll_bottomview_comic_read_activity.setVisibility(View.GONE);
            vp_comic_read_activity.setCurrentItem(vp_comic_read_activity.getCurrentItem() + 1, true);
        });
        v_touch_comic_read_activity.setCenterClicl(() -> {
            if (rl_topview_comic_read_activity.getVisibility() == View.VISIBLE) {
                rl_topview_comic_read_activity.setVisibility(View.GONE);
                ll_bottomview_comic_read_activity.setVisibility(View.GONE);
            } else {
                rl_topview_comic_read_activity.setVisibility(View.VISIBLE);
                ll_bottomview_comic_read_activity.setVisibility(View.VISIBLE);
            }
        });
        v_touch_comic_read_activity.setDoubleClicl(() -> {

        });
    }

    @SuppressLint("NewApi")
    private Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            if (isDestroyed())
                return false;
            long time = System.currentTimeMillis();
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String data = format.format(date);
            tv_sysytemtime_comic_read_activity.setText(data);

            BatteryManager manager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int pecent = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);///当前电量百分比
            tv_sysytembattery_comic_read_activity.setText("电量 " + pecent + "%");
        }
        return false;
    });

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private boolean reloading = false;

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            if (reloading)
                return;
            reloading = true;
            if (clientCurrent != null) {
                clientCurrent.cancel();
                clientCurrent = null;
            }
            reloadData(false);
        } else if (position == items.size() - 1) {
            if (reloading)
                return;
            reloading = true;
            if (clientCurrent != null) {
                clientCurrent.cancel();
                clientCurrent = null;
            }
            reloadData(true);
        } else if (position < items.size() - 1 && position > 0) {
            ReadView readView = items.get(position);
            tv_pagenumber_comic_read_activity.setText(readView.getIndex() + "/" + readView.getCount());

            thread.submit(() -> {
                comicPreference.commitHist(mWebBean.getWeb(), mDetailUrl,
                        mDetail.getTitle(), mEpisodes.get(mEpisode).getEpisode(), position, mDetail.getImg());
            });
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void response(int notify, Object response) {
        if (notify == 1001) {
            items.clear();
            adapter.notifyDataSetChanged();

            ArrayList<ComicReadBean> comicReadBean = (ArrayList<ComicReadBean>) response;
            mIndex = 1;
            mCount = comicReadBean.size();
            tv_pagenumber_comic_read_activity.setText(mIndex + "/" + mCount);
            tv_comicepisode_comic_read_activity.setText(mDetail.getTitle() + "" + mEpisodes.get(mEpisode).getEpisode());

            items.add(new ReadView(this, 0, 0));
            for (int i = 0; i < mCount; i++) {
                items.add(new ReadView(this, i + 1, mCount));
            }
            items.add(new ReadView(this, 0, 0));
            adapter.notifyDataSetChanged();
            if (mHistIndex == 0) {
                mHistIndex = 1;
            }
            vp_comic_read_activity.setCurrentItem(mHistIndex, false);
            mHistIndex = 1;
            reloading = false;
            for (int i = 0; i < mCount; i++) {
                items.get(i + 1).loadImage(comicReadBean.get(i).getImageUrl());
            }
        } else if (response != null && response instanceof ComicReadBean) {
            items.clear();
            adapter.notifyDataSetChanged();
            ComicReadBean comicReadBean = (ComicReadBean) response;
            mIndex = comicReadBean.getIndex();
            mCount = comicReadBean.getCount();
            tv_pagenumber_comic_read_activity.setText(mIndex + "/" + mCount);
            tv_comicepisode_comic_read_activity.setText(mDetail.getTitle() + "" + mEpisodes.get(mEpisode).getEpisode());

            items.add(new ReadView(this, 0, 0));
            for (int i = 0; i < mCount; i++) {
                items.add(new ReadView(this, i + 1, mCount));
            }
            items.add(new ReadView(this, 0, 0));
            adapter.notifyDataSetChanged();
            items.get(1).loadImage(comicReadBean.getImageUrl());
            if (mHistIndex == 0) {
                mHistIndex = 1;
            }
            vp_comic_read_activity.setCurrentItem(mHistIndex, false);
            mHistIndex = 1;
            reloading = false;
        } else if (response != null && response instanceof ArrayList) {
            List<ComicReadBean> list = (List<ComicReadBean>) response;
            ComicReadBean comicReadBean = list.get(0);
            items.get(comicReadBean.getIndex()).loadImage(comicReadBean.getImageUrl());
        }
    }

    @Override
    public void error() {

    }

    private void reloadData(boolean nextVol) {
        if (nextVol) {
            if (mEpisode - 1 < 0) {
                Toast.makeText(this, "没了，别翻了", Toast.LENGTH_SHORT).show();
                reloading = false;
                return;
            }
            mEpisode--;
        } else {
            if (mEpisode + 1 >= mEpisodes.size()) {
                Toast.makeText(this, "没了，别翻了", Toast.LENGTH_SHORT).show();
                reloading = false;
                return;
            }
            mEpisode++;
        }
        clientCurrent = new KiKyoReadClient(this, mEpisodes.get(mEpisode).getLink(), mWebBean.getRead(), this);
    }
}
