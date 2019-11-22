package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.bean.req.BatAlarm;
import com.smona.gpstrack.device.bean.req.PhonesConfig;
import com.smona.gpstrack.device.bean.req.ReqChangeOwnerDevice;
import com.smona.gpstrack.device.bean.req.ReqDeviceAlarm;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ReqDeviceName;
import com.smona.gpstrack.device.bean.req.ReqShareDevice;
import com.smona.gpstrack.device.bean.req.ReqViewDevice;
import com.smona.gpstrack.device.bean.req.SosAlarm;
import com.smona.gpstrack.device.bean.req.TmprAlarm;
import com.smona.gpstrack.device.bean.req.VocMonAlarm;
import com.smona.gpstrack.device.model.DeviceModel;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceEvent;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class DeviceDetailPresenter extends BasePresenter<DeviceDetailPresenter.IDeviceDetailView> {

    private DeviceDecorate<Device> deviceDecorate = new DeviceDecorate<>();
    private DeviceModel deviceModel = new DeviceModel();

    public void viewDeviceDetail(String deviceId) {
        ReqViewDevice viewDevice = new ReqViewDevice();
        viewDevice.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        viewDevice.setDeviceId(deviceId);
        deviceModel.viewDevice(viewDevice, new OnResultListener<ReqDeviceDetail>() {
            @Override
            public void onSuccess(ReqDeviceDetail deviceDetail) {
                if (mView != null) {
                    mView.onViewSuccess(deviceDetail);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("", stateCode, errorInfo);
                }
            }
        });
    }

    public void deleteDevice(String deviceId) {
        ReqViewDevice viewDevice = new ReqViewDevice();
        viewDevice.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        viewDevice.setDeviceId(deviceId);
        deviceModel.deleteDevice(viewDevice, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean deviceDetail) {
                deviceDecorate.delDevice(deviceId);
                notifyDeleteDevice(deviceId);
                if (mView != null) {
                    mView.onDelSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("delDetail", stateCode, errorInfo);
                }
            }
        });
    }

    public void updateDeviceName(String deviceId, String newName ) {
        ReqViewDevice device = new ReqViewDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);

        ReqDeviceName deviceName = new ReqDeviceName();
        deviceName.setName(newName);

        deviceModel.updateDeviceName(device, deviceName, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                refreshDbDevice(deviceId, newName);
                if (mView != null) {
                    mView.onUpdateSuccess(ReqDeviceDetail.DEVICE_NAME);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateDeviceName", stateCode, errorInfo);
                }
            }
        });
    }

    private void refreshDbDevice(String deviceId, String name) {
        WorkHandlerManager.getInstance().runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                Device device = (Device)deviceDecorate.query(deviceId);
                if(device != null) {
                    device.setName(name);
                    deviceDecorate.update(device);
                }
                notifyRefreshDevice(deviceId);
            }
        });
    }

    private void notifyDeleteDevice(String id) {
        NotifyCenter.getInstance().postEvent(new DeviceEvent(DeviceEvent.ACTION_DEL, id));
    }

    private void notifyRefreshDevice(String id) {
        NotifyCenter.getInstance().postEvent(new DeviceEvent(DeviceEvent.ACTION_UPDATE, id));
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
                    mView.onUpdateSuccess(type);
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
                    mView.onUpdateSuccess(ReqDeviceDetail.DEVICE_PHONES);
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

    public void addShare(String deviceId, String email) {
        ReqShareDevice device = new ReqShareDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);
        device.setEmail(email);
        deviceModel.addShare(device, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onAddShare();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("addShare", stateCode, errorInfo);
                }
            }
        });
    }

    public void unShare(String deviceId, String shareId) {
        ReqChangeOwnerDevice device = new ReqChangeOwnerDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);
        device.setShareId(shareId);
        deviceModel.unShare(device, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onUnShare();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("unShare", stateCode, errorInfo);
                }
            }
        });
    }

    public void changeOwner(String deviceId, String shareId) {
        ReqChangeOwnerDevice device = new ReqChangeOwnerDevice();
        device.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        device.setDeviceId(deviceId);
        device.setShareId(shareId);
        deviceModel.changeOwner(device, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onChangeOwner();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("changeOwner", stateCode, errorInfo);
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
        void onViewSuccess(ReqDeviceDetail deviceDetail);
        void onDelSuccess();
        void onUpdateSuccess(int type);
        void onAddShare();
        void onUnShare();
        void onChangeOwner();
    }
}
