package com.smona.gpstrack.component;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * description:
 *  RecyclerView通用初始化类
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 1:18 PM
 */
public class WidgetComponent {

    public static void initXRecyclerView(Context context, XRecyclerView recyclerView, boolean isMore, boolean isRefresh, XRecyclerView.LoadingListener listener) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreEnabled(isMore);
        recyclerView.setPullRefreshEnabled(isRefresh);
        recyclerView.setLoadingListener(listener);
    }

    public static void initXRecyclerView(Context context, XRecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
    }

    public static void initGridXRecyclerView(Context context, XRecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
    }

    public static void initRecyclerView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static void initGridRecyclerView(Context context, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
