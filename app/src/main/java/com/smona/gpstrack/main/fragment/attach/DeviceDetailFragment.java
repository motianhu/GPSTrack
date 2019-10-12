package com.smona.gpstrack.main.fragment.attach;

import android.view.View;
import android.widget.TextView;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.PopupAnim;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:16 PM
 */
public class DeviceDetailFragment extends BasePresenterFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

    private TextView deviceNameTv;
    private TextView deviceIdTv;
    private TextView lastLocationTv;

    private PopupAnim popupAnim = new PopupAnim();
    private View contentView;
    private View maskView;

    private Device device;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_device;
    }

    @Override
    protected DeviceListPresenter initPresenter() {
        return new DeviceListPresenter();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        contentView = rootView.findViewById(R.id.contentView);
        maskView = rootView.findViewById(R.id.maskView);
        maskView.setOnTouchListener((v, event) -> true);
        rootView.findViewById(R.id.routeHistory).setOnClickListener(v -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_DEVICE_HISTORY));

        deviceNameTv = rootView.findViewById(R.id.device_name);
        deviceIdTv = rootView.findViewById(R.id.device_id);
        lastLocationTv = rootView.findViewById(R.id.device_last_location);

        rootView.findViewById(R.id.close_devicePart).setOnClickListener(view -> closeFragment());

        refreshUI();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
    }

    public void setDevice(Device device) {
        this.device = device;
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
        refreshUI();
        popupAnim.ejectView(true, mActivity, rootView, maskView, contentView);
    }

    public void closeFragment() {
        popupAnim.retract(true, mActivity, getView(), maskView, contentView, null);
    }

    private void refreshUI() {
        if (device == null) {
            return;
        }
        deviceNameTv.setText(device.getName());
        String id = "ID: " + device.getId();
        deviceIdTv.setText(id);
        lastLocationTv.setText(TimeStamUtil.timeStampToDate(device.getOnlineDate()));
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
