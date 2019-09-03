package com.smona.logger.printer;

import android.util.Log;

import com.smona.logger.common.LogLevel;
import com.smona.logger.common.LogConfig;

public class ConsolePrinter implements Printer {
    private  LogConfig mLogConfig;
    private  boolean mHasPrintedHead =false;
    @Override
    public void println(int logLevel, String tag, String message) {
        //打印头信息
        if(!mHasPrintedHead){
            mHasPrintedHead =true;
        }

        Log.println(logLevel,tag,message);
    }

    @Override
    public void printCrash(String tag, String message) {
        //打印头信息
        if(!mHasPrintedHead){
            mHasPrintedHead =true;
        }
        Log.println(LogLevel.ERROR,tag,message);
    }

    @Override
    public LogConfig getLogConfig() {
        return mLogConfig;
    }

    @Override
    public void setLogConfig(LogConfig logConfig) {
        this.mLogConfig =logConfig;
    }

}
