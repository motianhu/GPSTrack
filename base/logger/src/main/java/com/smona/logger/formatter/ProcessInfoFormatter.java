package com.smona.logger.formatter;

import android.app.ActivityManager;

public class ProcessInfoFormatter implements Formatter<ActivityManager.RunningAppProcessInfo> {

    @Override
    public String format(ActivityManager.RunningAppProcessInfo data, boolean printOneline){
        if (data == null) {
            return "";
        }

        if (printOneline) {

            return data.pid + "-";
        }else {
            return data.pid + "\n";

        }
    }

}
