package com.smona.http.business;

public interface BusinessHttpService {
    String BASE_URL = "http://61.92.168.2:9091";
    String API_KEY = "0h8a00FSgoQfQ8YTbi4NBkmKxfMtuw6guZ73BGzt";

    /**
     * 固定apiKey
     */
    //Login/Register/Verify/ForgetPwd
    String REGISTER = "/app-api/%s/loginAccount/register";
    String VERIFY = "/app-api/%s/loginAccount/verify/%s/%s/%s";
    String LOGIN = "/app-api/%s/loginAccount/login";
    String FORGET_PASSWORD = "/app-api/%s/loginAccount/forgetPassword/%s";

    /**
     * 以下API全部用登录的apiKey
     */
    //Account
    String LOGOUT = "/app-api/%s/loginAccount/logout/%s";
    String ACCOUNT = "/app-api/%s/loginAccount";  //get-view;put-update
    String CHNAGE_PASSWORD = "/app-api/%s/loginAccount/changePassword";

    //Device
    String DEVICE_LIST = "/app-api/%s/devicePlatform/list?page_size=%s&page=%s";
    String ADD_DEVICE = "/app-api/%s/devicePlatform";
    String DEVICE = "/app-api/%s/devicePlatform/%s"; //get-view;put-update;delete-delete
    String DEVICE_SHARE = "/app-api/%s/devicePlatform/share/%s/%s";
    String DEVICE_UNSHARE = "/app-api/%s/devicePlatform/unshare/%s/%s";
    String DEVICE_CHANGEOWNER = "/app-api/%s/devicePlatform/changeOwner/%s/%s";

    //GEO
    String GEO_LIST = "/app-api/%s/geoFence/list?page_size=%s&page=%s";
    String GEO_ADD = "/app-api/%s/geoFence";
    String GEO_UPDATE = "/app-api/%s/geoFence/%s"; //put-update;delete-delete


    //Alert
    String ALERT_LIST = "/app-api/%s/alarm/list?page_size=%s&page=%s&date_from=%s";
    String ALERT_DELETE = "/app-api/%s/alarm/%s";//delete-delete
    String ALERT_READ = "/app-api/%s/alarm/read";

    //Location
    String LOCATION_LIVE = "/app-api/%s/location/live?page_size=%s&page=%s&map=%s&date_from=%s";
    String LOCATION_DEVICE = "/app-api/%s/location/list?devicePlatformId=%s&page_size=%s&page=%s&map=%s&date_from=%s&date_to=%s";

    //Term
    String TNC = "/app-api/%s/info/tnc";
}
