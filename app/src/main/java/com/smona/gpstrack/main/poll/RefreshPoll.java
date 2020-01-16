package com.smona.gpstrack.main.poll;

import android.os.CountDownTimer;

import com.smona.gpstrack.common.param.AccountCenter;

/**
 * description:
 * 定时轮询器
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/8/19 9:17 AM
 */
public class RefreshPoll {
    private static  long DURATION = 10 * 1000;
    private static final long STEP = 1000;
    private CountDownTimer mCountDownTimer;
    private OnPollListener mPollListener;

    public RefreshPoll() {
        DURATION = AccountCenter.getInstance().getAccountInfo().getRefreshInterval() * 1000;
        mCountDownTimer = new CountDownTimer(DURATION, STEP) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mPollListener != null) {
                    mPollListener.onTick((int) millisUntilFinished / 1000);
                }
            }

            @Override
            public void onFinish() {
                if (mPollListener != null) {
                    mPollListener.onFinish();
                }
            }
        };
    }

    public void setParam(OnPollListener pollListener) {
        mPollListener = pollListener;
    }

    public void starPoll() {
        mCountDownTimer.start();
    }

    public void cancleTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (mPollListener != null) {
            mPollListener.cancle();
        }
    }
}
