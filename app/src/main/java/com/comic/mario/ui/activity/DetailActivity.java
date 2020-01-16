package com.comic.mario.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.List;

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
        rv_comic_detail_activity.addItemDecoration(new SpaceItemDecoration(4, SizeUtil.dip2px(this, 15), integers, true));
        adapter = new AdapterManager().getGridMultiAdapter(this, items = new ArrayList<>(), this);
        rv_comic_detail_activity.setAdapter(adapter);
        ig_finish_comic_detail_activity.setOnClickListener(v -> {
            finish();
        });
        tv_collect_comic_detail_activity.setOnClickListener(v -> {
            if (mUrl == null || mTitle.isEmpty() || mImage.isEmpty())
                return;
            new Thread(()->{
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
                intent.putExtra("episode", position - 1);
                ArrayList<ComicEpisodeBean> episodes = new ArrayList<>();
                for (int i = 1; i < items.size(); i++) {
                    episodes.add((ComicEpisodeBean) items.get(i).getData());
                }
                intent.putExtra("episodes", (Serializable) episodes);
                intent.putExtra("detail", (Serializable) (ComicDetailBean) items.get(0).getData());
                intent.putExtra("histIndex", dataBean.getHistIndex());
                startActivity(intent);
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

    private void charge(ArrayList response) {
        new Thread(() -> {
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

            } finally {
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            }
        }).start();
    }

}
