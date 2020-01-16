package com.smona.gpstrack.common;

import com.smona.base.ui.mvp.IBaseView;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 * MVP中的View层通用回调接口
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:39 PM
 */
public interface ICommonView extends IBaseView {
    /**
     * 网络请求出错时的回调方法
     * @param api  调用的API
     * @param errCode  错误码
     * @param errorInfo  错误信息
     */
    void onError(String api, int errCode, ErrorInfo errorInfo);
}
