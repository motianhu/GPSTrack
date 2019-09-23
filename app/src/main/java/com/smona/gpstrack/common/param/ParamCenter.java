package com.smona.gpstrack.common.param;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 3:44 PM
 */
public class ParamCenter {

    private ConfigParam configParam;

    private ParamCenter() {
    }

    private static class ParamHolder {
        private static ParamCenter paramCenter = new ParamCenter();
    }

    public static ParamCenter getInstance() {
        return ParamHolder.paramCenter;
    }

    public ConfigParam getConfigParam() {
        return configParam;
    }

    public void setConfigParam(ConfigParam configParam) {
        this.configParam = configParam;
    }
}
