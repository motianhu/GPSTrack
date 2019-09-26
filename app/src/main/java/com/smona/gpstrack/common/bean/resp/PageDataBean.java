package com.smona.gpstrack.common.bean.resp;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 1:34 PM
 */
public class PageDataBean<T> {
    private int page;
    private int ttlPage;
    private int ttlRec;
    private List<T> datas;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTtlPage() {
        return ttlPage;
    }

    public void setTtlPage(int ttlPage) {
        this.ttlPage = ttlPage;
    }

    public int getTtlRec() {
        return ttlRec;
    }

    public void setTtlRec(int ttlRec) {
        this.ttlRec = ttlRec;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
