package com.smona.gpstrack.common.param;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 3:44 PM
 */
public class AccountCenter {

    private AccountInfo accountInfo;

    private AccountCenter() {
    }

    private static class ParamHolder {
        private static AccountCenter paramCenter = new AccountCenter();
    }

    public static AccountCenter getInstance() {
        return ParamHolder.paramCenter;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
}
