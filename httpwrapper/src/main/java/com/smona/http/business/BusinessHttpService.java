package com.smona.http.business;

public interface BusinessHttpService {
    String BASE_URL = "http://61.92.168.2:9091";

    //固定apiKey
    String REGISTER = "/app-api/%s/loginAccount/register";
    String VERIFY = "/app-api/%s/loginAccount/verify/%s/%s/%s";
    String LOGIN = "/app-api/%s/loginAccount/login";
    String FORGETPASSWORD = "/app-api/%s/loginAccount/forgetPassword/%s";

    //以下API全部用登录的apiKey
    String LOGOUT = "/app-api/%s/loginAccount/logout/%s";
    String CHNAGEPASSWORD = "/app-api/%s/loginAccount/changePassword";
}
