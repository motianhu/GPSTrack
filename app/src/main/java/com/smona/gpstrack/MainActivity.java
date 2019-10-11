package com.smona.gpstrack;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.main.adapter.MainFragmentAdapter;
import com.smona.gpstrack.main.fragment.AlarmListFragemnt;
import com.smona.gpstrack.main.fragment.DeviceListFragment;
import com.smona.gpstrack.main.fragment.GEOListFragment;
import com.smona.gpstrack.main.fragment.MapContainerFragment;
import com.smona.gpstrack.main.fragment.SettingMainFragment;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:02 PM
 */

@Route(path = ARouterPath.PATH_TO_MAIN)
public class MainActivity extends BaseActivity {
    private TabLayout tabs;
    private NoScrollViewPager viewpager;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragmentAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        refreshragments();
    }

    private void initViews() {
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
        pagerAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setNeedScroll(false);
        tabs.setSelectedTabIndicatorHeight(0);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }

    private void initData() {
        fragments.add(new MapContainerFragment());
        fragments.add(new DeviceListFragment());
        fragments.add(new GEOListFragment());
        fragments.add(new AlarmListFragemnt());
        fragments.add(new SettingMainFragment());

        titles.add(getString(R.string.map));
        titles.add(getString(R.string.device_list));
        titles.add(getString(R.string.ele_fence));
        titles.add(getString(R.string.alert_list));
        titles.add(getString(R.string.settings));
    }

    private void refreshragments() {
        tabs.setupWithViewPager(viewpager, true);
        pagerAdapter.updateFragments(fragments);
        viewpager.setOffscreenPageLimit(fragments.size());
        tabs.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(titles.get(i)));
            }
        }
        viewpager.setCurrentItem(0);
    }

    // Tab自定义view
    public View getTabView(String title) {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout_item, null);
        TextView textView = v.findViewById(R.id.textview);
        textView.setText(title);
        return v;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出时的时间
    private long mExitTime;

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.showShort(getString(R.string.app_exit_tip));
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
