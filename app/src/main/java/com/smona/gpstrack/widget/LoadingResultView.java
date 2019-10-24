package com.smona.gpstrack.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.common.exception.InitExceptionProcess;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 3/27/19 5:30 PM
 */
public class LoadingResultView extends RelativeLayout {

    private enum Status {
        IDLE, LOADING, NONETWORK, NOCONTENT, COUNTDOWN
    }

    private View mLoading;
    private View mNoNetwork;
    private View mNoContent;

    private Status mCurStatus = Status.IDLE;

    public LoadingResultView(Context context) {
        super(context);
        initChild();
    }

    public LoadingResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChild();
    }

    public LoadingResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChild();
    }

    private void initChild() {
        View.inflate(getContext(), R.layout.loading_result_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoading = findViewById(R.id.loading);
        mNoNetwork = findViewById(R.id.nonetwork);
        mNoContent = findViewById(R.id.nocontent);
        setStatus(Status.LOADING);
    }


    public void setLoading() {
        setStatus(Status.LOADING);
    }

    public void setNoNetwork(InitExceptionProcess.OnReloadListener listener) {
        setStatus(Status.NONETWORK);
        mNoNetwork.findViewById(R.id.tv_no_network).setOnClickListener(l -> {
            if (listener != null) {
                listener.onReload();
            }
        });
    }

    public void setNoContent() {
        setStatus(Status.NOCONTENT);
    }

    public void setIdle() {
        setStatus(Status.IDLE);
    }

    public void setNoContent(String content, int resId) {
        setStatus(Status.NOCONTENT);

        TextView tv = mNoContent.findViewById(R.id.tv_no_content);
        tv.setText(content);
        ImageView iv = mNoContent.findViewById(R.id.iv_no_content);
        iv.setImageResource(resId);
    }

    private void setStatus(Status status) {
        if (status.ordinal() == mCurStatus.ordinal()) {
            return;
        }
        mCurStatus = status;
        changeUI();
    }

    private void changeUI() {
        setVisibility(VISIBLE);
        if (Status.LOADING.ordinal() == mCurStatus.ordinal()) {
            mLoading.setVisibility(VISIBLE);
            mNoContent.setVisibility(GONE);
            mNoNetwork.setVisibility(GONE);
        } else if (Status.NONETWORK.ordinal() == mCurStatus.ordinal()) {
            mLoading.setVisibility(GONE);
            mNoNetwork.setVisibility(VISIBLE);
            mNoContent.setVisibility(GONE);
        } else if (Status.NOCONTENT.ordinal() == mCurStatus.ordinal()) {
            mLoading.setVisibility(GONE);
            mNoNetwork.setVisibility(GONE);
            mNoContent.setVisibility(VISIBLE);
        } else if (Status.COUNTDOWN.ordinal() == mCurStatus.ordinal()) {
            mLoading.setVisibility(GONE);
            mNoNetwork.setVisibility(GONE);
            mNoContent.setVisibility(GONE);
        } else if (Status.IDLE.ordinal() == mCurStatus.ordinal()) {
            mLoading.setVisibility(GONE);
            mNoNetwork.setVisibility(GONE);
            mNoContent.setVisibility(GONE);
            setVisibility(GONE);
        }
    }
}
