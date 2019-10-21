package com.smona.gpstrack.common.param;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 3:44 PM
 */
public class ParamCenter {

    private AccountInfo accountInfo;
    private ConfigInfo configInfo;

    private ParamCenter() {
    }

    private static class ParamHolder {
        private static ParamCenter paramCenter = new ParamCenter();
    }

    public static ParamCenter getInstance() {
        return ParamHolder.paramCenter;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public ConfigInfo getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(ConfigInfo configInfo) {
        this.configInfo = configInfo;
    }
}
