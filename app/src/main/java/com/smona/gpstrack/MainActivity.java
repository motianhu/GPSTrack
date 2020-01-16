package com.smona.gpstrack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.gpstrack.main.adapter.MainFragmentAdapter;
import com.smona.gpstrack.main.fragment.AlarmListFragemnt;
import com.smona.gpstrack.main.fragment.DeviceListFragment;
import com.smona.gpstrack.main.fragment.FenceListFragment;
import com.smona.gpstrack.main.fragment.MainFragment;
import com.smona.gpstrack.main.fragment.SettingMainFragment;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmUnReadEvent;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * 主页
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:02 PM
 */

@Route(path = ARouterPath.PATH_TO_MAIN)
public class MainActivity extends BaseActivity {
    private TabLayout tabs;
    private NoScrollViewPager viewpager;
    private List<String> titles = new ArrayList<>();
    private List<Integer> resIds = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private MainFragmentAdapter pagerAdapter;

    private String params;
    private TextView unReadTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        params = getIntent().getStringExtra(ARouterPath.PATH_TO_MAIN);
        initViews();
        initData();
        refreshragments();
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotifyCenter.getInstance().unRegisterListener(this);
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
        unReadTv = findViewById(R.id.unReadTv);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int cellWidth = metrics.widthPixels / 5 ;
        int width = cellWidth * 3 + cellWidth / 2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)unReadTv.getLayoutParams();
        params.setMarginStart(width);
    }

    private void initData() {
        fragments.add(new MainFragment());
        fragments.add(new DeviceListFragment());
        fragments.add(new FenceListFragment());
        fragments.add(new AlarmListFragemnt());
        fragments.add(new SettingMainFragment());

        titles.add(getString(R.string.map));
        titles.add(getString(R.string.device_list));
        titles.add(getString(R.string.ele_fence));
        titles.add(getString(R.string.alert_list));
        titles.add(getString(R.string.settings));

        resIds.add(R.drawable.select_icon_location);
        resIds.add(R.drawable.select_icon_list);
        resIds.add(R.drawable.select_icon_fance);
        resIds.add(R.drawable.select_icon_alarm);
        resIds.add(R.drawable.select_icon_install);
    }

    private void refreshragments() {
        tabs.setupWithViewPager(viewpager, true);
        pagerAdapter.updateFragments(fragments);
        viewpager.setOffscreenPageLimit(fragments.size());
        tabs.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(titles.get(i), resIds.get(i), i == 0));
            }
        }
        int index = 0;
        if (!TextUtils.isEmpty(params)) {
            index = fragments.size() - 1;
        }
        viewpager.setCurrentItem(index);
    }

    // Tab自定义view
    public View getTabView(String title, int resId, boolean isSelected) {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout_item, null);
        TextView textView = v.findViewById(R.id.textview);
        textView.setText(title);
        ImageView imageView = v.findViewById(R.id.imageview);
        imageView.setImageResource(resId);
        imageView.setSelected(isSelected);
        v.setSelected(isSelected);
        return v;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            int curPos = viewpager.getCurrentItem();
            if (fragments.get(curPos).backpressed()) {
                return true;
            }
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

    /**
     * 已读报警个数更新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgUnReadAlarm(AlarmUnReadEvent event) {
        if(isFinishing() || isDestroyed()) {
            return;
        }
        if(event.getUnReadCount() ==0) {
            unReadTv.setVisibility(View.GONE);
        } else {
            unReadTv.setVisibility(View.VISIBLE);
            String unread = event.getUnReadCount() + "";
            unReadTv.setText(unread);
        }
    }
}
