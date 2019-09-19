package com.smona.base.http;

import android.support.annotation.StringDef;

public class HttpMethod {

    public static final String POST = "post";

    public static final String GET = "get";

    public static final String PUT = "put";

    @StringDef({POST, GET, PUT})
    public @interface IMethod {
    }
}
