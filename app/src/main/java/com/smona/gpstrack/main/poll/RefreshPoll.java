package com.smona.gpstrack.main.poll;

import android.os.CountDownTimer;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/8/19 9:17 AM
 */
public class RefreshPoll {
    private static final long DURATION = 12 * 1000;
    private static final long STEP = 1000;
    private CountDownTimer mCountDownTimer;
    private OnPollListener mPollListener;

    public RefreshPoll() {
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
