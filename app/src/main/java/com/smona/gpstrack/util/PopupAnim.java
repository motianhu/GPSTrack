package com.smona.gpstrack.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.smona.gpstrack.R;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/22/19 4:30 PM
 */
public class PopupAnim {

    private boolean mIsHiding;
    private Animation mInAnimation;
    private Animation mOutAnimation;
    private Animation mAlphaInAnimation;
    private Animation mAlphaOutAnimation;

    public void ejectView(boolean animation, Context context, View rootView, View maskView, View contentView) {
        mIsHiding = false;
        if (animation) {
            show(context, rootView, maskView, contentView);
        } else {
            rootView.setVisibility(View.VISIBLE);
        }
    }

    public void retract(boolean animation, Context context, View rootView, View maskView, View contentView, OnRetractListener listener) {
        if (animation) {
            if (mIsHiding) {
                return;
            }
            mIsHiding = true;
        }

        if (animation) {
            hide(context, rootView, maskView, contentView, listener);
        } else {
            rootView.setVisibility(View.GONE);
        }
    }

    private void show(Context context, View rootView, View maskView, View contentView) {
        rootView.setVisibility(View.VISIBLE);
        mAlphaInAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_0_1);
        mAlphaInAnimation.setDuration(500);
        mAlphaInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                maskView.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                maskView.setAnimation(null);
                maskView.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        maskView.setAnimation(mAlphaInAnimation);
        maskView.animate();

        mInAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        mInAnimation.setDuration(500);
        mInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                contentView.setAnimation(null);
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentView.setAnimation(mInAnimation);
        contentView.animate();
    }

    private void hide(Context context, View rootView, View maskView, View contentView, OnRetractListener listener) {
        mAlphaOutAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_1_0);
        mAlphaOutAnimation.setDuration(500);
        mAlphaOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                maskView.setClickable(false);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                maskView.setAnimation(null);
                rootView.setVisibility(View.GONE);
                maskView.setClickable(true);
                if (listener != null) {
                    listener.onEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        maskView.startAnimation(mAlphaOutAnimation);

        mOutAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_out);
        mOutAnimation.setDuration(500);
        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentView.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentView.startAnimation(mOutAnimation);
    }

    public interface OnRetractListener {
        void onEnd();
    }
}
