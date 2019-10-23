package com.smona.gpstrack.device.bean;

public class AvatarItem {
    private int resId;
    private String url;
    private boolean isSelcted;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelcted() {
        return isSelcted;
    }

    public void setSelcted(boolean selcted) {
        isSelcted = selcted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
