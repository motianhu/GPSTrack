package com.smona.gpstrack.settings.bean;

public class MapItem {
    private int resId;
    private String mapDefault;
    private boolean isSelected;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMapDefault() {
        return mapDefault;
    }

    public void setMapDefault(String mapDefault) {
        this.mapDefault = mapDefault;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
