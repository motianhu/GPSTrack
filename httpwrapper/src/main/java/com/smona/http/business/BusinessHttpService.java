package com.smona.http.business;

public interface BusinessHttpService {
    String BASE_URL = "http://61.92.168.2:9091";

    String REGISTER = "/app-api/%s/loginAccount/register";
    String REGISTER_VERIFY = "/app-api/%s/loginAccount/verify/%s/%s/%s";
    String LOGIN = "/app-api/%s/loginAccount/login";
    String LOGOUT = "/app-api/%s/loginAccount/logout/%s";
    String CHNAGEPASSWORD = "/app-api/%s/loginAccount/changePassword";
}
