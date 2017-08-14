package cht.com.cht.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<Fragment> mViews;

    public TabFragmentAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mViews) {
        super(fm);
        this.mTitles = mTitles;
        this.mViews = mViews;
    }



    @Override
    public Fragment getItem(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
