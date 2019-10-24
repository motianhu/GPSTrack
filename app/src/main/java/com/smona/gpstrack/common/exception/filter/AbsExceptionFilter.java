package com.smona.gpstrack.common.exception.filter;

import com.smona.gpstrack.common.exception.IExceptionProcess;
import com.smona.gpstrack.common.exception.InitExceptionProcess;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 9:34 AM
 */
public abstract class AbsExceptionFilter implements IExceptionFilter {
    protected IExceptionProcess mProcess;
    protected IExceptionFilter mNextFilter;

    @Override
    public void addNextFilter(IExceptionProcess process, IExceptionFilter filter) {
        mProcess = process;
        mNextFilter = filter;
    }

    @Override
    public void doFilter(String api, int errCode, String errMsg, InitExceptionProcess.OnReloadListener listener) {
        if (isFilter(api, errCode, errMsg)) {
            exeFilter(api, errCode, errMsg, listener);
        } else if (mNextFilter != null) {
            mNextFilter.doFilter(api, errCode, errMsg, listener);
        }
    }

    abstract boolean isFilter(String api, int errCode, String errMsg);

    abstract void exeFilter(String api, int errCode, String errMsg, InitExceptionProcess.OnReloadListener listener);
}
