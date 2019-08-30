package com.smona.base.http.builder;

public abstract class LifeCycleBuilder<T> extends CommonBuilder<T> {

    private String TAG = "LifeCycleBuilder";

    protected int mTagHash;

    public CommonBuilder<T> setTag(Object tag) {
        mTag = tag;
        mTagHash = tag == null ? TAG.hashCode() : tag.hashCode();
        return this;
    }

    @Override
    protected int getTagHash() {
        if (mTagHash == 0) {
            return TAG.hashCode();
        }else {
            return mTagHash;
        }
    }
}
