package com.smona.gpstrack.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.guide.pageIndicator.LinePageIndicator;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.SPUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 10:41 AM
 */

@Route(path = ARouterPath.PATH_TO_GUIDE)
public class GuideActivity extends BaseActivity {

    private GuideAdapter guideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewPager viewPager = findViewById(R.id.guidePager);
        guideAdapter = new GuideAdapter(this);

        List<Integer> data = new ArrayList<>();
        data.add(R.drawable.guide_1);
        data.add(R.drawable.guide_2);
        data.add(R.drawable.guide_3);
        data.add(R.drawable.guide_4);
        guideAdapter.setData(data);

        TextView gotoMain = findViewById(R.id.gotoMain);
        gotoMain.setOnClickListener(view -> {
            ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);
            SPUtils.put("user_guide", 1);
            finish();
        });

        viewPager.setAdapter(guideAdapter);
        LinePageIndicator adIndicator = findViewById(R.id.guideIndicator);
        adIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == data.size() - 1) {
                    gotoMain.setVisibility(View.VISIBLE);
                } else {
                    gotoMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
