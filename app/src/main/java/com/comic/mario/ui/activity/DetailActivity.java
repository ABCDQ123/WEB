package com.comic.mario.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.comic.mario.R;
import com.comic.mario.ui.bean.ComicDetailBean;
import com.comic.mario.ui.bean.ComicEpisodeBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.present.ImpKiKyo;
import com.comic.mario.ui.present.KiKyoDetailClient;
import com.comic.mario.util.ComicPreference;
import com.comic.mario.util.SizeUtil;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MGridLayoutManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.comic.mario.util.recyclerview.SpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DetailActivity extends AppCompatActivity implements MutiViewHolder, ImpKiKyo {

    private ImageView ig_finish_comic_detail_activity;
    private TextView tv_title_comic_detail_activity;
    private TextView tv_collect_comic_detail_activity;
    private RecyclerView rv_comic_detail_activity;
    private RecyclerView.Adapter adapter;
    private ArrayList<MultiData> items;

    private KiKyoDetailClient kiKyoDetailClient;
    private WebBean.DetailBean detailBean;
    private WebBean mWebBean;

    private String mUrl = "";
    private String mImage = "";
    private String mTitle = "";

    private ComicPreference comicPreference;

    private MultiData multiDataDetail;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != items && items.size() != 0) {
            charge(items);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
        ig_finish_comic_detail_activity = findViewById(R.id.ig_finish_comic_detail_activity);
        tv_title_comic_detail_activity = findViewById(R.id.tv_title_comic_detail_activity);
        tv_collect_comic_detail_activity = findViewById(R.id.tv_collect_comic_detail_activity);
        rv_comic_detail_activity = findViewById(R.id.rv_comic_detail_activity);
        init();
    }

    private void init() {
        rv_comic_detail_activity.setLayoutManager(new MGridLayoutManager(this, 4));
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        rv_comic_detail_activity.addItemDecoration(new SpaceItemDecoration(4, SizeUtil.dip2px(this, 15), integers, true));
        adapter = new AdapterManager().getGridMultiAdapter(this, items = new ArrayList<>(), this);
        rv_comic_detail_activity.setAdapter(adapter);
        ig_finish_comic_detail_activity.setOnClickListener(v -> {
            finish();
        });
        tv_collect_comic_detail_activity.setOnClickListener(v -> {
            if (mUrl == null || mTitle.isEmpty() || mImage.isEmpty())
                return;
            new Thread(() -> {
                comicPreference.commitCollect(mWebBean.getWeb(), mUrl, mTitle, mImage);
            }).start();
            Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
        });

        mWebBean = (WebBean) getIntent().getSerializableExtra("detail");
        detailBean = mWebBean.getDetail();
        mUrl = getIntent().getStringExtra("url");
        kiKyoDetailClient = new KiKyoDetailClient();
        kiKyoDetailClient.request(this, items, detailBean, mUrl, this);

        comicPreference = new ComicPreference(this);
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 0) {
            ImageView ig_image_item_comic_info = holder.itemView.findViewById(R.id.ig_image_item_comic_info);
            TextView tv_title_item_comic_info = holder.itemView.findViewById(R.id.tv_title_item_comic_info);
            TextView tv_author_item_comic_info = holder.itemView.findViewById(R.id.tv_author_item_comic_info);
            TextView tv_type_item_comic_info = holder.itemView.findViewById(R.id.tv_type_item_comic_info);
            TextView tv_pop_item_comic_info = holder.itemView.findViewById(R.id.tv_pop_item_comic_info);
            TextView tv_status_item_comic_info = holder.itemView.findViewById(R.id.tv_status_item_comic_info);
            TextView tv_time_item_comic_info = holder.itemView.findViewById(R.id.tv_time_item_comic_info);

            ComicDetailBean dataBean = (ComicDetailBean) items.get(position).getData();
            Glide.with(this).load(dataBean.getImg()).into(ig_image_item_comic_info);
            tv_title_item_comic_info.setText("" + dataBean.getTitle());
            tv_author_item_comic_info.setText("" + dataBean.getAuthor());
            tv_type_item_comic_info.setText("" + dataBean.getClassify());
            tv_pop_item_comic_info.setText("" + dataBean.getPopular());
            tv_status_item_comic_info.setText("" + dataBean.getStatus());
            tv_time_item_comic_info.setText("" + dataBean.getUpdatetime());
            mImage = "" + dataBean.getImg();
            mTitle = "" + dataBean.getTitle();
        } else if (tag == 1) {
            TextView tv_item_tag = holder.itemView.findViewById(R.id.tv_item_episode);
            ComicEpisodeBean dataBean = (ComicEpisodeBean) items.get(position).getData();

            tv_item_tag.setText("" + dataBean.getEpisode());
            if (dataBean.isHist()) {
                tv_item_tag.setBackground(ContextCompat.getDrawable(this, R.drawable.square_bg_hint));
                tv_item_tag.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                tv_item_tag.setBackground(ContextCompat.getDrawable(this, R.drawable.square_bg_gray));
                tv_item_tag.setTextColor(ContextCompat.getColor(this, R.color.txt_black));
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ReadActivity.class);
                intent.putExtra("detailUrl", mUrl);
                intent.putExtra("read", (Serializable) mWebBean);
                intent.putExtra("episode", position - 2);
                ArrayList<ComicEpisodeBean> episodes = new ArrayList<>();
                for (int i = 2; i < items.size(); i++) {
                    episodes.add((ComicEpisodeBean) items.get(i).getData());
                }
                intent.putExtra("episodes", (Serializable) episodes);
                intent.putExtra("detail", (Serializable) (ComicDetailBean) items.get(0).getData());
                intent.putExtra("histIndex", dataBean.getHistIndex());
                startActivity(intent);
            });
        } else if (tag == 2) {
            TextView tv_switch_sort = holder.itemView.findViewById(R.id.tv_switch_sort);
            tv_switch_sort.setOnClickListener(v -> {
                if (items.size() == 0 || items.size() == 1 || items.size() == 2) {
                    return;
                }
                ArrayList<MultiData> itemSort = new ArrayList<>();
                new Thread(() -> {
                    multiDataDetail = items.get(0);
                    itemSort.addAll(items);
                    itemSort.remove(0);
                    itemSort.remove(0);
                    Collections.reverse(itemSort);
                    runOnUiThread(() -> {
                        items.clear();
                        adapter.notifyDataSetChanged();
                        items.add(multiDataDetail);
                        items.add(new MultiData(2, R.layout.item_episode_sort, 4, null));
                        items.addAll(itemSort);
                        adapter.notifyDataSetChanged();
                    });
                }).start();
            });
        }
    }

    @Override
    public void response(int notify, Object response) {
        adapter.notifyDataSetChanged();
        charge((ArrayList) response);
    }

    @Override
    public void error() {

    }

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    private boolean isReversed = false;

    private void charge(ArrayList response) {
        fixedThreadPool.submit(() -> {
            try {
                if (null == comicPreference.getHist())
                    return;
                JSONArray mEpisodes = new JSONArray(comicPreference.getHist());
                ArrayList<MultiData> temp = response;
                for (int i = 0; i < mEpisodes.length(); i++) {
                    JSONObject jsonObject = (JSONObject) mEpisodes.get(i);
                    for (int j = 1; j < temp.size() && temp.size() > 1; j++) {
                        if (temp.get(0).getData() instanceof ComicDetailBean && temp.get(j).getData() instanceof ComicEpisodeBean) {
                            ComicDetailBean comicDetailBean = (ComicDetailBean) temp.get(0).getData();
                            ComicEpisodeBean mhfComicEpisode = (ComicEpisodeBean) temp.get(j).getData();
                            if (comicDetailBean.getTitle().equals(jsonObject.getString("Title"))) {
                                if (jsonObject.opt("Episode").equals(mhfComicEpisode.getEpisode())) {
                                    mhfComicEpisode.setHist(true);
                                    mhfComicEpisode.setHistIndex(jsonObject.optInt("index"));
                                } else {
                                    mhfComicEpisode.setHist(false);
                                    mhfComicEpisode.setHistIndex(0);
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (isDestroyed()) {
                    return;
                }
                runOnUiThread(() -> {
                    if (!isReversed) {
                        isReversed = true;
                        if (null == mWebBean.getDetail().getEpisodeSort()) {

                        } else if (mWebBean.getDetail().getEpisodeSort().isEmpty()) {

                        } else if (mWebBean.getDetail().getEpisodeSort().equals("true")) {
                            ArrayList<MultiData> itemSort = new ArrayList<>();
                            multiDataDetail = items.get(0);
                            itemSort.addAll(items);
                            itemSort.remove(0);
                            itemSort.remove(0);
                            Collections.reverse(itemSort);
                            items.clear();
                            items.add(multiDataDetail);
                            items.add(new MultiData(2, R.layout.item_episode_sort, 4, null));
                            items.addAll(itemSort);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

}
