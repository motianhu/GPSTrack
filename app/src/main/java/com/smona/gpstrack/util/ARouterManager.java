package com.smona.gpstrack.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Parcelable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;

public class ARouterManager {

    private static ARouterManager INSTANCE = null;

    /**
     * 初始化客户端配置
     */
    public static void init(Application context, boolean isAppDebug) {
        if (isAppDebug) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openLog();// 打印日志
        }
        ARouter.init(context);// 尽可能早，推荐在Application中初始化
    }

    /**
     * 初始化信息
     */
    public static synchronized ARouterManager getInstance() {
        // 这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        // 使用时生成实例，提高了效率！
        if (INSTANCE == null) INSTANCE = new ARouterManager();
        return INSTANCE;
    }

    /**
     * activity 普通跳转 不携带参数
     *
     * @param path
     */
    public void gotoActivity(String path) {
        ARouter.getInstance().build(path)
                .navigation();
    }


    /**
     * activity 普通跳转 携带单个string参数
     *
     * @param path
     */
    public void gotoActivityWithString(String path, String key, String value) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .navigation();
    }

    public void gotoActivityWithDouble(String path, String key, Double value) {
        ARouter.getInstance().build(path)
                .withDouble(key, value)
                .navigation();
    }

    public void gotoActivity(final String path, int... flags) {
        Postcard postcard = ARouter.getInstance().build(path);
        if (flags != null && flags.length > 0) {
            for (int flag : flags) {
                postcard.withFlags(flag);
            }
        }
        postcard.navigation();
    }

    /**
     * activity 普通跳转 Integer
     *
     * @param path
     */
    public void gotoActivityWithInteger(String path, Integer value) {
        ARouter.getInstance().build(path)
                .withInt(path, value)
                .navigation();
    }

    /*
     * activity 普通跳转，带返回码
     * */
    public void gotoActivityForResult(String path, Activity activity, int requestCode) {
        ARouter.getInstance().build(path)
                .greenChannel()
                .navigation(activity, requestCode);
    }

    /*
     * activity 普通跳转，带返回码
     * */
    public void gotoActivityForResult(String path, Activity activity, int requestCode, String value) {
        ARouter.getInstance().build(path)
                .withString(path, value)
                .greenChannel()
                .navigation(activity, requestCode);
    }

    /*
     * activity 普通跳转
     * */
    public void gotoActivityForResultBundle(String path, Activity activity, int requestCode, Bundle bundle) {
        ARouter.getInstance().build(path)
                .withBundle(path, bundle)
                .greenChannel()
                .navigation(activity, requestCode);
    }

    /**
     * 调用activity
     * 接口
     **/
    public void gotoActivityBundle(String path, Bundle bundle) {
        ARouter.getInstance().build(path)
                .withBundle(path, bundle)
                .navigation();
    }
}
