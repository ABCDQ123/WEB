package com.comic.mario.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.comic.mario.R;
import com.comic.mario.ui.activity.ClassifyActivity;
import com.comic.mario.ui.activity.DetailActivity;
import com.comic.mario.ui.bean.ComicBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.ui.present.ImpKiKyo;
import com.comic.mario.ui.present.KiKyoClassifyClient;
import com.comic.mario.ui.present.KiKyoRankClient;
import com.comic.mario.util.SizeUtil;
import com.comic.mario.util.SystemUtil;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MGridLayoutManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;
import com.comic.mario.util.recyclerview.SpaceItemDecoration;
import com.comic.mario.util.recyclerview.StateRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ClassifyFragment extends LazyFragment implements MutiViewHolder, ImpKiKyo {
    private SwipeRefreshLayout srl_rank_fragment;
    private StateRecyclerView rv_rank_fragment;
    private RecyclerView.Adapter adapter;
    private ArrayList<MultiData> items;

    private WebBean.ClassifyBean mClassifyBean;
    private KiKyoClassifyClient kiKyoClassifyClient;

    @Override
    public void onLazyLoad() {
        init();
    }

    public static ClassifyFragment newInstance(Serializable obj) {
        Bundle args = new Bundle();
        ClassifyFragment fragment = new ClassifyFragment();
        args.putSerializable("key", obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl_rank_fragment = view.findViewById(R.id.srl_rank_fragment);
        rv_rank_fragment = view.findViewById(R.id.rv_rank_fragment);
        mClassifyBean = (WebBean.ClassifyBean) getArguments().getSerializable("key");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ClassifyFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (kiKyoClassifyClient != null) {
            kiKyoClassifyClient.cancel();
        }
    }

    private void init() {
        adapter = new AdapterManager().getGridMultiAdapter(getContext(), items = new ArrayList<>(), this);
        rv_rank_fragment.addItemDecoration(new SpaceItemDecoration(3, SizeUtil.dip2px(getContext(), 10), true));
        rv_rank_fragment.setLayoutManager(new MGridLayoutManager(getContext(), 9));
        rv_rank_fragment.setAdapter(adapter);

        srl_rank_fragment.setRefreshing(true);
        rv_rank_fragment.setLoading(true);

        srl_rank_fragment.setOnRefreshListener(() -> {
            rv_rank_fragment.setLoading(true);
            kiKyoClassifyClient.request(getContext(), items, mClassifyBean, this);
        });
        rv_rank_fragment.setLoadMoreListener(() -> {
            rv_rank_fragment.setLoading(true);
            kiKyoClassifyClient.next();
        });

        kiKyoClassifyClient = new KiKyoClassifyClient();
        kiKyoClassifyClient.request(getContext(), items, mClassifyBean, this);
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 1) {
            int imageSize = (SystemUtil.getSreenWith(getActivity()) - SizeUtil.dip2px(getContext(), 30)) / 3 * 4 / 3;
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
            holder.itemView.setOnClickListener(v -> {
                ((ClassifyActivity) getActivity()).startActivity(dataBean.getLink());
            });
        }
    }

    @Override
    public void response(int notify, Object response) {
        if (notify != 0) {
            adapter.notifyItemRangeChanged(notify, items.size());
        } else {
            adapter.notifyDataSetChanged();
        }
        srl_rank_fragment.setRefreshing(false);
        rv_rank_fragment.setLoading(false);
    }

    @Override
    public void error() {
        srl_rank_fragment.setRefreshing(false);
        rv_rank_fragment.setLoading(false);
    }
}
