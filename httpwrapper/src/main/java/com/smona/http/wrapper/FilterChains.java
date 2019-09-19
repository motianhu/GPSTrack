package com.smona.http.wrapper;

import android.util.SparseArray;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/11/19 1:56 PM
 */
public class FilterChains {
    private SparseArray<IFilter> mAspectFilter = new SparseArray<>();

    private static class Holder {
        private final static FilterChains sFilterChain = new FilterChains();
    }

    public static FilterChains getInstance() {
        return Holder.sFilterChain;
    }

    public void addAspectRouter(int errCode, IFilter aspectRouter) {
        mAspectFilter.put(errCode, aspectRouter);
    }

    public void exeFilter(int errCode) {
        IFilter filter = mAspectFilter.get(errCode);
        if (filter != null) {
            filter.exeFilter();
        }
    }
}
