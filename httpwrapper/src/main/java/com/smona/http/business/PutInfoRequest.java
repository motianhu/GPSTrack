package com.smona.http.business;

import com.smona.http.wrapper.BasePutRequest;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 10:02 AM
 */
public class PutInfoRequest<T> extends BasePutRequest<T> {

    PutInfoRequest(String path) {
        super(path);
    }

    @Override
    protected String getBaseUrl() {
        return BusinessHttpService.BASE_URL;
    }
}
