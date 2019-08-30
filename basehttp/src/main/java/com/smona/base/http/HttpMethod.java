package com.smona.base.http;

import androidx.annotation.StringDef;

public class HttpMethod {

    public static final String POST = "post";

    public static final String GET = "get";

    @StringDef({POST, GET})
    public @interface IMethed {
    }
}
