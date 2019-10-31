package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ToastUtil;

public class EditCommonDialog extends Dialog {

    private TextView titleTxt;
    private EditText contentTxt;
    private TextView submitBtn;

    private String title;
    private String content;
    private String hint;
    private String positiveName;
    private EditCommonDialog.OnCommitListener listener;

    public EditCommonDialog(Context context) {
        super(context, R.style.CommonDialog);
        setCanceledOnTouchOutside(false);
    }

    public EditCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public EditCommonDialog setHint(String hint) {
        this.hint = hint;
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

        refreshContent(title, titleTxt);
        refreshHint();
        refreshContent(content, contentTxt);
        refreshContent(positiveName, submitBtn);
    }

    private void refreshHint() {
        if (!TextUtils.isEmpty(hint)) {
            contentTxt.setHint(hint);
        }
    }

    private void refreshContent(String title, TextView titleTxt) {
        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }
    }

    private void clickOk() {
        if (listener != null) {
            String inputContent = contentTxt.getText().toString();
            if(TextUtils.isEmpty(inputContent)) {
                ToastUtil.showShort(R.string.input_empty);
            } else {
                listener.onClick(this, contentTxt.getText().toString());
            }
        }
    }

    @Override
    public void show() {
        super.show();
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_240dp);
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCommitListener {
        void onClick(Dialog dialog, String content);
    }
}