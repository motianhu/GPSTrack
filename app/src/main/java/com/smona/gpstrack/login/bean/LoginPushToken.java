package com.smona.gpstrack.login.bean;

public class LoginPushToken {
    private MobileBean mobile = new MobileBean();

    public void setPushToken(String pushToken) {
        mobile.setPushToken(pushToken);
    }
}
