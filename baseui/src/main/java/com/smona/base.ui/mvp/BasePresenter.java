package com.smona.base.ui.mvp;

public class BasePresenter<V extends IView> implements IBasePresenter<V> {

    protected V mView;

    @Override
    public void onAttachView(V view) {

        this.mView = view;
    }

    @Override
    public void onDetachView() {
        this.mView = null;
    }
}
