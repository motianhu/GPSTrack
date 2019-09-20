package com.smona.base.http;

import android.support.annotation.StringDef;

public class HttpMethod {

    public static final String POST = "post";

    public static final String GET = "get";

    public static final String PUT = "put";

    public static final String DELETE = "delete";

    @StringDef({POST, GET, PUT, DELETE})
    public @interface IMethod {
    }
}
