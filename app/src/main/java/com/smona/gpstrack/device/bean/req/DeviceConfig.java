package com.smona.gpstrack.device.bean.req;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 3:06 PM
 */
public class DeviceConfig {
    private int phnLmt;
    private String phones;
    private Boolean sosAlm;
    private Boolean batAlm;
    private Boolean tmprAlm;
    private Boolean vocMon;

    public int getPhnLmt() {
        return phnLmt;
    }

    public void setPhnLmt(int phnLmt) {
        this.phnLmt = phnLmt;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public boolean isSosAlm() {
        return sosAlm;
    }

    public void setSosAlm(boolean sosAlm) {
        this.sosAlm = sosAlm;
    }

    public boolean isBatAlm() {
        return batAlm;
    }

    public void setBatAlm(boolean batAlm) {
        this.batAlm = batAlm;
    }

    public boolean isTmprAlm() {
        return tmprAlm;
    }

    public void setTmprAlm(boolean tmprAlm) {
        this.tmprAlm = tmprAlm;
    }

    public boolean isVocMon() {
        return vocMon;
    }

    public void setVocMon(boolean vocMon) {
        this.vocMon = vocMon;
    }
}
