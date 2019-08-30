package com.smona.base.ui.activity;

import android.os.Bundle;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IView;


public abstract class BasePresenterActivity<P extends BasePresenter<V>, V extends IView> extends BaseUiActivity {
    protected P mPresenter;

    protected abstract P initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initData在基类中是oncreate之后，所以需要这里在oncreate之前初始化出来
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
        super.onDestroy();
    }
}
