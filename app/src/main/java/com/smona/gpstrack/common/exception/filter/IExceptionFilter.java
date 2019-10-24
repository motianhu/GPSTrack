package com.smona.gpstrack.common.exception.filter;

import com.smona.gpstrack.common.exception.IExceptionProcess;
import com.smona.gpstrack.common.exception.InitExceptionProcess;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 9:16 AM
 */
public interface IExceptionFilter {
    void doFilter(String api, int errCode, String errMsg, InitExceptionProcess.OnReloadListener listener);
    void addNextFilter(IExceptionProcess process, IExceptionFilter filter);
}
