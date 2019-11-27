package com.smona.gpstrack.common.exception.filter;

import android.text.TextUtils;

import com.smona.gpstrack.R;
import com.smona.gpstrack.common.exception.InitExceptionProcess;
import com.smona.gpstrack.util.ToastUtil;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 9:48 AM
 */
public class FinishExceptionFilter extends AbsExceptionFilter {
    @Override
    boolean isFilter(String api, int errCode, String errMsg) {
        return true;
    }

    @Override
    void exeFilter(String api, int errCode, String errMsg, InitExceptionProcess.OnReloadListener listener) {
        if (TextUtils.isEmpty(api) || api.endsWith("_first")) {
            mProcess.getErrorView().setNoContent(mProcess.getErrorView().getContext().getString(R.string.no_content), R.drawable.nodata);
        }
        if (TextUtils.isEmpty(errMsg)) {
            ToastUtil.showShort(R.string.network_error);
        } else {
            ToastUtil.showShort(errMsg);
        }
    }
}
