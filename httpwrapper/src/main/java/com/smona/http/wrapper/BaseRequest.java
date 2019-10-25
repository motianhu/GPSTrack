package com.smona.http.wrapper;

import com.smona.http.business.BusinessHttpService;
import com.smona.http.config.LoadConfig;

public abstract class BaseRequest<T> extends BaseBuilder<T> {
    private String path;

    BaseRequest(String path) {
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }

    @Override
    protected String getBaseUrl() {
        return LoadConfig.appConfig != null ? LoadConfig.appConfig.getApiUrl() : BusinessHttpService.BASE_URL;
    }
}
