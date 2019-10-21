package com.smona.gpstrack.main.poll;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 6/28/19 8:05 AM
 */
public interface OnPollListener {
    void onFinish();

    void onTick(int mills);

    void cancle();
}
