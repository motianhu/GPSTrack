package com.smona.gpstrack.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 7:30 PM
 */
@Entity
public class Device {
    private static final long serialVersionUID = 1L;
    public static final String ONLINE = "a";
    public static final String OFFLINE = "o";
    public static final String INACTIVE = "i";

    @Unique
    private String id;
    private String name;
    private long expiryDate;
    private long onlineDate;
    private String status;

    @Generated(hash = 1627657446)
    public Device(String id, String name, long expiryDate, long onlineDate,
                  String status) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.onlineDate = onlineDate;
        this.status = status;
    }

    @Generated(hash = 1469582394)
    public Device() {
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

    public long getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getOnlineDate() {
        return this.onlineDate;
    }

    public void setOnlineDate(long onlineDate) {
        this.onlineDate = onlineDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
