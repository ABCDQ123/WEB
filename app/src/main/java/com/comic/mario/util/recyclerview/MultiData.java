package com.comic.mario.util.recyclerview;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MultiData<T> implements Serializable {

    private int tag;
    private int layoutRes;//R.layout
    private int spanSize;//Grid item所占份
    private T data;

    //LinearLayout
    public MultiData(int tag, int layoutRes, T data) {
        this.tag = tag;
        this.layoutRes = layoutRes;
        this.data = data;
    }

    //GridLayout
    public MultiData(int tag, int layoutRes, int spanSize, T data) {
        this.tag = tag;
        this.layoutRes = layoutRes;
        this.data = data;
        this.spanSize = spanSize;
    }

    protected MultiData(Parcel in) {
        tag = in.readInt();
        layoutRes = in.readInt();
        spanSize = in.readInt();
    }

    public int getSpanSize() {
        return spanSize;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public int getTag() {
        return tag;
    }

    public Object getData() {
        return data;
    }
}
