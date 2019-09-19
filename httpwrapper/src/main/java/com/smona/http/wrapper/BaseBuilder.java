package com.smona.http.wrapper;

import com.smona.base.http.HttpConfig;
import com.smona.base.http.builder.LifeCycleBuilder;

public abstract class BaseBuilder<T> extends LifeCycleBuilder<T> {

    public BaseBuilder() {
        HttpConfig config = HttpConfig.create(true).readTimeout(30).writeTimeout(30);
        config.addHeader("x-api-key", "0h8a00FSgoQfQ8YTbi4NBkmKxfMtuw6guZ73BGzt");
        setHttpCustomConfig(config);
    }
}
