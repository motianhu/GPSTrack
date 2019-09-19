package com.smona.http.business;

public interface BusinessHttpService {
    String BASE_URL = "http://61.92.168.2:9091";

    String REGISTER = "/app-api/$1s/loginAccount/register";
    String REGISTER_VERIFY = "/app-api/$1s/loginAccount/verify/$2s/$3s/$4s";
    String LOGIN = "/app-api/$1s/loginAccount/login";
    String LOGOUT = "/app-api/$1s/loginAccount/logout/$2s";
    String CHNAGEPASSWORD = "/app-api/$1s/loginAccount/changePassword";
}
