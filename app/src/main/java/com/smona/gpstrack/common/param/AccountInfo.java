package com.smona.gpstrack.common.param;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 3:32 PM
 */
public class AccountInfo extends ConfigInfo {
    private String email;
    private int refreshInterval;
    private int geoFenceLimit;
    private String apiKey;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public int getGeoFenceLimit() {
        return geoFenceLimit;
    }

    public void setGeoFenceLimit(int geoFenceLimit) {
        this.geoFenceLimit = geoFenceLimit;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
