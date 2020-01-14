package com.comic.mario.util.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface MutiViewHolder {
    void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position);
}
