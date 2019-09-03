package com.smona.logger.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMsgFilter implements IFilter {
    String mTag;//根据tag来过滤
    boolean mSupportReg;//是否支持正则表达式

    Pattern mPattern;

    @Override
    public boolean accept(String logMsg) {
        if (mTag == null) return true;
        boolean flag = false;
        //判断是否过滤
        if (mSupportReg) {//使用正则表达式
            Matcher matcher = mPattern.matcher(logMsg);
            flag = matcher.matches();
        } else {//不使用正则表达式
            flag = mTag.equals(logMsg);
        }

        return flag;
    }

    public LogMsgFilter(String tag) {
        this.mTag = tag;
        this.mSupportReg = true;
        mPattern = Pattern.compile(tag, Pattern.MULTILINE);
    }

    public LogMsgFilter() {

    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public boolean isSupportReg() {
        return mSupportReg;
    }

    public void setSupportReg(boolean supportReg) {
        this.mSupportReg = supportReg;
    }
}
