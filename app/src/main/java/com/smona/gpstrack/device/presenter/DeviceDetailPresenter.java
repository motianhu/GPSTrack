package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.device.bean.req.BatAlarm;
import com.smona.gpstrack.device.bean.req.PhonesConfig;
import com.smona.gpstrack.device.bean.req.ReqDeviceAlarm;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ReqViewDevice;
import com.smona.gpstrack.device.bean.req.SosAlarm;
import com.smona.gpstrack.device.bean.req.TmprAlarm;
import com.smona.gpstrack.device.bean.req.VocMonAlarm;
import com.smona.gpstrack.device.model.DeviceModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class DeviceDetailPresenter extends BasePresenter<DeviceDetailPresenter.IDeviceDetailView> {

    private DeviceModel deviceModel = new DeviceModel();

    public void deviceDetail(String deviceId) {
        ReqViewDevice viewDevice = new ReqViewDevice();
        viewDevice.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        viewDevice.setDeviceId(deviceId);
        deviceModel.viewDevice(viewDevice, new OnResultListener<ReqDeviceDetail>() {
            @Override
            public void onSuccess(ReqDeviceDetail deviceDetail) {
                if (mView != null) {
                    mView.onSuccess(deviceDetail);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("deviceDetail", stateCode, errorInfo);
                }
            }
        });
    }

    public void updateAlarmSwitch(String deviceId, int type, boolean enable) {
        ReqViewDevice device = new ReqViewDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);

        ReqDeviceAlarm deviceAlarm = createDeviceAlarm(type, enable);
        deviceModel.updateSwitch(device, deviceAlarm, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSuccess(1);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateAlarmSwitch", stateCode, errorInfo);
                }
            }
        });
    }

    public void updateAlarmPhones(String deviceId, String phones) {
        ReqViewDevice device = new ReqViewDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);

        PhonesConfig phonesConfig = new PhonesConfig();
        phonesConfig.setPhones(phones);
        ReqDeviceAlarm<PhonesConfig> deviceAlarm = new ReqDeviceAlarm<>();
        deviceAlarm.setConfigs(phonesConfig);

        deviceModel.updateSwitch(device, deviceAlarm, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSuccess(1);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateAlarmSwitch", stateCode, errorInfo);
                }
            }
        });
    }

    private ReqDeviceAlarm createDeviceAlarm(int type, boolean enable) {
       if (type == ReqDeviceDetail.BAT_ALARM) {
            BatAlarm batAlarm = new BatAlarm();
            batAlarm.setBatAlm(enable);
            ReqDeviceAlarm<BatAlarm> deviceAlarm = new ReqDeviceAlarm<>();
            deviceAlarm.setConfigs(batAlarm);
            return deviceAlarm;
        } else if (type == ReqDeviceDetail.SOS_ALARM) {
            SosAlarm sosAlarm = new SosAlarm();
            sosAlarm.setSosAlm(enable);
            ReqDeviceAlarm<SosAlarm> deviceAlarm = new ReqDeviceAlarm<>();
            deviceAlarm.setConfigs(sosAlarm);
            return deviceAlarm;
        } else if (type == ReqDeviceDetail.TMPR_ALARM) {
            TmprAlarm tmprAlm = new TmprAlarm();
            tmprAlm.setTmprAlm(enable);
            ReqDeviceAlarm<TmprAlarm> deviceAlarm = new ReqDeviceAlarm<>();
            deviceAlarm.setConfigs(tmprAlm);
            return deviceAlarm;
        } else {
            VocMonAlarm vocMonAlarm = new VocMonAlarm();
            vocMonAlarm.setVocMon(enable);
            ReqDeviceAlarm<VocMonAlarm> deviceAlarm = new ReqDeviceAlarm<>();
            deviceAlarm.setConfigs(vocMonAlarm);
            return deviceAlarm;
        }
    }

    public interface IDeviceDetailView extends ICommonView {
        void onSuccess(ReqDeviceDetail deviceDetail);

        void onSuccess(int type);
    }
}
