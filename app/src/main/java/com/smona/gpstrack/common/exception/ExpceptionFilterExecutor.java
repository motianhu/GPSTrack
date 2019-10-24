package com.smona.gpstrack.common.exception;


import com.smona.gpstrack.common.exception.filter.FinishExceptionFilter;
import com.smona.gpstrack.common.exception.filter.NetExceptionFilter;
import com.smona.gpstrack.common.exception.filter.StartExceptionFilter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 11:11 AM
 */
class ExpceptionFilterExecutor {

    private StartExceptionFilter startExceptionFilter;

    void initFilter(IExceptionProcess process) {
        //init
        startExceptionFilter = new StartExceptionFilter();
        NetExceptionFilter netExceptionFilter = new NetExceptionFilter();
        FinishExceptionFilter finishExceptionFilter = new FinishExceptionFilter();

        //过滤器执行顺序
        startExceptionFilter.addNextFilter(process, netExceptionFilter);
        netExceptionFilter.addNextFilter(process, finishExceptionFilter);
        finishExceptionFilter.addNextFilter(process, null);
    }

    void executeFilter(String api, int errCode, String errMsg, InitExceptionProcess.OnReloadListener listener) {
        startExceptionFilter.doFilter(api, errCode, errMsg, listener);
    }
}
