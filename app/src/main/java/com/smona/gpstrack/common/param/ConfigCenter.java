package com.smona.gpstrack.common.param;

public class ConfigCenter {

    private ConfigInfo configInfo;

    private ConfigCenter() {
    }

    private static class ConfigHolder {
        private static ConfigCenter configCenter = new ConfigCenter();
    }

    public static ConfigCenter getInstance() {
        return ConfigHolder.configCenter;
    }

    public ConfigInfo getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(ConfigInfo configInfo) {
        this.configInfo = configInfo;
    }
}
