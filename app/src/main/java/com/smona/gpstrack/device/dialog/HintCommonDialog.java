package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.smona.gpstrack.R;

public class HintCommonDialog extends Dialog {

    private TextView titleTxt;
    private TextView contentTxt;
    private TextView submitBtn;

    private String title;
    private String content;
    private String subContent;
    private String positiveName;
    private OnCommitListener listener;

    public HintCommonDialog(Context context) {
        this(context, null, null, null);
    }

    public HintCommonDialog(Context context, String title, String content) {
        this(context, title, content, null);
    }

    public HintCommonDialog(Context context, String title, String content, OnCommitListener listener) {
        this(context, title, content, null, null);
    }

    public HintCommonDialog(Context context, String title, String content, String subContent, OnCommitListener listener) {
        super(context, R.style.CommonDialog);
        this.title = title;
        this.content = content;
        this.subContent = subContent;
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public HintCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public HintCommonDialog setContent(String content) {
        this.content = content;
        return this;
    }


    public HintCommonDialog setOkName(String name) {
        this.positiveName = name;
        return this;
    }

    public HintCommonDialog setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint_layout);
        initView();
    }

    private void initView() {
        titleTxt = findViewById(R.id.tv_title);
        contentTxt = findViewById(R.id.tv_content);
        submitBtn = findViewById(R.id.tv_ok);
        submitBtn.setOnClickListener(v -> clickOk());

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

        if (!TextUtils.isEmpty(content)) {
            contentTxt.setText(content);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            submitBtn.setText(positiveName);
        }

    }

    private void clickOk() {
        if (listener != null) {
            listener.onClick(this, true);
        }
    }

    @Override
    public void show() {
        super.show();
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_200dp);
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCommitListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}