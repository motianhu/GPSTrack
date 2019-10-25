package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smona.gpstrack.R;

public class ListCommonDialog extends Dialog {

    private TextView titleTv;
    private LinearLayout contentLL;
    private ImageView addIv;
    private TextView okTv;

    private String title;
    private String hint;
    private String positiveName;
    private OnCommitListener listener;

    public ListCommonDialog(Context context) {
        this(context, null, null, null);
    }

    public ListCommonDialog(Context context, String title, String content) {
        this(context, title, content, null);
    }

    public ListCommonDialog(Context context, String title, String hint, OnCommitListener listener) {
        super(context, R.style.CommonDialog);
        this.title = title;
        this.hint = hint;
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public ListCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListCommonDialog setContent(String hint) {
        this.hint = hint;
        return this;
    }

    public ListCommonDialog setOkName(String name) {
        this.positiveName = name;
        return this;
    }

    public ListCommonDialog setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_layout);
        initView();
    }

    private void initView() {
        titleTv = findViewById(R.id.tv_title);
        contentLL = findViewById(R.id.contentLL);
        addIv = findViewById(R.id.hintIcon);
        okTv = findViewById(R.id.tv_ok);
        okTv.setOnClickListener(v -> clickOk());

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            okTv.setText(positiveName);
        }
    }

    private void clickOk() {
        if (listener != null) {
            int count = contentLL.getChildCount();
            StringBuffer phones = new StringBuffer();
            for (int i = 0; i < count; i++) {
                EditText editText = (EditText) contentLL.getChildAt(i);
                phones.append(editText.getText().toString() + ",");
            }
            listener.onClick(this, phones.toString());
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