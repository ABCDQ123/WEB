package com.comic.mario.util.viewpager;

import android.content.Context;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class ViewPagerScroller extends Scroller {
    private int mDuration = 1000;

    public void setScrollDuration(int duration){
        mDuration = duration;
    }

    public int getmDuration() {
        return mDuration;
    }

    public ViewPagerScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void initViewPagerScroll(ViewPager pager){
        try{
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(pager,this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}