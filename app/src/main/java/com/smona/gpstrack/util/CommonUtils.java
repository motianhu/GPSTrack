package com.smona.gpstrack.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ActionModeCallbackInterceptor;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.LocationDecorate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些工具类
 */
public class CommonUtils {

    private static SparseIntArray sparseArray = new SparseIntArray();

    static {

        sparseArray.put(1, R.string.Sunday_short);
        sparseArray.put(2, R.string.Monday_short);
        sparseArray.put(3, R.string.Tuesday_short);
        sparseArray.put(4, R.string.Wednesday_short);
        sparseArray.put(5, R.string.Thursday_short);
        sparseArray.put(6, R.string.Friday_short);
        sparseArray.put(7, R.string.Saturday_short);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static String dayToWeek(Context context, int pos) {
        int resId = sparseArray.get(pos);
        return context.getString(resId);
    }

    /**
     * 检验是否是邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 拦截403和网络错误的Toast。
     * @param stateCode
     * @param msg
     */
    public static void showToastByFilter(int stateCode, String msg) {
        if (stateCode == 403) {
            return;
        }
        if(stateCode >= 1000 && stateCode <= 1006) {
            ToastUtil.showShort(R.string.network_error);
          return;
        }
        ToastUtil.showShort(msg);
    }

    /**
     * 关掉所有页面
     * @param context
     */
    public static void sendCloseAllActivity(Context context) {
        Intent closeAllIntent = new Intent(BaseActivity.ACTION_BASE_ACTIVITY);
        closeAllIntent.putExtra(BaseActivity.ACTION_BASE_ACTIVITY_EXIT_KEY, BaseActivity.ACTION_BASE_ACTIVITY_EXIT_VALUE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(closeAllIntent);
    }

    /**
     * 清空DB数据
     */
    public static void clearAllCache() {
        new DeviceDecorate().deleteAll();
        new FenceDecorate<>().deleteAll();
        new LocationDecorate().deleteAll();
    }

    public static void setMaxLenght(EditText editText, int length) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public static final int MAX_NAME_LENGHT = 100;
    public static final int MAX_PWD_LENGHT = 60;
    public static final int MAX_PHONE_LENGHT = 30;

    public static boolean isInValidLatln(double la, double lo) {
        return la == 0d && lo == 0d;
    }

    /**
     * 禁用密码框的复制粘贴
     * @param editText
     */
    public static void disableEditTextCopy(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomInsertionActionModeCallback(new ActionModeCallbackInterceptor());
        editText.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // setInsertionDisabled when user touches the view
                setInsertionDisabled(editText);
            }
            return false;
        });
    }

    private static void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取启动时的系统语言，并转换为可支持的语言。
     * @return
     */
    public static String getSysLanuage(){
        String curSysLa = Locale.getDefault().toString();
        ConfigInfo configInfo = ConfigCenter.getInstance().getConfigInfo();
        String language = configInfo != null ? configInfo.getLocale() : "";
        if (TextUtils.isEmpty(language)) {
            String value = ParamConstant.SYSLANUAGEMAP.get(curSysLa);
            if (TextUtils.isEmpty(value)) {
                language = ParamConstant.LOCALE_EN;
            } else {
                language = value;
            }
        }
        return language;
    }
}
