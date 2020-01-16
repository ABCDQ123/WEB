package com.comic.mario.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.comic.mario.R;
import com.comic.mario.ui.bean.ComicBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.present.ImpKiKyo;
import com.comic.mario.ui.present.KiKyoSearchClient;
import com.comic.mario.util.SizeUtil;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MGridLayoutManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.comic.mario.util.recyclerview.SpaceItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SearchActivity extends AppCompatActivity implements MutiViewHolder, ImpKiKyo {

    private ImageView ig_finish_search_activity;
    private EditText et_search_activity;
    private RecyclerView rv_search_activity;

    private RecyclerView.Adapter adapter;
    private ArrayList<MultiData> items;

    private KiKyoSearchClient kiKyoSearchClient;

    private WebBean.SearchBean searchBean;
    private WebBean mWebBean;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kiKyoSearchClient != null) {
            kiKyoSearchClient.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kiKyoSearchClient != null) {
            kiKyoSearchClient.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (kiKyoSearchClient != null) {
            kiKyoSearchClient.pause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ig_finish_search_activity = findViewById(R.id.ig_finish_search_activity);
        et_search_activity = findViewById(R.id.et_search_activity);
        rv_search_activity = findViewById(R.id.rv_search_activity);
        init();
        initListener();
    }

    private void init() {
        adapter = new AdapterManager().getGridMultiAdapter(this, items = new ArrayList<>(), this);
        rv_search_activity.addItemDecoration(new SpaceItemDecoration(3, SizeUtil.dip2px(this, 10), true));
        rv_search_activity.setLayoutManager(new MGridLayoutManager(this, 9));
        rv_search_activity.setAdapter(adapter);

        kiKyoSearchClient = new KiKyoSearchClient();
        mWebBean = (WebBean) getIntent().getSerializableExtra("search");
        searchBean = mWebBean.getSearch();
    }

    private void initListener() {
        et_search_activity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                items.clear();
                adapter.notifyDataSetChanged();
                kiKyoSearchClient.request(this, items, searchBean, et_search_activity.getText().toString(), this);
            }
            return false;
        });
        ig_finish_search_activity.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 1) {
            int imageSize = (SystemUtil.getSreenWith(this) - SizeUtil.dip2px(this, 30)) / 3 * 4 / 3;
            ImageView ig_img_item_comic = holder.itemView.findViewById(R.id.ig_img_item_comic);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ig_img_item_comic.getLayoutParams();
            params.height = imageSize;
            ig_img_item_comic.setLayoutParams(params);
            TextView tv_name_item_comic = holder.itemView.findViewById(R.id.tv_name_item_comic);
            TextView tv_intro_item_comic = holder.itemView.findViewById(R.id.tv_intro_item_comic);

            ComicBean dataBean = (ComicBean) items.get(position).getData();
            Glide.with(this).load(dataBean.getImg()).placeholder(R.drawable.imgloading).into(ig_img_item_comic);
            tv_name_item_comic.setText("" + dataBean.getTitle());
            tv_intro_item_comic.setText("" + dataBean.getIntro());
            holder.itemView.setOnClickListener(v -> {
                startActivity(dataBean.getLink());
            });
        }
    }

    @Override
    public void response(int notify, Object response) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void error() {

    }

    public void startActivity(String url) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", (Serializable) mWebBean);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}