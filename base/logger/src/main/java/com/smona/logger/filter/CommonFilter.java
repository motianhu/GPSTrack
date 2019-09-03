package com.smona.logger.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smona.logger.Logger;

public class CommonFilter implements  IFilter {
    private static final String TAG = "CommonFilter";
    String mTag;//根据tag来过滤
    boolean mSupportReg;//是否支持正则表达式

    Pattern mPattern;

    @Override
    public boolean accept(String targetTag) {
        if(mTag ==null)return  true;
        //判断是否过滤
        if(mSupportReg){//使用正则表达式
            if(mPattern==null){
                try{
                    mPattern =Pattern.compile(mTag);
                }catch (Exception e){
                    Logger.w(TAG,e.toString());
                    return  mTag.equals(targetTag);
                }

            }

            Matcher matcher = mPattern.matcher(targetTag);
            return matcher.matches();
        }else {//不使用正则表达式
            return  mTag.equals(targetTag);
        }
    }

    public  CommonFilter(String tag,boolean useReg){
        this.mTag =tag;
        this.mSupportReg =useReg;
        if(useReg){
            mPattern =Pattern.compile(tag);
        }
    }

    public CommonFilter() {
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

    @Override
    public String toString() {
        return "CommonFilter{" +
                "mTag='" + mTag + '\'' +
                ", mSupportReg=" + mSupportReg +
                '}';
    }
}
