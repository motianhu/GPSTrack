package com.smona.gpstrack.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:14 PM
 */

@Entity(indexes = {@Index(value = "deviceId DESC,  date DESC", unique = true)})
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    public Long id;
    private String deviceId;
    private long date;

    private double latitude;
    private double longitude;
    private double direction;
    private double velocity;
    private double battery;

    @Generated(hash = 137584471)
    public Location(Long id, String deviceId, long date, double latitude,
                    double longitude, double direction, double velocity, double battery) {
        this.id = id;
        this.deviceId = deviceId;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.velocity = velocity;
        this.battery = battery;
    }

    @Generated(hash = 375979639)
    public Location() {
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
