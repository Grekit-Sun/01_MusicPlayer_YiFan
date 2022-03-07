package cn.weli.mediaplayer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmets;

    public MyViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmets = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmets.get(position);
    }

    @Override
    public int getCount() {
        return mFragmets.size();
    }
}
