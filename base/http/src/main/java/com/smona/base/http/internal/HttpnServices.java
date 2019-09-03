package com.smona.base.http.internal;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface HttpnServices {
    /* common interface */

    @POST
    Observable<Response<String>> post(@Url String path);

    // 带参数
    @POST
    Observable<Response<String>> postWithParams(@Url String path, @QueryMap(encoded = true) Map<String, String> params);

    // 带头
    @POST
    Observable<Response<String>> postWithHeaders(@Url String path, @HeaderMap Map<String, String> headers);

    // 带参数和头
    @POST
    Observable<Response<String>> post(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> headers);

    // 带body
    @POST
    Observable<Response<String>> post(@Url String path, @Body Object requestBody);

    // 带参数和body
    @POST
    Observable<Response<String>> postWithParamsAndBody(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @Body Object requestBody);

    // 带头和body
    @POST
    Observable<Response<String>> postWithHeadersAndBody(@Url String path, @HeaderMap Map<String, String> headers, @Body Object requestBody);

    // 带参数、头和body
    @POST
    Observable<Response<String>> post(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> headers, @Body Object requestBody);


    //get
    @GET
    Observable<Response<String>> get(@Url String path);

    // 带参数
    @GET
    Observable<Response<String>> getWithParams(@Url String path, @QueryMap(encoded = true) Map<String, String> params);

    // 带头
    @GET
    Observable<Response<String>> getWithHeaders(@Url String path, @HeaderMap Map<String, String> headers);

    // 带参数和头
    @GET
    Observable<Response<String>> get(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> headers);

    //上传
    @POST
    @Multipart
    Observable<Response<String>> upload(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @PartMap Map<String, RequestBody> partMap
    );
}
