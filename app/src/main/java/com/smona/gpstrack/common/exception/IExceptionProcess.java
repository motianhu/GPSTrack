package com.smona.gpstrack.common.exception;

import android.view.View;

import com.smona.gpstrack.widget.LoadingResultView;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 9:45 AM
 */
public interface IExceptionProcess {
    LoadingResultView getErrorView();
    View[] getContentViews();
}
