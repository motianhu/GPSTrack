package com.smona.gpstrack.fence.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:15 PM
 */
public class FenceBean implements Serializable {

    public static final String STATUS_ENABLE = "a";
    public static final String STATUS_DISABLE = "i";

    private String id;
    private String name;
    private String color;
    private double radius;
    private double latitude;
    private double longitude;

    private List<TimeAlarm> entryAlarm;
    private List<TimeAlarm> leaveAlarm;
    private List<String> devicePlatformIds;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<TimeAlarm> getEntryAlarm() {
        return entryAlarm;
    }

    public void setEntryAlarm(List<TimeAlarm> entryAlarm) {
        this.entryAlarm = entryAlarm;
    }

    public List<TimeAlarm> getLeaveAlarm() {
        return leaveAlarm;
    }

    public void setLeaveAlarm(List<TimeAlarm> leaveAlarm) {
        this.leaveAlarm = leaveAlarm;
    }

    public List<String> getDevicePlatformIds() {
        return devicePlatformIds;
    }

    public void setDevicePlatformIds(List<String> devicePlatformIds) {
        this.devicePlatformIds = devicePlatformIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
