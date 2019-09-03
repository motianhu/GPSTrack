package com.smona.logger.formatter;

import android.os.Process;

public class ThreadFormatter implements  Formatter<Thread> {
    @Override
    public String format(Thread data,boolean oneline){
        long threadID = data.getId();
        if(1 == threadID){
            threadID = Process.myPid();//线程为1是主线程，将起改成跟进程号同名
        }

        if (oneline){
            return threadID+"  ";
        }else {
            return threadID+"\n";
        }

    }
}
