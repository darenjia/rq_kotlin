package com.bkjcb.rqapplication.base.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2019/2/12.
 * Description :
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private long time;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        if (fragments != null) {
            this.fragments.addAll(fragments);
        }
    }

    public void setNewData(List<Fragment> fragments) {
        this.fragments.clear();
        this.fragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
