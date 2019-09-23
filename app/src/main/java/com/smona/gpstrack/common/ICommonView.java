package com.smona.gpstrack.common;

import com.smona.base.ui.mvp.IBaseView;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:39 PM
 */
public interface ICommonView extends IBaseView {
    void onError(String api, int errCode, ErrorInfo errorInfo);
}
