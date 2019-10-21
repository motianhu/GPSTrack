package com.smona.gpstrack.common.param;

public class ConfigInfo {
    private String name;
    private String locale;
    private String dateFormat;
    private String timeZone;
    private String mapDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
