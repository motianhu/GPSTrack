package com.smona.gpstrack.util;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 8/30/19 11:43 AM
 */
public interface ARouterPath {
    String PATH_TO_SPLASH = "/app/activity/splash";
    String PATH_TO_GUIDE = "/app/activity/guide";
    String PATH_TO_LOGIN = "/app/activity/login";
    String PATH_TO_REGISTER = "/app/activity/register";
    String PATH_TO_FORGETPWD = "/app/activity/forgetPwd";

    String PATH_TO_MAIN = "/app/activity/main";
    String PATH_TO_MAP = "/app/activity/map";
    String PATH_TO_SCAN = "/app/activity/scan";

    String PATH_TO_DEVICE_PART = "/app/activity/devicePart";
    String PATH_TO_ALARM_LIST = "/app/activity/alarmList";

    String PATH_TO_ADD_DEVICE = "/app/activity/addDevice";
    String PATH_TO_DEVICE_DETAIL = "/app/activity/viewDeviceDetail";
    String PATH_TO_DEVICE_HISTORY = "/app/activity/deviceHistory";
    String PATH_TO_DEVICE_NAVIGATION = "/app/activity/deviceNavigation";
    String PATH_TO_DEVICE_PIC_MODIFY = "/app/activity/modifyDevicePic";

    //GEO
    String PATH_TO_EDIT_GEO = "/app/activity/editGEO";

    String PATH_TO_ABOUT = "/app/activity/about";
    String PATH_TO_SETTING_LANUAGE = "/app/activity/settingLanuage";
    String PATH_TO_SETTING_MAP = "/app/activity/settingMap";
    String PATH_TO_SETTING_DATEFORMAT = "/app/activity/settingDateFormat";
    String PATH_TO_SETTING_TIMEZONE = "/app/activity/settingTimeZone";
    String PATH_TO_SETTING_UPDATE_PWD = "/app/activity/updatePwd";
    String PATH_TO_SETTING_PROTOCAL = "/app/activity/protocal";




    //request code
    int REQUEST_DEVICE_DETAIL = 1;
    int REQUEST_DEVICE_DETAIL_MODIFY_PIC = 2;
}
