package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ToastUtil;

public class EditCommonDialog extends Dialog {

    private TextView titleTxt;
    private EditText contentEt;
    private TextView submitBtn;

    private String title;
    private String content;
    private String hint;
    private String positiveName;
    private int resId = 0;
    private int length = 0;
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

    public EditCommonDialog setMaxLength(int length) {
        this.length = length;
        return this;
    }

    public EditCommonDialog setOkName(String name) {
        this.positiveName = name;
        return this;
    }

    public EditCommonDialog setIv(int resId) {
        this.resId = resId;
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
        contentEt = findViewById(R.id.tv_content);
        submitBtn = findViewById(R.id.tv_ok);
        submitBtn.setOnClickListener(v -> clickOk());
        findViewById(R.id.close).setOnClickListener(v -> this.dismiss());

        refreshContent(title, titleTxt);
        refreshHint();
        refreshContent(content, contentEt);
        refreshContent(positiveName, submitBtn);
        refreshIv(resId);
        refreshEtMaxLength();
    }

    private void refreshEtMaxLength() {
        if(length > 0) {
            contentEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }

    private void refreshHint() {
        if (!TextUtils.isEmpty(hint)) {
            contentEt.setHint(hint);
        }
    }

    private void refreshContent(String title, TextView titleTxt) {
        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }
    }

    private void clickOk() {
        if (listener != null) {
            String inputContent = contentEt.getText().toString();
            if (TextUtils.isEmpty(inputContent)) {
                ToastUtil.showShort(R.string.input_empty);
            } else {
                listener.onClick(this, contentEt.getText().toString());
            }
        }
    }

    private void refreshIv(int resId) {
        if (resId == -1) {
            contentEt.setCompoundDrawables(null, null, null, null);
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