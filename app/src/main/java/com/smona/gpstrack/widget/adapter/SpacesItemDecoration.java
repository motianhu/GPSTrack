package com.smona.gpstrack.widget.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 3/7/19 2:12 PM
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int hor;
    private int ver;

    public SpacesItemDecoration(int hor, int ver) {
        this.hor = hor;
        this.ver = ver;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = hor;
        outRect.right = hor;
        outRect.bottom = ver;
        outRect.top = ver;
    }
}
