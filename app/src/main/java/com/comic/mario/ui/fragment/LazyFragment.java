package com.comic.mario.ui.fragment;

import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

public abstract class LazyFragment extends Fragment {

    private boolean isLoad;

    @Override
    public void onResume() {
        super.onResume();
        tryLoad();
    }

    private void tryLoad() {
        if (!isLoad) {
            onLazyLoad();
            isLoad = true;
        }
    }

    @UiThread
    public abstract void onLazyLoad();
}
