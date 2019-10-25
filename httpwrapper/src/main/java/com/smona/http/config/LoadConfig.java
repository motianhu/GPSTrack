package com.smona.http.config;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadConfig {

    public static AppConfig appConfig;

    public static void loadConfig() {
        String config = getJson( "/sdcard/pingo.config");
        if(TextUtils.isEmpty(config)) {
            return;
        }
        appConfig = GsonUtil.jsonToObj(config, AppConfig.class);
    }

    private static String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
