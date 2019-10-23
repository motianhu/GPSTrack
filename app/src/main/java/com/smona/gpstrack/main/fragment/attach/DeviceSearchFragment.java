package com.smona.gpstrack.main.fragment.attach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.main.adapter.SearchDeviceAdapter;
import com.smona.gpstrack.main.presenter.SearchDevicePresenter;
import com.smona.gpstrack.util.PopupAnim;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:17 PM
 */
public class DeviceSearchFragment extends BasePresenterFragment<SearchDevicePresenter, SearchDevicePresenter.IView> implements SearchDevicePresenter.IView {

    private EditText searchEtName;
    private SearchDeviceAdapter adapter;

    private PopupAnim popupAnim = new PopupAnim();
    private View contentView;
    private View maskView;

    private OnSearchDeviceListener listener;

    @Override
    protected SearchDevicePresenter initPresenter() {
        return new SearchDevicePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_search;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View content) {
        super.initView(content);

        contentView = content.findViewById(R.id.contentView);
        maskView = content.findViewById(R.id.maskView);
        maskView.setOnTouchListener((v, event) -> true);
        content.findViewById(R.id.cancel_action).setOnClickListener(v -> closeFragment());

        searchEtName = content.findViewById(R.id.edit_search_device);
        searchEtName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWord = searchEtName.getText().toString();
                mPresenter.requestSearchDevice(keyWord);
            }
        });

        RecyclerView recyclerView = content.findViewById(R.id.device_list);
        WidgetComponent.initRecyclerView(mActivity, recyclerView);

        adapter = new SearchDeviceAdapter(R.layout.adapter_item_search);
        adapter.setListener((item, pos) -> {
            if (listener != null) {
                listener.onSearchDevice(item);
            }
            closeFragment();
        });
        recyclerView.setAdapter(adapter);
    }

    public void setListener(OnSearchDeviceListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestSearchDevice("");
    }

    @Override
    public void onSearchResult(List<Device> devices) {
        adapter.setNewData(devices);
    }

    public void showFragment() {
        if (contentView == null) {
            return;
        }

        View rootView = getView();
        if (rootView == null) {
            // Fragment View 还没创建
            return;
        }
        popupAnim.ejectView(true, mActivity, rootView, maskView, contentView);
    }

    public void closeFragment() {
        hideSoftKeyboard();
        popupAnim.retract(true, mActivity, getView(), maskView, contentView, null);
    }


    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && mActivity.getCurrentFocus() != null) {
            if (mActivity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public interface OnSearchDeviceListener {
        void onSearchDevice(Device device);
    }
}
