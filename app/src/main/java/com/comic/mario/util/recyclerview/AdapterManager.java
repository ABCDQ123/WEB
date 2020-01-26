package com.comic.mario.util.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterManager<T> {

    private List<MultiData> mMultiData;
    private Context mContext;
    private int mPosition;
    private MutiViewHolder mMutiViewHolder;

    //grid多布局(相册多布局)
    public RecyclerView.Adapter getGridMultiAdapter(Context context, List<MultiData> yzmMultiDatas, MutiViewHolder mutiViewHolder) {
        mMultiData = yzmMultiDatas;
        mContext = context.getApplicationContext();
        mMutiViewHolder = mutiViewHolder;
        return new RecyclerView.Adapter() {
            @Override
            public int getItemViewType(int position) {
                mPosition = position;
                return mMultiData.get(position).getTag();
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                if (viewType == mMultiData.get(mPosition).getTag()) {
                    view = LayoutInflater.from(mContext).inflate(mMultiData.get(mPosition).getLayoutRes(), null);
                    view.setTag(mMultiData.get(mPosition).getTag());
                } else {
                    f:
                    for (MultiData yzmMultiData : mMultiData)
                        if (viewType == yzmMultiData.getTag()) {
                            view = LayoutInflater.from(mContext).inflate(yzmMultiData.getLayoutRes(), null);
                            view.setTag(yzmMultiData.getTag());
                            break f;
                        }
                }
                return new RecyclerView.ViewHolder(view) {

                };
            }

            @Override
            public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof GridLayoutManager) {
                    GridLayoutManager gridManager = ((GridLayoutManager) manager);
                    gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return mMultiData.get(position).getSpanSize();
                        }
                    });
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                mMutiViewHolder.onBindViewHolder((Integer) holder.itemView.getTag(), holder, position);
            }

            @Override
            public int getItemCount() {
                return mMultiData == null ? 0 : mMultiData.size();
            }
        };
    }

    //线性多布局
    public RecyclerView.Adapter getLinearMultiAdapter(Context context, List<MultiData> multiData, MutiViewHolder mutiViewHolder) {
        mMultiData = multiData;
        mContext = context.getApplicationContext();
        mMutiViewHolder = mutiViewHolder;
        return new RecyclerView.Adapter() {
            @Override
            public int getItemViewType(int position) {
                mPosition = position;
                return mMultiData.get(position).getTag();
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                if (viewType == mMultiData.get(mPosition).getTag()) {
                    view = LayoutInflater.from(mContext).inflate(mMultiData.get(mPosition).getLayoutRes(), null);
                    view.setTag(mMultiData.get(mPosition).getTag());
                } else {
                    f:
                    for (MultiData yzmMultiData : mMultiData)
                        if (viewType == yzmMultiData.getTag()) {
                            view = LayoutInflater.from(mContext).inflate(yzmMultiData.getLayoutRes(), null);
                            view.setTag(yzmMultiData.getTag());
                            break f;
                        }
                }
                return new RecyclerView.ViewHolder(view) {

                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                mMutiViewHolder.onBindViewHolder((Integer) holder.itemView.getTag(), holder, position);
            }

            @Override
            public int getItemCount() {
                return mMultiData == null ? 0 : mMultiData.size();
            }
        };
    }
}
