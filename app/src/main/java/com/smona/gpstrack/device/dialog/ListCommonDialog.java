package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ToastUtil;

public class ListCommonDialog extends Dialog {

    private TextView titleTv;
    private LinearLayout contentLL;
    private ImageView addIv;
    private TextView okTv;

    private String title;
    private String content;
    private int limit = 5;
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
        this.content = hint;
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public ListCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListCommonDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public ListCommonDialog setItemLimit(int limit) {
        this.limit = limit;
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
        okTv = findViewById(R.id.tv_ok);
        okTv.setOnClickListener(v -> clickOk());
        findViewById(R.id.close).setOnClickListener(v -> this.dismiss());

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            okTv.setText(positiveName);
        }

        for (int i = 0; i < limit; i++) {
            View view = View.inflate(getContext(), R.layout.dialog_list_layout_item, null);
            contentLL.addView(view);
        }

        if (!TextUtils.isEmpty(content)) {
            String[] phones = content.split(",");
            int count = phones.length;
            for (int j = 0; j < count; j++) {
                EditText editText = contentLL.getChildAt(j).findViewById(R.id.edittext);
                editText.setText(phones[j]);
            }
        }
    }

    private void clickOk() {
        if (listener != null) {
            int count = contentLL.getChildCount();
            String reuslt = "";
            StringBuffer phones = new StringBuffer();
            for (int i = 0; i < count; i++) {
                EditText editText = contentLL.getChildAt(i).findViewById(R.id.edittext);
                phones.append(editText.getText().toString() + ",");
            }
            if (phones.length() > 0) {
                reuslt = phones.substring(0, phones.length() - 1);
            }
            listener.onClick(this, reuslt);
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