package com.comic.mario.util.eventview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class TouchView extends FrameLayout {

    private float zoneX;

    public TouchView(Context context) {
        super(context);
        zoneX = getMetric(context) / 3;
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        zoneX = getMetric(context) / 3;
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        zoneX = getMetric(context) / 3;
    }

    float lastX = 0;
    float moveX = 0;

    //记录连续点击次数
    private int clickCount = 0;
    private Handler handler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            moveX = ev.getX();
            if (lastX == moveX) {
                clickCount++;
                int timeout = 180;
                handler.postDelayed(() -> {
                    if (clickCount == 1) {
                        if (lastX >= 0 && lastX <= zoneX) {
                            leftClick.onClick();
                        } else if (lastX <= zoneX * 3 && lastX >= zoneX * 2) {
                            rightClick.onClick();
                        } else {
                            centerClicl.onClick();
                        }
                    } else if (clickCount == 2) {
                        doubleClicl.onClick();
                    }
                    handler.removeCallbacksAndMessages(null);
                    clickCount = 0;
                }, timeout);

            }
        }
        getChildAt(0).dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = ev.getX();
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return true;
    }

    public void setLeftClick(LeftClick leftClick) {
        this.leftClick = leftClick;
    }

    public void setRightClick(RightClick rightClick) {
        this.rightClick = rightClick;
    }

    public void setCenterClicl(CenterClicl centerClicl) {
        this.centerClicl = centerClicl;
    }

    public void setDoubleClicl(DoubleClicl doubleClicl) {
        this.doubleClicl = doubleClicl;
    }

    private LeftClick leftClick;
    private RightClick rightClick;
    private CenterClicl centerClicl;
    private DoubleClicl doubleClicl;

    public interface LeftClick {
        void onClick();
    }

    public interface RightClick {
        void onClick();
    }

    public interface CenterClicl {
        void onClick();
    }

    public interface DoubleClicl {
        void onClick();
    }

    public float getMetric(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
