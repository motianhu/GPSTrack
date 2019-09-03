package com.smona.base.ui.mvp;

public interface IBasePresenter<V extends IView> {
    void onAttachView(V view);

    void onDetachView();
}
