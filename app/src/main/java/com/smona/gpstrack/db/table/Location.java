package com.smona.gpstrack.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:14 PM
 */

@Entity
public class Location {
    private String devicePlatformId;
    private String devicePlatformName;
    private long date;
    private double latitude;
    private double longitude;
    private double direction;
    private double velocity;
    private double battery;
    private int alarmPriority;
    @Generated(hash = 1858555477)
    public Location(String devicePlatformId, String devicePlatformName, long date,
            double latitude, double longitude, double direction, double velocity,
            double battery, int alarmPriority) {
        this.devicePlatformId = devicePlatformId;
        this.devicePlatformName = devicePlatformName;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.velocity = velocity;
        this.battery = battery;
        this.alarmPriority = alarmPriority;
    }
    @Generated(hash = 375979639)
    public Location() {
    }
    public String getDevicePlatformId() {
        return this.devicePlatformId;
    }
    public void setDevicePlatformId(String devicePlatformId) {
        this.devicePlatformId = devicePlatformId;
    }
    public String getDevicePlatformName() {
        return this.devicePlatformName;
    }
    public void setDevicePlatformName(String devicePlatformName) {
        this.devicePlatformName = devicePlatformName;
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getDirection() {
        return this.direction;
    }
    public void setDirection(double direction) {
        this.direction = direction;
    }
    public double getVelocity() {
        return this.velocity;
    }
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
    public double getBattery() {
        return this.battery;
    }
    public void setBattery(double battery) {
        this.battery = battery;
    }
    public int getAlarmPriority() {
        return this.alarmPriority;
    }
    public void setAlarmPriority(int alarmPriority) {
        this.alarmPriority = alarmPriority;
    }
}
