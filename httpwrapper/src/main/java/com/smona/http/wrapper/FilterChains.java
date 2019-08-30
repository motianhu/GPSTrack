package com.smona.http.wrapper;

import java.util.HashMap;
import java.util.Map;

public class FilterChains {
    private Map<String, IFilter> mAspectFilter = new HashMap<>();

    private static class Holder {
        private final static FilterChains sFilterChain = new FilterChains();
    }

    public static FilterChains getInstance() {
        return Holder.sFilterChain;
    }

    public void addAspectRouter(String errCode, IFilter aspectRouter) {
        mAspectFilter.put(errCode, aspectRouter);
    }

    public void exeFilter(String errCode) {
        IFilter filter = mAspectFilter.get(errCode);
        if (filter != null) {
            filter.exeFilter();
        }
    }
}
