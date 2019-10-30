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
        addIv = findViewById(R.id.hintIcon);
        okTv = findViewById(R.id.tv_ok);
        okTv.setOnClickListener(v -> clickOk());

        addIv.setOnClickListener(v-> clickAdd());

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            okTv.setText(positiveName);
        }

        if(!TextUtils.isEmpty(content)) {
            String[] phones = content.split(",");
            for(String phone: phones) {
                View view = View.inflate(getContext(), R.layout.dialog_list_layout_item, null);
                EditText editText = view.findViewById(R.id.edittext);
                editText.setText(phone);
                view.findViewById(R.id.delTv).setOnClickListener(v-> clickDel(view));
                contentLL.addView(view);
            }
            if(phones.length >= limit) {
                addIv.setVisibility(View.GONE);
            }
        }
    }

    private void clickAdd() {
        View view = View.inflate(getContext(), R.layout.dialog_list_layout_item, null);
        view.findViewById(R.id.delTv).setOnClickListener(v-> clickDel(view));
        contentLL.addView(view);
        int count = contentLL.getChildCount();
        if(count >= limit) {
            addIv.setVisibility(View.GONE);
        }
    }

    private void clickDel(View view) {
        contentLL.removeView(view);
        if(contentLL.getChildCount() < limit) {
            addIv.setVisibility(View.VISIBLE);
        }
    }

    private void clickOk() {
        if (listener != null) {
            int count = contentLL.getChildCount();
            String reuslt = "";
            StringBuffer phones = new StringBuffer();
            for (int i = 0; i < count; i++) {
                EditText editText = (EditText) contentLL.getChildAt(i).findViewById(R.id.edittext);
                phones.append(editText.getText().toString() + ",");
            }
            if(phones.length()>0) {
                reuslt = phones.substring(0,phones.length() - 1);
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