package com.smona.gpstrack.common;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 3:32 PM
 */
public class ConfigParam {
    private String name;
    private String email;
    private String locale;
    private String dateFormat;
    private String timeZone;
    private String mapDefault;
    private boolean appNotice;
    private int refreshInterval;
    private int geoFenceLimit;
    private String apiKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getMapDefault() {
        return mapDefault;
    }

    public void setMapDefault(String mapDefault) {
        this.mapDefault = mapDefault;
    }

    public boolean isAppNotice() {
        return appNotice;
    }

    public void setAppNotice(boolean appNotice) {
        this.appNotice = appNotice;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public int getGeoFenceLimit() {
        return geoFenceLimit;
    }

    public void setGeoFenceLimit(int geoFenceLimit) {
        this.geoFenceLimit = geoFenceLimit;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
