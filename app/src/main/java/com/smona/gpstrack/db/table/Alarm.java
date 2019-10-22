package com.smona.gpstrack.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:19 PM
 */
@Entity
public class Alarm {
    public static final String C_SOS = "sos";
    public static final String C_GEO = "geo-fence";
    public static final String C_TMP = "tamper";
    public static final String C_POWER = "power";
    public static final String C_BATTERY = "battery";

    @Unique
    private String id;
    private String devicePlatformId;
    private String devicePlatformName;
    private long date;
    private int priority;
    private String category;
    private String content;
    private String status;

    @Generated(hash = 1592463535)
    public Alarm(String id, String devicePlatformId, String devicePlatformName,
                 long date, int priority, String category, String content,
                 String status) {
        this.id = id;
        this.devicePlatformId = devicePlatformId;
        this.devicePlatformName = devicePlatformName;
        this.date = date;
        this.priority = priority;
        this.category = category;
        this.content = content;
        this.status = status;
    }

    @Generated(hash = 1972324134)
    public Alarm() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
