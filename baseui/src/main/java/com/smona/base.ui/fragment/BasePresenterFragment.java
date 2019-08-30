package com.smona.base.ui.fragment;

import android.content.Context;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IView;

public abstract class BasePresenterFragment<P extends BasePresenter<V>, V extends IView> extends BaseUiFragment {

    protected P mPresenter;

    protected abstract P initPresenter();

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mPresenter = initPresenter();

        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
    }

    @Override
    public void onDetach() {
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
        super.onDetach();
    }
}
