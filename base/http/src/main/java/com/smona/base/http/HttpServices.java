package com.smona.base.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface HttpServices {
    /* common interface */

    @POST
    Observable<Response<String>> post(@Url String path);

    @POST
    Observable<Response<String>> postWithParamsMap(@Url String path, @QueryMap(encoded = true) Map<String, String> params);

    @POST
    Observable<Response<String>> post(@Url String path, @Body Object requestBody);

    @POST
    Observable<Response<String>> postWithHeaderMap(@Url String path, @HeaderMap Map<String, String> headers);

    @POST
    Observable<Response<String>> post(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @Body Object requestBody);

    @POST
    Observable<Response<String>> post(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> headers);

    @POST
    Observable<Response<String>> post(@Url String path, @Body Object requestBody, @HeaderMap Map<String, String> headers);

    @POST
    Observable<Response<String>> post(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @Body Object requestBody, @HeaderMap Map<String, String> headers);


    //get
    @GET
    Observable<Response<String>> get(@Url String path);

    @GET
    Observable<Response<String>> getWithParamsMap(@Url String path, @QueryMap(encoded = true) Map<String, String> params);

    @GET
    Observable<Response<String>> getWithHeaderMap(@Url String path, @HeaderMap Map<String, String> header);

    @GET
    Observable<Response<String>> get(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> header);


    //put
    @PUT
    Observable<Response<String>> put(@Url String path);

    @PUT
    Observable<Response<String>> putWithParamsMap(@Url String path, @QueryMap(encoded = true) Map<String, String> params);

    @PUT
    Observable<Response<String>> put(@Url String path, @Body Object requestBody);

    @PUT
    Observable<Response<String>> putWithHeaderMap(@Url String path, @HeaderMap Map<String, String> headers);

    @PUT
    Observable<Response<String>> put(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @Body Object requestBody);

    @PUT
    Observable<Response<String>> put(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @HeaderMap Map<String, String> headers);

    @PUT
    Observable<Response<String>> put(@Url String path, @Body Object requestBody, @HeaderMap Map<String, String> headers);

    @PUT
    Observable<Response<String>> put(@Url String path, @QueryMap(encoded = true) Map<String, String> params, @Body Object requestBody, @HeaderMap Map<String, String> headers);
}
