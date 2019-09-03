package com.smona.base.http.utils;

import android.util.Log;

import com.smona.base.http.HttpConstants;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
    public static void closeAll(Closeable...closeables){
        if(closeables == null){
            return;
        }
        for (Closeable closeable : closeables) {
            if(closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    Log.e(HttpConstants.LOG_TAG,"IOException: " + e);
                }
            }
        }
    }
}
