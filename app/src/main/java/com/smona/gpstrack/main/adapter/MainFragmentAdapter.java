package com.smona.gpstrack.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 6/26/19 11:28 AM
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentManager mFragmentManager;
    private List<String> tags;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.tags = new ArrayList<>();
        this.mFragmentManager = fm;
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //这个方法返回Tab显示的文字。这里通过在实例化TabFragment的时候，传入的title参数返回标题。
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void updateFragments(List<Fragment> fragments) {
        if (this.tags != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            for (int i = 0; i < tags.size(); i++) {
                fragmentTransaction.remove(mFragmentManager.findFragmentByTag(tags.get(i)));
            }
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
            tags.clear();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tags.add(makeFragmentName(container.getId(), getItemId(position)));
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.mFragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
