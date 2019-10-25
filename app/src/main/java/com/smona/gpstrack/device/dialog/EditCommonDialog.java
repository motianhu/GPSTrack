package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.smona.gpstrack.R;

public class EditCommonDialog extends Dialog {

    private TextView titleTxt;
    private EditText contentTxt;
    private TextView submitBtn;

    private String title;
    private String content;
    private String positiveName;
    private EditCommonDialog.OnCommitListener listener;

    public EditCommonDialog(Context context) {
        this(context, null, null, null);
    }

    public EditCommonDialog(Context context, String title, String content) {
        this(context, title, content, null);
    }

    public EditCommonDialog(Context context, String title, String content, EditCommonDialog.OnCommitListener listener) {
        super(context, R.style.CommonDialog);
        this.title = title;
        this.content = content;
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public EditCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public EditCommonDialog setContent(String content) {
        this.content = content;
        return this;
    }


    public EditCommonDialog setOkName(String name) {
        this.positiveName = name;
        return this;
    }

    public EditCommonDialog setOnCommitListener(EditCommonDialog.OnCommitListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_layout);
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
            contentTxt.setHint(content);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            submitBtn.setText(positiveName);
        }

    }

    private void clickOk() {
        if (listener != null) {
            listener.onClick(this, contentTxt.getText().toString());
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
        void onClick(Dialog dialog, String content);
    }
}