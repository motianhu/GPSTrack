package com.smona.base.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.smona.base.ui.R;
import com.smona.base.ui.mvp.IBaseView;
import com.smona.base.ui.widget.AppDialog;

public abstract class BaseUiActivity extends BaseActivity implements IBaseView {
    protected ViewGroup loadingLayout;
    protected View loadingTips;
    protected ViewGroup baseUiGroup;
    //融合版loading
    protected AppDialog appDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化布局
        initLayout();
        //初始化数据
        initData();
    }

    @Override
    public void finish() {
        //如果键盘开启，则收起键盘
        hideSoftKeyboard();
        super.finish();
    }

    private void initLayout() {
        baseUiGroup = (ViewGroup) View.inflate(this, R.layout.base_ui_layout, null);
        loadingLayout = (ViewGroup) View.inflate(this, R.layout.common_layout_loading, null);

        View contentView = View.inflate(this, getLayoutId(), null);
        baseUiGroup.addView(contentView);
        setContentView(baseUiGroup);

        initContentView();
        appDialog = AppDialog.loadingCreate(this, "加载中");
    }

    protected void initContentView() {
        loadingTips = loadingLayout.findViewById(R.id.loading_pb);
    }

    public void showLoadingDialog() {
        showLoading(true);
    }

    public void hideLoadingDialog() {
        showLoading(false);
    }

    private void showLoading(boolean show) {
        if (appDialog == null) {
            return;
        }
        if (show) {
            if (!appDialog.isShowing()) {
                appDialog.showLoadingDialog();
            }
        } else {
            appDialog.dismiss();
        }
    }


    protected abstract int getLayoutId();


    /**
     * 一些初始化操作在这里执行
     */
    protected void initData() {
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
