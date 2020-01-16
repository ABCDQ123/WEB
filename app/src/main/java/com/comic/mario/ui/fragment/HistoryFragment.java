package com.comic.mario.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.comic.mario.MarioApplication;
import com.comic.mario.R;
import com.comic.mario.ui.activity.DetailActivity;
import com.comic.mario.ui.bean.ComicBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.util.ComicPreference;
import com.comic.mario.util.SizeUtil;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.Util;
import com.comic.mario.util.WebPreference;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MGridLayoutManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.comic.mario.util.recyclerview.SpaceItemDecoration;
import com.comic.mario.util.recyclerview.StateRecyclerView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryFragment extends Fragment implements MutiViewHolder {

    private SwipeRefreshLayout srl_rank_fragment;
    private StateRecyclerView rv_rank_fragment;
    private RecyclerView.Adapter adapter;
    private ArrayList<MultiData> items;
    private int imageSize;

    private WebBean webBean;

    private ExecutorService thread = Executors.newFixedThreadPool(1);
    private ComicPreference preferencesManager;

    private WebPreference mWebPreference;

    public static HistoryFragment newInstance(Serializable obj) {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        args.putSerializable("key", obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_rank_fragment = view.findViewById(R.id.rv_rank_fragment);
        srl_rank_fragment = view.findViewById(R.id.srl_rank_fragment);
        imageSize = (SystemUtil.getSreenWith(getActivity()) - SizeUtil.dip2px(getContext(), 30)) / 3 * 4 / 3;
        webBean = (WebBean) getArguments().getSerializable("key");
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HistoryFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HistoryFragment");
    }

    private void init() {
        adapter = new AdapterManager().getGridMultiAdapter(getContext(), items = new ArrayList<>(), this);
        rv_rank_fragment.addItemDecoration(new SpaceItemDecoration(3, SizeUtil.dip2px(getContext(), 10), true));
        rv_rank_fragment.setLayoutManager(new MGridLayoutManager(getContext(), 9));
        rv_rank_fragment.setAdapter(adapter);
        preferencesManager = new ComicPreference(getContext());
        mWebPreference = new WebPreference(getContext());
        initData();
        srl_rank_fragment.setOnRefreshListener(() -> {
            srl_rank_fragment.setRefreshing(false);
            items.clear();
            adapter.notifyDataSetChanged();
            initData();
        });

    }

    private void initData() {
        new Thread(() -> {
            try {
                ComicPreference preferencesManager = new ComicPreference(getContext());
                if (preferencesManager.getHist() != null) {
                    JSONArray jsonArray = new JSONArray(preferencesManager.getHist());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        ComicBean comicBean = new ComicBean();
                        comicBean.setLink("" + jsonObject.optString("Url"));
                        comicBean.setTitle("" + jsonObject.optString("Title"));
                        comicBean.setImg("" + jsonObject.optString("Image"));
                        comicBean.setIntro("" + jsonObject.optString("Episode"));
                        comicBean.setWebName("" + jsonObject.optString("WebName"));
                        items.add(new MultiData(1, R.layout.item_comic, 3, comicBean));
                    }
                    Collections.reverse(items);
                }
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 1) {
            ImageView ig_img_item_comic = holder.itemView.findViewById(R.id.ig_img_item_comic);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ig_img_item_comic.getLayoutParams();
            params.height = imageSize;
            ig_img_item_comic.setLayoutParams(params);
            TextView tv_name_item_comic = holder.itemView.findViewById(R.id.tv_name_item_comic);
            TextView tv_intro_item_comic = holder.itemView.findViewById(R.id.tv_intro_item_comic);

            ComicBean dataBean = (ComicBean) items.get(position).getData();
            Glide.with(getContext()).load(dataBean.getImg()).placeholder(R.drawable.imgloading).into(ig_img_item_comic);
            tv_name_item_comic.setText("" + dataBean.getTitle());
            tv_intro_item_comic.setText("" + dataBean.getIntro());
            tv_intro_item_comic.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            holder.itemView.setOnClickListener(v -> {
                String files[] = mWebPreference.get().split("@");
                for (String webname : files) {
                    if (webname.replace("web_", "").equals(dataBean.getWebName())) {
                        WebBean mWebBean = new Gson().fromJson(Util.getJsonfromAsset(getContext(), webname), WebBean.class);
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("detail", (Serializable) mWebBean);
                        intent.putExtra("url", dataBean.getLink());
                        getContext().startActivity(intent);
                        return;
                    }
                }
                List<String> ohterFiles = Util.getFilesAllName(MarioApplication.WebFilePath);
                for (String string : ohterFiles) {
                    if (string.replace("web_", "").contains(dataBean.getIntro())) {
                        WebBean mWebBean = new Gson().fromJson(Util.readTxtFile(MarioApplication.WebFilePath + "/" + string), WebBean.class);
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("detail", (Serializable) mWebBean);
                        intent.putExtra("url", dataBean.getLink());
                        getContext().startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(getContext(), "无可用web", Toast.LENGTH_SHORT).show();
            });
            holder.itemView.setOnLongClickListener(v -> {
                thread.submit(() -> {
                    preferencesManager.removeHist(dataBean.getWebName().replace("web_", ""), dataBean.getTitle());
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "已删除，刷新即可", Toast.LENGTH_SHORT).show();
                    });
                });
                return true;
            });
        }
    }
}