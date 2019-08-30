package com.smona.base.ui.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.smona.base.ui.R;
import com.smona.base.ui.mvp.IBaseView;

public abstract class BaseUiFragment extends BaseFragment implements IBaseView {
    private ViewGroup containerView;
    private ViewGroup loadingLayout;

    @Override
    protected View getBaseView() {
        containerView = (ViewGroup) View.inflate(getActivity(), R.layout.base_ui_layout, null);
        View contentView = View.inflate(getActivity(), getLayoutId(), null);
        loadingLayout = (ViewGroup) View.inflate(getActivity(), R.layout.common_layout_loading, null);
        containerView.addView(contentView);
        return containerView;
    }

    @Override
    public void showLoadingDialog() {
        containerView.removeView(loadingLayout);
        containerView.addView(loadingLayout);
        if (loadingLayout.getVisibility() != View.VISIBLE) {
            loadingLayout.setVisibility(View.VISIBLE);
        }
        startLoadingAnim();
    }

    @Override
    public void hideLoadingDialog() {
        containerView.removeView(loadingLayout);
        loadingLayout.setVisibility(View.GONE);
    }

    private void startLoadingAnim() {
        ImageView ivRotate = loadingLayout.findViewById(R.id.loading_pb);
        if (ivRotate != null) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_anim);
            ivRotate.setAnimation(rotateAnimation);
            ivRotate.startAnimation(rotateAnimation);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    abstract int getLayoutId();

    protected void initData() {

    }
}
