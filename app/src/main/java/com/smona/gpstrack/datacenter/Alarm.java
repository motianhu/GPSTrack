package com.smona.gpstrack.datacenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:19 PM
 */

public class Alarm {

    public static final String STATUS_N = "n";//new
    public static final String STATUS_S = "s";//read

    /**
     * 报警类型
     */
    public static final String C_SOS = "sos";
    public static final String C_GEO = "geo-fence";
    public static final String C_TMP = "tamper";
    public static final String C_POWER = "power";
    public static final String C_BATTERY = "battery";

    private String id;
    private long date;
    private int priority;
    private String title;
    private String category;
    private String content;
    private String status;


    public static String getcSos() {
        return C_SOS;
    }

    public static String getcGeo() {
        return C_GEO;
    }

    public static String getcTmp() {
        return C_TMP;
    }

    public static String getcPower() {
        return C_POWER;
    }

    public static String getcBattery() {
        return C_BATTERY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
