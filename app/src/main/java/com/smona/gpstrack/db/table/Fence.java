package com.smona.gpstrack.db.table;

import android.graphics.Color;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 7:30 PM
 */
@Entity
public class Fence implements Serializable {
    public static final String ACTIVE = "a";
    public static final String INACTIVE = "i";

    private static final long serialVersionUID = 1L;

    @Id
    @Unique
    public String id;
    private String name;
    private String color;
    private double radius;
    private double latitude;
    private double longitude;
    private String status;
    @Generated(hash = 1225576682)
    public Fence(String id, String name, String color, double radius,
            double latitude, double longitude, String status) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }
    @Generated(hash = 673726420)
    public Fence() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public double getRadius() {
        return this.radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
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
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Fence{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", radius=" + radius +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", status='" + status + '\'' +
                '}';
    }

    public static int getFenceColor(String status) {
        int color = Color.argb(0x80, 0x64,0xB8,0xD7);
        if (Fence.ACTIVE.equals(status)) {
            color = Color.argb(0x80, 1, 1, 255);
        }
        return color;
    }
}
