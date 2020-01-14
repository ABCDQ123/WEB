package com.comic.mario.util.viewpager;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    private FragmentManager fragmentManager;

    public FragmentPagerAdapter(FragmentManager fm, List fragments, List tabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        this.fragments = fragments;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public void reset() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            fragmentTransaction.remove(fragmentManager.getFragments().get(i));
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        notifyDataSetChanged();
    }

}
